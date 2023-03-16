package cn.navyd.leetcode.sort;

import java.util.PriorityQueue;
import java.util.Queue;

import cn.navyd.annotation.algorithm.ComplexityEnum;
import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
 * The power of an integer x is defined as the number of steps needed to transform x into 1 using the following steps:
 *
 * if x is even then x = x / 2
 * if x is odd then x = 3 * x + 1
 * For example, the power of x = 3 is 7 because 3 needs 7 steps to become 1 (3 --> 10 --> 5 --> 16 --> 8 --> 4 --> 2 --> 1).
 *
 * Given three integers lo, hi and k. The task is to sort all integers in the interval [lo, hi] by the power value in ascending order, if two or more integers have the same power value sort them by ascending order.
 *
 * Return the k-th integer in the range [lo, hi] sorted by the power value.
 *
 * Notice that for any integer x (lo <= x <= hi) it is guaranteed that x will transform into 1 using these steps and that the power of x is will fit in 32 bit signed integer.
 *
 *
 *
 * Example 1:
 *
 * Input: lo = 12, hi = 15, k = 2
 * Output: 13
 * Explanation: The power of 12 is 9 (12 --> 6 --> 3 --> 10 --> 5 --> 16 --> 8 --> 4 --> 2 --> 1)
 * The power of 13 is 9
 * The power of 14 is 17
 * The power of 15 is 17
 * The interval sorted by the power value [12,13,14,15]. For k = 2 answer is the second element which is 13.
 * Notice that 12 and 13 have the same power value and we sorted them in ascending order. Same for 14 and 15.
 * Example 2:
 *
 * Input: lo = 1, hi = 1, k = 1
 * Output: 1
 * Example 3:
 *
 * Input: lo = 7, hi = 11, k = 4
 * Output: 7
 * Explanation: The power array corresponding to the interval [7, 8, 9, 10, 11] is [16, 3, 19, 6, 14].
 * The interval sorted by power is [8, 10, 11, 7, 9].
 * The fourth number in the sorted array is 7.
 * Example 4:
 *
 * Input: lo = 10, hi = 20, k = 5
 * Output: 13
 * Example 5:
 *
 * Input: lo = 1, hi = 1000, k = 777
 * Output: 570
 *
 *
 * Constraints:
 *
 * 1 <= lo <= hi <= 1000
 * 1 <= k <= hi - lo + 1
 * </pre>
 */
public interface SortIntegersByThePowerValue {
  /**
   * 在lo,hi的index中找第k大的power value的index
   * <p>如何找kTh：quick select, min heap
   * <p>如何对应index与power value: 二维数组，indexes values两个数组
   *
   * @param lo
   * @param hi
   * @param k
   * @return
   */
  public int getKth(int lo, int hi, int k);

