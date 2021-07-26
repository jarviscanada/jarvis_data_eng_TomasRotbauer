package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Duplicate-Characters-389f06a3ddc54d52bb93053d2d13cd7b
 */
public class DuplicateCharacters {

  /**
   * Complexity: O(n)
   * Justification: Traverse the String's characters exactly once, performing unit time operations
   * per each iteration.
   */
  public char[] findDuplicateChars(String s) {
    char[] duplicates = new char[s.length()/2];
    int j = 0;
    Map<Character, Boolean> map = new HashMap<>();

    for (int i = 0; i < s.length(); i++) {
      char c = s.charAt(i);
      if (map.containsKey(c)) {
        if (!map.get(c)) {
          map.put(c, true);
          duplicates[j] = c;
          j++;
        }
      }
      else {
        map.put(c, false);
      }
    }
    return Arrays.copyOfRange(duplicates, 0, j);
  }
}
