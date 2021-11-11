package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Reverse-Linked-List-df224586b746422687d0a30ab477f195
 */
public class ReverseLinkedList {

  public static class ListNode {
    int val;
    ListNode next;
    ListNode() {}
    ListNode(int val) { this.val = val; }
    ListNode(int val, ListNode next) { this.val = val; this.next = next; }
 }

  /**
   * Complexity: O(n)
   * Justification: Only pass through all nodes once.
   */
 public ListNode iteration(ListNode head) {
    ListNode current = head, previous = null, next = null;

    while (current != null) {
      next = current.next;
      current.next = previous;
      previous = current;
      current = next;
    }

    return previous;
 }

  /**
   * Complexity: O(n)
   * Justification: Only pass through all nodes once.
   */
  public ListNode recursion(ListNode head) {
    ListNode tail;
    if (head == null || head.next == null)
      return head;
    else
      tail = recursion(head.next);
    head.next.next = head;
    head.next = null;
    return tail;
  }
}
