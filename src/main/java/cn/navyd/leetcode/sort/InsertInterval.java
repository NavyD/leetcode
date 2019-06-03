package cn.navyd.leetcode.sort;

import java.util.LinkedList;
import java.util.List;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
Given a set of non-overlapping intervals, insert a new interval into the intervals (merge if necessary).

You may assume that the intervals were initially sorted according to their start times.

Example 1:

Input: intervals = [[1,3],[6,9]], newInterval = [2,5]
Output: [[1,5],[6,9]]
Example 2:

Input: intervals = [[1,2],[3,5],[6,7],[8,10],[12,16]], newInterval = [4,8]
Output: [[1,2],[3,10],[12,16]]
Explanation: Because the new interval [4,8] overlaps with [3,5],[6,7],[8,10].
NOTE: input types have been changed on April 15, 2019. Please reset to default code definition to get new method signature.
 * </pre>
 * @author navyd
 *
 */
@Problem(number = 57, difficulty = Difficulty.HARD, tags = Tag.SORT, url = "https://leetcode.com/problems/insert-interval/")
public interface InsertInterval {
  public int[][] insert(int[][] intervals, int[] newInterval);
  
  @Author(name = "shpolsky", significant = true, referenceUrls = "https://leetcode.com/problems/insert-interval/discuss/21602/Short-and-straight-forward-Java-solution")
  @Submission(date = "2019-06-03", memory = 39.9, memoryBeatRate = 63.84, runtime = 1, runtimeBeatRate = 99.90, url = "https://leetcode.com/submissions/detail/233328525/")
  @Solution(spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_N)
  public static class SolutionByStraightForward implements InsertInterval {

    /**
     * 思路：找到intervals的插入点newInterval.start > intervals.end，即newInterval.start <= intervals.end，
     * 合并时newInterval.end >= intervals.start中的所有intervals。
     * <p>合并时注意需要使用新的合并空间比较，保证之前的合并能够在下一次合并中提供新的空间信息
     * <p>这两个条件保证了中间所有的区间与newInterval可以直接合并到一起
     * 由于可能导致以后的区间重合，需要不断的合并
     */
    @Override
    public int[][] insert(int[][] intervals, int[] newInterval) {
      // 使用链表减少内存比ArrayList
      List<int[]> answer = new LinkedList<>();
      // 找到合并点
      int i = 0;
      // 如果intervals.end < newInterval.start则继续找，否则表示找到合并点
      while (i < intervals.length && intervals[i][1] < newInterval[0])
        answer.add(intervals[i++]);
      // 合并 使用newInterval保存之前合并的空间，并参与下次合并
      while (i < intervals.length && intervals[i][0] <= newInterval[1]) {
        newInterval[0] = Integer.min(intervals[i][0], newInterval[0]);
        newInterval[1] = Integer.max(intervals[i][1], newInterval[1]);
        i++;
      }
      answer.add(newInterval);
      // 添加剩余的interval
      while (i < intervals.length)
        answer.add(intervals[i++]);
      return answer.toArray(new int[][] {});
    }
    
    
    
  }
}
