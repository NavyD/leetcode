package cn.navyd.leetcode.sort;

import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;

@Problem(number = 220, tags = Tag.SORT, difficulty = Difficulty.MEDIUM, url = "https://leetcode.com/problems/contains-duplicate-iii/")
public interface ContainsDuplicateIII {
  /**
   * 要求nums两个下标i,j的绝对值在k范围内对应的nums[i], nums[j]的绝对值在t范围内
   * @param nums
   * @param k
   * @param t
   * @return
   */
  public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t);

  @Submission(runtime = 405, runtimeBeatRate = 11.46, memory = 37.2, memoryBeatRate = 71.16,
      date = "2019-05-12", 
      url = "https://leetcode.com/submissions/detail/228294328/")
  @Solution(author = "navyd", timeComplexity = Complexity.O_N_K, spaceComplexity = Complexity.O_1)
  public static class SolutionByIteration implements ContainsDuplicateIII {

    /**
     * 遍历迭代，暴力破解
     */
    @Override
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
      for (int i = 0; i < nums.length; i++) {
        final int limit = i+k;
        for (int next = i+1; next <= limit && next < nums.length; next++) {
          long val = nums[i], nextVal = nums[next];
          // 可能导致int溢出
          long abs = Math.abs(val - nextVal);
          // 比较绝对值 <= t则返回true
          if (abs <= t)
            return true;
        }
      }
      return false;
    }
  }
}
