package ca.jrvs.practice.sorting;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MergeSortTest {

  private int[] unsortedArray;
  private int[] sortedArray;
  private MergeSort mergeSort;

  @Before
  public void setUp() throws Exception {
    unsortedArray = new int[] {1,212,31,46,41,34,6,44,646,4,6,8};
    sortedArray = new int[] {1,4,6,6,8,31,34,41,44,46,212,646};
    mergeSort = new MergeSort();
  }

  @Test
  public void mergeSort() {
    mergeSort.mergeSort(unsortedArray,0, unsortedArray.length-1);
    Assert.assertArrayEquals(sortedArray, unsortedArray);
  }
}