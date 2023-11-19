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
    String stringState = convertIntStateToStringState(state);
    return new Board(convertStringStateToArray(stringState));
  }

  /**
   * Возвращает состояние в виде строки для состояния в виде числа.
   *
   * @param state состояние
   * @return состояние в виде строки для состояния в виде числа
   */
  public static String convertIntStateToStringState(int state) {
    return String.format("%09d", state);
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

  /**
   * Возвращает количество фишек, расположение которых отличается в двух состояниях.
   *
   * @param firstState первое состояние
   * @param secondState второе состояние
   * @return количество фишек, расположение которых отличается в двух состояниях
   */
  public static int getChipsOutOfPlaceCount(int firstState, int secondState) {
    char[] firstStateCharArray = convertIntStateToStringState(firstState).toCharArray();
    char[] secondStateCharArray = convertIntStateToStringState(secondState).toCharArray();

    int result = 0;
    for (int i = 0; i < 9; ++i) {
      if (firstStateCharArray[i] != secondStateCharArray[i]) {
        ++result;
      }
    }

    return result;
  }

  /**
   * Возвращает манхэттенское расстояние для фишки.
   *
   * @param y координата y текущего расположения фишки
   * @param x координата x текущего расположения фишки
   * @param chipFromFirstChips фишка
   * @param secondChips целевое расположение фишек
   * @return манхэттенское расстояние для фишки
   */
  private static int getManhattanDistanceForChip(
      int y, int x, int chipFromFirstChips, int[][] secondChips) {
    for (int y1 = 0; y1 < 3; ++y1) {
      for (int x1 = 0; x1 < 3; ++x1) {
        if (secondChips[y1][x1] == chipFromFirstChips) {
          return Math.abs(y - y1) + Math.abs(x - x1);
        }
      }
    }

    return 0;
  }

  /**
   * Возвращает сумму манхэттенских расстояний между двумя состояниями для всех фишек.
   *
   * @param firstState первое состояние
   * @param secondState второе состояние
   * @return сумма манхэттенских расстояний между двумя состояниями для всех фишек
   */
  public static int getManhattanDistance(int firstState, int secondState) {
    int[][] firstChips = convertStringStateToArray(convertIntStateToStringState(firstState));
    int[][] secondChips = convertStringStateToArray(convertIntStateToStringState(secondState));
    int result = 0;

    for (int y = 0; y < 3; ++y) {
      for (int x = 0; x < 3; ++x) {
        if (firstChips[y][x] == secondChips[y][x]) {
          continue;
        }
        if (firstChips[y][x] == 0) {
          continue;
        }

        result += getManhattanDistanceForChip(y, x, firstChips[y][x], secondChips);
      }
    }

    return result;
  }
}
