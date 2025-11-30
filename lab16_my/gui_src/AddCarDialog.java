import javax.swing.*;
import java.awt.*;

/**
 * Диалог добавления автомобиля.
 */
public class AddCarDialog extends JDialog {

  private JTextField brandField;
  private JTextField modelField;
  private JTextField yearField;
  private JTextField colorField;
  private JTextField priceField;
  private JTextField regNumberField;
  private boolean success = false;
  private Car car;

  public AddCarDialog(JFrame parent) {
    super(parent, "Добавить автомобиль", true);
    initUI();
    pack();
    setLocationRelativeTo(parent);
  }

  private void initUI() {
    setLayout(new BorderLayout(10, 10));

    JPanel fields = new JPanel(new GridLayout(6, 2, 5, 5));

    fields.add(new JLabel("Марка:"));
    brandField = new JTextField();
    fields.add(brandField);

    fields.add(new JLabel("Модель:"));
    modelField = new JTextField();
    fields.add(modelField);

    fields.add(new JLabel("Год:"));
    yearField = new JTextField();
    fields.add(yearField);

    fields.add(new JLabel("Цвет:"));
    colorField = new JTextField();
    fields.add(colorField);

    fields.add(new JLabel("Цена:"));
    priceField = new JTextField();
    fields.add(priceField);

    fields.add(new JLabel("Рег. номер:"));
    regNumberField = new JTextField();
    fields.add(regNumberField);

    add(fields, BorderLayout.CENTER);

    JPanel buttons = new JPanel();
    JButton ok = new JButton("OK");
    JButton cancel = new JButton("Отмена");
    buttons.add(ok);
    buttons.add(cancel);
    add(buttons, BorderLayout.SOUTH);

    ok.addActionListener(e -> onOk());
    cancel.addActionListener(e -> onCancel());
  }

  private void onOk() {
    try {
      String brand = brandField.getText().trim();
      String model = modelField.getText().trim();
      int year = Integer.parseInt(yearField.getText().trim());
      String color = colorField.getText().trim();
      double price = Double.parseDouble(priceField.getText().trim());
      String reg = regNumberField.getText().trim();

      car = ValidationUtil.createValidatedCar(brand, model, year, color, price, reg);
      success = true;
      dispose();
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this,
          "Некорректный формат числа (год/цена).",
          "Ошибка", JOptionPane.ERROR_MESSAGE);
    } catch (IllegalArgumentException ex) {
      JOptionPane.showMessageDialog(this,
          ex.getMessage(),
          "Ошибка валидации", JOptionPane.ERROR_MESSAGE);
    } catch (Exception ex) {
      JOptionPane.showMessageDialog(this,
          "Ошибка: " + ex.getMessage(),
          "Ошибка", JOptionPane.ERROR_MESSAGE);
    }
  }

  private void onCancel() {
    success = false;
    dispose();
  }

  public boolean isSuccess() {
    return success;
  }

  public Car getCar() {
    return car;
  }
}
