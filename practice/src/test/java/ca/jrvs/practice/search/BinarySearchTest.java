package ca.jrvs.practice.search;

import static org.junit.Assert.*;

import java.util.Optional;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BinarySearchTest {

  private Integer[] arr;
  private BinarySearch binarySearch;

  @Before
  public void setUp() throws Exception {
    arr = new Integer[]{0,1,2,3,4,5,6,7,8,9,10};
    binarySearch = new BinarySearch();
  }

  @Test
  public void binarySearchRecursion() {
    Assert.assertEquals(Optional.of(0), binarySearch.binarySearchRecursion(arr, 0));
    Assert.assertEquals(Optional.of(1), binarySearch.binarySearchRecursion(arr, 1));
    Assert.assertEquals(Optional.of(2), binarySearch.binarySearchRecursion(arr, 2));
    Assert.assertEquals(Optional.of(10), binarySearch.binarySearchRecursion(arr, 10));
    Assert.assertEquals(Optional.of(7), binarySearch.binarySearchRecursion(arr, 7));
    Assert.assertEquals(Optional.empty(), binarySearch.binarySearchRecursion(arr, -5));
    Assert.assertEquals(Optional.empty(), binarySearch.binarySearchRecursion(arr, 15));
  }

  @Test
  public void binarySearchIteration() {
  }
}