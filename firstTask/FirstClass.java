package helloWorld;

public class FirstClass {

	public static void main(String[] args) {
		  if (args.length != 2) {
	            System.err.println("Invalid number of arguments");
	            System.exit(1);
	        }
	        
	        double x = Double.parseDouble(args[0]);
	        if (x >= 1 || x <= -1) {
	            System.err.println("Invalid argument: " + x);
	            System.exit(1);
	        }
	        
	        int k = Integer.parseInt(args[1]);
	        if (k <= 1) {
	            System.err.println("Invalid argument: " + k);
	            System.exit(1);
	        }
	        
	        double Eps = 1 / Math.pow(10, k + 1);
	        double result = 0.0;
	        double term = 1.0;
	        int n = 0;
	        
	        while (Math.abs(term) >= Eps) {
	            result += term;
	            n++;
	            term = term * (-1) * (n + 2) / n * x;
	        }
	        
	        String fmt = "%." + k + "f\n";
	        System.out.printf(fmt, result);
	        System.out.printf(fmt, 1 / Math.pow(x + 1, 3));
	        System.exit(0);

	}

}
