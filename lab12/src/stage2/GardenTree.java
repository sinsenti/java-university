package stage2;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;

public class GardenTree implements Serializable {
  private static final long serialVersionUID = 1L;
  private static int nextId = 1;

  private final int id;
  private int age;
  private int fruiting;
  public final Date creationDate = new Date();

  public GardenTree(int age, int fruiting) {
    this.id = nextId++;
    setAge(age);
    setFruiting(fruiting);
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

  public String getCreationDate() {
    DateFormat dateFormatter = DateFormat.getDateTimeInstance(
        DateFormat.DEFAULT, DateFormat.DEFAULT, AppLocale.get());
    return dateFormatter.format(creationDate);
  }

  public boolean needsTransplant() {
    return age > 5 && fruiting < 20;
  }

  public String getTransplantDecision() {
    return needsTransplant() ? AppLocale.getString(AppLocale.needs_transplant)
        : AppLocale.getString(AppLocale.no_transplant);
  }

  @Override
  public String toString() {
    return AppLocale.getString(AppLocale.tree) + ": " +
        AppLocale.getString(AppLocale.tree_id) + " " + id + ", " +
        AppLocale.getString(AppLocale.age) + ": " + age + ", " +
        AppLocale.getString(AppLocale.fruiting) + ": " + fruiting + ", " +
        AppLocale.getString(AppLocale.creation) + ": " + getCreationDate() + ", " +
        AppLocale.getString(AppLocale.transplant_decision) + ": " + getTransplantDecision();
  }
}
