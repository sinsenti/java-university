public class TriangleTest {
  public static void main(String[] args) {
    System.out.println("\n\n Тестирование класса triangle \n\n\n");

    System.out.println("1. Тест конструктора по умолчанию:");
    Triangle t1 = new Triangle();
    System.out.println(t1);
    System.out.println("Тип: " + t1.getType());
    System.out.println("Площадь: " + t1.area());
    System.out.println("Периметр: " + t1.perimeter());
    System.out.println();

    System.out.println("2. Тест конструктора с координатами:");
    Triangle t2 = new Triangle(0, 0, 3, 0, 0, 4);
    System.out.println(t2);
    System.out.println("Тип: " + t2.getType());
    System.out.println("Площадь: " + t2.area());
    System.out.println("Периметр: " + t2.perimeter());
    System.out.println("Высота из A: " + t2.heightA());
    System.out.println("Медиана из A: " + t2.medianA());
    System.out.println("Биссектриса из A: " + t2.bisectorA());
    System.out.println();

    System.out.println("3. Тест конструктора копирования:");
    Triangle t3 = new Triangle(t2);
    System.out.println("Оригинал: " + t2);
    System.out.println("Копия: " + t3);
    System.out.println("Равны? " + t2.equals(t3));
    System.out.println();

    System.out.println("4. Тест разных типов треугольников:");

    Triangle equilateral = new Triangle(0, 0, 2, 0, 1, Math.sqrt(3));
    System.out.println("Равносторонний: " + equilateral.getType());

    Triangle isosceles = new Triangle(0, 0, 3, 0, 1.5, 2);
    System.out.println("Равнобедренный: " + isosceles.getType());

    Triangle right = new Triangle(0, 0, 3, 0, 0, 4);
    System.out.println("Прямоугольный: " + right.getType());

    Triangle acute = new Triangle(0, 0, 4, 0, 1, 3);
    System.out.println("Остроугольный: " + acute.getType());

    Triangle obtuse = new Triangle(0, 0, 3, 0, 1, 1);
    System.out.println("Тупоугольный: " + obtuse.getType());
    System.out.println();

    System.out.println("5. Тест обработки исключений:");
    try {
      Triangle degenerate = new Triangle(0, 0, 1, 1, 2, 2);
      System.out.println("Ошибка: Исключение не было выброшено!");
    } catch (IllegalArgumentException e) {
      System.out.println("Исключение: " + e.getMessage());
    }

    try {
      Triangle nullTriangle = new Triangle(null);
      System.out.println("Ошибка: Исключение не было выброшено!");
    } catch (IllegalArgumentException e) {
      System.out.println("Исключение: " + e.getMessage());
    }

    System.out.println("\n6. Тест изменения вершин:");
    Triangle t6 = new Triangle();
    System.out.println("До изменения: " + t6);
    t6.setVertices(0, 0, 5, 0, 0, 5);
    System.out.println("После изменения: " + t6);
    System.out.println("Площадь: " + t6.area());

    System.out.println("\n\nТестирование завершено \n\n");
  }
}
