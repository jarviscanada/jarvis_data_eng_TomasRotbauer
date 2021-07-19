package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import ca.jrvs.practice.codingChallenge.LinkedListCycle.ListNode;
import org.junit.Test;

public class LinkedListCycleTest {

  private final LinkedListCycle solver = new LinkedListCycle();

  @Test
  public void hasCycleTwoPointers() {
    ListNode head = null;
    assertFalse(solver.hasCycleTwoPointers(head));
    head = new ListNode(0);
    assertFalse(solver.hasCycleTwoPointers(head));

    ListNode current = head;
    ListNode cycle = null;
    for (int i = 0; i < 10; i++) {
      current.next = new ListNode(i);
      current = current.next;
      if (i == 5)
        cycle = current;
    }
    current.next = cycle;

    assertTrue(solver.hasCycleTwoPointers(head));
  }
}