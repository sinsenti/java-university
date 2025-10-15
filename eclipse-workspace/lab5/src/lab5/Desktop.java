package lab5;

import java.util.*;

public class Desktop extends Computer {
  private String formFactor;

  public Desktop(String model, double price, String processor, int ram, String formFactor) {
    super(model, price, processor, ram);
    setFormFactor(formFactor);
  }

  public String getFormFactor() {
    return formFactor;
  }

  public void setFormFactor(String formFactor) {
    if (formFactor == null || formFactor.trim().isEmpty()) {
      throw new IllegalArgumentException("Форм-фактор не может быть пустым");
    }
    this.formFactor = formFactor;
  }

  public static Comparator<Desktop> byModel = Comparator.comparing(Desktop::getModel);
  public static Comparator<Desktop> byPrice = Comparator.comparing(Desktop::getPrice);
  public static Comparator<Desktop> byFormFactor = Comparator.comparing(Desktop::getFormFactor);

  @Override
  public Iterator<String> iterator() {
    List<String> fields = new ArrayList<>();
    Iterator<String> superIterator = super.iterator();
    while (superIterator.hasNext()) {
      fields.add(superIterator.next());
    }
    fields.add(formFactor);
    return fields.iterator();
  }

  @Override
  public String toString() {
    return super.toString() + "|" + formFactor;
  }
}
