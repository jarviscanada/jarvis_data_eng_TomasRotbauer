package ca.jrvs.practice.dataStructure.list;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ArrayJListsTest {
  private ArrayJLists<String> jList;

  @Before
  public void setUp() {
    jList = new ArrayJLists<>();
  }

  public void initialize() {
    jList.add("H");
    jList.add("Hi");
    jList.add("Hey");
    jList.add("Hello");
  }

  @Test
  public void add() {
    jList.add("hello");
    Assert.assertEquals(jList.get(0), "hello");
  }

  @Test
  public void toArray() {
    initialize();
    Object[] arr = jList.toArray();
    Assert.assertEquals(arr.length, 4);
    Assert.assertEquals(arr[0], "H");
    Assert.assertEquals(arr[1], "Hi");
    Assert.assertEquals(arr[2], "Hey");
    Assert.assertEquals(arr[3], "Hello");

  }

  @Test
  public void size() {
    initialize();
    Assert.assertEquals(jList.size(), 4);
  }

  @Test
  public void isEmpty() {
    Assert.assertTrue(jList.isEmpty());
    initialize();
    Assert.assertFalse(jList.isEmpty());
  }

  @Test
  public void indexOf() {
    initialize();
    Assert.assertEquals(jList.indexOf("H"), 0);
    Assert.assertEquals(jList.indexOf("Hi"), 1);
    Assert.assertEquals(jList.indexOf("Hey"), 2);
    Assert.assertEquals(jList.indexOf("Hello"), 3);
  }

  @Test
  public void contains() {
    initialize();
    Assert.assertTrue(jList.contains("H"));
    Assert.assertFalse(jList.contains("Ola"));
  }

  @Test
  public void get() {
    initialize();
    Assert.assertEquals(jList.get(0), "H");
    Assert.assertEquals(jList.get(1), "Hi");
    Assert.assertEquals(jList.get(2), "Hey");
    Assert.assertEquals(jList.get(3), "Hello");
  }

  @Test
  public void remove() {
    initialize();
    jList.remove(2);
    Assert.assertFalse(jList.contains("Hey"));
    Assert.assertEquals(jList.get(0), "H");
    Assert.assertEquals(jList.get(1), "Hi");
    Assert.assertEquals(jList.get(2), "Hello");
    jList.remove(0);
    Assert.assertEquals(jList.size(), 2);
  }

  @Test
  public void clear() {
    initialize();
    jList.clear();
    Assert.assertFalse(jList.contains("Hey"));
    Assert.assertEquals(jList.size(), 0);
  }
}