package stage2;

import java.util.*;
import java.io.*;

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

  static Locale createLocale(String[] args) {
    if (args.length == 2) {
      return new Locale(args[0], args[1]);
    } else if (args.length == 4) {
      return new Locale(args[2], args[3]);
    }
    return null;
  }

  static void setupConsole(String[] args) {
    if (args.length >= 2) {
      if (args[0].equals("-encoding")) {
        try {
          System.setOut(new PrintStream(System.out, true, args[1]));
        } catch (UnsupportedEncodingException ex) {
          System.err.println("Unsupported encoding: " + args[1]);
          System.exit(1);
        }
      }
    }
  }

  public static void main(String[] args) {
    try {
      setupConsole(args);
      Locale loc = createLocale(args);

      if (loc == null) {
        System.err.println("Invalid argument(s)\n" +
            "Syntax: [-encoding ENCODING_ID] language country\n" +
            "Example: -encoding UTF-8 ru RU\n" +
            "Example: be BY");
        System.exit(1);
      }

      AppLocale.set(loc);

      // Debug: Check if locale is set correctly
      System.err.println("Locale set to: " + loc);
      System.err.println("Testing resource bundle - Tree: " + AppLocale.getString(AppLocale.tree));
      System.err.println("Testing resource bundle - Garden: " + AppLocale.getString(AppLocale.garden));

      // Create data directory if it doesn't exist
      File dataDir = new File("data");
      if (!dataDir.exists()) {
        dataDir.mkdirs();
      }

      Connector con = new Connector(new File(dataDir, "garden_trees_stage2.dat"));
      con.write(createGarden());

      GardenTree[] garden = con.read();
      System.out.println(AppLocale.getString(AppLocale.garden) + ":");
      for (GardenTree tree : garden) {
        System.out.println(tree);
      }

    } catch (Exception e) {
      System.err.println("Error: " + e.getMessage());
      e.printStackTrace();
    }
  }
}
