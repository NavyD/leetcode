package cn.navyd.leetcode.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;

/**
 * <pre>
You are given an integer array nums and you have to return a new counts array. The counts array has the property where counts[i] is the number of smaller elements to the right of nums[i].

Example:

Input: [5,2,6,1]
Output: [2,1,1,0] 
Explanation:
To the right of 5 there are 2 smaller elements (2 and 1).
To the right of 2 there is only 1 smaller element (1).
To the right of 6 there is 1 smaller element (1).
To the right of 1 there is 0 smaller element.
 * </pre>
 * 
 * @author navyd
 *
 */
@Problem(number = 315, difficulty = Difficulty.HARD, tags = Tag.SORT, url = "https://leetcode.com/problems/count-of-smaller-numbers-after-self/")
public interface CountOfSmallerNumbersAfterSelf {
  public List<Integer> countSmaller(int[] nums);

  @Author(name = "danyfang7",
      referenceUrls = "https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/76583/11ms-JAVA-solution-using-merge-sort-with-explanation/143754")
  @Author(name = "lzyfriday", significant = true,
      referenceUrls = "https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/76583/11ms-JAVA-solution-using-merge-sort-with-explanation")
  @Submission(date = "2019-06-29", memory = 39.1, memoryBeatRate = 89.77, runtime = 5,
      runtimeBeatRate = 90.75, url = "https://leetcode.com/submissions/detail/239393643/")
  @Solution(spaceComplexity = Complexity.O_N_LOG_N, timeComplexity = Complexity.O_N)
  public static class SolutionByMergeSort implements CountOfSmallerNumbersAfterSelf {

    /**
     * 思路：
     * <p>排序，只需要记录num位置在排序后的对应位置即可。如果排序nums的下标，而不是nums数组，则不需要找num在nums中的位置。
     * <p>假设要合并两个有序数组left[], right[]到一个数组中，使用一个变量count记录从right[]移动到新数组中的数量，同时使用counts[]记录
     * 对应left num的对应的count，即counts就是smaller count结果
     * <p>算法：
     * <p>对nums的indexes做归并排序。归并移动right时记录counts数组，在归并完成后写入新排序的indexes
     * <p>count变量记录的是在有序数组right[], left[]归并时当right被移动到left前时的right num的数量，
     * 即记录nums中小于指定num的数量。
     * <p>复杂度：
     * <p>时间复杂度：归并排序为O(log N)，在merge中遍历start--end。遍历counts，nums为O(N)即为O(N)
     * <p>空间复杂度：merge中创建了一个数组，长度为start-end+1，平均长度为N/2，归并总空间为O(N/2 * log N)即O(N log N)
     */
    @Override
    public List<Integer> countSmaller(int[] nums) {
      if (nums == null || nums.length == 0)
        return Collections.emptyList();
      if (nums.length == 1)
        return Arrays.asList(0);
      // 初始化 构造counts indexes数组
      final int[] 
          indexes = new int[nums.length], 
          counts = new int[nums.length];
      for (int i = 0; i < nums.length; i++)
        indexes[i] = i;
      
      // 归并排序 生成counts
      mergeSort(nums, indexes, 0, nums.length - 1, counts);
      // 结果
      List<Integer> result = new ArrayList<>(nums.length);
      for (int count : counts)
        result.add(count);
      return result;
    }

    /**
     * 归并排序。将nums排序使的indexes在nums的下标有序
     */
    private void mergeSort(int[] nums, int[] indexes, int start, int end, int[] counts) {
      if (start >= end)
        return;
      int mid = (start + end) / 2;
      mergeSort(nums, indexes, start, mid, counts);
      mergeSort(nums, indexes, mid + 1, end, counts);
      merge(nums, indexes, start, mid, end, counts);
    }

    /**
     * 合并两个数组
     * <p>
     * 对于右边的 i <= end时，如果是一开始就是最大的，则count就为0，不需要记录count。 如果不是最大的，但是在归并后成了最大的，之前将保存了对应下标的count
     */
    private void merge(int[] nums, int[] indexes, int start, int mid, int end, int[] counts) {
      // i为左边开始，j为右边开始，k迭代newIndexes
      int i = start, j = mid + 1, k = 0;
      // 记录归并时left num小于的数量
      int count = 0;
      // 临时的新有序的索引
      final int[] newIndexes = new int[end - start + 1];
      // 归并
      while (i <= mid && j <= end) {
        // 如果右边num < 左边num
        if (nums[indexes[j]] < nums[indexes[i]]) {
          // 右边 排到前面
          newIndexes[k++] = indexes[j++];
          count++;
        } else {
          // 统计 被移动的左边num的count
          counts[indexes[i]] += count;
          // 左边排到前面
          newIndexes[k++] = indexes[i++];
        }
      }
      // 左边的都大于 排序数组最大的
      while (i <= mid) {
        counts[indexes[i]] += count;
        newIndexes[k++] = indexes[i++];
      }
      // 右边的
      while (j <= end) {
        newIndexes[k++] = indexes[j++];
      }
      // 保存排序下标
      for (int h = start; h <= end; h++)
        indexes[h] = newIndexes[h - start];
    }
  }

  public static void main(String[] args) {
    int[] a = {5, 2, 6, 1};
    CountOfSmallerNumbersAfterSelf test = new SolutionByMergeSort();
    System.out.println("count: " + test.countSmaller(a));
  }
}
