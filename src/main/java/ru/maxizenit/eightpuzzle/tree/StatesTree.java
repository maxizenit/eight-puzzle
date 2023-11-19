package ru.maxizenit.eightpuzzle.tree;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import lombok.SneakyThrows;
import ru.maxizenit.eightpuzzle.board.Board;
import ru.maxizenit.eightpuzzle.board.BoardUtils;
import ru.maxizenit.eightpuzzle.heuristic.HeuristicFunction;

/** Дерево состояний. */
public class StatesTree {

  /** Корневой узел. */
  private final StatesTreeNode root;

  /** Начальное состояние. */
  private final int startState;

  /** Конечное состояние. */
  private final int endState;

  /** Максимальная глубина. */
  private final int maxDepth;

  /** Флаг включения пошагового режима. */
  private final boolean enabledStepByStepMode;

  /** BufferedReader, необходимый для отслеживания нажатия кнопки Enter в пошаговом режиме. */
  private final BufferedReader reader;

  /** Кэш состояний. */
  private final StatesCache statesCache = new StatesCache();

  /** Эвристическая функция. */
  private final HeuristicFunction heuristicFunction;

  /** Флаг остановки поиска. */
  private boolean stopSearching = false;

  /** Успешен ли поиск. */
  private boolean isSearchSuccessful = false;

  /** Количество шагов. */
  private int stepsCount;

  /** Количество узлов. */
  private int nodesCount;

  public StatesTree(
      int startState,
      int endState,
      int maxDepth,
      boolean enabledStepByStepMode,
      BufferedReader reader,
      HeuristicFunction heuristicFunction) {
    this.startState = startState;
    this.endState = endState;
    this.maxDepth = maxDepth;
    this.enabledStepByStepMode = enabledStepByStepMode;
    this.reader = reader;
    this.heuristicFunction = heuristicFunction;

    root =
        new StatesTreeNode(
            BoardUtils.createBoardFromState(startState),
            0,
            null,
            null,
            BoardUtils.getChipsOutOfPlaceCount(startState, endState),
            BoardUtils.getManhattanDistance(startState, endState));
    nodesCount = 1;
  }

  /**
   * Создаёт дочерние узлы для переданного.
   *
   * @param node узел, для которого создаются дочерние узлы
   */
  private void createChildrenForNode(StatesTreeNode node) {
    List<StatesTreeNode> children = new ArrayList<>();

    node.getBoard().getPossibleMoveDirections().stream()
        .filter(
            md ->
                node.getLastMoveDirection() == null
                    || !md.equals(node.getLastMoveDirection().getReverseMoveDirection()))
        .forEach(
            md -> {
              Board newBoard = new Board(node.getBoard());
              newBoard.move(md);

              StatesTreeNode newNode =
                  new StatesTreeNode(
                      newBoard,
                      node.getDepth() + 1,
                      md,
                      node,
                      BoardUtils.getChipsOutOfPlaceCount(newBoard.getState(), endState),
                      BoardUtils.getManhattanDistance(newBoard.getState(), endState));
              children.add(newNode);
            });

    node.getChildren()
        .addAll(
            children.stream()
                .sorted(
                    Comparator.comparing(
                        HeuristicFunction.OUT_OF_PLACE.equals(heuristicFunction)
                            ? StatesTreeNode::getOutOfPlaceNodesCount
                            : StatesTreeNode::getManhattanDistance))
                .toList());
    nodesCount += node.getChildren().size();
  }

  /** Начинает поиск в дереве от корня. */
  public void search() {
    search(root);
    if (!isSearchSuccessful) {
      System.out.println("Решение не найдено");
    }
  }

  /**
   * Ведёт поиск по текущему узлу.
   *
   * @param node узел
   */
  @SneakyThrows
  protected void search(StatesTreeNode node) {
    if (stopSearching) {
      return;
    }

    ++stepsCount;
    if (enabledStepByStepMode) {
      System.out.println();
      reader.readLine();

      System.out.printf(
          "Шаг %d, состояние %s, глубина %d, ход %s из %s, значение эвристической функции %d%n",
          stepsCount,
          BoardUtils.convertIntStateToStringState(node.getState()),
          node.getDepth(),
          node.getLastMoveDirection() != null ? node.getLastMoveDirection().getArrow() : "",
          node.getParent() != null
              ? String.format(
                  "%s", BoardUtils.convertIntStateToStringState(node.getParent().getState()))
              : "",
          HeuristicFunction.OUT_OF_PLACE.equals(heuristicFunction)
              ? node.getOutOfPlaceNodesCount()
              : node.getManhattanDistance());
      printBorder(node);
    }

    if (node.getDepth() > maxDepth) {
      if (enabledStepByStepMode) {
        System.out.println("Максимальная глубина превышена");
      }
      return;
    }
    if (!statesCache.add(node.getState(), node.getDepth())) {
      if (enabledStepByStepMode) {
        System.out.println("Данное состояние ранее достигалось");
      }
      return;
    }

    if (node.getState() == endState) {
      stopSearching = true;
      isSearchSuccessful = true;
      printPathToEndState(node);
      printComplexity();
      return;
    }

    createChildrenForNode(node);
    node.getChildren().forEach(this::search);
  }

  /**
   * Возвращает путь из начального состояния в целевое в виде строки из стрелок.
   *
   * @param endNode конечный узел
   * @return путь из начального состояния в целевое в виде строки из стрелок
   */
  private String getPathToEndState(StatesTreeNode endNode) {
    StringBuilder pathBuilder = new StringBuilder();
    StatesTreeNode currentNode = endNode;

    while (currentNode != root) {
      pathBuilder.append(currentNode.getLastMoveDirection().getArrow());
      currentNode = currentNode.getParent();
    }

    return pathBuilder.reverse().toString();
  }

  /**
   * Печатает путь из начального состояния в целевое в виде строки из стрелок.
   *
   * @param endNode конечный узел
   */
  private void printPathToEndState(StatesTreeNode endNode) {
    System.out.println("Целевое состояние достигнуто!");
    System.out.printf(
        "Путь из состояния %s в состояние %s:%n%s%n",
        BoardUtils.convertIntStateToStringState(startState),
        BoardUtils.convertIntStateToStringState(endState),
        getPathToEndState(endNode));
    System.out.printf("Стоимость пути: %d%n", endNode.getDepth());
  }

  /**
   * Возвращает кайму для узла (всех детей его родителя).
   *
   * @param node узел
   * @return кайма узла
   */
  private List<StatesTreeNode> getBorder(StatesTreeNode node) {
    List<StatesTreeNode> border = new ArrayList<>();

    if (node.getParent() != null) {
      border.addAll(node.getParent().getChildren());
    }

    return border;
  }

  /**
   * Печатает кайму.
   *
   * @param node узел, для которого требуется печатать кайму
   */
  private void printBorder(StatesTreeNode node) {
    System.out.print("Кайма (в скобках - значение эвристической функции): ");
    getBorder(node)
        .forEach(
            n ->
                System.out.printf(
                    "%s (%d) | ",
                    BoardUtils.convertIntStateToStringState(n.getState()),
                    HeuristicFunction.OUT_OF_PLACE.equals(heuristicFunction)
                        ? n.getOutOfPlaceNodesCount()
                        : n.getManhattanDistance()));
    System.out.println();
  }

  /** Печатает сложность поиска (временную - количество шагов, по памяти - количество узлов). */
  private void printComplexity() {
    System.out.printf("Количество шагов (временная сложность): %d%n", stepsCount);
    System.out.printf("Количество узлов (сложность по памяти): %d%n", nodesCount);
  }
}
