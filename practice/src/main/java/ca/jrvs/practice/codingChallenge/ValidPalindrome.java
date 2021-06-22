package ca.jrvs.practice.codingChallenge;

import java.util.Locale;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Valid-Palindrome-3041533e13434bb8b0c80278bb19af66
 */
public class ValidPalindrome {

  /**
   * Complexity: O(n)
   * Justification: need to iterate through each character and sanitize it as well as compare.
   */
  public boolean twoPointers(String s) {
    s = sanitizeString(s);
    if (s.isEmpty())
      return true;

    int front = -1, back = s.length();
    
    while (front++ < back--)
      if (s.charAt(front) != s.charAt(back))
        return false;
    
    return true;
  }

  private String sanitizeString(String s) {
    s = s.replaceAll("[^a-zA-Z0-9]", "");
    s = s.toLowerCase();
    return s;
  }
}
