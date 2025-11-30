import java.io.Serializable;
import java.util.Objects;

public class Car implements Serializable {
  private static final long serialVersionUID = 1L;

  private String brand;
  private String model;
  private int year;
  private String color;
  private double price;
  private String registrationNumber;

  public Car() {
  }

  public Car(String brand, String model, int year, String color, double price, String registrationNumber) {
    setBrand(brand);
    setModel(model);
    setYear(year);
    setColor(color);
    setPrice(price);
    setRegistrationNumber(registrationNumber);
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    if (brand == null || brand.trim().isEmpty()) {
      throw new IllegalArgumentException("Марка не может быть пустой");
    }
    this.brand = brand.trim();
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    if (model == null || model.trim().isEmpty()) {
      throw new IllegalArgumentException("Модель не может быть пустой");
    }
    this.model = model.trim();
  }

  public int getYear() {
    return year;
  }

  public void setYear(int year) {
    int currentYear = java.time.Year.now().getValue();
    if (year < 1900 || year > currentYear + 1) {
      throw new IllegalArgumentException("Некорректный год выпуска: " + year);
    }
    this.year = year;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    if (color == null || color.trim().isEmpty()) {
      throw new IllegalArgumentException("Цвет не может быть пустым");
    }
    this.color = color.trim();
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    if (price < 0) {
      throw new IllegalArgumentException("Цена не может быть отрицательной");
    }
    this.price = price;
  }

  public String getRegistrationNumber() {
    return registrationNumber;
  }

  public void setRegistrationNumber(String registrationNumber) {
    if (registrationNumber == null || registrationNumber.trim().isEmpty()) {
      throw new IllegalArgumentException("Регистрационный номер не может быть пустым");
    }
    this.registrationNumber = registrationNumber.trim().toUpperCase();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o)
      return true;
    if (o == null || getClass() != o.getClass())
      return false;
    Car car = (Car) o;
    return registrationNumber.equals(car.registrationNumber);
  }

  @Override
  public int hashCode() {
    return Objects.hash(registrationNumber);
  }

  @Override
  public String toString() {
    return String.format("Car{brand='%s', model='%s', year=%d, color='%s', price=%.2f, regNumber='%s'}",
        brand, model, year, color, price, registrationNumber);
  }

  public String toFormattedString() {
    return String.format("%-10s %-12s %-6d %-8s %-10.2f %s",
        brand, model, year, color, price, registrationNumber);
  }
}
