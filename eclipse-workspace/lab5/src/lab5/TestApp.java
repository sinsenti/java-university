package lab5;

import java.util.*;

public class TestApp {
  public static void main(String[] args) {
    try {
      Computer[] computers = {
          new Desktop("Dell XPS", 1500.0, "Intel i7", 16, "Mini Tower"),
          new Notebook("MacBook Pro", 2000.0, "Apple M1", 8, 10.5),
          new Desktop("HP Pavilion", 800.0, "AMD Ryzen 5", 8, "Desktop"),
          new Notebook("Lenovo ThinkPad", 1200.0, "Intel i5", 16, 15.0),
          new Desktop("Asus ROG", 1800.0, "Intel i9", 32, "Gaming")
      };

      System.out.println(" Демонстрация Iterable ");
      for (Computer computer : computers) {
        System.out.println("Поля объекта " + computer.getModel() + ":");
        int fieldCount = 1;
        for (String field : computer) {
          System.out.println("  Поле " + fieldCount + ": " + field);
          fieldCount++;
        }
        System.out.println("(" + computer.getClass().getSimpleName() + ")");
        computer.getClass().getSimpleName();
        System.out.println();
      }

      System.out.println(" Сортировка по цене (Comparable) ");
      Arrays.sort(computers);
      printComputers(computers);

      System.out.println("\n Сортировка Desktop по моделям (Comparator) ");
      List<Desktop> desktops = new ArrayList<>();
      for (Computer computer : computers) {
        if (computer instanceof Desktop) {
          desktops.add((Desktop) computer);
        }
      }
      desktops.sort(Desktop.byModel);
      for (Desktop desktop : desktops) {
        System.out.println(desktop.toString());
      }

      System.out.println("\n Сортировка Notebook по времени работы батареи (Comparator) ");
      List<Notebook> notebooks = new ArrayList<>();
      for (Computer computer : computers) {
        if (computer instanceof Notebook) {
          notebooks.add((Notebook) computer);
        }
      }
      notebooks.sort(Notebook.byBatteryLife);
      for (Notebook notebook : notebooks) {
        System.out.println(notebook.toString());
      }

      System.out.println("\n Сериализация и десериализация ");
      String saved = computers[0].toString();
      System.out.println("Сохраненная строка: " + saved);
      Computer restored = Computer.fromString(saved);
      System.out.println("Восстановленный объект: " + restored.toString());

      System.out.println("\n Тестирование исключений ");
      try {
        new Desktop("", 100, "Intel", 8, "Tower");
      } catch (IllegalArgumentException e) {
        System.out.println("Поймано исключение: " + e.getMessage());
      }

      try {
        new Notebook("Test", -100, "Intel", 8, 5.0);
      } catch (IllegalArgumentException e) {
        System.out.println("Поймано исключение: " + e.getMessage());
      }

    } catch (Exception e) {
      System.out.println("Ошибка: " + e.getMessage());
      e.printStackTrace();
    }
  }

  private static void printComputers(Computer[] computers) {
    for (Computer computer : computers) {
      System.out.println(computer.toString());
    }
  }
}
