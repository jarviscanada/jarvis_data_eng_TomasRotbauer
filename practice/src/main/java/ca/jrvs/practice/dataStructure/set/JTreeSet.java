package ca.jrvs.practice.dataStructure.set;

import java.util.NavigableMap;
import java.util.TreeMap;

public class JTreeSet<E> implements JSet<E> {

  private final NavigableMap<E, Object> tree;

  public JTreeSet() {
    tree = new TreeMap<>();
  }

  /**
   * Returns the number of elements in this set (its cardinality).  If this set contains more than
   * <tt>Integer.MAX_VALUE</tt> elements, returns
   * <tt>Integer.MAX_VALUE</tt>.
   *
   * @return the number of elements in this set (its cardinality)
   */
  @Override
  public int size() {
    return tree.size();
  }

  /**
   * Returns <tt>true</tt> if this set contains the specified element. More formally, returns
   * <tt>true</tt> if and only if this set contains an element <tt>e</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
   *
   * @param o element whose presence in this set is to be tested
   * @return <tt>true</tt> if this set contains the specified element
   * @throws NullPointerException if the specified element is null and this set does not permit null
   *                              elements
   */
  @Override
  public boolean contains(Object o) throws NullPointerException {
    if (o == null)
      throw new NullPointerException("Null values are not supported");
    return tree.containsKey((E)o);
  }

  /**
   * Adds the specified element to this set if it is not already present (optional operation).  More
   * formally, adds the specified element
   * <tt>e</tt> to this set if the set contains no element <tt>e2</tt>
   * such that
   * <tt>(e==null&nbsp;?&nbsp;e2==null&nbsp;:&nbsp;e.equals(e2))</tt>.
   * If this set already contains the element, the call leaves the set unchanged and returns
   * <tt>false</tt>.  In combination with the restriction on constructors, this ensures that sets
   * never contain duplicate elements.
   *
   * @param e element to be added to this set
   * @return <tt>true</tt> if this set did not already contain the specified
   * element
   * @throws NullPointerException if the specified element is null
   */
  @Override
  public boolean add(E e) throws NullPointerException {
    if (e == null)
      throw new NullPointerException("Null values are not supported");
    tree.putIfAbsent(e, new Object());
    return true;
  }

  /**
   * Removes the specified element from this set if it is present (optional operation).  More
   * formally, removes an element <tt>e</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>, if
   * this set contains such an element.  Returns <tt>true</tt> if this set contained the element (or
   * equivalently, if this set changed as a result of the call).  (This set will not contain the
   * element once the call returns.)
   *
   * @param o object to be removed from this set, if present
   * @return <tt>true</tt> if this set contained the specified element
   * @throws NullPointerException if the specified element is null and this set does not permit null
   *                              elements
   */
  @Override
  public boolean remove(Object o) throws NullPointerException {
    if (o == null)
      throw new NullPointerException("Null values are bot supported");
    tree.remove((E)o);
    return true;
  }

  /**
   * Removes all of the elements from this set The set will be empty after this call returns.
   */
  @Override
  public void clear() {
    tree.clear();
  }
}
