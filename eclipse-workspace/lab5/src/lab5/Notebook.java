package lab5;

import java.util.*;

public class Notebook extends Computer {
  private double batteryLife;

  public Notebook(String model, double price, String processor, int ram, double batteryLife) {
    super(model, price, processor, ram);
    setBatteryLife(batteryLife);
  }

  public double getBatteryLife() {
    return batteryLife;
  }

  public void setBatteryLife(double batteryLife) {
    if (batteryLife <= 0) {
      throw new IllegalArgumentException("Время работы батареи должно быть положительным");
    }
    this.batteryLife = batteryLife;
  }

  public static Comparator<Notebook> byBatteryLife = Comparator.comparing(Notebook::getBatteryLife);
  public static Comparator<Notebook> byModel = Comparator.comparing(Notebook::getModel);
  public static Comparator<Notebook> byPrice = Comparator.comparing(Notebook::getPrice);

  @Override
  public Iterator<String> iterator() {
    List<String> fields = new ArrayList<>();
    Iterator<String> superIterator = super.iterator();
    while (superIterator.hasNext()) {
      fields.add(superIterator.next());
    }
    fields.add(String.valueOf(batteryLife));
    return fields.iterator();
  }

  @Override
  public String toString() {
    return super.toString() + "|" + batteryLife;
  }
}
