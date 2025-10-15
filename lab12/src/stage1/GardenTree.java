package stage1;

import java.io.Serializable;

public class GardenTree implements Serializable {
  private static final long serialVersionUID = 1L;
  private static int nextId = 1;

  private final int id;
  private int age;
  private int fruiting;

  public GardenTree(int age, int fruiting) {
    this.id = nextId++;
    this.age = age;
    this.fruiting = fruiting;
  }

  protected GardenTree() {
    this.id = nextId++;
    this.age = 0;
    this.fruiting = 0;
  }

  public int getId() {
    return id;
  }

  public int getAge() {
    return age;
  }

  public int getFruiting() {
    return fruiting;
  }

  public void setAge(int age) {
    if (age < 0)
      throw new IllegalArgumentException("Age cannot be negative");
    this.age = age;
  }

  public void setFruiting(int fruiting) {
    if (fruiting < 0)
      throw new IllegalArgumentException("Fruiting cannot be negative");
    this.fruiting = fruiting;
  }

  public boolean needsTransplant() {
    return age > 5 && fruiting < 20;
  }

  @Override
  public String toString() {
    return "Tree ID: " + id + ", Age: " + age + ", Fruiting: " + fruiting +
        ", Transplant: " + (needsTransplant() ? "Needed" : "Not needed");
  }
}
