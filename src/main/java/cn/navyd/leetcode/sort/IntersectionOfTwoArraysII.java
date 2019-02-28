package cn.navyd.leetcode.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * <pre>
Given two arrays, write a function to compute their intersection.

Example 1:

Input: nums1 = [1,2,2,1], nums2 = [2,2]
Output: [2,2]
Example 2:

Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
Output: [4,9]
Note:

Each element in the result should appear as many times as it shows in both arrays.
The result can be in any order.
Follow up:

What if the given array is already sorted? How would you optimize your algorithm?
What if nums1's size is small compared to nums2's size? Which algorithm is better?
What if elements of nums2 are stored on disk, and the memory is limited such that you cannot load all elements into the memory at once?

 * </pre>
 * @author navyd
 *
 */
public class IntersectionOfTwoArraysII {
  // 写不出
  public int[] intersect(int[] nums1, int[] nums2) {
    return null;
  }
}


class Solution {
  /**
   * 通过hash记录一个数组的元素出现次数，另一个数组元素出现则减去hash次数，保证不多不少匹配
   * @param nums1
   * @param nums2
   * @return
   */
  public int[] intersectByHash(int[] nums1, int[] nums2) {
    HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
    ArrayList<Integer> result = new ArrayList<Integer>();
    for (int i = 0; i < nums1.length; i++) {
      if (map.containsKey(nums1[i]))
        map.put(nums1[i], map.get(nums1[i]) + 1);
      else
        map.put(nums1[i], 1);
    }

    for (int i = 0; i < nums2.length; i++) {
      if (map.containsKey(nums2[i]) && map.get(nums2[i]) > 0) {
        result.add(nums2[i]);
        map.put(nums2[i], map.get(nums2[i]) - 1);
      }
    }

    int[] r = new int[result.size()];
    for (int i = 0; i < result.size(); i++) {
      r[i] = result.get(i);
    }

    return r;
  }

  /**
   * 将两个数组排序，依次比较两个数组的元素，如果相等则存入结果数组
   * @param nums1
   * @param nums2
   * @return
   */
  public int[] intersectBySort(int[] nums1, int[] nums2) {
    Arrays.sort(nums1);
    Arrays.sort(nums2);
    List<Integer> result = new ArrayList<>();
    {
      int i = 0, j = 0;
      while (i < nums1.length && j < nums2.length) {
        // 需要依次比较每个元素
        int n1 = nums1[i], n2 = nums2[j];
        if (n1 < n2)
          i++;
        else if (n1 > n2)
          j++;
        else {
          result.add(n1);
          // 相等的元素则比较后面的元素
          i++;
          j++;
        }
      }
    }
    int[] r = new int[result.size()];
    for (int k = 0; k < result.size(); k++) {
      r[k] = result.get(k);
    }
    return r;
  }
}
