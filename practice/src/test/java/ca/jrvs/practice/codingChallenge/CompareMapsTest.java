package ca.jrvs.practice.codingChallenge;

import java.util.HashMap;
import java.util.Map;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CompareMapsTest {

  private final Map<Integer, Integer> m1 = new HashMap<>();
  private final Map<Integer, Integer> m2 = new HashMap<>();
  private final CompareMaps compareMaps = new CompareMaps();

  @Before
  public void setUp() {
    int i = 0;
    int j = 10;

    while (i < 10) {
      m1.put(i, j);
      m2.put(i, j);
      i++;
      j--;
    }
  }

  @After
  public void tearDown() {
    m1.clear();
    m2.clear();
  }

  @Test
  public void testEqual() {
    setUp();
    Assert.assertTrue(compareMaps.compareMaps(m1, m2));
    tearDown();
  }

  @Test
  public void testNotEqual() {
    setUp();
    m1.put(-1, -1);
    Assert.assertFalse(compareMaps.compareMaps(m1, m2));
    tearDown();
  }

}