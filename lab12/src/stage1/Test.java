package stage1;

import java.io.File;
import java.nio.file.Paths;

public class Test {

  public static GardenTree[] createGarden() {
    GardenTree[] garden = new GardenTree[6];

    garden[0] = new AppleTree(3, 25, "Golden Delicious");
    garden[1] = new AppleTree(7, 15, "Granny Smith");
    garden[2] = new CherryTree(4, 30, true);
    garden[3] = new CherryTree(8, 10, false);
    garden[4] = new PearTree(5, 22, 8.5);
    garden[5] = new PearTree(9, 12, 6.2);

    return garden;
  }

  public static void main(String[] args) {
    try {
      File dataDir = new File("data");
      if (!dataDir.exists()) {
        dataDir.mkdirs();
      }

      Connector con = new Connector(new File(dataDir, "garden_trees_stage1.dat"));
      con.write(createGarden());

      GardenTree[] garden = con.read();
      System.out.println("Garden Trees:");
      for (GardenTree tree : garden) {
        System.out.println(tree);
      }

    } catch (IllegalArgumentException e) {
      System.err.println("Invalid data: " + e.getMessage());
    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
