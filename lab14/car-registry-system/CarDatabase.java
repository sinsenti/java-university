import java.io.*;
import java.util.*;

public class CarDatabase implements AutoCloseable {
  private static final int RECORD_HEADER_SIZE = 8;
  private RandomAccessFile file;
  private String filename;

  private TreeMap<String, List<Long>> brandIndex = new TreeMap<>();
  private TreeMap<String, List<Long>> modelIndex = new TreeMap<>();
  private TreeMap<String, Long> registrationNumberIndex = new TreeMap<>();
  private TreeMap<Double, List<Long>> priceIndex = new TreeMap<>();

  public CarDatabase(String filename) throws IOException {
    this.filename = filename;
    this.file = new RandomAccessFile(filename, "rw");
    loadIndexes();
  }

  private byte[] serializeCar(Car car) throws IOException {
    try (ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ObjectOutputStream oos = new ObjectOutputStream(baos)) {
      oos.writeObject(car);
      return baos.toByteArray();
    }
  }

  private Car deserializeCar(byte[] data) throws IOException, ClassNotFoundException {
    try (ByteArrayInputStream bais = new ByteArrayInputStream(data);
        ObjectInputStream ois = new ObjectInputStream(bais)) {
      return (Car) ois.readObject();
    }
  }

  public long addCar(Car car) throws IOException {
    if (registrationNumberIndex.containsKey(car.getRegistrationNumber())) {
      throw new IllegalArgumentException("Автомобиль с номером " + car.getRegistrationNumber() + " уже существует");
    }

    byte[] carData = serializeCar(car);
    file.seek(file.length());
    long position = file.getFilePointer();

    file.writeInt(carData.length);
    file.writeInt(1);

    file.write(carData);

    updateIndexes(car, position);

    return position;
  }

  public Car readCar(long position) throws IOException {
    try {
      file.seek(position);
      int dataLength = file.readInt();
      int activeFlag = file.readInt();

      if (activeFlag == 0) {
        return null;
      }

      byte[] carData = new byte[dataLength];
      file.readFully(carData);

      return deserializeCar(carData);
    } catch (ClassNotFoundException e) {
      throw new IOException("Ошибка десериализации автомобиля", e);
    }
  }

  private void updateIndexes(Car car, long position) {
    brandIndex.computeIfAbsent(car.getBrand(), k -> new ArrayList<>()).add(position);
    modelIndex.computeIfAbsent(car.getModel(), k -> new ArrayList<>()).add(position);
    registrationNumberIndex.put(car.getRegistrationNumber(), position);
    priceIndex.computeIfAbsent(car.getPrice(), k -> new ArrayList<>()).add(position);
  }

  private void loadIndexes() throws IOException {
    long filePointer = 0;
    brandIndex.clear();
    modelIndex.clear();
    registrationNumberIndex.clear();
    priceIndex.clear();

    while (filePointer < file.length()) {
      try {
        file.seek(filePointer);
        int dataLength = file.readInt();
        int activeFlag = file.readInt();

        if (activeFlag == 1) {
          byte[] carData = new byte[dataLength];
          file.readFully(carData);

          Car car = deserializeCar(carData);
          updateIndexes(car, filePointer);
        }

        filePointer += RECORD_HEADER_SIZE + dataLength;
      } catch (Exception e) {
        System.err.println("Ошибка загрузки записи по позиции " + filePointer + ": " + e.getMessage());
        break;
      }
    }
  }

  public void fillWithTestData() throws IOException {
    System.out.println("Заполнение базы тестовыми данными...");

    Car[] testCars = {
        ValidationUtil.createValidatedCar("Toyota", "Camry", 2020, "Black", 30000, "AB123CD"),
        ValidationUtil.createValidatedCar("BMW", "X5", 2019, "White", 50000, "EF456GH"),
        ValidationUtil.createValidatedCar("Audi", "A4", 2021, "Red", 35000, "IJ789KL"),
        ValidationUtil.createValidatedCar("Toyota", "Corolla", 2018, "Blue", 20000, "MN012OP"),
        ValidationUtil.createValidatedCar("BMW", "3 Series", 2020, "Silver", 40000, "QR345ST"),
        ValidationUtil.createValidatedCar("Mercedes", "C-Class", 2022, "Black", 45000, "UV678WX"),
        ValidationUtil.createValidatedCar("Honda", "Civic", 2017, "Gray", 18000, "YZ901AB"),
        ValidationUtil.createValidatedCar("Ford", "Focus", 2019, "Green", 22000, "CD234EF")
    };

    int added = 0;
    int skipped = 0;

    for (Car car : testCars) {
      try {
        if (!registrationNumberIndex.containsKey(car.getRegistrationNumber())) {
          addCar(car);
          added++;
          System.out.println(
              "✓ Добавлен: " + car.getBrand() + " " + car.getModel() + " (" + car.getRegistrationNumber() + ")");
        } else {
          skipped++;
          System.out.println("✗ Пропущен (дубликат): " + car.getRegistrationNumber());
        }
      } catch (IllegalArgumentException e) {
        skipped++;
        System.out.println("✗ Ошибка: " + e.getMessage());
      }
    }

    System.out.println("\nИтог: добавлено " + added + ", пропущено " + skipped + " автомобилей");
  }

