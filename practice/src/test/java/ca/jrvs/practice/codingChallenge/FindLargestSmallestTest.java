package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FindLargestSmallestTest {

  private static final FindLargestSmallest solver = new FindLargestSmallest();
  private static final int[] array = {14,22,999998,3746,20913,-99082,0,212};
  private static final List<Integer> list = new ArrayList<>();

  @BeforeClass
  public static void setUp() {
    for (int num : array)
      list.add(num);
  }

  @Test
  public void forLoop() {
    assertEquals(solver.forLoop(array, true), 999998);
    assertEquals(solver.forLoop(array, false), -99082);
  }

  @Test
  public void streamAPI() {
    assertEquals(solver.streamAPI(array, true), 999998);
    assertEquals(solver.streamAPI(array, false), -99082);
  }

  @Test
  public void javaAPI() {
    assertEquals(solver.javaAPI(list, true).intValue(), 999998);
    assertEquals(solver.javaAPI(list, false).intValue(), -99082);
  }
}