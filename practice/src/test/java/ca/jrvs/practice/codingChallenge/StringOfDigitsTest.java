package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class StringOfDigitsTest {
  private StringOfDigits solver = new StringOfDigits();

  @Test
  public void checkStringASCII() {
    assertTrue(solver.checkStringASCII("1234567890"));
    assertTrue(solver.checkStringASCII("1"));
    assertFalse(solver.checkStringASCII("0.8"));
    assertFalse(solver.checkStringASCII("123,123,678"));
  }

  @Test
  public void checkStringJavaInteger() {
    assertTrue(solver.checkStringJavaInteger("1234567890"));
    assertTrue(solver.checkStringJavaInteger("1"));
    assertFalse(solver.checkStringJavaInteger("0.8"));
    assertFalse(solver.checkStringJavaInteger("123,123,678"));
  }
}