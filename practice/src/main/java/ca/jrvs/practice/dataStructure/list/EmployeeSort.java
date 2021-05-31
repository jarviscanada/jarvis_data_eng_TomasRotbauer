package ca.jrvs.practice.dataStructure.list;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EmployeeSort {

  public static void main(String[] args) {
    List<Employee> employees = new ArrayList<>();
    EmployeeSort empSort = new EmployeeSort();
    empSort.populate(employees);
    System.out.println("INITIAL:");
    employees.forEach(o -> System.out.println(o.getName()));

    System.out.println("\nComparableL:");
    Collections.sort(employees);
    employees.forEach(o -> System.out.println(o.getName()));
    employees.clear();

    System.out.println("\nComparator:");
    empSort.populate(employees);
    employees.sort(new EmployeeCompare());
    employees.forEach(o -> System.out.println(o.getName()));
  }

  public void populate(List<Employee> employees) {
    employees.add(new Employee(50, "Charles", 50, 50000));
    employees.add(new Employee(10, "Edward", 20, 100000));
    employees.add(new Employee(1,"David", 100, 500000000));
    employees.add(new Employee(0, "Martin", 45, 51236556));
    employees.add(new Employee(0, "MartinRich", 45, 51236557));
  }

}

class EmployeeCompare implements Comparator<Employee> {

  /**
   * Compares its two arguments for order.  Returns a negative integer, zero, or a positive integer
   * as the first argument is less than, equal to, or greater than the second.<p>
   * <p>
   * In the foregoing description, the notation
   * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
   * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
   * <tt>0</tt>, or <tt>1</tt> according to whether the value of
   * <i>expression</i> is negative, zero or positive.<p>
   * <p>
   * The implementor must ensure that <tt>sgn(compare(x, y)) == -sgn(compare(y, x))</tt> for all
   * <tt>x</tt> and <tt>y</tt>.  (This implies that <tt>compare(x, y)</tt> must throw an exception
   * if and only if <tt>compare(y, x)</tt> throws an exception.)<p>
   * <p>
   * The implementor must also ensure that the relation is transitive:
   * <tt>((compare(x, y)&gt;0) &amp;&amp; (compare(y, z)&gt;0))</tt> implies
   * <tt>compare(x, z)&gt;0</tt>.<p>
   * <p>
   * Finally, the implementor must ensure that <tt>compare(x, y)==0</tt> implies that
   * <tt>sgn(compare(x, z))==sgn(compare(y, z))</tt> for all
   * <tt>z</tt>.<p>
   * <p>
   * It is generally the case, but <i>not</i> strictly required that
   * <tt>(compare(x, y)==0) == (x.equals(y))</tt>.  Generally speaking,
   * any comparator that violates this condition should clearly indicate this fact.  The recommended
   * language is "Note: this comparator imposes orderings that are inconsistent with equals."
   *
   * @param o1 the first object to be compared.
   * @param o2 the second object to be compared.
   * @return a negative integer, zero, or a positive integer as the first argument is less than,
   * equal to, or greater than the second.
   * @throws NullPointerException if an argument is null and this comparator does not permit null
   *                              arguments
   * @throws ClassCastException   if the arguments' types prevent them from being compared by this
   *                              comparator.
   */
  @Override
  public int compare(Employee o1, Employee o2) {
    if (o1.getAge() - o2.getAge() == 0)
      return (int)(o1.getSalary() - o2.getSalary());

    return o1.getAge() - o2.getAge();
  }
}
