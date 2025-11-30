import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.io.IOException;
import java.util.List;

/**
 * Главное окно GUI для CarDatabase.
 */
public class CarMainFrame extends JFrame {

  private final CarService service;
  private final CarDatabase database;

  private JPanel contentPane;
  private JTable carTable;
  private JTextField statusField;

  private static final String[] TABLE_HEADER = {
      "Марка", "Модель", "Год", "Цвет", "Цена", "Рег. номер"
  };

  public CarMainFrame(CarService service, CarDatabase database) {
    this.service = service;
    this.database = database;
    initializeUI();
    setupMenuActions();
    reloadAllCars();
  }

  private void initializeUI() {
    setTitle("Car Database GUI");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setBounds(100, 100, 900, 600);

    contentPane = new JPanel();
    contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
    contentPane.setLayout(new BorderLayout(0, 0));
    setContentPane(contentPane);

    createMenuBar();
    createTable();
    createStatusBar();

    setLocationRelativeTo(null);
  }

  private void createMenuBar() {
    JMenuBar menuBar = new JMenuBar();

    JMenu mnFile = new JMenu("Файл");
    JMenuItem mntmExit = new JMenuItem("Выход");

    mnFile.add(mntmExit);

    JMenu mnCommands = new JMenu("Команды");
    JMenuItem mntmAddCar = new JMenuItem("Добавить");
    JMenuItem mntmRemoveCar = new JMenuItem("Удалить выбранный");
    JMenuItem mntmShowAll = new JMenuItem("Показать все");
    JMenuItem mntmShowSorted = new JMenuItem("Сортировать");
    JMenuItem mntmFindCar = new JMenuItem("Поиск");
    JMenuItem mntmFillTest = new JMenuItem("Заполнить тестовыми");

    mnCommands.add(mntmAddCar);
    mnCommands.add(mntmRemoveCar);
    mnCommands.addSeparator();
    mnCommands.add(mntmShowAll);
    mnCommands.add(mntmShowSorted);
    mnCommands.add(mntmFindCar);
    mnCommands.addSeparator();
    mnCommands.add(mntmFillTest);

    JMenu mnHelp = new JMenu("Справка");
    JMenuItem mntmAbout = new JMenuItem("О программе");
    mnHelp.add(mntmAbout);

    menuBar.add(mnFile);
    menuBar.add(mnCommands);
    menuBar.add(mnHelp);

    setJMenuBar(menuBar);

    // Сохраняем в client properties для доступа в setupMenuActions
    mnFile.putClientProperty("exitItem", mntmExit);
    mnCommands.putClientProperty("addItem", mntmAddCar);
    mnCommands.putClientProperty("removeItem", mntmRemoveCar);
    mnCommands.putClientProperty("showAllItem", mntmShowAll);
    mnCommands.putClientProperty("showSortedItem", mntmShowSorted);
    mnCommands.putClientProperty("findItem", mntmFindCar);
    mnCommands.putClientProperty("fillTestItem", mntmFillTest);
    mnHelp.putClientProperty("aboutItem", mntmAbout);
  }