  @Author(value = "Thare", references = "https://leetcode-cn.com/problems/sort-integers-by-the-power-value/comments/299677")
  @Author("navyd")
  @Submission(memory = 39.3, memoryBeatRate = 100, runtime = 38, runtimeBeatRate = 90.84, submittedDate = @DateTime("20200405"), url = "https://leetcode.com/submissions/detail/319852093/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_N)
  public static class SolutionByQuickSelect implements SortIntegersByThePowerValue {
    /**
     * 如何使用index与powervalue作kTh：使用二维数组，避免用lo-hi作index用
     * <p>如何解决同值相等的问题：在select比较时考虑lo,hi的index下标关系{@code a[0] - b[0] == 0 ? a[1] - b[1] : a[0] - b[0])}
     * <p>如何找到第k个index(lo,hi)：quickselect
     */
    @Override
    public int getKth(int lo, int hi, int k) {
      if (lo == hi) {
        return lo;
      }
      // 0. cache powervalue and index
      final int[][] counts = new int[hi - lo + 1][2];
      for (int i = 0; i < counts.length; i++) {
        counts[i] = new int[] {powerValue(lo), lo++};
      }
      // 1. get Kth with find Nth select algorithm
      k--;
      lo = 0;
      hi = counts.length - 1;
      while (lo <= hi) {
        int p = select(counts, lo, hi);
        if (k < p) {
          hi = p - 1;
        } else if (k > p) {
          lo = p + 1;
        } else {
          return counts[p][1];
        }
      }
      return -1;
    }

    static int select(int[][] a, int lo, int hi) {
      final int pivot = lo;
      hi++;
      while (true) {
        while (compare(a[pivot], a[--hi]) < 0) {
          ;
        }
        while (lo < hi && compare(a[pivot], a[++lo]) > 0) {
          ;
        }
        if (lo >= hi) {
          break;
        }
        swap(a, lo, hi);
      }
      swap(a, pivot, hi);
      return hi;
    }

    static int compare(int[] a, int[] b) {
      int cmp = a[0] - b[0];
      return (cmp == 0 ? a[1] - b[1] : cmp);
    }

    static void swap(int[][] a, int i, int j) {
      int[] t = a[i];
      a[i] = a[j];
      a[j] = t;
    }

    static int powerValue(int x) {
      int count = 0;
      while (x > 1) {
        if ((x & 1) == 0) {
          x /= 2;
        } else {
          x = 3 * x + 1;
        }
        count++;
      }
      return count;
    }
  }

  @Author(value = "3", references = "https://leetcode.com/submissions/api/detail/1488/java/3/")
  @Submission(memory = 36.8, memoryBeatRate = 100, runtime = 3, runtimeBeatRate = 100, submittedDate = @DateTime("20200406"), url = "https://leetcode.com/submissions/detail/320415172/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_N)
  public static class SolutionByQuickSelectCache implements SortIntegersByThePowerValue {
    // cached powervalue
    private static final int[] POWER_VALUES = new int[1001];

    static {
      for (int i = 0; i < POWER_VALUES.length; i++) {
        POWER_VALUES[i] = powerValue(i);
      }
    }

    /**
     * 缓存的POWER_VALUES对多次算法有效
     */
    @Override
    public int getKth(int lo, int hi, int k) {
      if (lo == hi) {
        return lo;
      }
      // 0. create index cache
      final int[] indexes = new int[hi - lo + 1];
      for (int i = 0; i < indexes.length; i++) {
        indexes[i] = lo++;
      }
      lo = 0;
      hi = indexes.length - 1;
      // 1. select
      k--;
      while (lo <= hi) {
        int p = select(indexes, lo, hi);
        if (p < k) {
          lo = p + 1;
        } else if (p > k) {
          hi = p - 1;
        } else {
          return indexes[p];
        }
      }
      return -1;
    }

    static int powerValue(int x) {
      int count = 0;
      while (x > 1) {
        if ((x & 1) == 0) {
          x /= 2;
        } else {
          x = 3 * x + 1;
        }
        count++;
      }
      return count;
    }

    static int select(int[] a, int lo, int hi) {
      final int pivot = lo;
      hi++;
      while (true) {
        // indexes => POWER_VALUE
        while (compare(a[pivot], a[--hi]) < 0) {
          ;
        }
        while (lo < hi && compare(a[pivot], a[++lo]) > 0) {
          ;
        }
        if (lo >= hi) {
          break;
        }
        swap(a, lo, hi);
      }
      swap(a, pivot, hi);
      return hi;
    }

    static int compare(int i, int j) {
      int cmp = POWER_VALUES[i] - POWER_VALUES[j];
      return (cmp == 0 ? i - j : cmp);
    }

    static void swap(int[] a, int i, int j) {
      int t = a[i];
      a[i] = a[j];
      a[j] = t;
    }
  }

  @Author("navyd")
  @Submission(memory = 40.5, memoryBeatRate = 100, runtime = 34, runtimeBeatRate = 92.01, submittedDate = @DateTime("20200406"), url = "https://leetcode.com/submissions/detail/320432090/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_K), spaceComplexity = ComplexityEnum.O_K)
  public static class SolutionByHeap implements SortIntegersByThePowerValue {
    // cached powervalue
    private static final int[] POWER_VALUES = new int[1001];

    static {
      for (int i = 0; i < POWER_VALUES.length; i++) {
        POWER_VALUES[i] = powerValue(i);
      }
    }

    /**
     * 通过用max heap保存k个元素sort得到第k个
     */
    @Override
    public int getKth(int lo, int hi, int k) {
      if (lo == hi) {
        return lo;
      }
      // 0. create max heap, priority queue with Power value comparator
      final Queue<Integer> queue = new PriorityQueue<>((j, i) -> {
        int cmp = POWER_VALUES[i] - POWER_VALUES[j];
        return (cmp == 0 ? i - j : cmp);
      });
      // 1. push or poll in k range
      for (int i = lo; i <= hi; i++) {
        queue.offer(i);
        if (queue.size() > k) {
          queue.poll();
        }
      }
      return queue.peek();
    }

    static int powerValue(int x) {
      int count = 0;
      while (x > 1) {
        if ((x & 1) == 0) {
          x /= 2;
        } else {
          x = 3 * x + 1;
        }
        count++;
      }
      return count;
    }
  }

  public static void main(String[] args) {
    // int lo = 1, hi = 1000, k = 777;
    int lo = 10, hi = 20, k = 5;
    SortIntegersByThePowerValue p = new SolutionByQuickSelectCache();
    p.getKth(lo, hi, k);
  }
}