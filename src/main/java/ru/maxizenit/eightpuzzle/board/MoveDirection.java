package ru.maxizenit.eightpuzzle.board;

import lombok.Getter;

/** Направление хода. */
@Getter
public enum MoveDirection {

  /** Вверх. */
  UP(-1, 0, "^"),

  /** Вниз. */
  DOWN(1, 0, "v"),

  /** Влево. */
  LEFT(0, -1, "<"),

  /** Вправо. */
  RIGHT(0, 1, ">");

  /** Изменение координаты фишки по вертикали. */
  private final int verticalIncrement;

  /** Изменение координаты фишки по горизонтали. */
  private final int horizontalIncrement;

  /** Стрелка, соответствующая направлению хода, в виде строки. */
  private final String arrow;

  MoveDirection(int verticalIncrement, int horizontalIncrement, String arrow) {
    this.verticalIncrement = verticalIncrement;
    this.horizontalIncrement = horizontalIncrement;
    this.arrow = arrow;
  }

  /**
   * Возвращает противоположное направление движения.
   *
   * @return противоположное направление движения
   */
  public MoveDirection getReverseMoveDirection() {
    return switch (this) {
      case UP -> DOWN;
      case DOWN -> UP;
      case LEFT -> RIGHT;
      case RIGHT -> LEFT;
    };
  }
}
