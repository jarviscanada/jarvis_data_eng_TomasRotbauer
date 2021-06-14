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

/**
 * Approach 2
 */
class StackUsingQueues {

  private final Queue<Integer> q1;
  private final Queue<Integer> q2;
  private boolean currentQ1 = true;

  /**
   * Complexity: O(1)
   * Justification: Just initializing
   */
  public StackUsingQueues() {
    q1 = new LinkedList<>();
    q2 = new LinkedList<>();
  }

  /**
   * Complexity: O(1)
   * Justification: Just need to link the new node to the end of one of the linked-list queues
   */
  public void push(int x) {
    if (currentQ1)
      q1.add(x);
    else
      q2.add(x);
  }

  /**
   * Complexity: O(n)
   * Justification: Need to iterate through all n nodes to get the last one (in queue data structure)
   */
  public int pop() {
    int retVal;

    if (currentQ1) {
      while (q1.size() > 1)
        q2.add(q1.remove());
      retVal = q1.remove();
    }
    else {
      while (q2.size() > 1)
        q1.add(q2.remove());
      retVal = q2.remove();
    }

    currentQ1 = !currentQ1;
    return retVal;
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
    return currentQ1 ? q1.isEmpty() : q2.isEmpty();
  }
}
