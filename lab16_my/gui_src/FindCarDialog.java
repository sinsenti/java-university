import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;

public class FindCarDialog extends JDialog {

  private JComboBox<String> searchTypeComboBox;
  private JComboBox<String> searchConditionComboBox;
  private JTextField searchValueField;
  private boolean confirmed = false;
  private String searchType;
  private String searchCondition;
  private String searchValue;

  public FindCarDialog(JFrame parent) {
    super(parent, "Поиск автомобиля", true);
    initializeComponents();
    setupEventHandlers();
    pack();
    setLocationRelativeTo(parent);
  }

  private void initializeComponents() {
    setLayout(new BorderLayout());
    setPreferredSize(new Dimension(350, 200));

    JPanel mainPanel = new JPanel();
    mainPanel.setLayout(new GridLayout(4, 2, 10, 10));
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    mainPanel.add(new JLabel("Искать по:"));
    searchTypeComboBox = new JComboBox<>(new String[] {
        "Марка", "Регистрационный номер"
    });
    mainPanel.add(searchTypeComboBox);

    mainPanel.add(new JLabel("Условие:"));
    searchConditionComboBox = new JComboBox<>(new String[] {
        "Равно", "Больше (по алф.)", "Меньше (по алф.)"
    });
    mainPanel.add(searchConditionComboBox);

    mainPanel.add(new JLabel("Значение:"));
    searchValueField = new JTextField(15);
    mainPanel.add(searchValueField);

    mainPanel.add(new JLabel());
    mainPanel.add(new JLabel());

    add(mainPanel, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    JButton btnOK = new JButton("Найти");
    JButton btnCancel = new JButton("Отмена");
    buttonPanel.add(btnOK);
    buttonPanel.add(btnCancel);

    add(buttonPanel, BorderLayout.SOUTH);

    btnOK.addActionListener(e -> onOK());
    btnCancel.addActionListener(e -> onCancel());
  }

  private void setupEventHandlers() {
    getRootPane().registerKeyboardAction(
        e -> onCancel(),
        KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0),
        JComponent.WHEN_IN_FOCUSED_WINDOW);
  }

  private void onOK() {
    String value = searchValueField.getText().trim();
    if (value.isEmpty()) {
      JOptionPane.showMessageDialog(this, "Введите значение поиска",
          "Ошибка ввода", JOptionPane.WARNING_MESSAGE);
      return;
    }

    searchType = (String) searchTypeComboBox.getSelectedItem();
    searchCondition = (String) searchConditionComboBox.getSelectedItem();
    searchValue = value;
    confirmed = true;
    dispose();
  }

  private void onCancel() {
    confirmed = false;
    dispose();
  }

  public boolean isConfirmed() {
    return confirmed;
  }

  public String getSearchType() {
    return searchType;
  }

  public String getSearchCondition() {
    return searchCondition;
  }

  public String getSearchValue() {
    return searchValue;
  }
}
