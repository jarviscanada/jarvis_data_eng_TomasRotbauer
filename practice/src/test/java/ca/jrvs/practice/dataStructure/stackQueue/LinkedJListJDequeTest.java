package ca.jrvs.practice.dataStructure.stackQueue;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class LinkedJListJDequeTest {

  private JDeque<String> deque;

  @Before
  public void setUp() {
    deque = new LinkedJListJDeque<>();
    deque.add("Hello");
    deque.add("Hi");
    deque.add("Hey");
  }

  @After
  public void tearDown() {
    while (deque.peek() != null)
      deque.pop();
  }

  @Test
  public void add() {
    tearDown();
    deque.add("bye");
    Assert.assertEquals(deque.peek(), "bye");
  }

  @Test
  public void remove() {
    deque.remove();
    Assert.assertEquals(deque.peek(), "Hi");
  }

  @Test
  public void pop() {
    deque.pop();
    Assert.assertEquals(deque.peek(), "Hello");
    tearDown();
    Assert.assertEquals(deque.peek(), null);
  }

  @Test
  public void push() {
    deque.push("bye");
    Assert.assertEquals(deque.peek(), "bye");
  }

  @Test
  public void peek() {
    Assert.assertEquals(deque.peek(), "Hello");
    deque.remove();
    Assert.assertEquals(deque.peek(), "Hi");
  }
}