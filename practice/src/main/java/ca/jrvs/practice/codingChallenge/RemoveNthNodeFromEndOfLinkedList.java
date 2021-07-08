package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Nth-Node-From-End-of-LinkedList-ab11b750e2554af39ce45a923a385193
 */
public class RemoveNthNodeFromEndOfLinkedList {
  /**
   * Definition for singly-linked list
   */
  public static class ListNode {
      int val;
      ListNode next;
      ListNode() {}
      ListNode(int val) { this.val = val; }
      ListNode(int val, ListNode next) { this.val = val; this.next = next; }
  }

  /**
   * Complexity: O(n)
   * Justification: Pass through all nodes twice ~ O(2n) = O(n)
   */
  public ListNode removeNthFromEnd(ListNode head, int n) {
    int length = 0;
    ListNode current = head;

    while (current != null) {
      length++;
      current = current.next;
    }

    int removeIndex = length - n;
    if (removeIndex < 0 || n < 1)
      return head;

    current = head;
    if (removeIndex == 0) {
      head = head.next;
      current.next = null;
      return head;
    }

    for (int i = 0; i < removeIndex - 1; i++)
      current = current.next;

    ListNode del = current.next;
    current.next = del.next;
    del.next = null;

    return head;
  }
}
