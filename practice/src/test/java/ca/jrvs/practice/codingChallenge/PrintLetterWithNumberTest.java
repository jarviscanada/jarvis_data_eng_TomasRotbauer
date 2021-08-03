package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class PrintLetterWithNumberTest {

  private final PrintLetterWithNumber solver = new PrintLetterWithNumber();

  @Test
  public void enumerateStringChars() {
    assertEquals("", solver.enumerateStringChars(""));
    assertEquals("a1", solver.enumerateStringChars("a"));
    assertEquals("a1b2c3d4", solver.enumerateStringChars("abcd"));
    assertEquals("J36o15h8n14", solver.enumerateStringChars("John"));
    assertEquals("H34e5l12l12o15W49o15r18l12d4", solver.enumerateStringChars("HelloWorld"));
    assertEquals("", solver.enumerateStringChars("jhgjgh%%"));

  }
}