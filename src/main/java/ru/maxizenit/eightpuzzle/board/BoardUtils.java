package ru.maxizenit.eightpuzzle.board;

/** Вспомогательные методы для доски. */
public class BoardUtils {

  /**
   * Создаёт из состояния, заданного числом, доску, соответствующую этому состоянию.
   *
   * @param state состояние
   * @return доска, соответствующая состоянию
   */
  public static Board createBoardFromState(int state) {
    String stringState = String.format("%09d", state);
    return new Board(convertStringStateToArray(stringState));
  }

  /**
   * Возвращает массив фишек, расположенных согласно состоянию.
   *
   * @param stringState состояние в виде строки
   * @return массив фишек
   */
  private static int[][] convertStringStateToArray(String stringState) {
    int[][] array = new int[Board.DIMENSION][Board.DIMENSION];
    int y = 0;
    int x = 0;

    for (Character c : stringState.toCharArray()) {
      array[y][x] = Integer.parseInt(c.toString());
      ++x;

      if (x == Board.DIMENSION) {
        x = 0;
        ++y;
      }
    }

    return array;
  }
}
