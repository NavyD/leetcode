package cn.navyd.leetcode.sort;

import java.util.Arrays;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
Given an unsorted array, find the maximum difference between the successive elements in its sorted form.

Return 0 if the array contains less than 2 elements.

Example 1:

Input: [3,6,9,1]
Output: 3
Explanation: The sorted form of the array is [1,3,6,9], either
             (3,6) or (6,9) has the maximum difference 3.
Example 2:

Input: [10]
Output: 0
Explanation: The array contains less than 2 elements, therefore return 0.
Note:

You may assume all elements in the array are non-negative integers and fit in the 32-bit signed integer range.
Try to solve it in linear time/space.
 * </pre>
 * @author navyd
 *
 */
@Problem(number = 164, difficulty = Difficulty.HARD, tags = Tag.SORT,
    url = "https://leetcode.com/problems/maximum-gap/")
public interface MaximumGap {
  public int maximumGap(int[] nums);

  @Author(name = "navyd")
  @Submission(date = "2019-06-04", memory = 35.9, memoryBeatRate = 99.89, runtime = 5,
      runtimeBeatRate = 33.99, url = "https://leetcode.com/submissions/detail/233472726/")
  @Solution(spaceComplexity = Complexity.O_1, timeComplexity = Complexity.O_N_LOG_N)
  public static class SolutionBySimple implements MaximumGap {

    /**
     * 思路：将nums排序，然后遍历相邻元素的gap找到最大值。
     * <p>该方法不符合问题的线性时间空间要求
     */
    @Override
    public int maximumGap(int[] nums) {
      if (nums == null || nums.length < 2)
        return 0;
      // 快速排序
      Arrays.sort(nums);
      final int n = nums.length;
      // 寻找最大值
      int maxGap = 0;
      for (int i = 1; i < n; i++) {
        int gap = nums[i] - nums[i - 1];
        if (gap > maxGap)
          maxGap = gap;
      }
      return maxGap;
    }
  }
}
