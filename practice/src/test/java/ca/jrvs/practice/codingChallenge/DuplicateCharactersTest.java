package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class DuplicateCharactersTest {

  private final DuplicateCharacters solver = new DuplicateCharacters();

  @Test
  public void findDuplicateChars() {
    char[] arr = {'a', ' ', 'i', 'n', 'e', 'r', 'g'};
    assertArrayEquals(arr, solver.findDuplicateChars("Jarvis data engineering"));
  }
}