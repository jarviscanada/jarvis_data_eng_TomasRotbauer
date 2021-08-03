package ca.jrvs.practice.codingChallenge;

import java.util.Arrays;

/**
 * Ticket link: https://www.notion.so/jarvisdev/Count-Primes-337596ec61314aa3bddbff5fc33ee1fe
 */
public class CountPrimes {

  /**
   * Complexity: O(nloglogn)
   * Justification: The total time spent finding the next prime is equal to the number of times we
   * add one to p, which is at most O(sqrtn). Finally, the time spent removing multiples is at most
   * O(nloglogn).
   */
  public int sieveOfEratosthenes(int n) {
    //Only want numbers less than n, so subtract 1
    n -= 1;
    if (n < 2)
      return 0;

    int numPrimes = n - 1;

    boolean[] A = new boolean[n + 1];
    Arrays.fill(A, true);

    for (int i = 2; i <= java.lang.Math.sqrt(n); i++)
      if (A[i])
        for (int j = i*i; j <= n; j += i) {
          if (!A[j])
            continue;
          A[j] = false;
          numPrimes--;
        }

    return numPrimes;
  }
}
