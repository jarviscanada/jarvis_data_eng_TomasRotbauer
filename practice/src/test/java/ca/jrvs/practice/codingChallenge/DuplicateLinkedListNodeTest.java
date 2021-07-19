package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import ca.jrvs.practice.dataStructure.list.LinkedJList;
import org.junit.Test;

public class DuplicateLinkedListNodeTest {

  final DuplicateLinkedListNode remover = new DuplicateLinkedListNode();

  @Test
  public void removeDuplicates() {
    LinkedJList<Integer> list = new LinkedJList<>();
    remover.removeDuplicates(list);
    list.add(1); list.add(1); list.add(1);
    remover.removeDuplicates(list);
    assertEquals(1, list.size());
    assertEquals(1, list.get(0), 0);
    list.add(1);list.add(2);list.add(3);list.add(4);list.add(5);list.add(6);
    remover.removeDuplicates(list);
    assertEquals(6, list.size());
    list.add(1);list.add(2);list.add(2);list.add(4);list.add(5);list.add(4);
    list.add(19);list.add(2);list.add(31);list.add(14);list.add(5);list.add(6);
    remover.removeDuplicates(list);
    assertEquals(9, list.size());
    assertEquals(1, list.get(0), 0);
    assertEquals(14, list.get(8), 0);
  }
}