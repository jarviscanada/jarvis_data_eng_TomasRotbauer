package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class CountPrimesTest {

  private final CountPrimes solver = new CountPrimes();

  @Test
  public void sieveOfEratosthenes() {
    assertEquals(0, solver.sieveOfEratosthenes(0));
    assertEquals(0, solver.sieveOfEratosthenes(1));
    assertEquals(0, solver.sieveOfEratosthenes(2));
    assertEquals(1, solver.sieveOfEratosthenes(3));
    assertEquals(5, solver.sieveOfEratosthenes(13));
    assertEquals(25, solver.sieveOfEratosthenes(100));
  }
}