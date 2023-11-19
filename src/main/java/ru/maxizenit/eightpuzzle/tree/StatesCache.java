package ru.maxizenit.eightpuzzle.tree;

import java.util.HashMap;
import java.util.Map;

/** Кэш состояний. Хранит пары "состояние - глубина, на которой оно было достигнуто". */
public class StatesCache {

  private final Map<Integer, Integer> statesAndDepths = new HashMap<>();

  /**
   * Сохраняет состояние и глубину, на которой оно было достигнуто, в кэш, если такого состояния в
   * нём ещё нет или оно было достигнуто на глубине большей, чем переданная. Возвращает {@code true}
   * в случае успешного добавления.
   *
   * @param state состояние
   * @param depth глубина, на которой было достигнуто состояние
   * @return {@code true} в случае успешном добавления
   */
  public boolean add(int state, int depth) {
    boolean added = false;

    if (!statesAndDepths.containsKey(state) || statesAndDepths.get(state) > depth) {
      statesAndDepths.put(state, depth);
      added = true;
    }

    return added;
  }
}
