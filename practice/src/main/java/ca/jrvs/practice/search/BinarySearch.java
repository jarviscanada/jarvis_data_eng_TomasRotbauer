package ca.jrvs.practice.search;

import java.util.Arrays;
import java.util.Optional;

public class BinarySearch {

  /**
   * find the the target index in a sorted array
   *
   * @param arr input array is sorted
   * @param target value to be searched
   * @return target index or Optional.empty() if not found
   */
  public <E extends Comparable<E>> Optional<Integer> binarySearchRecursion(E[] arr, E target) {
    int middle = arr.length/2;

    if (arr.length == 0)
      return Optional.empty();
    else if (arr[middle].compareTo(target) > 0)
      return binarySearchRecursion(Arrays.copyOfRange(arr, 0, middle), target);
    else if (arr[middle].compareTo(target) < 0) {
      Optional<Integer> retVal;
      retVal = binarySearchRecursion(Arrays.copyOfRange(arr, middle + 1, arr.length), target);
      return retVal.map(integer -> 1 + integer + middle);
    }
    else
      return Optional.of(middle);
  }

  /**
   * find the the target index in a sorted array
   *
   * @param arr input array is sorted
   * @param target value to be searched
   * @return target index or Optional.empty() if not found
   */
  public <E extends Comparable<E>> Optional<Integer> binarySearchIteration(E[] arr, E target) {
    int start = 0, end = arr.length, middle;

    while (start < end) {
      middle = (start + end) / 2;
      if (arr[middle].compareTo(target) > 0)
        end = middle;
      else if (arr[middle].compareTo(target) < 0)
        start = middle + 1;
      else
        return Optional.of(middle);
    }

    return Optional.empty();
  }
}