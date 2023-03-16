package cn.navyd.leetcode.sort;

import cn.navyd.annotation.algorithm.ComplexityEnum;
import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.DifficultyEnum;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Submission;

/**
 * Given an array with n objects colored red, white or blue, sort them in-place so that objects of the same color are adjacent, with the colors in the order red, white and blue.
 * <p>
 * Here, we will use the integers 0, 1, and 2 to represent the color red, white, and blue respectively.
 * <p>
 * Note: You are not suppose to use the library's sort function for this problem.
 * <p>
 * Example:
 * <p>
 * Input: [2,0,2,1,1,0]
 * Output: [0,0,1,1,2,2]
 * Follow up:
 * <p>
 * A rather straight forward solution is a two-pass algorithm using counting sort.
 * First, iterate the array counting number of 0's, 1's, and 2's, then overwrite array with total number of 0's, then 1's and followed by 2's.
 * Could you come up with a one-pass algorithm using only constant space?
 *
 * @author navyd
 */
@Problem(number = 75, difficulty = DifficultyEnum.MEDIUM)
public interface SortColors {
  /**
   * 将nums的三种元素0,1,2排序。不能使用类库
   *
   * @param nums
   */
  public void sortColors(int[] nums);

  // two-pass
  @Author("navyd")
  @Submission(submittedDate = @DateTime("20190519"), runtime = 0, runtimeBeatRate = 100.00, memory = 34.3, memoryBeatRate = 100.00, url = "https://leetcode.com/submissions/detail/229812204/")
  @Submission(submittedDate = @DateTime("20190316"), runtime = 0, runtimeBeatRate = 100.00, memory = 34.8, memoryBeatRate = 95.61, url = "https://leetcode.com/submissions/detail/215028926/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_1)
  public static class SolutionByCounting implements SortColors {

    /**
     * 思路：对于连续范围的数0,1,2使用计数排序。小范围 int类型适合使用计数排序
     */
    @Override
    public void sortColors(int[] nums) {
      // 构造计数数组
      final int len = 3;
      int[] counts = new int[len];
      for (int num : nums) {
        counts[num]++;
      }
      // 重置nums。 i表示nums.num，j表示重置数组时的index
      for (int i = 0, j = 0; i < len; i++) {
        int count = counts[i];
        while (count-- > 0) {
          nums[j++] = i;
        }
      }
    }
  }

  // 最符合题意：one-pass
  @Author("navyd")
  @Submission(submittedDate = @DateTime("20190519"), runtime = 0, runtimeBeatRate = 100.00, memory = 34.2, memoryBeatRate = 100.00, url = "https://leetcode.com/submissions/detail/229819292/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_1)
  @Submission(submittedDate = @DateTime("20200101"), runtime = 0, runtimeBeatRate = 100.00, memory = 34.8, memoryBeatRate = 100.00, url = "https://leetcode.com/submissions/detail/290183906/")
  public static class SolutionByPartition implements SortColors {

    /**
     * 思路：nums仅存在三种元素，是大量重复元素类型，partition-3-way是quick select的最适合的情况
     * <p>
     * 使用pivot=1将数组元素分开
     */
    @Override
    public void sortColors(int[] nums) {
      int lo = 0, hi = nums.length - 1, i = lo;
      final int pivot = 1;
      // 1. 3way quick select with 1
      while (i <= hi) {
        int diff = nums[i] - pivot;
        if (diff < 0) {
          swap(nums, lo++, i++);
        } else if (diff > 0) {
          swap(nums, hi--, i);
        } else {
          i++;
        }
      }
    }

    static void swap(int[] nums, int i, int j) {
      int temp = nums[i];
      nums[i] = nums[j];
      nums[j] = temp;
    }
  }
}