  private void createTable() {
    DefaultTableModel model = new DefaultTableModel(TABLE_HEADER, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    carTable = new JTable(model);
    carTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JScrollPane scrollPane = new JScrollPane(carTable);
    contentPane.add(scrollPane, BorderLayout.CENTER);
  }

  private void createStatusBar() {
    statusField = new JTextField();
    statusField.setEditable(false);
    statusField.setText("Готово");
    contentPane.add(statusField, BorderLayout.SOUTH);
  }

  @SuppressWarnings("unchecked")
  private void setupMenuActions() {
    JMenuBar bar = getJMenuBar();
    JMenu mnFile = bar.getMenu(0);
    JMenu mnCommands = bar.getMenu(1);
    JMenu mnHelp = bar.getMenu(2);

    JMenuItem exitItem = (JMenuItem) mnFile.getClientProperty("exitItem");
    JMenuItem addItem = (JMenuItem) mnCommands.getClientProperty("addItem");
    JMenuItem removeItem = (JMenuItem) mnCommands.getClientProperty("removeItem");
    JMenuItem showAllItem = (JMenuItem) mnCommands.getClientProperty("showAllItem");
    JMenuItem showSortedItem = (JMenuItem) mnCommands.getClientProperty("showSortedItem");
    JMenuItem findItem = (JMenuItem) mnCommands.getClientProperty("findItem");
    JMenuItem fillTestItem = (JMenuItem) mnCommands.getClientProperty("fillTestItem");
    JMenuItem aboutItem = (JMenuItem) mnHelp.getClientProperty("aboutItem");

    exitItem.addActionListener(e -> System.exit(0));

    addItem.addActionListener(e -> showAddCarDialog());
    removeItem.addActionListener(e -> removeSelectedCar());
    showAllItem.addActionListener(e -> reloadAllCars());
    showSortedItem.addActionListener(e -> showSortDialog());
    findItem.addActionListener(e -> showFindDialog());
    fillTestItem.addActionListener(e -> fillTestData());
    aboutItem.addActionListener(e -> showAbout());
  }

  private void reloadAllCars() {
    try {
      List<Car> cars = service.getAllCars();
      updateTable(cars);
      statusField.setText("Всего автомобилей: " + cars.size());
    } catch (Exception e) {
      showError("Ошибка загрузки: " + e.getMessage());
    }
  }

  private void updateTable(List<Car> cars) {
    DefaultTableModel model = (DefaultTableModel) carTable.getModel();
    model.setRowCount(0);
    for (Car c : cars) {
      model.addRow(new Object[] {
          c.getBrand(),
          c.getModel(),
          c.getYear(),
          c.getColor(),
          c.getPrice(),
          c.getRegistrationNumber()
      });
    }
  }

  private void showAddCarDialog() {
    AddCarDialog dialog = new AddCarDialog(this);
    dialog.setVisible(true);
    if (dialog.isSuccess()) {
      try {
        service.addCar(dialog.getCar());
        reloadAllCars();
        statusField.setText("Автомобиль добавлен");
      } catch (IOException e) {
        showError("Ошибка добавления: " + e.getMessage());
      }
    }
  }

  private void removeSelectedCar() {
    int row = carTable.getSelectedRow();
    if (row == -1) {
      JOptionPane.showMessageDialog(this,
          "Выберите автомобиль в таблице",
          "Нет выбора", JOptionPane.WARNING_MESSAGE);
      return;
    }
    String reg = (String) carTable.getValueAt(row, 5);
    int confirm = JOptionPane.showConfirmDialog(this,
        "Удалить автомобиль с номером " + reg + "?",
        "Подтверждение", JOptionPane.YES_NO_OPTION);
    if (confirm != JOptionPane.YES_OPTION) {
      return;
    }
    try {
      if (service.deleteByRegistrationNumber(reg)) {
        reloadAllCars();
        statusField.setText("Удален: " + reg);
      } else {
        JOptionPane.showMessageDialog(this,
            "Автомобиль не найден",
            "Ошибка", JOptionPane.ERROR_MESSAGE);
      }
    } catch (IOException e) {
      showError("Ошибка удаления: " + e.getMessage());
    }
  }

  private void showSortDialog() {
    SortDialog dialog = new SortDialog(this);
    dialog.setVisible(true);
    if (!dialog.isConfirmed()) return;

    String field = dialog.getSortField();
    boolean reverse = dialog.isReverse();
    try {
      List<Car> cars;
      if ("Марка".equals(field)) {
        cars = service.getCarsSortedByBrand(!reverse ? true : false);
      } else {
        cars = service.getCarsSortedByRegistrationNumber(!reverse ? true : false);
      }
      if (reverse) {
        java.util.Collections.reverse(cars);
      }
      updateTable(cars);
      statusField.setText("Отсортировано по \"" + field + "\"" + (reverse ? " (обратный)" : ""));
    } catch (IOException e) {
      showError("Ошибка сортировки: " + e.getMessage());
    }
  }

  private void showFindDialog() {
    FindCarDialog dialog = new FindCarDialog(this);
    dialog.setVisible(true);
    if (!dialog.isConfirmed()) return;

    String type = dialog.getSearchType();
    String cond = dialog.getSearchCondition();
    String value = dialog.getSearchValue();

    try {
      if ("Регистрационный номер".equals(type)) {
        Car car = service.findCarByRegistrationNumber(value);
        if (car == null) {
          JOptionPane.showMessageDialog(this,
              "Автомобиль не найден",
              "Результат поиска", JOptionPane.INFORMATION_MESSAGE);
          return;
        }
        updateTable(java.util.List.of(car));
        statusField.setText("Найден автомобиль: " + value.toUpperCase());
      } else { // Марка
        java.util.List<Car> cars;
        if ("Равно".equals(cond)) {
          cars = service.findCarsByBrand(value);
        } else if ("Больше (по алф.)".equals(cond)) {
          cars = service.findCarsWithBrandGreaterThan(value);
        } else {
          cars = service.findCarsWithBrandLessThan(value);
        }
        updateTable(cars);
        statusField.setText("Найдено автомобилей: " + cars.size());
      }
    } catch (IOException e) {
      showError("Ошибка поиска: " + e.getMessage());
    }
  }

  private void fillTestData() {
    try {
      service.fillWithTestData();
      reloadAllCars();
      statusField.setText("Тестовые данные добавлены");
    } catch (IOException e) {
      showError("Ошибка заполнения: " + e.getMessage());
    }
  }

  private void showAbout() {
    JOptionPane.showMessageDialog(this,
        "Car Database GUI\nПример GUI поверх бинарной БД cars.dat",
        "О программе", JOptionPane.INFORMATION_MESSAGE);
  }

  private void showError(String msg) {
    JOptionPane.showMessageDialog(this, msg, "Ошибка", JOptionPane.ERROR_MESSAGE);
  }
}
