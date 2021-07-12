package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import ca.jrvs.practice.codingChallenge.MiddleOfLinkedList.ListNode;
import org.junit.Test;

public class MiddleOfLinkedListTest {

  private MiddleOfLinkedList solver = new MiddleOfLinkedList();

  @Test
  public void middleNode() {
    ListNode head = null;
    assertNull(solver.middleNode(head));
    head = new ListNode(1);
    assertEquals(head, solver.middleNode(head));
    head.next = new ListNode(2);
    assertEquals(head.next, solver.middleNode(head));

    ListNode current = head.next;
    ListNode middle = null;
    for (int i = 3; i < 51; i++) {
      current.next = new ListNode(i);
      current = current.next;
      if (i == 26)
        middle = current;
    }

    assertEquals(middle, solver.middleNode(head));
  }
}