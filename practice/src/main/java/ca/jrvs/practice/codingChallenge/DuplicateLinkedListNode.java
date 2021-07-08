package ca.jrvs.practice.codingChallenge;

import ca.jrvs.practice.dataStructure.list.LinkedJList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Duplicate-LinkedList-Node-062094aea1dd4e25969afb91d06c3d5e
 */
public class DuplicateLinkedListNode {

  /**
   * Complexity: O(n)
   * Justification: Only traverse all linked list nodes once
   */
  public void removeDuplicates(LinkedJList<Integer> list) {
    Set<Integer> set = new HashSet<>();
    int i = 0;
    while (i < list.size())
      if (set.contains(list.get(i)))
        list.remove(i);
      else {
        set.add(list.get(i));
        i++;
      }
  }
}
