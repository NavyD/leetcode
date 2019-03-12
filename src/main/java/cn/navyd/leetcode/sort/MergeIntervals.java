package cn.navyd.leetcode.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

/**
Given a collection of intervals, merge all overlapping intervals.

Example 1:

Input: [[1,3],[2,6],[8,10],[15,18]]
Output: [[1,6],[8,10],[15,18]]
Explanation: Since intervals [1,3] and [2,6] overlaps, merge them into [1,6].
Example 2:

Input: [[1,4],[4,5]]
Output: [[1,5]]
Explanation: Intervals [1,4] and [4,5] are considered overlapping.
 * @author navyd
 *
 */
public class MergeIntervals {
  /**
   * Definition for an interval.
   * public class Interval {
   *     int start;
   *     int end;
   *     Interval() { start = 0; end = 0; }
   *     Interval(int s, int e) { start = s; end = e; }
   * }
   */
  public static class Interval {
    int start;
    int end;

    Interval() {
      start = 0;
      end = 0;
    }

    Interval(int s, int e) {
      start = s;
      end = e;
    }

    @Override
    public String toString() {
      return "Interval [start=" + start + ", end=" + end + "]";
    }
    
  }
  
  Comparator<Interval> ascending = (o1, o2) -> o1.start < o2.start ? -1 : (o1.start > o2.start ? 1 : 0);
  
  /**
   * 思路：排序按照interval.start，对于当前的interval cur与后面每个interval that，
   * 如果相交（仅需要判断cur.end>=that.end），
   * 合并cur、that为新的cur，获取下一个that。
   * 时间复杂度：O(NlogN) 排序
   * 空间复杂度：O(N)
   * @param intervals
   * @return
   */
  public List<Interval> merge(List<Interval> intervals) {
    // 排序
    intervals.sort(ascending);
    ListIterator<Interval> it = intervals.listIterator();
    Interval cur = null;
    // 如果相交，则将两个interval并合并一个。然后使用当前合并的interval比较下一个是否能合并
    while (it.hasNext()) {
      if (cur == null)
        cur = it.next();
      Interval that;
      while (it.hasNext()) {
        that = it.next();
        if (isCross(cur, that)) {
          it.remove();
          mergeWith(cur, that);
        } else {
          cur = that;
          break;
        }
      }
    }
    return intervals;
  }
  
  static boolean isCross(Interval cur, Interval other) {
    return cur.start <= other.start && cur.end >= other.start;
  }
  
  static Interval mergeWith(Interval cur, Interval that) {
    if (cur.end < that.end)
      cur.end = that.end;
    return cur;
  }
  
  static class Solution {
    /**
     * 思路：类似
     * @param intervals
     * @return
     */
    public List<Interval> mergeBySort(List<Interval> intervals) {
      // 排序
      intervals.sort((i1, i2) -> i1.start - i2.start);
      List<Interval> merged = new LinkedList<>();
      // 获取最近的interval
      Interval last = null;
      for (Interval val : intervals) {
        // 如果last与下一个val不相交 则添加
        if (merged.isEmpty() || last.end < val.start)
          merged.add((last = val));
        // 相交 合并end
        else {
          // 修改引用
          last.end = Math.max(last.end, val.end);
        }
      }
      return merged;
    }
  }
  
  public static void main(String[] args) {
    int[][] 
//        a = {{1,3},{2,6},{8,10},{15,18}};
//        a = {{1,4},{4,5}};
        a = {{2,3},{2,2},{3,3},{1,3},{5,7},{2,2},{4,6}};
    List<Interval> intervals = new ArrayList<>();
    for (int[] val : a)
      intervals.add(new Interval(val[0], val[1]));
    System.err.println(intervals);
    MergeIntervals o = new MergeIntervals();
    System.err.println(o.merge(intervals));
  }
}
