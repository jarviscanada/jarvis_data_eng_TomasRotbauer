package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import java.util.Arrays;
import org.junit.Test;

public class RemoveElementTest {

  private final RemoveElement solver = new RemoveElement();

  @Test
  public void remove() {
    int[] arr = {0,1,1,1,2,3,4,5,2,1,1,99,8,6,767,4,5,3,1};
    int[] solution = {0,2,2,3,3,4,4,5,5,6,8,99,767};
    assertEquals(13, solver.remove(arr, 1));
    arr = Arrays.copyOfRange(arr, 0, 13);
    Arrays.sort(arr);
    assertArrayEquals(solution, arr);
  }
}