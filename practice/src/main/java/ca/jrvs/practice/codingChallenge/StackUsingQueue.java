package ca.jrvs.practice.codingChallenge;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Implement-Stack-using-Queue-c645b705f83849fcb2196ab5f1637684
 */
public class StackUsingQueue {

  private final Queue<Integer> queue;

  /**
   * Complexity: O(1)
   * Justification: Just initializing
   */
  public StackUsingQueue() {
    queue = new LinkedList<>();
  }

  /**
   * Complexity: O(1)
   * Justification: Just need to link the new node to the end of the linked-list queue
   */
  public void push(int x) {
    queue.add(x);
  }

  /**
   * Complexity: O(n)
   * Justification: Need to iterate through all n nodes to get the last one (in queue data structure)
   */
  public int pop() {
    for (int i = 1; i < queue.size(); i++)
      queue.add(queue.remove());

    return queue.remove();
  }

  /**
   * Complexity: O(n)
   * Justification: Need to iterate through all n nodes to get the last one (in queue data structure)
   */
  public int top() {
    int top = pop();
    push(top);
    return top;
  }

  /**
   * Complexity: O(1)
   * Justification: Only need to check if size > 0
   */
  public boolean empty() {
    return queue.isEmpty();
  }
}
