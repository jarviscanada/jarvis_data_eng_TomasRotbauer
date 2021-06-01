package ca.jrvs.practice.dataStructure.list;

public class LinkedJList<E> implements JList<E> {

  transient int size = 0;
  transient Node<E> head;
  transient Node<E> tail;

  private static class Node<E> {
    E item;
    Node<E> next;
    Node<E> prev;

    Node(E item, Node<E> prev, Node<E> next) {
      this.item = item;
      this.prev = prev;
      this.next = next;
    }
  }
  /**
   * Appends the specified element to the end of this list (optional operation).
   *
   * @param e element to be appended to this list
   * @return <tt>true</tt> (as specified by {@link Collection#add})
   * @throws NullPointerException if the specified element is null and this list does not permit
   *                              null elements
   */
  @Override
  public boolean add(E e) {
    if (size == 0) {
      head = new Node<E>(e, null, null);
      tail = head;
    }
    else {
      Node<E> newItem = new Node<>(e, tail, null);
      tail.next = newItem;
      tail = newItem;
    }
    size++;
    return true;
  }

  /**
   * Returns an array containing all of the elements in this list in proper sequence (from first to
   * last element).
   *
   * <p>This method acts as bridge between array-based and collection-based
   * APIs.
   *
   * @return an array containing all of the elements in this list in proper sequence
   */
  @Override
  public Object[] toArray() {
    Object[] arr = new Object[size];
    Node<E> node = head;
    for (int i = 0; i < size; i++){
      arr[i] = node.item;
      node = node.next;
    }
    return arr;
  }

  /**
   * The size of the ArrayList (the number of elements it contains).
   */
  @Override
  public int size() {
    return size;
  }

  /**
   * Returns <tt>true</tt> if this list contains no elements.
   *
   * @return <tt>true</tt> if this list contains no elements
   */
  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Returns the index of the first occurrence of the specified element in this list, or -1 if this
   * list does not contain the element. More formally, returns the lowest index <tt>i</tt> such
   * that
   * <tt>(o==null&nbsp;?&nbsp;get(i)==null&nbsp;:&nbsp;o.equals(get(i)))</tt>,
   * or -1 if there is no such index.
   *
   * @param o
   */
  @Override
  public int indexOf(Object o) {
    Node<E> node = head;
    if (o == null)
      for (int i = 0; i < size; i++) {
        if (node.item == null)
          return i;
        node = node.next;
      }
    else
      for (int i = 0; i < size; i++) {
        if (node.item.equals(o))
          return i;
        node = node.next;
      }

    return -1;
  }

  /**
   * Returns <tt>true</tt> if this list contains the specified element. More formally, returns
   * <tt>true</tt> if and only if this list contains at least one element <tt>e</tt> such that
   * <tt>(o==null&nbsp;?&nbsp;e==null&nbsp;:&nbsp;o.equals(e))</tt>.
   *
   * @param o element whose presence in this list is to be tested
   * @return <tt>true</tt> if this list contains the specified element
   * @throws NullPointerException if the specified element is null and this list does not permit
   *                              null elements
   */
  @Override
  public boolean contains(Object o) {
    return indexOf(o) >= 0;
  }

  /**
   * Returns the element at the specified position in this list.
   *
   * @param index index of the element to return
   * @return the element at the specified position in this list
   * @throws IndexOutOfBoundsException if the index is out of range (<tt>index &lt; 0 || index &gt;=
   *                                   size()</tt>)
   */
  @Override
  public E get(int index) {
    if (index < 0 || index >= size)
      return null;

    Node<E> node;
    if (index < size - 1 - index) {
      node = head;
      for (int i = 0; i < index; i++)
        node = node.next;
    }
    else {
      node = tail;
      for (int i = size - 1; i > index; i--)
        node = node.prev;
    }
    return node.item;
  }

  /**
   * Removes the element at the specified position in this list. Shifts any subsequent elements to
   * the left (subtracts one from their indices).
   *
   * @param index the index of the element to be removed
   * @return the element that was removed from the list
   * @throws IndexOutOfBoundsException {@inheritDoc}
   */
  @Override
  public E remove(int index) {
    if (index < 0 || index >= size)
      return null;

    Node<E> node;
    if (index < size - 1 - index) {
      node = head;
      for (int i = 0; i < index; i++)
        node = node.next;
    }
    else {
      node = tail;
      for (int i = size - 1; i > index; i--)
        node = node.prev;
    }
    if (node.prev != null)
      node.prev.next = node.next;
    if (node.next != null)
    node.next.prev = node.prev;

    if (index == 0)
      head = head.next;
    if (index == size - 1)
      tail = tail.prev;

    size--;
    return node.item;
  }

  /**
   * Removes all of the elements from this list (optional operation). The list will be empty after
   * this call returns.
   */
  @Override
  public void clear() {
    head = null;
    tail = null;
    size = 0;
  }
}
