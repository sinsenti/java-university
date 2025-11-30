import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Обертка над CarDatabase, которая возвращает списки Car,
 * вместо вывода в консоль.
 */
public class CarService {

  private final CarDatabase database;

  public CarService(CarDatabase database) {
    this.database = database;
  }

  public List<Car> getAllCars() throws IOException {
    List<Car> result = new ArrayList<>();
    for (Map.Entry<String, Long> entry : database.getRegistrationNumberIndex().entrySet()) {
      Car car = database.readCar(entry.getValue());
      if (car != null) {
        result.add(car);
      }
    }
    return result;
  }

  public void addCar(Car car) throws IOException {
    database.addCar(car);
  }

  public boolean deleteByRegistrationNumber(String regNumber) throws IOException {
    return database.deleteCarByRegistrationNumber(regNumber);
  }

  public void fillWithTestData() throws IOException {
    database.fillWithTestData();
  }

  // --- Поиск ---

  public Car findCarByRegistrationNumber(String regNumber) throws IOException {
    Long pos = database.getRegistrationNumberIndex().get(regNumber.toUpperCase());
    if (pos == null)
      return null;
    return database.readCar(pos);
  }

  public List<Car> findCarsByBrand(String brand) throws IOException {
    List<Car> result = new ArrayList<>();
    List<Long> positions = database.getBrandIndex().get(brand);
    if (positions == null)
      return result;

    for (Long p : positions) {
      Car car = database.readCar(p);
      if (car != null) {
        result.add(car);
      }
    }
    return result;
  }

  public List<Car> findCarsWithBrandGreaterThan(String brand) throws IOException {
    List<Car> result = new ArrayList<>();
    for (Map.Entry<String, java.util.List<Long>> e :
        database.getBrandIndex().tailMap(brand, false).entrySet()) {
      for (Long p : e.getValue()) {
        Car car = database.readCar(p);
        if (car != null) {
          result.add(car);
        }
      }
    }
    return result;
  }

  public List<Car> findCarsWithBrandLessThan(String brand) throws IOException {
    List<Car> result = new ArrayList<>();
    for (Map.Entry<String, java.util.List<Long>> e :
        database.getBrandIndex().headMap(brand, false).entrySet()) {
      for (Long p : e.getValue()) {
        Car car = database.readCar(p);
        if (car != null) {
          result.add(car);
        }
      }
    }
    return result;
  }

  // --- Сортировки (по аналогии с консолью) ---

  public List<Car> getCarsSortedByBrand(boolean ascending) throws IOException {
    List<Car> result = new ArrayList<>();
    List<String> brands = new ArrayList<>(database.getBrandIndex().keySet());
    Collections.sort(brands);
    if (!ascending) {
      Collections.reverse(brands);
    }
    for (String b : brands) {
      for (Long p : database.getBrandIndex().get(b)) {
        Car car = database.readCar(p);
        if (car != null) {
          result.add(car);
        }
      }
    }
    return result;
  }

  public List<Car> getCarsSortedByRegistrationNumber(boolean ascending) throws IOException {
    List<Car> result = new ArrayList<>();
    List<String> regNumbers = new ArrayList<>(database.getRegistrationNumberIndex().keySet());
    Collections.sort(regNumbers);
    if (!ascending) {
      Collections.reverse(regNumbers);
    }
    for (String reg : regNumbers) {
      Car car = database.readCar(database.getRegistrationNumberIndex().get(reg));
      if (car != null) {
        result.add(car);
      }
    }
    return result;
  }
}
