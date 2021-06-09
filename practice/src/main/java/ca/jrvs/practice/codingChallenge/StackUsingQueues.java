package ca.jrvs.practice.codingChallenge;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Implement-Stack-using-Queue-c645b705f83849fcb2196ab5f1637684
 */
public class StackUsingQueues {

  private final Queue<Integer> q1;
  private final Queue<Integer> q2;
  private boolean currentQ1 = true;

  public StackUsingQueues() {
    q1 = new LinkedList<>();
    q2 = new LinkedList<>();
  }

  public void push(int x) {
    if (currentQ1)
      q1.add(x);
    else
      q2.add(x);
  }

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

  public int top() {
    int top = pop();
    push(top);
    return top;
  }

  public boolean empty() {
    return currentQ1 ? q1.isEmpty() : q2.isEmpty();
  }
}
