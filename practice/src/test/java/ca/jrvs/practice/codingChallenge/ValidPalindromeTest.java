package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ValidPalindromeTest {

  private ValidPalindrome validPalindrome;

  @Before
  public void setUp() {
    validPalindrome = new ValidPalindrome();
  }

  @Test
  public void twoPointers() {
    assertTrue(validPalindrome.twoPointers(" "));
    assertTrue(validPalindrome.twoPointers("a,!"));
    assertTrue(validPalindrome.twoPointers("Aa"));
    assertTrue(validPalindrome.twoPointers("aBA//"));
    assertTrue(validPalindrome.twoPointers("aw 1wa"));
    assertTrue(validPalindrome.twoPointers("race car"));
    assertTrue(validPalindrome.twoPointers("A man, a plan, a canal: Panama"));

    assertFalse(validPalindrome.twoPointers("My name is Eminem!"));
    assertFalse(validPalindrome.twoPointers("race 1 car"));
    assertFalse(validPalindrome.twoPointers("abb"));
  }

  @Test
  public void recursion() {
    assertTrue(validPalindrome.recursion(" "));
    assertTrue(validPalindrome.recursion("a,!"));
    assertTrue(validPalindrome.recursion("Aa"));
    assertTrue(validPalindrome.recursion("aBA//"));
    assertTrue(validPalindrome.recursion("aw 1wa"));
    assertTrue(validPalindrome.recursion("race car"));
    assertTrue(validPalindrome.recursion("A man, a plan, a canal: Panama"));

    assertFalse(validPalindrome.recursion("My name is Eminem!"));
    assertFalse(validPalindrome.recursion("race 1 car"));
    assertFalse(validPalindrome.recursion("abb"));
  }
}