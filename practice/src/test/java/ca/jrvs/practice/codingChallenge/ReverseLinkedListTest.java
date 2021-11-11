package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import ca.jrvs.practice.codingChallenge.ReverseLinkedList.ListNode;
import org.junit.Before;
import org.junit.Test;

public class ReverseLinkedListTest {

  private ReverseLinkedList solver;
  private ListNode head;

  @Before
  public void setup() {
    solver = new ReverseLinkedList();
    head = new ListNode(0);
    ListNode current = head;
    for (int i = 1; i < 10; i++) {
      current.next = new ListNode(i);
      current = current.next;
    }
    current.next = null;
  }

  @Test
  public void iteration() {
    ListNode answer = solver.iteration(head);
    for (int i = 9; i > -1; i--) {
      assertEquals(i, answer.val);
      answer = answer.next;
    }
    assertNull(answer);
  }

  @Test
  public void recursion() {
    ListNode answer = solver.recursion(head);
    for (int i = 9; i > -1; i--) {
      assertEquals(i, answer.val);
      answer = answer.next;
    }
    assertNull(answer);
  }
}