package cn.navyd.leetcode.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <pre>
 * Given two arrays, write a function to compute their intersection.

Example 1:

Input: nums1 = [1,2,2,1], nums2 = [2,2]
Output: [2]
Example 2:

Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
Output: [9,4]
Note:

Each element in the result must be unique.
The result can be in any order.
 * 
 * </pre>
 * 
 * @author navyd
 *
 */
public class IntersectionOfTwoArrays {

  /**
   * 总的来说，解法只有两种，hash与sort
   * @param nums1
   * @param nums2
   * @return
   */
  public int[] intersection(int[] nums1, int[] nums2) {
    return intersectionByHash(nums1, nums2);
  }

  /**
   * 思路：将两个数组hash去重，添加一个到另一个hashset中，如果添加失败说明该元素是公共的
   * 如果仅去重一个数组，另一个添加到该hashset中时，就算hashset中不存在[7]，另一个存在如[7,7]会导致7是公共的
   * @author navyd
   *
   */
  private int[] intersectionByHash(int[] nums1, int[] nums2) {
    int[] max = nums1.length > nums2.length ? nums1 : nums2;
    int[] min = max == nums1 ? nums2 : nums1;
    Set<Integer> maxSet = new HashSet<>(max.length);
    Set<Integer> minSet = new HashSet<>(min.length);
    Set<Integer> result = new HashSet<>();
    addAll(maxSet, max);
    addAll(minSet, min);
    // 改进空间：不使用add，使用contains()，减少一个hashset开销
    for (Integer num : maxSet)
      if (!minSet.add(num))
        result.add(num);
    return toIntArray(result);
  }

  /**
   * 思路：通过将小数组排序，然后二分法搜索到set中
   * @param nums1
   * @param nums2
   * @return
   */
  public int[] intersectionBySort(int[] nums1, int[] nums2) {
    // 先排序
    int[] max = nums1.length > nums2.length ? nums1 : nums2;
    int[] min = max == nums1 ? nums2 : nums1;
    Arrays.sort(min);
    Set<Integer> set = new HashSet<>();
    // 二分法搜索
    for (int num : max) {
      if (Arrays.binarySearch(min, num) >= 0)
        set.add(num);
    }
    return toIntArray(set);
  }
  
  static void addAll(Set<Integer> set, int[] nums) {
    for (int num : nums)
      set.add(num);
  }

  static int[] toIntArray(Collection<Integer> col) {
    int[] resultNums = new int[col.size()];
    int i = 0;
    for (int num : col)
      resultNums[i++] = num;
    return resultNums;
  }
}

class SolutionReference {
  public int[] intersection(int[] nums1, int[] nums2) {
    return intersectionByHashMap(nums1, nums2);
  }
  
  public int[] intersectionByHashMap(int[] nums1, int[] nums2) {
    Map<Integer, Boolean> map = new HashMap<>();
    for (int num : nums1)
      map.put(num, true);
    List<Integer> list = new ArrayList<>();
    for (int num : nums2)
      if (map.containsKey(num) && map.get(num)) {
        map.put(num, false);
        list.add(num);
      }
    return toIntArray(list);
  }
  
  public int[] intersectionByHashSet(int[] nums1, int[] nums2) {
    Set<Integer> set1 = new HashSet<>();
    Set<Integer> set2 = new HashSet<>();
    for (int num : nums1)
      set1.add(num);
    for (int num : nums2)
      if (set1.contains(num))
        set2.add(num);
    return toIntArray(set2);
  }
  
  static int[] toIntArray(Collection<Integer> col) {
    int[] resultNums = new int[col.size()];
    int i = 0;
    for (int num : col)
      resultNums[i++] = num;
    return resultNums;
  }
}
