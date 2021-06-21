package ca.jrvs.practice.codingChallenge;

/**
 * Ticket link: https://www.notion.so/jarvisdev/String-to-Integer-atoi-461e3bd920554f5eabb2638c2219f7cc
 */
public class StringToInteger {

  /**
   * Complexity: O(n)
   * Justification: Need to parse all n characters to establish whether it is a digit.
   * There is nothing faster that can be done.
   */
  public int builtInParsingAtoi(String s) {
    int retVal = 0;
    int i = 0;
    boolean negative = false;

    s = s.trim();
    if (s.length() == 0)
      return 0;

    if (s.charAt(0) == '+' || s.charAt(0) == '-') {
      negative = s.charAt(0) == '-';
      i++;
      if (i == s.length())
        return 0;
    }

    while (s.codePointAt(i) >= 48 && s.codePointAt(i) <= 57) {
      i++;
      if (i == s.length())
        break;
    }

    if (!s.substring(0,i).matches("[+-]?\\d+"))
      return 0;

    try {
      retVal = Integer.parseInt(s.substring(0,i));
    } catch (NumberFormatException ex) {
      if (negative)
        retVal = Integer.MIN_VALUE;
      else
        retVal = Integer.MAX_VALUE;
    }

    return retVal;
  }

  public int myAtoi(String s) {
    int retVal = 0, index = -1, sign = 1, currentInt = 0;

    s = s.trim();

    if (s.length() == 0)
      return 0;

    if (s.charAt(0) == '-') {
      sign = -1;
      index = 0;
    }
    else if (s.charAt(0) == '+')
      index = 0;

    while (currentInt >= 0 && currentInt <= 9) {
      try {
        retVal = Math.multiplyExact(retVal, 10);
        retVal = Math.addExact(retVal, currentInt);
        index++;
        currentInt = s.codePointAt(index) - 48;
      }
      catch (ArithmeticException ex) {
        retVal = sign == 1 ? Integer.MAX_VALUE : Integer.MIN_VALUE;
        sign = 1;
        break;
      }
      catch (StringIndexOutOfBoundsException ex) {
        break;
      }
    }

    return sign * retVal;
  }

}