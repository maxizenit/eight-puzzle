package ru.maxizenit.eightpuzzle.tree;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import ru.maxizenit.eightpuzzle.board.Board;
import ru.maxizenit.eightpuzzle.board.MoveDirection;

/** Узел дерева состояний. */
@Getter
@RequiredArgsConstructor
public class StatesTreeNode {

  /** Доска состояния. */
  private final Board board;

  /** Глубина. */
  private final int depth;

  /** Последнее направление хода (приведшее к состоянию узла). */
  private final MoveDirection lastMoveDirection;

  /** Родительский узел. */
  private final StatesTreeNode parent;

  /** Дочерние узлы. */
  private final List<StatesTreeNode> children = new ArrayList<>();

  /**
   * Возвращает состояние узла.
   *
   * @return состояние узла
   */
  public int getState() {
    return board.getState();
  }
}