  public void printAllCars() throws IOException {
    if (registrationNumberIndex.isEmpty()) {
      System.out.println("База данных пуста");
      return;
    }

    System.out.println("\n" + "=".repeat(80));
    System.out.println("ВСЕ АВТОМОБИЛИ В БАЗЕ ДАННЫХ");
    System.out.println("=".repeat(80));
    System.out.printf("%-10s %-12s %-6s %-8s %-10s %s%n",
        "Марка", "Модель", "Год", "Цвет", "Цена", "Рег. номер");
    System.out.println("-".repeat(80));

    for (Long position : registrationNumberIndex.values()) {
      Car car = readCar(position);
      if (car != null) {
        System.out.println(car.toFormattedString());
      }
    }
    System.out.println("=".repeat(80));
    System.out.println("Всего автомобилей: " + registrationNumberIndex.size());
  }

  public void printIndexes() {
    System.out.println("\nИНДЕКСЫ БАЗЫ ДАННЫХ");

    System.out.println("\nМарки (" + brandIndex.size() + "):");
    for (String brand : brandIndex.keySet()) {
      System.out.println("  " + brand + ": " + brandIndex.get(brand).size() + " авто");
    }

    System.out.println("\nМодели (" + modelIndex.size() + "):");
    for (String model : modelIndex.keySet()) {
      System.out.println("  " + model + ": " + modelIndex.get(model).size() + " авто");
    }

    System.out.println("\nРегистрационные номера (" + registrationNumberIndex.size() + "):");
    int count = 0;
    for (String regNum : registrationNumberIndex.keySet()) {
      System.out.print(regNum + " ");
      count++;
      if (count % 5 == 0)
        System.out.println();
    }
    if (count % 5 != 0)
      System.out.println();

    System.out.println("\nЦены (" + priceIndex.size() + " разных значений):");
    for (Map.Entry<Double, List<Long>> entry : priceIndex.entrySet()) {
      System.out.println("  " + entry.getKey() + ": " + entry.getValue().size() + " авто");
    }
  }

  public void printCarsSortedByBrand(boolean ascending) throws IOException {
    if (brandIndex.isEmpty()) {
      System.out.println("База данных пуста");
      return;
    }

    System.out.println("\n" + "=".repeat(80));
    System.out.println("АВТОМОБИЛИ ОТСОРТИРОВАННЫЕ ПО МАРКЕ (" +
        (ascending ? "ВОЗРАСТАНИЕ" : "УБЫВАНИЕ") + ")");
    System.out.println("=".repeat(80));
    System.out.printf("%-10s %-12s %-6s %-8s %-10s %s%n",
        "Марка", "Модель", "Год", "Цвет", "Цена", "Рег. номер");
    System.out.println("-".repeat(80));

    List<String> brands = new ArrayList<>(brandIndex.keySet());
    if (!ascending) {
      Collections.reverse(brands);
    }

    for (String brand : brands) {
      for (Long position : brandIndex.get(brand)) {
        Car car = readCar(position);
        if (car != null) {
          System.out.println(car.toFormattedString());
        }
      }
    }
    System.out.println("=".repeat(80));
  }

  public void printCarsSortedByRegistrationNumber(boolean ascending) throws IOException {
    if (registrationNumberIndex.isEmpty()) {
      System.out.println("База данных пуста");
      return;
    }

    System.out.println("\n" + "=".repeat(80));
    System.out.println("АВТОМОБИЛИ ОТСОРТИРОВАННЫЕ ПО РЕГИСТРАЦИОННОМУ НОМЕРУ (" +
        (ascending ? "ВОЗРАСТАНИЕ" : "УБЫВАНИЕ") + ")");
    System.out.println("=".repeat(80));
    System.out.printf("%-10s %-12s %-6s %-8s %-10s %s%n",
        "Марка", "Модель", "Год", "Цвет", "Цена", "Рег. номер");
    System.out.println("-".repeat(80));

    List<String> regNumbers = new ArrayList<>(registrationNumberIndex.keySet());
    Collections.sort(regNumbers);
    if (!ascending) {
      Collections.reverse(regNumbers);
    }

    for (String regNumber : regNumbers) {
      Car car = readCar(registrationNumberIndex.get(regNumber));
      if (car != null) {
        System.out.println(car.toFormattedString());
      }
    }
    System.out.println("=".repeat(80));
  }

