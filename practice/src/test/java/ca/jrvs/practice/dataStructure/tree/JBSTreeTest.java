package ca.jrvs.practice.dataStructure.tree;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JBSTreeTest {

  private JTree<String> tree;

  @Before
  public void setUp() {
    tree = new JBSTree<>(String::compareToIgnoreCase);
  }

  public void initialize() {
    tree.insert("Hello");
    tree.insert("Hi");
    tree.insert("Hey");
    tree.insert("a");
    tree.insert("b");
    tree.insert("x");
    tree.insert("y");
    tree.insert("c");
    tree.insert("d");
  }

  @Test
  public void insert() {
    initialize();
    Assert.assertEquals(tree.search("Hello"), "Hello");
    Assert.assertEquals(tree.search("Hi"), "Hi");
    Assert.assertEquals(tree.search("Hey"), "Hey");
  }

  @Test
  public void search() {
    insert();
  }

  @Test
  public void remove() {
    initialize();
    tree.remove("b");
    Assert.assertEquals(tree.search("Hello"), "Hello");
    Assert.assertEquals(tree.search("Hi"), "Hi");
    Assert.assertEquals(tree.search("Hey"), "Hey");
    Assert.assertEquals(tree.search("a"), "a");
    Assert.assertEquals(tree.search("c"), "c");
    Assert.assertEquals(tree.search("b"), null);
    Assert.assertEquals(tree.search("x"), "x");
    Assert.assertEquals(tree.search("y"), "y");
    Assert.assertEquals(tree.search("c"), "c");
    Assert.assertEquals(tree.search("d"), "d");

    tree.remove("a");
    Assert.assertEquals(tree.search("Hello"), "Hello");
    Assert.assertEquals(tree.search("Hi"), "Hi");
    Assert.assertEquals(tree.search("Hey"), "Hey");
    Assert.assertEquals(tree.search("a"), null);
    Assert.assertEquals(tree.search("c"), "c");
    Assert.assertEquals(tree.search("b"), null);
    Assert.assertEquals(tree.search("x"), "x");
    Assert.assertEquals(tree.search("y"), "y");
    Assert.assertEquals(tree.search("c"), "c");
    Assert.assertEquals(tree.search("d"), "d");

    tree.remove("Hello");
    Assert.assertEquals(tree.search("Hello"), null);
    Assert.assertEquals(tree.search("Hi"), "Hi");
    Assert.assertEquals(tree.search("Hey"), "Hey");
    Assert.assertEquals(tree.search("a"), null);
    Assert.assertEquals(tree.search("c"), "c");
    Assert.assertEquals(tree.search("b"), null);
    Assert.assertEquals(tree.search("x"), "x");
    Assert.assertEquals(tree.search("y"), "y");
    Assert.assertEquals(tree.search("c"), "c");
    Assert.assertEquals(tree.search("d"), "d");
  }

  @Test
  public void preOrder() {
    initialize();
    String[] preOrder = {"Hello", "a", "b", "c", "d", "Hi", "Hey", "x", "y"};
    Assert.assertArrayEquals(tree.preOrder(), preOrder);
  }

  @Test
  public void inOrder() {
    initialize();
    String[] inOrder = {"a", "b", "c", "d", "Hello", "Hey", "Hi", "x", "y"};
    Assert.assertArrayEquals(tree.inOrder(), inOrder);
  }

  @Test
  public void postOrder() {
    initialize();
    String[] postOrder = {"d", "c", "b", "a", "Hey", "y", "x", "Hi", "Hello"};
    Assert.assertArrayEquals(tree.postOrder(), postOrder);
  }

  @Test
  public void findHeight() {
    initialize();
    Assert.assertEquals(tree.findHeight(), 4);
  }
}