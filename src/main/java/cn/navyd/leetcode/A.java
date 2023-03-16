package cn.navyd.leetcode;

import java.util.Arrays;

public class A {
  void sort(int[] a) {

  }

  static int[] merge(int[] a, int lo, int mid, int hi) {
    int left = lo, right = mid;
    while (left <= mid && right <= hi) {
      if (a[left] > a[right]) {
        // left整体移动1位到right 
        int tmp = a[right];
        int i = right;
        while (i > left)
        // i=1 a[1]=a[0]
        {
          a[i--] = a[i];
        }
        a[i] = tmp;
        // 
        left++;
        mid++;
        right++;
      } else {
        left++;
      }
    }
    return a;
  }

  static void wmerge(int[] a, int i, int j, int e) {
    while (j < e) {
      if (a[i] < a[j]) {
        i++;
      } else {
        swap(a, i, j);
        i++;
      }
    }
  }

  static void swap(int[] a, int i, int j) {
    int t = a[i];
    a[i] = a[j];
    a[j] = t;
  }

  public static void main(String[] args) {
    int[] a = {6, 8, 10, 2, 5, 12};
    System.out.println(Arrays.toString(a));
    System.out.println(Arrays.toString(merge(a, 0, a.length / 2, a.length - 1)));
  }
}

