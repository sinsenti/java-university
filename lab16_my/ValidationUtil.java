import java.util.regex.Pattern;

public class ValidationUtil {

  public static void validateBrand(String brand) {
    if (brand == null || brand.trim().isEmpty()) {
      throw new IllegalArgumentException("Марка не может быть пустой");
    }
    if (brand.length() > 20) {
      throw new IllegalArgumentException("Марка слишком длинная");
    }
  }

  public static void validateModel(String model) {
    if (model == null || model.trim().isEmpty()) {
      throw new IllegalArgumentException("Модель не может быть пустой");
    }
  }

  public static void validateYear(int year) {
    int currentYear = java.time.Year.now().getValue();
    if (year < 1900 || year > currentYear + 1) {
      throw new IllegalArgumentException("Некорректный год выпуска: " + year);
    }
  }

  public static void validateColor(String color) {
    if (color == null || color.trim().isEmpty()) {
      throw new IllegalArgumentException("Цвет не может быть пустым");
    }
  }

  public static void validatePrice(double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Цена не может быть отрицательной");
    }
  }

  public static void validateRegistrationNumber(String registrationNumber) {
    if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
      throw new IllegalArgumentException("Регистрационный номер не может быть пустым");
    }

    String pattern = "^[A-Za-z0-9]{2,10}$";
    if (!Pattern.matches(pattern, registrationNumber)) {
      throw new IllegalArgumentException("Неверный формат регистрационного номера");
    }
  }

  public static Car createValidatedCar(String brand, String model, int year,
      String color, double price, String registrationNumber) {
    validateBrand(brand);
    validateModel(model);
    validateYear(year);
    validateColor(color);
    validatePrice(price);
    validateRegistrationNumber(registrationNumber);

    return new Car(brand, model, year, color, price, registrationNumber);
  }
}
