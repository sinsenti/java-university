package lab6;

import java.util.*;

public class MatrixProcessor {
  public static void main(String[] args) {
    Scanner scanner = new Scanner(System.in);
    System.out.print("Введите размерность матрицы n: ");

    int n = scanner.nextInt();
    if (n <= 0) {
      System.out.println("Решение отсутствует: размерность матрицы должна быть положительным числом");
      scanner.close();
      return;
    }

    int[][] matrix = new int[n][n];
    Random random = new Random();
    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        matrix[i][j] = random.nextInt(2 * n + 1) - n;
      }
    }

    System.out.println("Исходная матрица:");
    printMatrix(matrix);

    int maxElement = findMaxElement(matrix);
    System.out.println("Максимальный элемент: " + maxElement);

    Set<Integer> rowsToRemove = new HashSet<>();
    Set<Integer> colsToRemove = new HashSet<>();

    for (int i = 0; i < n; i++) {
      for (int j = 0; j < n; j++) {
        if (matrix[i][j] == maxElement) {
          rowsToRemove.add(i);
          colsToRemove.add(j);
        }
      }
    }

    int newRows = n - rowsToRemove.size();
    int newCols = n - colsToRemove.size();

    if (newRows <= 0 || newCols <= 0) {
      System.out.println("После удаления строк и столбцов матрица стала пустой.");
      scanner.close();
      return;
    }

    int[][] newMatrix = new int[newRows][newCols];
    int newRowIndex = 0;

    for (int i = 0; i < n; i++) {
      if (rowsToRemove.contains(i))
        continue;

      int newColIndex = 0;
      for (int j = 0; j < n; j++) {
        if (colsToRemove.contains(j))
          continue;

        newMatrix[newRowIndex][newColIndex] = matrix[i][j];
        newColIndex++;
      }
      newRowIndex++;
    }

    System.out.println("\nМатрица после удаления строк и столбцов с максимальным элементом:");
    printMatrix(newMatrix);
    scanner.close();
  }

  private static int findMaxElement(int[][] matrix) {
    int max = matrix[0][0];
    for (int[] row : matrix) {
      for (int element : row) {
        if (element > max) {
          max = element;
        }
      }
    }
    return max;
  }

  private static void printMatrix(int[][] matrix) {
    for (int[] row : matrix) {
      for (int num : row) {
        System.out.printf("%4d", num);
      }
      System.out.println();
    }
  }
}
