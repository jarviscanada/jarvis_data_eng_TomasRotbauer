package ca.jrvs.practice.dataStructure.set;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JHashSetTest {

  private JSet<String> set;

  @Before
  public void setUp() {
    set = new JHashSet<>();
    set.add("Hello");
    set.add("Hi");
    set.add("Hey");
  }

  @After
  public void tearDown() {
    set.clear();
  }

  @Test
  public void size() {
    Assert.assertEquals(set.size(), 3);
    set.add("bye");
    Assert.assertEquals(set.size(), 4);
    set.add("Hi");
    Assert.assertEquals(set.size(), 4);
  }

  @Test
  public void contains() {
    Assert.assertTrue(set.contains("Hi"));
    Assert.assertFalse(set.contains("bye"));
  }

  @Test
  public void add() {
    contains();
    size();
  }

  @Test
  public void remove() {
    set.remove("Hi");
    Assert.assertFalse(set.contains("Hi"));
    set.remove("bye");
    Assert.assertEquals(set.size(), 2);
  }

  @Test
  public void clear() {
    set.clear();
    Assert.assertEquals(set.size(), 0);
  }
}