import javax.swing.*;
import java.io.IOException;

/**
 * Точка входа для GUI-версии.
 */
public class CarDatabaseGuiApp {

  private static final String DATABASE_FILE = "cars.dat";

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      try {
        CarDatabase database = new CarDatabase(DATABASE_FILE);
        CarService service = new CarService(database);
        CarMainFrame frame = new CarMainFrame(service, database);
        frame.setVisible(true);
      } catch (IOException e) {
        JOptionPane.showMessageDialog(null,
            "Ошибка инициализации базы: " + e.getMessage(),
            "Ошибка", JOptionPane.ERROR_MESSAGE);
      }
    });
  }
}
