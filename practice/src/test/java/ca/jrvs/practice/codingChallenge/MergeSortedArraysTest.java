package ca.jrvs.practice.codingChallenge;

import static org.junit.Assert.*;

import org.junit.Test;

public class MergeSortedArraysTest {

  private final MergeSortedArrays solver = new MergeSortedArrays();

  @Test
  public void merge() {
    int[] nums1 = {1,2,3,4,0,0,0};
    int[] nums2 = {-2,3,99};
    int[] correct = {-2,1,2,3,3,4,99};
    solver.merge(nums1, 4, nums2, 3);
    assertArrayEquals(correct, nums1);
  }
}