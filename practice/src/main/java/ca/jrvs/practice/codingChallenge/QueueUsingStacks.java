package ca.jrvs.practice.codingChallenge;

import java.util.Stack;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Implement-Queue-using-Stacks-551268c5a9cd44e28fb096f47dd49afd
 * Approach 1
 */
public class QueueUsingStacks {

  private Stack<Integer> empty;
  private Stack<Integer> full;

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
