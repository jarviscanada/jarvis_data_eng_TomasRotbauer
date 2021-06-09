package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class StackUsingQueuesTest {

  private StackUsingQueues stackUsingQueues;

  @Before
  public void setUp() {
    stackUsingQueues = new StackUsingQueues();
    stackUsingQueues.push(5);
    stackUsingQueues.push(6);
  }

  @Test
  public void push() {
    Assert.assertFalse(stackUsingQueues.empty());
  }

  @Test
  public void pop() {
    Assert.assertEquals(6, stackUsingQueues.pop());
    Assert.assertEquals(5, stackUsingQueues.pop());
  }

  @Test
  public void top() {
    Assert.assertEquals(6, stackUsingQueues.top());
    Assert.assertEquals(6, stackUsingQueues.top());
  }

  @Test
  public void empty() {
    Assert.assertFalse(stackUsingQueues.empty());
    stackUsingQueues.pop(); stackUsingQueues.pop();
    Assert.assertTrue(stackUsingQueues.empty());
  }
}