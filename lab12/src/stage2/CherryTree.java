package stage2;

import java.io.Serializable;

public class CherryTree extends GardenTree implements Serializable {
  private static final long serialVersionUID = 3L;

  private boolean isSweet;

  public CherryTree(int age, int fruiting, boolean isSweet) {
    super(age, fruiting);
    this.isSweet = isSweet;
  }

  public CherryTree() {
    super();
    this.isSweet = true;
  }

  public boolean isSweet() {
    return isSweet;
  }

  public void setSweet(boolean sweet) {
    isSweet = sweet;
  }

  @Override
  public String toString() {
    String type = isSweet ? "Sweet" : "Sour";
    return AppLocale.getString(AppLocale.cherry_tree) + " (" + type + ") - " + super.toString();
  }
}
