package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class RotateStringTest {

  private final RotateString rotateString = new RotateString();

  @Test
  public void simpleCheck() {
    assertTrue(rotateString.simpleCheck("abcde", "deabc"));
    assertFalse(rotateString.simpleCheck("abcde", "acbde"));
    assertFalse(rotateString.simpleCheck("abs", "a"));
    assertFalse(rotateString.simpleCheck("a", "b"));
    assertTrue(rotateString.simpleCheck("a", "a"));
    assertFalse(rotateString.simpleCheck(null, null));
  }
}