  public void findCarByRegistrationNumber(String regNumber) throws IOException {
    Long position = registrationNumberIndex.get(regNumber.toUpperCase());
    if (position != null) {
      Car car = readCar(position);
      if (car != null) {
        System.out.println("\n✓ Найден автомобиль:");
        System.out.println("  Марка: " + car.getBrand());
        System.out.println("  Модель: " + car.getModel());
        System.out.println("  Год: " + car.getYear());
        System.out.println("  Цвет: " + car.getColor());
        System.out.println("  Цена: " + car.getPrice());
        System.out.println("  Рег. номер: " + car.getRegistrationNumber());
      }
    } else {
      System.out.println("\n✗ Автомобиль с номером " + regNumber + " не найден");
    }
  }

  public void findCarsByBrand(String brand) throws IOException {
    List<Long> positions = brandIndex.get(brand);
    if (positions != null && !positions.isEmpty()) {
      System.out.println("\n" + "=".repeat(60));
      System.out.println("АВТОМОБИЛИ МАРКИ: " + brand + " (" + positions.size() + " шт.)");
      System.out.println("=".repeat(60));
      System.out.printf("%-12s %-6s %-8s %-10s %s%n",
          "Модель", "Год", "Цвет", "Цена", "Рег. номер");
      System.out.println("-".repeat(60));

      for (Long position : positions) {
        Car car = readCar(position);
        if (car != null) {
          System.out.printf("%-12s %-6d %-8s %-10.2f %s%n",
              car.getModel(), car.getYear(), car.getColor(),
              car.getPrice(), car.getRegistrationNumber());
        }
      }
      System.out.println("=".repeat(60));
    } else {
      System.out.println("\n✗ Автомобили марки " + brand + " не найдены");
    }
  }

  public void findCarsWithBrandGreaterThan(String brand) throws IOException {
    SortedMap<String, List<Long>> tailMap = brandIndex.tailMap(brand, false);
    if (!tailMap.isEmpty()) {
      System.out.println("\n" + "=".repeat(60));
      System.out.println("АВТОМОБИЛИ С МАРКОЙ ПОСЛЕ '" + brand + "'");
      System.out.println("=".repeat(60));
      printCarsFromIndex(tailMap);
    } else {
      System.out.println("\n✗ Автомобили с маркой после '" + brand + "' не найдены");
    }
  }

  public void findCarsWithBrandLessThan(String brand) throws IOException {
    SortedMap<String, List<Long>> headMap = brandIndex.headMap(brand, false);
    if (!headMap.isEmpty()) {
      System.out.println("\n" + "=".repeat(60));
      System.out.println("АВТОМОБИЛИ С МАРКОЙ ДО '" + brand + "'");
      System.out.println("=".repeat(60));
      printCarsFromIndex(headMap);
    } else {
      System.out.println("\n✗ Автомобили с маркой до '" + brand + "' не найдены");
    }
  }

  private void printCarsFromIndex(SortedMap<String, List<Long>> indexMap) throws IOException {
    System.out.printf("%-10s %-12s %-6s %-8s %-10s %s%n",
        "Марка", "Модель", "Год", "Цвет", "Цена", "Рег. номер");
    System.out.println("-".repeat(60));

    int total = 0;
    for (Map.Entry<String, List<Long>> entry : indexMap.entrySet()) {
      for (Long position : entry.getValue()) {
        Car car = readCar(position);
        if (car != null) {
          System.out.println(car.toFormattedString());
          total++;
        }
      }
    }
    System.out.println("=".repeat(60));
    System.out.println("Найдено автомобилей: " + total);
  }

  public boolean deleteCarByRegistrationNumber(String regNumber) throws IOException {
    Long position = registrationNumberIndex.get(regNumber.toUpperCase());
    if (position != null) {
      file.seek(position + 4);
      file.writeInt(0);

      Car car = readCar(position);
      if (car != null) {
        List<Long> brandPositions = brandIndex.get(car.getBrand());
        if (brandPositions != null) {
          brandPositions.remove(position);
          if (brandPositions.isEmpty()) {
            brandIndex.remove(car.getBrand());
          }
        }

        List<Long> modelPositions = modelIndex.get(car.getModel());
        if (modelPositions != null) {
          modelPositions.remove(position);
          if (modelPositions.isEmpty()) {
            modelIndex.remove(car.getModel());
          }
        }

        List<Long> pricePositions = priceIndex.get(car.getPrice());
        if (pricePositions != null) {
          pricePositions.remove(position);
          if (pricePositions.isEmpty()) {
            priceIndex.remove(car.getPrice());
          }
        }

        registrationNumberIndex.remove(regNumber.toUpperCase());
      }

      return true;
    }
    return false;
  }

