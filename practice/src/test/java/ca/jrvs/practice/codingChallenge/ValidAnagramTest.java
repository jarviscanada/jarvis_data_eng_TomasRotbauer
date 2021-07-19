package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class ValidAnagramTest {

  private static final ValidAnagram validAnagram = new ValidAnagram();

  @Test
  public void sorting() {
    assertFalse(validAnagram.sorting("a", "b"));
    assertTrue(validAnagram.sorting("a","a"));
    assertTrue(validAnagram.sorting("tomato", "amoott"));
    assertFalse(validAnagram.sorting("abc", "abcd"));
  }

  @Test
  public void hashMap() {
    assertFalse(validAnagram.hashMap("a", "b"));
    assertTrue(validAnagram.hashMap("a","a"));
    assertTrue(validAnagram.hashMap("tomato", "amoott"));
    assertFalse(validAnagram.hashMap("abc", "abcd"));
  }
}