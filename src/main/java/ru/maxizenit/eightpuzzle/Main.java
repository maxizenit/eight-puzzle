package ru.maxizenit.eightpuzzle;

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

      System.out.print("Включить пошаговый режим? (y/n): ");
      boolean stepByStep = reader.readLine().startsWith("y");

      System.out.println();

      StatesTree tree = new StatesTree(startState, targetState, maxDepth, stepByStep, reader);
      tree.search();
    }
  }
}
