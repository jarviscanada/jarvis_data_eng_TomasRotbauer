package ca.jrvs.practice.codingChallenge;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QueueUsingStacksUnitTimeTest {

  private QueueUsingStacksUnitTime queue;

  @Before
  public void setUp() {
    queue = new QueueUsingStacksUnitTime();
    queue.push(5);
    queue.push(6);
    queue.push(7);
  }

  @Test
  public void push() {
    Assert.assertFalse(queue.empty());
  }

  @Test
  public void pop() {
    Assert.assertEquals(5, queue.pop());
    Assert.assertEquals(6, queue.pop());
    Assert.assertEquals(7, queue.pop());
    Assert.assertTrue(queue.empty());
  }

  @Test
  public void peek() {
    Assert.assertEquals(5, queue.peek());
    Assert.assertEquals(5, queue.peek());
    Assert.assertEquals(5, queue.pop());
    Assert.assertEquals(6, queue.peek());
  }

  @Test
  public void empty() {
    Assert.assertFalse(queue.empty());
    queue.pop();
    queue.pop();
    queue.pop();
    Assert.assertTrue(queue.empty());
  }
}