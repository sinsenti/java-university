package com.example;
import java.util.*;


public class Main {

	public static void main(String[] args) {
	    System.out.println();
	    Scanner scanner = new Scanner(System.in);
	    while (scanner.hasNextLine()) {
	      String line = scanner.nextLine();
	      if (line.isEmpty()) {
	        continue;
	      }
	      Map<Character, Integer> frequencyMap = new TreeMap<>();
	      for (char c : line.toCharArray()) {
	        frequencyMap.put(c, frequencyMap.getOrDefault(c, 0) + 1);
	      }
	      System.out.println("Строка: \"" + line + "\"");
	      System.out.println("Символ | Код | Частота");
	      System.out.println("----------------------");
	      for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
	        char c = entry.getKey();
	        int code = (int) c;
	        int frequency = entry.getValue();

	        System.out.printf("%6s | %3d | %d%n",
	            c, code, frequency);
	      }
	      System.out.println();
	    }
	    scanner.close();


	}


}
