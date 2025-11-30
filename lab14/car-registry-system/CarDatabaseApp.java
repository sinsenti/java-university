import java.io.IOException;
import java.util.Scanner;

public class CarDatabaseApp {
  private static CarDatabase database;
  private static Scanner scanner = new Scanner(System.in);
  private static final String DATABASE_FILE = "cars.dat";

  public static void main(String[] args) {
    System.out.println("    СИСТЕМА УПРАВЛЕНИЯ АВТОМОБИЛЯМИ");
    System.out.println();

    try {
      database = new CarDatabase(DATABASE_FILE);
      System.out.println("База данных загружена: " + DATABASE_FILE);
      System.out.println("Загружено автомобилей: " + getCarCount() + "\n");

      showMainMenu();

    } catch (IOException e) {
      System.err.println("Ошибка инициализации базы данных: " + e.getMessage());
      System.out.println("Создаем новую базу данных...");
      try {
        database = new CarDatabase(DATABASE_FILE);
        showMainMenu();
      } catch (IOException ex) {
        System.err.println("Критическая ошибка: " + ex.getMessage());
      }
    } finally {
      if (database != null) {
        try {
          database.close();
          System.out.println("\nБаза данных закрыта");
        } catch (IOException e) {
          System.err.println("Ошибка закрытия базы данных: " + e.getMessage());
        }
      }
    }
  }

  private static int getCarCount() {
    return 0;
  }

  private static void showMainMenu() throws IOException {
    while (true) {
      System.out.println("\n=== ГЛАВНОЕ МЕНЮ ===");
      System.out.println("1. Просмотр всех автомобилей");
      System.out.println("2. Добавить автомобиль");
      System.out.println("3. Поиск автомобилей");
      System.out.println("4. Сортировка автомобилей");
      System.out.println("5. Удалить автомобиль");
      System.out.println("6. Заполнить тестовыми данными");
      System.out.println("7. Показать индексы");
      System.out.println("8. Статистика");
      System.out.println("0. Выход");
      System.out.print("Выберите действие: ");

      String input = scanner.nextLine().trim();

      switch (input) {
        case "1":
          database.printAllCars();
          break;
        case "2":
          database.addCarFromConsole();
          break;
        case "3":
          showSearchMenu();
          break;
        case "4":
          showSortMenu();
          break;
        case "5":
          deleteCarMenu();
          break;
        case "6":
          database.fillWithTestData();
          break;
        case "7":
          database.printIndexes();
          break;
        case "8":
          database.printStatistics();
          break;
        case "0":
          System.out.println("Выход из программы...");
          return;
        default:
          System.out.println("Такого варианта нет");
      }

      pause();
    }
  }

  private static void showSearchMenu() throws IOException {
    while (true) {
      System.out.println("\nПОИСК АВТОМОБИЛЕЙ");
      System.out.println("1. По регистрационному номеру");
      System.out.println("2. По марке");
      System.out.println("3. С маркой после указанной");
      System.out.println("4. С маркой до указанной");
      System.out.println("0. Назад в главное меню");
      System.out.print("Выберите тип поиска: ");

      String input = scanner.nextLine().trim();

      switch (input) {
        case "1":
          System.out.print("Введите регистрационный номер: ");
          String regNumber = scanner.nextLine();
          database.findCarByRegistrationNumber(regNumber);
          break;
        case "2":
          System.out.print("Введите марку: ");
          String brand = scanner.nextLine();
          database.findCarsByBrand(brand);
          break;
        case "3":
          System.out.print("Введите марку (поиск марок после): ");
          brand = scanner.nextLine();
          database.findCarsWithBrandGreaterThan(brand);
          break;
        case "4":
          System.out.print("Введите марку (поиск марок до): ");
          brand = scanner.nextLine();
          database.findCarsWithBrandLessThan(brand);
          break;
        case "0":
          return;
        default:
          System.out.println("Неверный выбор. Попробуйте снова.");
      }

      pause();
    }
  }

  private static void showSortMenu() throws IOException {
    while (true) {
      System.out.println("\nСОРТИРОВКА АВТОМОБИЛЕЙ");
      System.out.println("1. По марке (возрастание)");
      System.out.println("2. По марке (убывание)");
      System.out.println("3. По регистрационному номеру (возрастание)");
      System.out.println("4. По регистрационному номеру (убывание)");
      System.out.println("0. Назад в главное меню");
      System.out.print("Выберите тип сортировки: ");

      String input = scanner.nextLine().trim();

      switch (input) {
        case "1":
          database.printCarsSortedByBrand(true);
          break;
        case "2":
          database.printCarsSortedByBrand(false);
          break;
        case "3":
          database.printCarsSortedByRegistrationNumber(true);
          break;
        case "4":
          database.printCarsSortedByRegistrationNumber(false);
          break;
        case "0":
          return;
        default:
          System.out.println("✗ Неверный выбор. Попробуйте снова.");
      }

      pause();
    }
  }

  private static void deleteCarMenu() throws IOException {
    System.out.println("\nУДАЛЕНИЕ АВТОМОБИЛЯ");
    System.out.print("Введите регистрационный номер для удаления: ");
    String regNumber = scanner.nextLine();

    if (database.deleteCarByRegistrationNumber(regNumber)) {
      System.out.println("Автомобиль с номером " + regNumber + " успешно удален");
    } else {
      System.out.println("Автомобиль с номером " + regNumber + " не найден");
    }
  }

  private static void pause() {
    System.out.print("\nНажмите Enter для продолжения...");
    scanner.nextLine();
  }
}
