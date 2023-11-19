package ru.maxizenit.eightpuzzle;

import ru.maxizenit.eightpuzzle.heuristic.HeuristicFunction;
import ru.maxizenit.eightpuzzle.tree.StatesTree;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {

  public static void main(String[] args) throws IOException {
    try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
      System.out.print("Введите начальное состояние: ");
      int startState = Integer.parseInt(reader.readLine());

      System.out.print("Введите целевое состояние: ");
      int targetState = Integer.parseInt(reader.readLine());

      System.out.print("Введите максимальную глубину поиска: ");
      int maxDepth = Integer.parseInt(reader.readLine());

      System.out.print("Какую эвристическую функцию использовать? (h1 - количество фишек не на своём месте, h2 - сумма манхэттенских расстояний): ");
      HeuristicFunction heuristicFunction = reader.readLine().startsWith("h1") ? HeuristicFunction.OUT_OF_PLACE : HeuristicFunction.MANHATTAN_DISTANCE;

      System.out.print("Включить пошаговый режим? (y/n): ");
      boolean stepByStep = reader.readLine().startsWith("y");

      System.out.println();

      StatesTree tree = new StatesTree(startState, targetState, maxDepth, stepByStep, reader, heuristicFunction);
      tree.search();
    }
  }
}
