package stage2;

import java.util.*;
import java.io.*;

public class AppLocale {
  private static final String BASE_NAME = "Msg";
  private static Locale currentLocale = Locale.getDefault();
  private static ResourceBundle resourceBundle;

  static {
    loadBundle();
  }

  private static void loadBundle() {
    try {
      resourceBundle = ResourceBundle.getBundle(BASE_NAME, currentLocale,
          new ResourceBundle.Control() {
            @Override
            public ResourceBundle newBundle(String baseName, Locale locale, String format,
                ClassLoader loader, boolean reload)
                throws IllegalAccessException, InstantiationException, IOException {
              String bundleName = toBundleName(baseName, locale);
              String resourceName = toResourceName(bundleName, "properties");
              try (InputStream stream = loader.getResourceAsStream(resourceName)) {
                if (stream != null) {
                  return new PropertyResourceBundle(new InputStreamReader(stream, "UTF-8"));
                }
              }
              return super.newBundle(baseName, locale, format, loader, reload);
            }
          });
    } catch (Exception e) {
      System.err.println("Error loading resource bundle: " + e.getMessage());
      resourceBundle = createFallbackBundle();
    }
  }

  private static ResourceBundle createFallbackBundle() {
    return new ResourceBundle() {
      @Override
      protected Object handleGetObject(String key) {
        switch (key) {
          case "tree":
            return "Tree";
          case "age":
            return "Age";
          case "fruiting":
            return "Fruiting";
          case "creation":
            return "Creation Time";
          case "needs_transplant":
            return "Needs transplant";
          case "no_transplant":
            return "No transplant needed";
          case "apple_tree":
            return "Apple Tree";
          case "cherry_tree":
            return "Cherry Tree";
          case "pear_tree":
            return "Pear Tree";
          case "garden":
            return "Trees Garden";
          case "tree_id":
            return "Tree ID";
          case "transplant_decision":
            return "Transplant decision";
          default:
            return "[" + key + "]";
        }
      }

      @Override
      public Enumeration<String> getKeys() {
        Vector<String> keys = new Vector<>();
        keys.add("tree");
        keys.add("age");
        keys.add("fruiting");
        keys.add("creation");
        keys.add("needs_transplant");
        keys.add("no_transplant");
        keys.add("apple_tree");
        keys.add("cherry_tree");
        keys.add("pear_tree");
        keys.add("garden");
        keys.add("tree_id");
        keys.add("transplant_decision");
        return keys.elements();
      }
    };
  }

  public static Locale get() {
    return currentLocale;
  }

  public static void set(Locale locale) {
    currentLocale = locale;
    loadBundle();
  }

  public static ResourceBundle getBundle() {
    return resourceBundle;
  }

  public static String getString(String key) {
    try {
      return resourceBundle.getString(key);
    } catch (MissingResourceException e) {
      return "[" + key + "]";
    }
  }

  public static final String tree = "tree";
  public static final String age = "age";
  public static final String fruiting = "fruiting";
  public static final String creation = "creation";
  public static final String needs_transplant = "needs_transplant";
  public static final String no_transplant = "no_transplant";
  public static final String apple_tree = "apple_tree";
  public static final String cherry_tree = "cherry_tree";
  public static final String pear_tree = "pear_tree";
  public static final String garden = "garden";
  public static final String tree_id = "tree_id";
  public static final String transplant_decision = "transplant_decision";
}
