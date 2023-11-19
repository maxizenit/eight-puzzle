package ru.maxizenit.eightpuzzle.board;

import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

/**
 * Класс доски с фишками. Доска реализована двумерным массивом 3*3 (chips). 0 в массиве означает
 * пустую клетку.
 */
public class Board {

  /** Размерность доски головоломки. */
  public static final int DIMENSION = 3;

  /** Фишки. */
  private final int[][] chips;

  /** Текущая координата пустой клетки по оси X. */
  private int emptyFieldX;

  /** Текущая координата пустой клетки по оси Y. */
  private int emptyFieldY;

  /**
   * Текущее состояние в виде числа. Порядок цифр в этом числе определён порядком фишек на доске:
   * слева-направо, сверху-вниз.
   *
   * <p>Например, для поля 123/456/708 состояние будет задано числом 123456708.
   */
  @Getter private int state;

  public Board(int[][] chips) {
    this.chips = Arrays.stream(chips).map(int[]::clone).toArray(int[][]::new);

    for (int y = 0; y < DIMENSION; ++y) {
      for (int x = 0; x < DIMENSION; ++x) {
        if (chips[y][x] == 0) {
          setEmptyFieldCoordinates(x, y);
          break;
        }
      }
    }

    refreshState();
  }

  /**
   * Конструктор копирования.
   *
   * @param source источник
   */
  public Board(Board source) {
    this(source.chips);
  }

  /**
   * Перемещает фишку, которую можно переместить в заданном направлении, если это направление
   * допустимо.
   *
   * @param moveDirection направление
   */
  public void move(MoveDirection moveDirection) {
    if (getPossibleMoveDirections().contains(moveDirection)) {
      int x = emptyFieldX - moveDirection.getHorizontalIncrement();
      int y = emptyFieldY - moveDirection.getVerticalIncrement();

      move(moveDirection, x, y);
    }
  }

  /**
   * Перемещает фишку, находящуюся по координатам x и y, в указанном направлении.
   *
   * @param moveDirection направление перемещения фишки
   * @param x координата фишки по оси X
   * @param y координата фишки по оси Y
   */
  private void move(MoveDirection moveDirection, int x, int y) {
    int newX = x + moveDirection.getHorizontalIncrement();
    int newY = y + moveDirection.getVerticalIncrement();

    chips[newY][newX] = chips[y][x];
    chips[y][x] = 0;

    setEmptyFieldCoordinates(x, y);
    refreshState();
  }

  /**
   * Возвращает множество из допустимых направлений для перемешения фишки.
   *
   * @return множество из допустимых направлений для перемещения фишки
   */
  public Set<MoveDirection> getPossibleMoveDirections() {
    return Arrays.stream(MoveDirection.values())
        .filter(this::isDirectionPossible)
        .sorted()
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  /**
   * Возвращает {@code true}, если направление допустимо для хода. Здесь изменение координаты в
   * направлении отнимается, а не прибавляется, так как проверяются координаты нуля, а он
   * перемещается в обратном направлении.
   *
   * @return {@code true}, если направление допустимо для хода
   */
  private boolean isDirectionPossible(MoveDirection direction) {
    return coordinateIsValid(emptyFieldX - direction.getHorizontalIncrement())
        && coordinateIsValid(emptyFieldY - direction.getVerticalIncrement());
  }

  /**
   * Устанавливает координаты пустой клетки.
   *
   * @param x координата пустой клетки по оси X
   * @param y координата пустой клетки по оси Y
   */
  private void setEmptyFieldCoordinates(int x, int y) {
    emptyFieldX = x;
    emptyFieldY = y;
  }

  /**
   * Возвращает {@code true}, если координата лежит в пределах от 0 до 2 включительно.
   *
   * @param coordinate координата
   * @return {@code true}, если координата лежит в пределах от 0 до 2 включительно.
   */
  private boolean coordinateIsValid(int coordinate) {
    return coordinate >= 0 && coordinate < DIMENSION;
  }

  /** Обновляет поле состояния. */
  private void refreshState() {
    StringBuilder sb = new StringBuilder();

    for (int y = 0; y < DIMENSION; ++y) {
      for (int x = 0; x < DIMENSION; ++x) {
        sb.append(chips[y][x]);
      }
    }

    state = Integer.parseInt(sb.toString());
  }
}