  public void addCarFromConsole() throws IOException {
    Scanner scanner = new Scanner(System.in);

    try {
      System.out.println("\nДОБАВЛЕНИЕ НОВОГО АВТОМОБИЛЯ");

      System.out.print("Марка: ");
      String brand = scanner.nextLine();

      System.out.print("Модель: ");
      String model = scanner.nextLine();

      System.out.print("Год выпуска: ");
      int year = Integer.parseInt(scanner.nextLine());

      System.out.print("Цвет: ");
      String color = scanner.nextLine();

      System.out.print("Цена: ");
      double price = Double.parseDouble(scanner.nextLine());

      System.out.print("Регистрационный номер: ");
      String regNumber = scanner.nextLine();

      Car car = ValidationUtil.createValidatedCar(brand, model, year, color, price, regNumber);
      addCar(car);
      System.out.println("✓ Автомобиль успешно добавлен!");

    } catch (NumberFormatException e) {
      System.out.println("✗ Ошибка: неверный числовой формат");
    } catch (IllegalArgumentException e) {
      System.out.println("✗ Ошибка валидации: " + e.getMessage());
    } catch (Exception e) {
      System.out.println("✗ Ошибка: " + e.getMessage());
    }
  }

  public void findCarsByPrice(double price) throws IOException {
    List<Long> positions = priceIndex.get(price);
    if (positions != null && !positions.isEmpty()) {
      System.out.println("\n" + "=".repeat(60));
      System.out.println("АВТОМОБИЛИ С ЦЕНОЙ: " + price);
      System.out.println("=".repeat(60));
      System.out.printf("%-10s %-12s %-6s %-8s %-10s %s%n",
          "Марка", "Модель", "Год", "Цвет", "Цена", "Рег. номер");
      System.out.println("-".repeat(60));

      for (Long position : positions) {
        Car car = readCar(position);
        if (car != null) {
          System.out.println(car.toFormattedString());
        }
      }
      System.out.println("=".repeat(60));
    } else {
      System.out.println("\n✗ Автомобили с ценой " + price + " не найдены");
    }
  }

  public void findCarsByPriceRange(double minPrice, double maxPrice) throws IOException {
    SortedMap<Double, List<Long>> sub = priceIndex.subMap(minPrice, true, maxPrice, true);
    if (sub.isEmpty()) {
      System.out.println("\n✗ Автомобили в диапазоне цен " + minPrice + " - " + maxPrice + " не найдены");
      return;
    }

    System.out.println("\n" + "=".repeat(60));
    System.out.println("АВТОМОБИЛИ В ДИАПАЗОНЕ ЦЕН: " + minPrice + " - " + maxPrice);
    System.out.println("=".repeat(60));
    System.out.printf("%-10s %-12s %-6s %-8s %-10s %s%n",
        "Марка", "Модель", "Год", "Цвет", "Цена", "Рег. номер");
    System.out.println("-".repeat(60));

    int total = 0;
    for (Map.Entry<Double, List<Long>> entry : sub.entrySet()) {
      for (Long position : entry.getValue()) {
        Car car = readCar(position);
        if (car != null) {
          System.out.println(car.toFormattedString());
          total++;
        }
      }
    }
    System.out.println("=".repeat(60));
    System.out.println("Найдено автомобилей: " + total);
  }

  public void printStatistics() {
    System.out.println("\nСТАТИСТИКА БАЗЫ ДАННЫХ");
    System.out.println("Всего автомобилей: " + registrationNumberIndex.size());
    System.out.println("Уникальных марок: " + brandIndex.size());
    System.out.println("Уникальных моделей: " + modelIndex.size());
    System.out.println("Уникальных цен: " + priceIndex.size());

    if (!brandIndex.isEmpty()) {
      System.out.println("\nРаспределение по маркам:");
      for (Map.Entry<String, List<Long>> entry : brandIndex.entrySet()) {
        System.out.println("  " + entry.getKey() + ": " + entry.getValue().size() + " авто");
      }
    }

    if (!priceIndex.isEmpty()) {
      System.out.println("\nРаспределение по ценам:");
      for (Map.Entry<Double, List<Long>> entry : priceIndex.entrySet()) {
        System.out.println("  " + entry.getKey() + ": " + entry.getValue().size() + " авто");
      }
    }

    try {
      System.out.println("\nРазмер файла данных: " + file.length() + " байт");
    } catch (IOException e) {
      System.out.println("\nНе удалось получить информацию о файле");
    }
  }

  @Override
  public void close() throws IOException {
    if (file != null) {
      file.close();
    }
  }
}
