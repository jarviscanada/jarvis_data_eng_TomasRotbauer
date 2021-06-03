package ca.jrvs.practice.codingChallenge;

import java.util.Map;

/**
 * Ticket link: https://www.notion.so/jarvisdev/How-to-compare-two-maps-8d439bbb85db4b99ba2cdc183509a5db
 */
public class CompareMaps {

  /**
   * Complexity: O(n), n ~ the number of pairs in both maps Justification: Need to ensure one maps'
   * items are in the other and vice versa
   */
  public <K, V> boolean compareMaps(Map<K, V> m1, Map<K, V> m2) {
    return m1.equals(m2);
  }

}
