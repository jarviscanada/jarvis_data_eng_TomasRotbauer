package ca.jrvs.practice.dataStructure.tree;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Stack;

/**
 * A simplified BST implementation
 *
 * @param <E> type of object to be stored
 */
public class JBSTree<E> implements JTree<E> {

  /**
   * The comparator used to maintain order in this tree map
   * Comparator cannot be null
   */
  private final Comparator<E> comparator;
  private Node<E> root;

  /**
   * Create a new BST
   *
   * @param comparator the comparator that will be used to order this map.
   * @throws IllegalArgumentException if comparator is null
   */
  public JBSTree(Comparator<E> comparator) {
    this.comparator = comparator;
  }

  /**
   * Insert an object into the BST.
   * Please review the BST property.
   *
   * @param object item to be inserted
   * @return inserted item
   * @throws IllegalArgumentException if the object already exists
   */
  @Override
  public E insert(E object) throws IllegalArgumentException, NullPointerException {

    if (object == null)
      throw new NullPointerException("Cannot insert null value into tree");

    Node<E> current = root;
    Node<E> parent = null;

    while(current != null) {
      parent = current;
      if (comparator.compare(current.getValue(), object) > 0)
        current = current.getLeft();
      else if (comparator.compare(current.getValue(), object) < 0)
        current = current.getRight();
      else
        throw new IllegalArgumentException("Cannot insert value that already exists in the tree");
    }

    Node<E> newNode = new Node<>(object, parent);

    if (parent == null)
      this.root = newNode;
    else if (comparator.compare(parent.getValue(), object) > 0)
      parent.setLeft(newNode);
    else
      parent.setRight(newNode);

    return object;
  }

  /**
   * Search and return an object, return null if not found
   *
   * @param object to be found
   * @return the object if exists or null if not
   */
  @Override
  public E search(E object) {
    if (object == null)
      return null;

    Node<E> current = root;

    while(current != null) {
      if (comparator.compare(current.getValue(), object) > 0)
        current = current.getLeft();
      else if (comparator.compare(current.getValue(), object) < 0)
        current = current.getRight();
      else
        return current.getValue();
    }
    return null;
  }

  /**
   * Remove an object from the tree.
   *
   * @param object to be removed
   * @return removed object
   * @throws IllegalArgumentException if the object not exists
   */
  @Override
  public E remove(E object) throws IllegalArgumentException, NullPointerException {
    if (object == null)
      throw new NullPointerException("Null object cannot be deleted from the tree because "
          + "it is not permitted");

    Node<E> current = root;

    while(current != null) {
      if (comparator.compare(current.getValue(), object) > 0)
        current = current.getLeft();
      else if (comparator.compare(current.getValue(), object) < 0)
        current = current.getRight();
      else
        break;
    }

    if (current == null)
      throw new IllegalArgumentException("Cannot find the specified value in the tree");

    Node<E> parent = current.getParent();

    if (current.getLeft() == null && current.getRight() == null) {
      if (parent == null)
        this.root = null;
      else if (comparator.compare(parent.getValue(), object) > 0)
        parent.setLeft(null);
      else
        parent.setRight(null);
    }
    else if (current.getLeft() == null || current.getRight() == null) {
      Node<E> child = current.getLeft() == null ? current.getRight() : current.getLeft();

      if (parent == null) {
        this.root = child;
        child.setParent(null);
      }
      else if (comparator.compare(parent.getValue(), object) > 0) {
        parent.setLeft(child);
        child.setParent(parent);
      }
      else {
        parent.setRight(child);
        child.setParent(parent);
      }
    }
    else {
      Node<E> successor = inorderSuccessor(current);
      Node<E> leftChild = current.getLeft();
      Node<E> rightChild = current.getRight();

      if (successor.getLeft() != null) {
        successor.getParent().setRight(successor.getLeft());
        successor.getLeft().setParent(successor.getParent());
      }

      if (parent == null)
        this.root = successor;

      successor.setParent(null);
      successor.setRight(rightChild);
      successor.setLeft(leftChild);
      rightChild.setParent(successor);
      leftChild.setParent(successor);

    }
    return object;
  }

  private Node<E> inorderSuccessor(Node<E> root) {
    if (root == null || root.getLeft() == null)
      return root;

    Node<E> current = root.getLeft();

    while(current.getRight() != null)
      current = current.getRight();

    return current;
  }

