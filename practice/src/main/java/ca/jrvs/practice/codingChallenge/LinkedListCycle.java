package ca.jrvs.practice.codingChallenge;

import java.util.HashSet;
import java.util.Set;

/**
 * Ticket link: https://www.notion.so/jarvisdev/LinkedList-Cycle-6938f62cda004fbc97d761ab1ff7c5fd
 */
public class LinkedListCycle {
  /**
   * Definition for singly-linked list.
   */
  public static class ListNode {
     int val;
     ListNode next;
     ListNode(int x) {
         val = x;
         next = null;
     }
  }

  /**
   * Complexity: O(n)
   * Justification: Need to check each node to uncover a cycle
   */
  public boolean hasCycleSetApproach(ListNode head) {
    Set<ListNode> set = new HashSet<>();

    ListNode current = head;
    while (current != null) {
      if (set.contains(current))
        return true;
      set.add(current);
      current = current.next;
    }
    return false;
  }

  /**
   * Complexity: O(n)
   * Justification: Need to check each node to uncover a cycle
   */
  public boolean hasCycleTwoPointers(ListNode head) {
    if (head == null || head.next == null)
      return false;

    ListNode slow = head; ListNode fast = head;

    while (fast != null && fast.next != null) {
      fast = fast.next.next;
      slow = slow.next;

      if (fast == slow)
        return true;
    }
    return false;
  }

}
