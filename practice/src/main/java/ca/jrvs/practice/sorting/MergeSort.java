package ca.jrvs.practice.sorting;

public class MergeSort {

  public void mergeSort(int[] arr, int p, int r) {
    if (p < r) {
      int q = (p + r) / 2;
      mergeSort(arr, p, q);
      mergeSort(arr, q + 1, r);
      merge(arr, p, q, r);
    }
  }

  private void merge(int[] arr, int p, int q, int r) {
    int n = q - p + 1, m = r - q, i, j;
    int[] L = new int[n+1];
    int[] R = new int[m+1];

    for (i = 0; i < n; i++)
      L[i] = arr[p + i];
    for (j = 0; j < m; j++)
      R[j] = arr[q + j + 1];

    L[n] = Integer.MAX_VALUE;
    R[m] = Integer.MAX_VALUE;

    i = 0;
    j = 0;

    for (int k = p; k <= r; k++)
      if (L[i] <= R[j]) {
        arr[k] = L[i];
        i++;
      }
    else {
      arr[k] = R[j];
      j++;
      }

  }

}
