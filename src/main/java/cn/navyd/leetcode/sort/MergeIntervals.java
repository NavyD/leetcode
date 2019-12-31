package cn.navyd.leetcode.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
@Problem(number = 56, difficulty = DifficultyEnum.MEDIUM)
public interface MergeIntervals {
  /**
   * 合并重叠的区间
   * @param intervals
   * @return
   */
  public int[][] merge(int[][] intervals);
  
  @Author("navyd")
  @Submission(memory = 38.1, memoryBeatRate = 98.55, runtime = 6, runtimeBeatRate = 89.20, submittedDate = @DateTime("20191230"), url = "https://leetcode.com/submissions/detail/289604195/")
  @Author(value = "brubru777", references = "https://leetcode.com/problems/merge-intervals/discuss/21222/A-simple-Java-solution")
  @Submission(submittedDate = @DateTime("20190516"), runtime = 36, runtimeBeatRate = 41.45, memory = 36, memoryBeatRate = 99.99, url = "https://leetcode.com/submissions/detail/229207368/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_N), spaceComplexity = ComplexityEnum.O_N)
  public static class SolutionBySort implements MergeIntervals {

    /**
     * 思路：合并两个重复的区间，如果intervals是根据interval.start有序的，合并区间时，只需要判断当前区间cur.end与下一个区间的next.start的关系即可。
     * 利用sort start的有序性，避免区间合并前后对不相交的情况
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
      if (intervals.length < 2)
        return intervals;
      // 0. sort
      Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
      int count = intervals.length;
      // 1. iterative
      int[] curInterval = intervals[0];
      for (int i = 1; i < intervals.length; i++) {
        int[] that = intervals[i];
        // 2. merge if is overlap
        if (curInterval[1] >= that[0]) {
          curInterval[1] = Math.max(curInterval[1], that[1]);
          intervals[i] = null;
          count--;
        }
        // 3. next is not overlap
        else
          curInterval = that;
      }
      // 4. get result
      int[][] res = new int[count][2];
      for (int[] interval : intervals)
        if (interval != null)
          res[--count] = interval;
      return res;
    }

    // 初版
    public int[][] merge1(int[][] intervals) {
      // 0. sort
      Arrays.sort(intervals, (a, b) -> a[0] - b[0]);
      // 1. iterative
      int count = intervals.length;
      for (int i = 0; i < intervals.length; i++) {
        int[] interval = intervals[i];
        if (interval == null)
          continue;
        for (int j = i + 1; j < intervals.length; j++) {
          int[] that = intervals[j];
          if (interval[1] >= that[0]) {
            // merge
            interval[1] = Math.max(interval[1], that[1]);
            // delete
            intervals[j] = null;
            count--;
          }
        }
      }
      // 2. merge if is overlap
      int[][] res = new int[count][2];
      for (int[] interval : intervals)
        if (interval != null)
          res[--count] = interval;
      return res;
    }
  }
  
  @Author(value = "vegito2002", references = "https://leetcode.com/problems/merge-intervals/discuss/21223/Beat-98-Java.-Sort-start-and-end-respectively./115420")
  @Author(value = "baselRus", references = "https://leetcode.com/problems/merge-intervals/discuss/21223/Beat-98-Java.-Sort-start-and-end-respectively./21227")
  @Author(value = "D_shaw", references = "https://leetcode.com/problems/merge-intervals/discuss/21223/Beat-98-Java.-Sort-start-and-end-respectively.")
  @Submission(submittedDate = @DateTime("20190517"), runtime = 2, runtimeBeatRate = 99.96, memory = 35.9, memoryBeatRate = 99.99, url = "https://leetcode.com/submissions/detail/229427496/")
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

  @Author(value = "0", references = "https://leetcode.com/submissions/api/detail/56/java/1/")
  @Author("navyd")
  @Author(value = "Eric.zhou", references = "https://leetcode-cn.com/problems/merge-intervals/solution/ji-bai-9969de-ni-men-shi-jian-fu-za-du-zui-pi-o2n-/")
  @Submission(memory = 40.8, memoryBeatRate = 65.94, runtime = 1, runtimeBeatRate = 100, submittedDate = @DateTime("20191231"), url = "https://leetcode.com/submissions/detail/289943744/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(best = ComplexityEnum.O_N, worst = ComplexityEnum.O_N_POW_2), spaceComplexity = ComplexityEnum.O_N)
  public static class SolutionByIterativeMergeToEnd implements MergeIntervals {
    /**
     * 思路：遍历合并区间到最后。
     * <p>问题
     * <p>对一个区间可以正确合并，但是对于后面未合并interval时，此时不是overlap，合并后则是overlap，
     * 但是对该interval已经遍历过。cur=[0,0]与[1,6]不相交，但是考虑到[0,0],[0,2]=>[0,2]，这种遍历方式
     * 导致[0,0]被跳过。
     * <pre>
     * intervals: [[1,6],[0,0],null,[0,2],null,null,null,null,null,null,null]
     * count: 3
     * i: 1
     * cur: [0,0]
     * </pre>
     * <p>解决方式：合并interval到next位置
     * <p>优化
     * <ul>
     * <li>对合并两个区间到后区间位置后再回到外合并区间，而不是对某个区间合并到底，减少了不必要的null循环判断
     * <li>内循环从i+1开始，由于每个可合并的已经在最后，i前的已经扫描过，所以不会出现跳过的问题。
     * </ul>
     * <p>时间复杂度：当intervals没有可合并interval时，duple iterative时间为O(N^2)。当所有intervals可合并一个interval时，
     * 只有external loop即O(N)
     * <p>空间复杂度：最差intervals没有可合并interval时复制result数组为O(N)
     */
    @Override
    public int[][] merge(int[][] intervals) {
      if (intervals.length < 2)
        return intervals;
      int count = intervals.length;
      // 0. duple iterative
      for (int i = 0; i < intervals.length; i++) {
        int[] cur = intervals[i];
        // break if once merged
        for (int nextIdx = i + 1; nextIdx < intervals.length; nextIdx++) {
          int[] next = intervals[nextIdx];
          if (next == null || isDisjoint(cur, next))
            continue;
          // 1. merge if is overlap
          next[0] = Math.min(cur[0], next[0]);
          next[1] = Math.max(cur[1], next[1]);
          // 2. remove merged interval for cur
          intervals[i] = null;
          count--;
          // reduce duplicate next interval iterative
          break;
        }
      }
      // 3. get result
      int[][] res = new int[count][];
      for (int[] interval : intervals)
        if (interval != null)
          res[--count] = interval;
      return res;
    }

    static boolean isDisjoint(int[] cur, int[] next) {
      return cur[1] < next[0] || next[1] < cur[0];
    }
  }

  @Author("navyd")
  @Author(value = "Eric.zhou", references = "https://leetcode-cn.com/problems/merge-intervals/solution/ji-bai-9969de-ni-men-shi-jian-fu-za-du-zui-pi-o2n-/")
  @Submission(memory = 40.6, memoryBeatRate = 66.67, runtime = 2, runtimeBeatRate = 99.14, submittedDate = @DateTime("20191231"), url = "https://leetcode.com/submissions/detail/289931007/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(best = ComplexityEnum.O_N, worst = ComplexityEnum.O_N_POW_2), spaceComplexity = ComplexityEnum.O_N)
  public static class SolutionByIterativeMergeToEndOriginal implements MergeIntervals {
    @Override
    public int[][] merge(int[][] intervals) {
      if (intervals.length < 2)
        return intervals;
      int count = intervals.length;
      // 0. duplex iterative
      for (int i = 0; i < intervals.length; i++) {
        int[] cur = intervals[i];
        if (cur == null)
          continue;
        int curIdx = i;
        for (int nextIdx = i + 1; nextIdx < intervals.length; nextIdx++) {
          int[] next = intervals[nextIdx];
          if (next == null)
            continue;
          // 1. merge if is overlap
          else if (isOverlap(cur, next)) {
            next[0] = Math.min(cur[0], next[0]);
            next[1] = Math.max(cur[1], next[1]);
            // 2. remove merged interval for cur
            intervals[curIdx] = null;
            count--;
            curIdx = nextIdx;
            cur = next;
          }
        }
      }
      // 3. get result
      int[][] res = new int[count][2];
      for (int[] interval : intervals)
        if (interval != null)
          res[--count] = interval;
      return res;
    }

    static boolean isOverlap(int[] cur, int[] next) {
      // cur include or intersect next
      return (cur[0] <= next[0] && (cur[1] >= next[1] || cur[1] >= next[0]))
          // next include or intersect cur
          || (cur[0] > next[0] && (next[1] >= cur[1] || next[1] >= cur[0]));
    }
}
}
