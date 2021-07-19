package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import ca.jrvs.practice.codingChallenge.RemoveNthNodeFromEndOfLinkedList.ListNode;
import org.junit.Test;

public class RemoveNthNodeFromEndOfLinkedListTest {

  private final RemoveNthNodeFromEndOfLinkedList remover = new RemoveNthNodeFromEndOfLinkedList();

  @Test
  public void removeNthFromEnd() {
    ListNode head = new ListNode(0);
    try {
      remover.removeNthFromEnd(head, 0);
      remover.removeNthFromEnd(head, 1);
      remover.removeNthFromEnd(head, 5);
    } catch (Exception e) {
      fail();
    }

    ListNode temp = head;
    for (int i = 1; i < 10; i++) {
      temp.next = new ListNode(i);
      temp = temp.next;
    }

    head = remover.removeNthFromEnd(head, 10);
    assertEquals(1, head.val);
    head = remover.removeNthFromEnd(head, 7);
    assertEquals(2, head.next.val);
    assertEquals(4, head.next.next.val);
  }
}