package ca.jrvs.practice.codingChallenge;

import java.util.EmptyStackException;
import java.util.Stack;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Valid-Parentheses-6885bc4158db484aab2ab91f82992e33
 */
public class ValidParentheses {

  private final Stack<Character> stack = new Stack<>();

  /**
   * Complexity: O(n)
   * Justification: Just iterate through each character once, and perform unit-time operations.
   */
  public boolean isValid(String s) {
    char current;
    for (int i = 0; i < s.length(); i++) {
      current = s.charAt(i);
      if (current == '(')
        stack.push(')');
      else if (current == '[')
        stack.push(']');
      else if (current == '{')
        stack.push('}');
      else if (current != tryPop())
        return false;
    }
    return stack.empty();
  }

  private char tryPop() {
    try {
      return stack.pop();
    } catch (EmptyStackException ex) {
      return '\0';
    }
  }
}
