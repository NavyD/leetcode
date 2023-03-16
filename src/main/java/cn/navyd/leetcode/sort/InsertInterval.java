package cn.navyd.leetcode.sort;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.DifficultyEnum;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
 * Given a set of non-overlapping intervals, insert a new interval into the intervals (merge if necessary).
 *
 * You may assume that the intervals were initially sorted according to their start times.
 *
 * Example 1:
 *
 * Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
 * Output: [[1,5],[6,9]]
 * Example 2:
 *
 * Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
 * Output: [[1,2],[3,10],[12,16]]
 * Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].
 * NOTE: input types have been changed on April 15, 2019. Please reset to default code definition to get new method signature.
 * </pre>
 *
 * @author navyd
 */
@Problem(number = 57, difficulty = DifficultyEnum.HARD, url = "https://leetcode.com/problems/insert-interval/")
public interface InsertInterval {
  public int[][] insert(int[][] intervals, int[] newInterval);

  @Author(value = "shpolsky", references = "https://leetcode.com/problems/insert-interval/discuss/21602/Short-and-straight-forward-Java-solution")
  @Submission(submittedDate = @DateTime("20190603"), memory = 39.9, memoryBeatRate = 63.84, runtime = 1, runtimeBeatRate = 99.90, url = "https://leetcode.com/submissions/detail/233328525/")
  public static class SolutionByStraightForward implements InsertInterval {

    /**
     * 思路：找到intervals的插入点newInterval.start > intervals.end，即newInterval.start <=
     * intervals.end， 合并时newInterval.end >= intervals.start中的所有intervals。
     * <p>
     * 合并时注意需要使用新的合并空间比较，保证之前的合并能够在下一次合并中提供新的空间信息
     * <p>
     * 这两个条件保证了中间所有的区间与newInterval可以直接合并到一起 由于可能导致以后的区间重合，需要不断的合并
     */
    @Override
    public int[][] insert(int[][] intervals, int[] newInterval) {
      // 使用链表减少内存比ArrayList
      List<int[]> answer = new LinkedList<>();
      // 找到合并点
      int i = 0;
      // 如果intervals.end < newInterval.start则继续找，否则表示找到合并点
      while (i < intervals.length && intervals[i][1] < newInterval[0]) {
        answer.add(intervals[i++]);
      }
      // 合并 使用newInterval保存之前合并的空间，并参与下次合并
      while (i < intervals.length && intervals[i][0] <= newInterval[1]) {
        newInterval[0] = Integer.min(intervals[i][0], newInterval[0]);
        newInterval[1] = Integer.max(intervals[i][1], newInterval[1]);
        i++;
      }
      answer.add(newInterval);
      // 添加剩余的interval
      while (i < intervals.length) {
        answer.add(intervals[i++]);
      }
      return answer.toArray(new int[][] {});
    }
  }

  @Author(value = "navyd")
  @Submission(memory = 41.6, memoryBeatRate = 71.88, runtime = 1, runtimeBeatRate = 98.22, submittedDate = @DateTime("20200428"), url = "https://leetcode.com/submissions/detail/331608805/")
  public static class SolutionByPhase implements InsertInterval {
    /**
     * 思路：由于区间是有序的，在找到合并区间前后可直接add elements。将区间分为
     * 3段：不可合并前，可合并，不可合并后。
     * <p>满足可合并区间条件i.end >= newI.start, i.start <= newI.end，
     * 在有序区间中只要i.end >= newI.start开始，直到i.start <= newI.end中间的
     * 都是被包在merged区间中的:merged.start = min(firstI.start, newI.start),
     * merged.end = max(lastI.end, newI.end)
     * <p>区间有3种关系：不相交，相交，包含
     * 需要注意边界条件：
     * <p>没有任何区间可合并时：两种情况，一种是(1,5),(7,8)时到最后i==n时直接插newI返回。
     * 另一种在分开判断可合并条件时可能有(1,5),(0,0)这样的尝试合并
     * 此时应该直接插入newI，因为结果要有序且i.start <= newI.end不满足，newI在前面
     * <p>区间可合并时要考虑：newI在两端导致merged.start=newI.start,merged.end=newI.end，
     * 中间时为merged.start=firstI.start, end=last.end
     * <p>时间复杂度：仅遍历intervals为O(N)
     * <p>空间复杂度：结果list为O(N)
     */
    @Override
    public int[][] insert(int[][] intervals, int[] newInterval) {
      final int n = intervals.length;
      List<int[]> res = new ArrayList<>();
      // 0. add elements before you can merge
      int i = 0;
      while (i < n && intervals[i][1] < newInterval[0]) {
        res.add(intervals[i++]);
      }
      // no more elements
      if (i >= n) {
        res.add(newInterval);
        return res.toArray(new int[res.size()][]);
      }
      // 1. now it's not certain that we can merged, so try to merge
      int[] merged = intervals[i];
      while (i < n && intervals[i][0] <= newInterval[1]) {
        merged[0] = Math.min(merged[0], newInterval[0]);
        merged[1] = Math.max(intervals[i][1], newInterval[1]);
        i++;
      }
      // 2. insert new interval if there is no elements to merge, else merged
      res.add((i < n && merged == intervals[i]) ? newInterval : merged);
      // 3. add elements that can't be merged later
      while (i < n) {
        res.add(intervals[i++]);
      }
      return res.toArray(new int[res.size()][]);
    }
  }

  public static class SolutionByUnfinished implements InsertInterval {
    // 有序的 不重叠的intervals 只要新interval找出在哪两个区间中就可以合并

    /**
     * 在一个for考虑情况过多，爆炸了做不出
     */
    public int[][] insert(int[][] intervals, int[] newInterval) {
      int[] merged = null;
      int count = intervals.length;
      for (int i = 0; i < intervals.length; i++) {
        int[] interval = intervals[i];
        // new.start在区间中 相交
        if (interval[1] >= newInterval[0]) {
          if (merged == null) {
            merged = interval;
            merged[0] = Math.min(merged[0], newInterval[0]);
          }
          count--;
          intervals[i] = null;
        }
        // new.end 在区间中 检查new.start是否在
        if (interval[1] >= newInterval[1]) {
          // mergeable
          if (interval[0] <= newInterval[1]) {
            merged[1] = interval[1];
            intervals[i] = null;
          }
          break;
        }

      }

      int[][] res = new int[count][2];
      int j = 0;
      boolean hasMerged = false;
      for (int[] i : intervals) {
        if (i != null) {
          res[j++] = i;
        } else if (!hasMerged) {
          res[j++] = merged;
          hasMerged = true;
        }
      }
      return res;
      // 0. find the first if interval.end > new.start

      // 1. find the end interval if next interval.end > new.end
      // 2. merge intervals
    }
  }
}
