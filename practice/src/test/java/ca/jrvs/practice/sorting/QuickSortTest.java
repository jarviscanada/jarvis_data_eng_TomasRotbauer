package ca.jrvs.practice.sorting;

import static org.junit.Assert.*;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class QuickSortTest {

  private int[] unsortedArray;
  private int[] sortedArray;
  private QuickSort quickSort;

  @Before
  public void setUp() throws Exception {
    unsortedArray = new int[] {1,212,31,46,41,34,6,44,646,4,6,8};
    sortedArray = new int[] {1,4,6,6,8,31,34,41,44,46,212,646};
    quickSort = new QuickSort();
  }

  @Test
  public void quickSort() {
    quickSort.quickSort(unsortedArray,0, unsortedArray.length-1);
    Assert.assertArrayEquals(sortedArray, unsortedArray);
  }
}