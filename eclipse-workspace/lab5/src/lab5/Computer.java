package lab5;

import java.util.*;

public abstract class Computer implements Comparable<Computer>, Iterable<String> {
  protected String model;
  protected double price;
  protected String processor;
  protected int ram;

  public Computer(String model, double price, String processor, int ram) {
    setModel(model);
    setPrice(price);
    setProcessor(processor);
    setRam(ram);
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    if (model == null || model.trim().isEmpty()) {
      throw new IllegalArgumentException("Модель не может быть пустой");
    }
    this.model = model;
  }

  public double getPrice() {
    return price;
  }

  public void setPrice(double price) {
    if (price <= 0) {
      throw new IllegalArgumentException("Цена должна быть положительной");
    }
    this.price = price;
  }

  public String getProcessor() {
    return processor;
  }

  public void setProcessor(String processor) {
    if (processor == null || processor.trim().isEmpty()) {
      throw new IllegalArgumentException("Процессор не может быть пустым");
    }
    this.processor = processor;
  }

  public int getRam() {
    return ram;
  }

  public void setRam(int ram) {
    if (ram <= 0) {
      throw new IllegalArgumentException("ОЗУ должна быть положительной");
    }
    this.ram = ram;
  }

  @Override
  public int compareTo(Computer other) {
    return Double.compare(this.price, other.price);
  }

  @Override
  public Iterator<String> iterator() {
    List<String> fields = Arrays.asList(
        model,
        String.valueOf(price),
        processor,
        String.valueOf(ram));
    return fields.iterator();
  }

  @Override
  public String toString() {
    return String.format("%s|%.2f|%s|%d", model, price, processor, ram);
  }

  public static Computer fromString(String data) {
    String[] parts = data.split("\\|");
    if (parts.length < 4)
      throw new IllegalArgumentException("Неверный формат данных");

    String model = parts[0];
    double price = Double.parseDouble(parts[1]);
    String processor = parts[2];
    int ram = Integer.parseInt(parts[3]);

    if (parts.length == 4) {
      return new Desktop(model, price, processor, ram, "Standard");
    } else if (parts.length == 5) {
      try {
        Double.parseDouble(parts[4]);
        return new Notebook(model, price, processor, ram, Double.parseDouble(parts[4]));
      } catch (NumberFormatException e) {
        return new Desktop(model, price, processor, ram, parts[4]);
      }
    }
    throw new IllegalArgumentException("Неверный формат данных");
  }
}
