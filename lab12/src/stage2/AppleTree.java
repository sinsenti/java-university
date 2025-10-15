package stage2;

import java.io.Serializable;

public class AppleTree extends GardenTree implements Serializable {
  private static final long serialVersionUID = 2L;

  private String appleType;

  public AppleTree(int age, int fruiting, String appleType) {
    super(age, fruiting);
    this.appleType = appleType;
  }

  public AppleTree() {
    super();
    this.appleType = "Unknown";
  }

  public String getAppleType() {
    return appleType;
  }

  public void setAppleType(String appleType) {
    this.appleType = appleType;
  }

  @Override
  public String toString() {
    return AppLocale.getString(AppLocale.apple_tree) + " (" + appleType + ") - " + super.toString();
  }
}
