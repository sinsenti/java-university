import java.util.Arrays;

public class Triangle {
  private double x1, y1, x2, y2, x3, y3;

  public Triangle() {
    this(0, 0, 1, 0, 0.5, Math.sqrt(3) / 2);
  }

  public Triangle(double x1, double y1, double x2, double y2, double x3, double y3) {
    setVertices(x1, y1, x2, y2, x3, y3);
  }

  public Triangle(Triangle other) {
    if (other == null) {
      throw new IllegalArgumentException("Другой треугольник не может быть null");
    }
    this.x1 = other.x1;
    this.y1 = other.y1;
    this.x2 = other.x2;
    this.y2 = other.y2;
    this.x3 = other.x3;
    this.y3 = other.y3;
  }

  public void setVertices(double x1, double y1, double x2, double y2, double x3, double y3) {
    double area = Math.abs((x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1)) / 2.0;
    if (area < 1e-10) {
      throw new IllegalArgumentException("Точки лежат на одной прямой - не может существовать");
    }

    this.x1 = x1;
    this.y1 = y1;
    this.x2 = x2;
    this.y2 = y2;
    this.x3 = x3;
    this.y3 = y3;
  }

  public double[] getVertexA() {
    return new double[] { x1, y1 };
  }

  public double[] getVertexB() {
    return new double[] { x2, y2 };
  }

  public double[] getVertexC() {
    return new double[] { x3, y3 };
  }

  private double sideLength(double x1, double y1, double x2, double y2) {
    return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
  }

  public double getSideAB() {
    return sideLength(x1, y1, x2, y2);
  }

  public double getSideBC() {
    return sideLength(x2, y2, x3, y3);
  }

  public double getSideCA() {
    return sideLength(x3, y3, x1, y1);
  }

  public double perimeter() {
    return getSideAB() + getSideBC() + getSideCA();
  }

  public double area() {
    double a = getSideBC();
    double b = getSideCA();
    double c = getSideAB();
    double p = perimeter() / 2;
    return Math.sqrt(p * (p - a) * (p - b) * (p - c));
  }

  public boolean equals(Triangle other) {
    if (other == null)
      return false;

    double a1 = this.getSideBC();
    double b1 = this.getSideCA();
    double c1 = this.getSideAB();

    double a2 = other.getSideBC();
    double b2 = other.getSideCA();
    double c2 = other.getSideAB();

    double[] sides1 = { a1, b1, c1 };
    double[] sides2 = { a2, b2, c2 };

    Arrays.sort(sides1);
    Arrays.sort(sides2);

    return Math.abs(sides1[0] - sides2[0]) < 1e-10 &&
        Math.abs(sides1[1] - sides2[1]) < 1e-10 &&
        Math.abs(sides1[2] - sides2[2]) < 1e-10;
  }

  public double heightA() {
    double area = area();
    double base = getSideBC();
    if (base == 0)
      return 0;
    return (2 * area) / base;
  }

  public double medianA() {
    double b = getSideCA();
    double c = getSideAB();
    double a = getSideBC();
    return 0.5 * Math.sqrt(2 * b * b + 2 * c * c - a * a);
  }

  public double bisectorA() {
    double b = getSideCA();
    double c = getSideAB();
    double a = getSideBC();
    return Math.sqrt(b * c * (a + b + c) * (b + c - a)) / (b + c);
  }

  public String getType() {
    double a = getSideBC();
    double b = getSideCA();
    double c = getSideAB();

    if (Math.abs(a - b) < 1e-10 && Math.abs(b - c) < 1e-10) {
      return "равносторонний";
    }

    if (Math.abs(a - b) < 1e-10 || Math.abs(b - c) < 1e-10 || Math.abs(c - a) < 1e-10) {
      return "равнобедренный";
    }

    double a2 = a * a;
    double b2 = b * b;
    double c2 = c * c;

    if (Math.abs(a2 + b2 - c2) < 1e-10 ||
        Math.abs(b2 + c2 - a2) < 1e-10 ||
        Math.abs(c2 + a2 - b2) < 1e-10) {
      return "прямоугольный";
    }

    double maxSide = Math.max(a, Math.max(b, c));
    double maxSide2 = maxSide * maxSide;
    double sumSquaresOtherTwo = a2 + b2 + c2 - maxSide2;

    if (maxSide2 > sumSquaresOtherTwo + 1e-10) {
      return "тупоугольный";
    }

    return "остроугольный";
  }

  @Override
  public String toString() {
    return String.format("Треугольник A(%.2f, %.2f), B(%.2f, %.2f), C(%.2f, %.2f)",
        x1, y1, x2, y2, x3, y3);
  }
}
