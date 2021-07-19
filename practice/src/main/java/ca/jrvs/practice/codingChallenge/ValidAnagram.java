package ca.jrvs.practice.codingChallenge;

import static java.util.Arrays.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Valid-Anagram-661a9b8f3029459592215c30e0fa02ad
 */
public class ValidAnagram {

  /**
   * Complexity: O(nlogn)
   * Justification: sorting is the bottleneck, and it's complexity is as specified.
   */
  public boolean sorting(String s, String t) {
    if (s == null || s.isEmpty() || t == null || t.isEmpty())
      throw new IllegalArgumentException("Strings must have length of at least 1");

    if (s.length() != t.length())
      return false;

    char[] sArr = s.toCharArray();
    char[] tArr = t.toCharArray();

    Arrays.sort(tArr);
    Arrays.sort(sArr);

    return Arrays.equals(Arrays.copyOfRange(sArr, 0, tArr.length), tArr);
  }

  /**
   * Complexity: O(n)
   * Justification: Go through all n characters once to populate hashmap, then once more to validate.
   */
  public boolean hashMap(String s, String t) {
    if (s == null || s.isEmpty() || t == null || t.isEmpty())
      throw new IllegalArgumentException("Strings must have length of at least 1");

    if (s.length() != t.length())
      return false;

    Map<Character, Integer> map = new HashMap<>();
    char curr;

    for(int i = 0; i < s.length(); i++) {
      curr = s.charAt(i);
      if (map.containsKey(curr))
        map.put(curr, map.get(curr) + 1);
      else
        map.put(curr, 1);
    }

    for (int i = 0; i < s.length(); i++) {
      curr = t.charAt(i);
      if (!map.containsKey(curr) || map.get(curr) == 0)
        return false;
      map.put(curr, map.get(curr) - 1);
    }

    return true;
  }
}
