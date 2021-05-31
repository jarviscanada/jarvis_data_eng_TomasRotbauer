package ca.jrvs.practice.dataStructure.list;

import java.util.LinkedList;
import java.util.List;

public class LinkedListAPIs {

  public static void main(String[] args) {
    List<String> cars = new LinkedList<>();
    cars.add("Lamborghini");
    cars.add("Audi TT");
    cars.add("VW Golf GTI");

    int s = cars.size();

    String firstElement = cars.get(0);

    Boolean hasMercedes = cars.contains("Mercedes");

    int LamboIndex = cars.indexOf("Lamborghini");
    cars.remove("Audi TT");
    cars.remove(0);
    cars.sort(String::compareTo);
    System.out.println(cars.toString());
  }
}
