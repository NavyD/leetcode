package cn.navyd.leetcode.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Optimal;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Submission.Status;
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
     * <li>将已合并的区间标记为null
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
          // 将next置为null，标记为已合并的区间
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
  
  @Optimal
  @Author(name = "vegito2002", 
      referenceUrls = "https://leetcode.com/problems/merge-intervals/discuss/21223/Beat-98-Java.-Sort-start-and-end-respectively./115420")
  @Author(name = "baselRus", 
      referenceUrls = "https://leetcode.com/problems/merge-intervals/discuss/21223/Beat-98-Java.-Sort-start-and-end-respectively./21227")
  @Author(name = "D_shaw", significant = true, 
      referenceUrls = "https://leetcode.com/problems/merge-intervals/discuss/21223/Beat-98-Java.-Sort-start-and-end-respectively.")
  @Submission(date = "2019-05-17", status = Status.ACCEPTED,
      runtime = 2, runtimeBeatRate = 99.96, memory = 35.9, memoryBeatRate = 99.99,
      url = "https://leetcode.com/submissions/detail/229427496/")
  @Solution(timeComplexity = Complexity.O_N_LOG_N, spaceComplexity = Complexity.O_N)
  public static class SolutionBySortStartAndEnd implements MergeIntervals {

    /**
     * 思路：对于不同的区间必须满足{@code next.start > cur.end}。
     * <p>由于区间的特殊性{@code start<=end}，对于有序的starts,ends，如果区间是重叠的，{@code cur.end >= next.start}将会一直成立，
     * 直到遇到不重合的空间{@code next.start > cur.end}
     * 实现：
     * <ul>
     * <li>分别排序辅助数组starts与ends。由于quick sort和int原始类型的原因，会比comparator的比较快
     * <li>仅当找到不重合区间{@code next.start > cur.end}时合并区间。
     * 不同点{@link SolutionBySort#merge(int[][])}在与{@code cur.end >= next.start}开始合并区间并比较
     * <li>使用一个下标j记录合并区间start，用于当找到不重合区间时，读取该下标作为merged interval.start，并重置为不重合区间的start
     * <li>当遇到最后一个interval.end时，直接与记录的j合并即可
     * </ul>
     * <pre>
     * 如：存在区间[1,3], [2,5],[0,4],[8,9]
     * starts:  [0,1,2,8]
     * ends:    [3,4,5,9]
     * </pre>
     * <ol>
     * <li>cur.end=3,next.start=1 ==> end >= start ==> 重合[0,3]
     * <li>cur.end=4,next.start=2 ==> end >= start ==> 重合
     * <li>cur.end=5,next.start=8 ==> end >= start ==> 不重合区间start=8 合并之前重合区间[0,5]
     * <li>没有更多的next，合并之前不重合start=8, 最后的end=9。结果为[0,5],[8,9]
     * </ol>
     * 
     */
    @Override
    public int[][] merge(int[][] intervals) {
      if (intervals == null || intervals.length < 2)
        return intervals;
      final int n = intervals.length;
      // 分别排序starts, ends
      final int[] starts = new int[n], ends = new int[n];
      for (int i = 0; i < n; i++) {
        starts[i] = intervals[i][0];
        ends[i] = intervals[i][1];
      }
      Arrays.sort(starts);
      Arrays.sort(ends);
      // 合并区间
      List<int[]> mergedIntervals = new ArrayList<>();
      // 表示重合区间的start
      int j = 0;
      for (int i = 1; i < n; i++) {
        int curEnd = ends[i-1], nextStart = starts[i];
        // 如果发现下一个区间与之前的不重合
        if (curEnd < nextStart) {
          // 合并之前重合的区间
          int[] mergedInterval = {starts[j], curEnd};
          mergedIntervals.add(mergedInterval);
          // 将j置为下一个不重合区间的start
          j = i;
        }
      }
      // 添加最后一个区间end与之前找到的重合区间下标j
      mergedIntervals.add(new int[]{starts[j], ends[n-1]});
      return mergedIntervals.toArray(new int[0][]);
    }
    
  }
}
