package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Check-if-a-String-contains-only-digits-2248a6d7520d427cb364728eaf9dc131
 */
public class StringOfDigits {

  /**
   * Complexity: O(n)
   * Justification: Need to verify all n characters. There is no other way.
   */
  public boolean checkStringASCII(String s) {
    for (int i = 0; i < s.length(); i++)
      if (s.codePointAt(i) < 48 || s.codePointAt(i) > 57)
        return false;
    return true;
  }

  /**
   * Complexity: O(n)
   * Justification: Need to verify all n characters. There is no other way.
   */
  public boolean checkStringJavaInteger(String s) {
    boolean isInteger = true;
    try {
      Integer.parseInt(s);
    } catch (NumberFormatException e) {
      isInteger = false;
    }
    return isInteger;
  }
}
