package cn.navyd.leetcode.sort;

import java.util.Arrays;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Submission.Status;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Unskilled;

/**
 * <pre>
Given a collection of intervals, merge all overlapping intervals.

Example 1:

Input: [[1,3],[2,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
Example 2:

Input: [[1,4],[4,5]]
Output: [[1,5]]
Explanation: Intervals [1,4] and [4,5] are considered overlapping.
NOTE: input types have been changed on April 15, 2019. Please reset to default code definition to get new method signature.
 * </pre>
 * @author navyd
 *
 */
@Unskilled
@Problem(number = 56, difficulty = Difficulty.MEDIUM, tags = Tag.SORT, url = "https://leetcode.com/problems/merge-intervals/")
public interface MergeIntervals {
  /**
   * 合并重叠的区间
   * @param intervals
   * @return
   */
  public int[][] merge(int[][] intervals);
  
  @Author(name = "brubru777", significant = true, 
      referenceUrls = "https://leetcode.com/problems/merge-intervals/discuss/21222/A-simple-Java-solution")
  @Submission(date = "2019-05-16", status = Status.ACCEPTED,
      runtime = 36, runtimeBeatRate = 41.45, memory = 36, memoryBeatRate = 99.99,
      url = "https://leetcode.com/submissions/detail/229207368/")
  @Solution(timeComplexity = Complexity.O_N_LOG_N, spaceComplexity = Complexity.O_N)
  public static class SolutionBySort implements MergeIntervals {

    /**
     * 思路：合并两个重复的区间，如果intervals是根据interval.start有序的，合并区间时，只需要判断当前区间cur.end与下一个区间的next.start的关系即可
     * <ol>
     * <li>如果cur.end >= next.start说明两区间相交。否则不相交
     * <li>如果相交，则cur.end更新为cur next end的较大值
     * <li>继续使用更新后的cur interval去合并下一个区间。保证区间的连续
     * <li>当两个区间不相交时，更新cur interval为next，上一个区间已经完全合并完毕
     * </ol>
     * 实现：
     * <ul>
     * <li>排序比较器：使用start比较{@code (i1, i2) -> i1[0] - i2[0]}
     * <li>不允许返回存在null的intervals，只能返回一个新的数组。该length在合并时统计即可
     * </ul>
     */
    @Override
    public int[][] merge(int[][] intervals) {
      if (intervals == null || intervals.length < 2)
        return intervals;
      // 排序
      Arrays.sort(intervals, (i1, i2) -> i1[0] - i2[0]);
      // 合并的新数组 size
      int mergedIntervalsSize = intervals.length;
      // 当前合并的区间
      int[] curInterval = intervals[0];
      for (int i = 1; i < intervals.length; i++) {
        int[] nextInterval = intervals[i];
        final int curEnd = curInterval[1], 
            nextStart = nextInterval[0], 
            nextEnd = nextInterval[1]; 
        // 两个区间 相交
        if (curEnd >= nextStart) {
          // 将当前区间end置为两区间end较大值
          curInterval[1] = Integer.max(curEnd, nextEnd);
          // 将next置为null
          intervals[i] = null;
          // 合并一个区间 size-1
          mergedIntervalsSize--;
        } 
        // 不相交，重置比较区间
        else
          curInterval = nextInterval;
      }
      // 返回比较结果
      int[][] mergedIntervals = new int[mergedIntervalsSize][];
      for (int i = 0, j = 0; i < intervals.length; i++) {
        int[] cur = intervals[i];
        if (cur != null)
          mergedIntervals[j++] = cur;
      }
      return mergedIntervals;
    }
  }
}
