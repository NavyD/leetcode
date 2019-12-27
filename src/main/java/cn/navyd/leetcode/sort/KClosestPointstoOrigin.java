package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

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
We have a list of points on the plane.  Find the K closest points to the origin (0, 0).

(Here, the distance between two points on a plane is the Euclidean distance.)

You may return the answer in any order.  The answer is guaranteed to be unique (except for the order that it is in.)

 

Example 1:

Input: points = [[1,3],[-2,2]], K = 1
Output: [[-2,2]]
Explanation: 
The distance between (1, 3) and the origin is sqrt(10).
The distance between (-2, 2) and the origin is sqrt(8).
Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
We only want the closest K = 1 points from the origin, so the answer is just [[-2,2]].
Example 2:

Input: points = [[3,3],[5,-1],[-2,4]], K = 2
Output: [[3,3],[-2,4]]
(The answer [[-2,4],[3,3]] would also be accepted.)
 

Note:

1 <= K <= points.length <= 10000
-10000 < points[i][0] < 10000
-10000 < points[i][1] < 10000
 * </pre>
 * @author navyd
 *
 */
@Problem(difficulty = DifficultyEnum.MEDIUM, number = 973)
public interface KClosestPointstoOrigin {

  /**
   * 找k个最小的point
   * <p>思路
   * <ul>
   * <li>full sort
   * <li>half sort for bubble, selection
   * <li>priority queue
   * <li>quick select
   * </ul>
   * @param points
   * @param K
   * @return
   */
  public int[][] kClosest(int[][] points, int K);

