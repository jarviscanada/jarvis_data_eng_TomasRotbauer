package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StackUsingQueueTest {

  private StackUsingQueue stackUsingQueue;

  @Before
  public void setUp() {
    stackUsingQueue = new StackUsingQueue();
    stackUsingQueue.push(5);
    stackUsingQueue.push(6);
  }

  @Test
  public void push() {
    Assert.assertFalse(stackUsingQueue.empty());
  }

  @Test
  public void pop() {
    Assert.assertEquals(6, stackUsingQueue.pop());
    Assert.assertEquals(5, stackUsingQueue.pop());
  }

  @Test
  public void top() {
    Assert.assertEquals(6, stackUsingQueue.top());
    Assert.assertEquals(6, stackUsingQueue.top());
  }

  @Test
  public void empty() {
    Assert.assertFalse(stackUsingQueue.empty());
    stackUsingQueue.pop(); stackUsingQueue.pop();
    Assert.assertTrue(stackUsingQueue.empty());
  }
}