  /**
   * traverse the tree recursively
   *
   * @return all objects in pre-order
   */
  @Override
  public E[] preOrder() {
    List<E> list = new ArrayList<>();
    Node<E> current;
    int step;
    Stack<Node<E>> stack = new Stack<>();
    Stack<Integer> steps = new Stack<>();

    stack.push(root);
    steps.push(1);

    while(!stack.empty()) {
      current = stack.pop();
      step = steps.pop();
      if (step == 1)
        list.add(current.getValue());
      if (step < 3 && current.getLeft() != null) {
        stack.push(current);
        stack.push(current.getLeft());
        steps.push(3);
        steps.push(1);
        continue;
      }
      if (current.getRight() != null) {
        stack.push(current.getRight());
        steps.push(1);
      }
    }

    return (E[]) list.toArray();
  }

  /**
   * traverse the tree recursively
   *
   * @return all objects in-order
   */
  @Override
  public E[] inOrder() {
    List<E> list = new ArrayList<>();
    Node<E> current;
    int step;
    Stack<Node<E>> stack = new Stack<>();
    Stack<Integer> steps = new Stack<>();

    stack.push(root);
    steps.push(1);

    while(!stack.empty()) {
      current = stack.pop();
      step = steps.pop();
      if (step == 1 && current.getLeft() != null) {
        stack.push(current);
        stack.push(current.getLeft());
        steps.push(2);
        steps.push(1);
        continue;
      }
      if (step < 3)
        list.add(current.getValue());
      if (current.getRight() != null) {
        stack.push(current.getRight());
        steps.push(1);
      }
    }

    return (E[]) list.toArray();
  }

  /**
   * traverse the tree recursively
   *
   * @return all objects pre-order
   */
  @Override
  public E[] postOrder() {
    List<E> list = new ArrayList<>();
    Node<E> current;
    int step;
    Stack<Node<E>> stack = new Stack<>();
    Stack<Integer> steps = new Stack<>();

    stack.push(root);
    steps.push(1);

    while(!stack.empty()) {
      current = stack.pop();
      step = steps.pop();
      if (step == 1 && current.getLeft() != null) {
        stack.push(current);
        stack.push(current.getLeft());
        steps.push(2);
        steps.push(1);
        continue;
      }
      if (step < 3 && current.getRight() != null) {
        stack.push(current);
        stack.push(current.getRight());
        steps.push(3);
        steps.push(1);
        continue;
      }
      list.add(current.getValue());
    }

    return (E[]) list.toArray();
  }

  /**
   * traverse through the tree and find out the tree height
   * @return height
   * @throws NullPointerException if the BST is empty
   */
  @Override
  public int findHeight() {
    if (root == null)
      return 0;

    Stack<Integer> steps = new Stack<>();
    steps.push(1);

    int step;
    int height = 0;
    int max = 0;
    Node<E> current = root;

    while (current != null) {
      step = steps.pop();
      if (max < height)
        max = height;
      if (current.getRight() != null && step == 1) {
        current = current.getRight();
        height++;
        steps.push(2);
        steps.push(1);
        continue;
      }
      if (current.getLeft() != null && step < 3) {
        current = current.getLeft();
        height++;
        steps.push(3);
        steps.push(1);
        continue;
      }
      current = current.getParent();
      height--;
    }

    return max;
  }

  static final class Node<E> {

    E value;
    Node<E> left;
    Node<E> right;
    Node<E> parent;

    public Node(E value, Node<E> parent) {
      this.value = value;
      this.parent = parent;
    }

    public E getValue() {
      return value;
    }

    public void setValue(E value) {
      this.value = value;
    }

    public Node<E> getLeft() {
      return left;
    }

    public void setLeft(Node<E> left) {
      this.left = left;
    }

    public Node<E> getRight() {
      return right;
    }

    public void setRight(Node<E> right) {
      this.right = right;
    }

    public Node<E> getParent() {
      return parent;
    }

    public void setParent(Node<E> parent) {
      this.parent = parent;
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (!(o instanceof Node)) {
        return false;
      }
      Node<?> node = (Node<?>) o;
      return getValue().equals(node.getValue()) &&
          Objects.equals(getLeft(), node.getLeft()) &&
          Objects.equals(getRight(), node.getRight()) &&
          getParent().equals(node.getParent());
    }

    @Override
    public int hashCode() {
      return Objects.hash(getValue(), getLeft(), getRight(), getParent());
    }
  }

}