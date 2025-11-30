import javax.swing.*;
import java.awt.*;

public class SortDialog extends JDialog {

  private JComboBox<String> fieldCombo;
  private JCheckBox reverseCheck;
  private boolean confirmed = false;

  public SortDialog(JFrame parent) {
    super(parent, "Сортировка", true);
    initUI();
    pack();
    setLocationRelativeTo(parent);
  }

  private void initUI() {
    setLayout(new BorderLayout(10, 10));

    JPanel center = new JPanel(new GridLayout(2, 2, 5, 5));
    center.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    center.add(new JLabel("Поле:"));
    fieldCombo = new JComboBox<>(new String[] { "Марка", "Рег. номер" });
    center.add(fieldCombo);

    center.add(new JLabel("Обратный порядок:"));
    reverseCheck = new JCheckBox();
    center.add(reverseCheck);

    add(center, BorderLayout.CENTER);

    JPanel south = new JPanel();
    JButton ok = new JButton("OK");
    JButton cancel = new JButton("Отмена");
    south.add(ok);
    south.add(cancel);
    add(south, BorderLayout.SOUTH);

    ok.addActionListener(e -> {
      confirmed = true;
      dispose();
    });
    cancel.addActionListener(e -> {
      confirmed = false;
      dispose();
    });
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public String getSortField() {
    return (String) fieldCombo.getSelectedItem();
  }

  public boolean isReverse() {
    return reverseCheck.isSelected();
  }
}
