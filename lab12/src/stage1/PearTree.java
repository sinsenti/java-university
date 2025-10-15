package stage1;

import java.io.Serializable;

public class PearTree extends GardenTree implements Serializable {
  private static final long serialVersionUID = 4L;

  private double fruitSize;

  public PearTree(int age, int fruiting, double fruitSize) {
    super(age, fruiting);
    this.fruitSize = fruitSize;
  }

  public PearTree() {
    super();
    this.fruitSize = 0.0;
  }

  public double getFruitSize() {
    return fruitSize;
  }

  public void setFruitSize(double fruitSize) {
    if (fruitSize < 0)
      throw new IllegalArgumentException("Fruit size cannot be negative");
    this.fruitSize = fruitSize;
  }

  @Override
  public String toString() {
    return "Pear Tree (Fruit size: " + fruitSize + "cm) - " + super.toString();
  }
}
