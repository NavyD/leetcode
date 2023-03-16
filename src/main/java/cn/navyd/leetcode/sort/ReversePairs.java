package cn.navyd.leetcode.sort;

import java.util.Arrays;

public class ReversePairs {
  public int reversePairs(int[] nums) {
    int res = 0;
    int[] copy = Arrays.copyOf(nums, nums.length);
    int[] bit = new int[copy.length + 1];

    Arrays.sort(copy);

    for (int ele : nums) {
      res += search(bit, index(copy, 2L * ele + 1));
      insert(bit, index(copy, ele));
    }

    return res;
  }

  private int index(int[] arr, long val) {
    int l = 0, r = arr.length - 1, m = 0;

    while (l <= r) {
      m = l + ((r - l) >> 1);

      if (arr[m] >= val) {
        r = m - 1;
      } else {
        l = m + 1;
      }
    }

    return l + 1;
  }

  private int search(int[] bit, int i) {
    int sum = 0;

    while (i < bit.length) {
      sum += bit[i];
      i += i & -i;
    }

    return sum;
  }

  private void insert(int[] bit, int i) {
    while (i > 0) {
      bit[i] += 1;
      i -= i & -i;
    }
  }

  public static void main(String[] args) {
    int[] nums = {2, 4, 3, 5, 1};
    ReversePairs p = new ReversePairs();
    System.out.println(p.reversePairs(nums));
  }
}