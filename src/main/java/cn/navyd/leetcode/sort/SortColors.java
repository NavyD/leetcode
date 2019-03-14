package cn.navyd.leetcode.sort;

import java.util.Arrays;

/**
Given an array with n objects colored red, white or blue, sort them in-place so that objects of the same color are adjacent, with the colors in the order red, white and blue.

Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.

Note: You are not suppose to use the library's sort function for this problem.

Example:

Input: [2,0,2,1,1,0]
Output: [0,0,1,1,2,2]
Follow up:

A rather straight forward solution is a two-pass algorithm using counting sort.
First, iterate the array counting number of 0's, 1's, and 2's, then overwrite array with total number of 0's, then 1's and followed by 2's.
Could you come up with a one-pass algorithm using only constant space?

 * @author navyd
 *
 */
public class SortColors {
  /**
   * 思路：利用快速排序的partition将仅有三种类型的值0,1,2分开，使用pivot=1分为四个区域：
   * [=pivot...<pivot...>pivot...=pivot]，然后合并为三个区域[<pivot...=pivot...>pivot]
   * 该方法不需要快速排序的递归，分区后即完成排序
   * 时间复杂度：O(N) 主要为元素比较和交换
   * 空间复杂度：O(1)
   * @param nums
   */
  public void sortColors(int[] nums) {
    partition(nums, 0, nums.length-1);
  }
  
  static void partition(int[] nums, int lo, int hi) {
    final int pivot = 1;
    int i = lo-1, p = lo-1, j = hi+1, q = hi+1;
    while (true) {
      while (i < hi && nums[++i] < pivot)
        ;
      while (j > lo && nums[--j] > pivot)
        ;
      if (i == j && nums[i] == pivot)
        swap(nums, i, ++p);
      if (i >= j)
        break;
      swap(nums, i, j);
      
      if (nums[i] == pivot)
        swap(nums, i, ++p);
      
      if (nums[j] == pivot)
        swap(nums, j, --q);
    }
    i = j + 1;
    for (int k = lo; k <= p; k++) 
      swap(nums, k, j--);
    for (int k = hi; k >= q; k--)
      swap(nums, i++, k);
  }
  
  static void swap(int[] nums, int i, int j) {
    if (i == j)
      return;
    int tmp = nums[i];
    nums[i] = nums[j];
    nums[j] = tmp;
  }
  
  public static void main(String[] args) {
    int[] 
//        a = {2,0,2,1,1,0};
        a= {1,0,2};
    SortColors o = new SortColors();
    o.sortColors(a);
    System.err.println(Arrays.toString(a));
  }
}