  @Author(value = "Frimish", references = "https://leetcode.com/problems/k-closest-points-to-origin/discuss/220235/Java-Three-solutions-to-this-classical-K-th-problem.")
  @Author("navyd")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_N), spaceComplexity = ComplexityEnum.O_N, stability = true)
  @Submission(memory = 59.1, memoryBeatRate = 76.40, runtime = 24, runtimeBeatRate = 62.29, submittedDate = @DateTime("20191210"), url = "https://leetcode.com/submissions/detail/284941961/")
  public static class SolutionBySort implements KClosestPointstoOrigin {
    /**
     * 思路：对points完整排序，找K个元素
     */
    @Override
    public int[][] kClosest(int[][] points, int K) {
      // 0. create comparator
      final Comparator<int[]> cmp = (a, b) -> (a[0]*a[0]+a[1]*a[1]) - (b[0]*b[0]+b[1]*b[1]);
      // 1. sort
      Arrays.sort(points, cmp);
      // 2. select k
      return Arrays.copyOf(points, K);
    }
  }

  @Author("navyd")
  @SortAlgorithm(timeComplexity = @TimeComplexity(best = ComplexityEnum.O_N_K, worst = ComplexityEnum.O_N_POW_2), spaceComplexity = ComplexityEnum.O_K, inplace = true)
  @Submission(memory = 60.5, memoryBeatRate = 60.87, runtime = 657, runtimeBeatRate = 5.09, submittedDate = @DateTime("20191210"), url = "https://leetcode.com/submissions/detail/284946428/")
  public static class SolutionByHalfSort implements KClosestPointstoOrigin {
    /**
     * 思路：对points使用half selection，当k个元素有序时就可以不用排序
     * <p>时间复杂度：最差是K=N即O(N*N)
     * <p>空间复杂度：归并排序使用空间O(N)
     * 
     */
    @Override
    public int[][] kClosest(int[][] points, int K) {
      // 0. create comparator
      final Comparator<int[]> cmp = (a, b) -> (a[0]*a[0]+a[1]*a[1]) - (b[0]*b[0]+b[1]*b[1]);
      // 1. selection for k
      for (int i = 0; i < points.length; i++) {
        int minIdx = i;
        for (int j = minIdx+1; j < points.length; j++) {
          if (cmp.compare(points[minIdx], points[j]) > 0)
            minIdx = j;
        }
        swap(points, i, minIdx);
        // 2. at least k has sorted
        if (i >= K-1)
          break;
      }
      return Arrays.copyOf(points, K);
    }

    static void swap(Object[] a, int i, int j) {
      Object temp = a[i];
      a[i] = a[j];
      a[j] = temp;
    }
  }

  @Author(value = "Frimish", references = "https://leetcode.com/problems/k-closest-points-to-origin/discuss/220235/Java-Three-solutions-to-this-classical-K-th-problem.")
  @Submission(memory = 62.6, memoryBeatRate = 40.37, runtime = 23, runtimeBeatRate = 65.35, submittedDate = @DateTime("20191211"), url = "https://leetcode.com/submissions/detail/285124273/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_K), spaceComplexity = ComplexityEnum.O_K)
  public static class SolutionByPriorityQueue implements KClosestPointstoOrigin {
    /**
     * 思路：使用max priority queue保存K个最小的元素。
     * 为何需要max priority在删除queue元素时只有poll head操作，min priority poll就删了最小的元素，不可行。
     * 如果是max priority poll时删的是第K+1个最大的元素，保存了K个最小元素
     * <p>时间复杂度：points对queue.offer为O(N log K)，遍历queue是O(K)
     * <p>空间复杂度：queue是O(K)
     */
    @Override
    public int[][] kClosest(int[][] points, int K) {
      // 0. create max priority queue
      final Comparator<int[]> cmp = (a, b) -> (b[0]*b[0]+b[1]*b[1]) - (a[0]*a[0]+a[1]*a[1]);
      final Queue<int[]> queue = new PriorityQueue<>(K, cmp);
      // 1. offer k elements for all points
      for (int[] p : points) {
        queue.offer(p);
        // 2. poll k+1 elelment
        if (queue.size() > K)
          queue.poll();
      }
      int[][] res = new int[K][2];
      int i = 0;
      // 使用poll会增加多余的操作时间
      for (int[] p : queue)
        res[i++] = p;
      return res;
    }
  }

  @Author(value = "Frimish", references = "https://leetcode.com/problems/k-closest-points-to-origin/discuss/220235/Java-Three-solutions-to-this-classical-K-th-problem.")
  @Author("navyd")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N, best = ComplexityEnum.O_N, worst = ComplexityEnum.O_N_K), spaceComplexity = ComplexityEnum.O_K)
  @Submission(memory = 57.8, memoryBeatRate = 85.09, runtime = 4, runtimeBeatRate = 99.68, submittedDate = @DateTime("20191223"), url = "https://leetcode.com/submissions/detail/287854522/")
  public static class SolutionByQuickSelect implements KClosestPointstoOrigin {
    
    /**
     * 思路：quick select切分出第K个元素。不需要排序
     * <p>问题：
     * <ul>
     * <li>存在的相等元素时，如何解决找K个：什么时候有lo>hi.
     * <p>不需要这个条件，由于return mid切分的是在lo,hi中，
     * +1-1逼进[0]
     * <li>partition的位置与K的关系{@code >K <K =K}怎么解决：
     * <p> =k时刚好有K个元素满足。
     * <p> {@code >k}时说明mid左边元素更多，取小元素[lo,mid-1]partition。
     * <p> {@code <k}时mid左边元素少，取更大的元素[mid+1,hi]partition
     * <li>怎样在partition中使用非lo的pivot，如何处理pivot的swap,return。为何不能在partition中使用lo,hi指针
     * <p>partition中直接使用random的pivot值会可能导致pivot被swap丢失pivot的位置，return hi的下标不再表示 <= hi >关系，
     * 仅能表示的hi位置长度是小于原来pivot信息丢失
     * <p>由于pivot>lo时导致while中pivot被swap,必须记录pivot，否则将会少一次swap丢失pivot信息
     * </ul>
     * <p>时间复杂度：最差是pivot=max|min，每次partition返回的是最min|max index，这样只能每次+-1逼进到K次，即O(K N)
     * <p>空间复杂度：返回结果给copy array即O(N)
     */
    @Override
    public int[][] kClosest(int[][] points, int K) {
      K -= 1;
      int lo = 0, hi = points.length-1;
      while (true) {
        // 0. get position of partition between lo, hi
        int mid = partition(points, lo, hi);
        // 1. position elements less than K
        if (mid < K)
          lo = mid + 1;
        // 2. elements more than K
        else if (mid > K)
          hi = mid - 1;
        // 3. K elements
        else 
          return Arrays.copyOf(points, K+1);
      }
    }

    static final Random RAND = new Random();

    static int partition(int[][] points, int lo, int hi) {
      // random pivot
      swap(points, lo, RAND.nextInt(hi-lo+1)+lo);
      final int 
        pivot = lo,
        pivotDist = distance(points, pivot);
      hi++;
      while (true) {
        while (distance(points, --hi) > pivotDist);
        while (lo < hi && distance(points, ++lo) < pivotDist);
        if (lo >= hi) break;
        swap(points, lo, hi);
      }
      swap(points, pivot, hi);
      return hi;
    }

    static void swap(Object[] a, int i, int j) {
      Object temp = a[i];
      a[i] = a[j];
      a[j] = temp;
    }

    static int compare(int[] a, int[] b) {
      return (a[0]*a[0]+a[1]*a[1]) - (b[0]*b[0]+b[1]*b[1]);
    }

    static int distance(int[][] points, int i) {
      return points[i][0] * points[i][0] + points[i][1] * points[i][1];
    }

  }

  @Author("navyd")
  @Submission(memory = 60.9, memoryBeatRate = 51.55, runtime = 5, runtimeBeatRate = 92.42, submittedDate = @DateTime("20191227"), url = "https://leetcode.com/submissions/detail/288875001/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N, best = ComplexityEnum.O_N, worst = ComplexityEnum.O_N_K), spaceComplexity = ComplexityEnum.O_K, stability = false, inplace = false)
  public static class SolutionBySimpleQuickSelect implements KClosestPointstoOrigin {
    /**
     * 一个简单的quick select版本。
     */
    @Override
    public int[][] kClosest(int[][] points, int K) {
      K -= 1;
      int lo = 0, hi = points.length - 1; 
      while (true) {
        // 0. partition
        int mid = partition(points, lo, hi);
        // 1. check partition position and K 
        if (mid < K)
          lo = mid + 1;
        else if (mid > K)
          hi = mid - 1;
        else  
          break;
      }
      // 2. return array of k
      return Arrays.copyOf(points, K+1);
    }

    static int partition(int[][] points, int lo, int hi) {
      int pivot = lo;
      while (lo <= hi) {
        if (compare(points[pivot], points[lo]) < 0)
          swap(points, lo, hi--);
        else lo++;
      }
      swap(points, pivot, hi);
      return hi;
    }

    static void swap(Object[] a, int i, int j) {
      Object temp = a[i];
      a[i] = a[j];
      a[j] = temp;
    }

    static int compare(int[] a, int[] b) {
      return (a[0]*a[0]+a[1]*a[1]) - (b[0]*b[0]+b[1]*b[1]);
    }

    static int distance(int[][] points, int i) {
      return points[i][0] * points[i][0] + points[i][1] * points[i][1];
    }
  }

  public static void main(String[] args) {
    KClosestPointstoOrigin p = new SolutionByQuickSelect();
    // int[][] a = {{3,3},{5,-1},{-2,4}};
    int count = 100;
    while (count-- > 0) {
      int[][] a = { { -2, 10 }, { -4, -8 }, { 10, 7 }, { -4, -7 } };
      // int[][] a = {{1,3},{-2,2}};
      System.err.println(Arrays.deepToString(p.kClosest(a, 3)));
    }
  }
}