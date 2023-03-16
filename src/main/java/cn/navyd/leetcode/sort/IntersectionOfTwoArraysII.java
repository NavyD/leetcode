package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.navyd.annotation.algorithm.ComplexityEnum;
import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.DifficultyEnum;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
 * Given two arrays, write a function to compute their intersection.
 *
 * Example 1:
 *
 * Input: nums1 = [1,2,2,1], nums2 = [2,2]
 * Output: [2,2]
 * Example 2:
 *
 * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
 * Output: [4,9]
 * Note:
 *
 * Each element in the result should appear as many times as it shows in both arrays.
 * The result can be in any order.
 * Follow up:
 *
 * What if the given array is already sorted? How would you optimize your algorithm?
 * What if nums1's size is small compared to nums2's size? Which algorithm is better?
 * What if elements of nums2 are stored on disk, and the memory is limited such that you cannot load all elements into the memory at once?
 *
 * </pre>
 *
 * @author navyd
 */
@Problem(difficulty = DifficultyEnum.EASY, number = 350)
public interface IntersectionOfTwoArraysII {
  /**
   * 思路：取交集，结果与元素次数一致。那么hash计数首先想到
   * 这个也就转为查找、统计的问题
   * <p>
   * 查找可行方案：
   * <ul>
   * <li>hash
   * <li>binary search：注意统计不可行，无法保证区别重复查找
   * <li>two pointers
   * </ul>
   * <p>follow up
   * <p>如果array已有序：对于仅一个有序时，使用binary search，复杂度O(N log K).都有序时使用two pointers，复杂度为O(N+K)
   * <p>如果有小的nums：对于Hash minNums，可以更小的空间。
   * <p>如果内存不足，考虑归并排序
   *
   * @param nums1
   * @param nums2
   * @return
   */
  public int[] intersect(int[] nums1, int[] nums2);

  @Author("navyd")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_K), spaceComplexity = ComplexityEnum.O_N)
  @Submission(memory = 36.2, memoryBeatRate = 83.87, runtime = 2, runtimeBeatRate = 91.46, submittedDate = @DateTime("20191206"), url = "https://leetcode.com/submissions/detail/284008214/")
  public static class SolutionByHash implements IntersectionOfTwoArraysII {
    /**
     * 思路：hash计数
     * <p>优化：对于hash minNums可以使空间复杂度更小O(K)，时间还是O(N)
     */
    @Override
    public int[] intersect(int[] nums1, int[] nums2) {
      // 0. count for min nums
      final int[] maxNums = nums1.length > nums2.length ? nums1 : nums2,
          minNums = maxNums != nums1 ? nums1 : nums2;
      final Map<Integer, Integer> counts = new HashMap<>(minNums.length);
      for (int n : minNums) {
        counts.put(n, counts.getOrDefault(n, 0) + 1);
      }
      int[] res = new int[minNums.length];
      int i = 0;
      // 1. find intersection
      for (int n : maxNums) {
        Integer count = counts.get(n);
        // 2. check existence
        if (count != null && count > 0) {
          // 3. decrease count if exists
          res[i++] = n;
          counts.put(n, count - 1);
        }
      }
      return Arrays.copyOf(res, i);
    }
  }

  @Submission(memory = 36.6, memoryBeatRate = 83.87, runtime = 1, runtimeBeatRate = 100, submittedDate = @DateTime("20191206"), url = "https://leetcode.com/submissions/detail/284022925/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_N), spaceComplexity = ComplexityEnum.O_N)
  public static class SolutionByTwoPointers implements IntersectionOfTwoArraysII {
    /**
     * 思路：two pointers
     */
    @Override
    public int[] intersect(int[] nums1, int[] nums2) {
      // 0. sort both
      Arrays.sort(nums1);
      Arrays.sort(nums2);
      int[] res = new int[Math.min(nums1.length, nums2.length)];
      // res index
      int k = 0;
      // 1. find with two pointers
      int i = 0, j = 0;
      while (i < nums1.length && j < nums2.length) {
        if (nums1[i] < nums2[j]) {
          i++;
        } else if (nums1[i] > nums2[j]) {
          j++;
        } else {
          res[k++] = nums1[i];
          i++;
          j++;
        }
      }
      return Arrays.copyOf(res, k);
    }
  }

  public static class SolutionByBinarySearch implements IntersectionOfTwoArraysII {
    /**
     * binary search方案不可行：存在重复查找的可能
     * <pre>
     * [3,1,2]
     * [1,1]
     * Output
     * [1,1]
     * Expected
     * [1]
     * </pre>
     */
    @Override
    public int[] intersect(int[] nums1, int[] nums2) {
      // 0. sort max nums
      final int[] maxNums = nums1.length > nums2.length ? nums1 : nums2,
          minNums = maxNums != nums1 ? nums1 : nums2;
      Arrays.sort(maxNums);
      int[] res = new int[minNums.length];
      int i = 0;
      // 1. find nums with binary search
      for (int n : minNums) {
        if (binarySearch(maxNums, n) >= 0) {
          res[i++] = n;
        }
      }
      return Arrays.copyOf(res, i);
    }

    static int binarySearch(int[] nums, int val) {
      int hi = nums.length - 1, lo = 0;
      while (lo <= hi) {
        int mid = (hi + lo) / 2;
        if (nums[mid] < val) {
          lo = mid + 1;
        } else if (nums[mid] > val) {
          hi = mid - 1;
        } else {
          return mid;
        }
      }
      return -1;
    }
  }
}