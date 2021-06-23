package ca.jrvs.practice.codingChallenge;

import java.util.Stack;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Implement-Queue-using-Stacks-551268c5a9cd44e28fb096f47dd49afd
 * Approach 1
 */
public class QueueUsingStacks {

  private final Stack<Integer> empty, full;

  /**
   * Initialize data structure.
   * Complexity: O(1)
   * Justification: Just creating two new stacks
   */
  public QueueUsingStacks() {
    empty = new Stack<>();
    full = new Stack<>();
  }

  /**
   * Push element x to the back of queue.
   * Complexity: O(n)
   * Justification: Need to iterate through all existing elements twice - hence linear.
   */
  public void push(int x) {
    while (!full.empty())
      empty.push(full.pop());

    full.push(x);

    while (!empty.empty())
      full.push(empty.pop());
  }

  /**
   * Removes the element from in front of queue and returns that element.
   * Complexity: O(1)
   * Justification: Just pop.
   */
  public int pop() {
    return full.pop();
  }

  /**
   * Get the front element.
   * Complexity: O(1)
   * Justification: Just peek.
   */
  public int peek() {
    return full.peek();
  }

  /**
   * Returns whether the queue is empty.
   * Complexity: O(1)
   * Justification: Just check if empty.
   */
  public boolean empty() {
    return full.empty();
  }
}

/**
 * Approach 2
 */
class QueueUsingStacksUnitTime {

  private final Stack<Integer> popStack, pushStack;

  /**
   * Initialize data structure.
   * Complexity: O(1)
   * Justification: Just creating two new stacks
   */
  public QueueUsingStacksUnitTime() {
    popStack = new Stack<>();
    pushStack = new Stack<>();
  }

  /**
   * Push element x to the back of queue.
   * Complexity: O(1)
   * Justification: Just push onto pushStack.
   */
  public void push(int x) {
    pushStack.push(x);
  }

  /**
   * Removes the element from in front of queue and returns that element.
   * Complexity: Amortized O(1)
   * Justification: When popStack is empty, filling it is O(n). However, it then
   * lasts for n pops without needing to be refilled. Hence, complexity = O(n/n) = O(1).
   */
  public int pop() {
    if (popStack.empty())
      while (!pushStack.empty())
        popStack.push(pushStack.pop());

    return popStack.pop();
  }

  /**
   * Get the front element.
   * Complexity: O(1)
   * Justification: Same as pop, just don't remove the element.
   */
  public int peek() {
    int retVal = pop();
    popStack.push(retVal);
    return retVal;
  }

  /**
   * Returns whether the queue is empty.
   * Complexity: O(1)
   * Justification: Just check if empty.
   */
  public boolean empty() {
    return popStack.empty() && pushStack.empty();
  }
}
