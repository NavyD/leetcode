package cn.navyd.leetcode.sort;

import java.util.Arrays;

import cn.navyd.annotation.algorithm.ComplexityEnum;
import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.DifficultyEnum;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Submission;

/**
 * Given an array A of positive lengths, return the largest perimeter of a triangle with non-zero
 * area, formed from 3 of these lengths.
 * <p>
 * If it is impossible to form any triangle of non-zero area, return 0.
 * <p>
 * <p>
 * <p>
 * Example 1:
 * <p>
 * Input: [2,1,2] Output: 5 Example 2:
 * <p>
 * Input: [1,2,1] Output: 0 Example 3:
 * <p>
 * Input: [3,2,3,4] Output: 10 Example 4:
 * <p>
 * Input: [3,6,2,3] Output: 8
 * <p>
 * <p>
 * Note:
 * <p>
 * 3 <= A.length <= 10000 1 <= A[i] <= 10^6
 *
 * @author navyd
 */
@Problem(difficulty = DifficultyEnum.EASY, number = 976)
public interface LargestPerimeterTriangle {
  /**
   * 思路:
   * <p>
   * 三角形边长条件: 如果a<=b<=c,则a+b>c 才能构成三角形。那么对于确定大小关系的三个数，只需要1个条件就能满足：a+b>c。
   * 否则可能需要测试三个条件a+b a+c b+c。
   * <p>要求最大周长，找满足三角形最大的三个边长即可
   * <p>如何找最大值
   * <ul>
   * <li>完整排序
   * <li>排序中，满足条件后不需要剩下的值。可用的算法：bubble, selection
   * </ul>
   * <p>为何heap sort不能满足：仅sink操作构造的堆：结点>=两个子结点，无法保证最大的三个边相加。如果使用sink排序那就是完整的排序了
   */
  public int largestPerimeter(int[] A);

  @Submission(memory = 39.3, memoryBeatRate = 100, runtime = 9, runtimeBeatRate = 99.44, submittedDate = @DateTime("20191127"), url = "https://leetcode.com/submissions/detail/281940029/")
  @Author(value = "awice", references = "https://leetcode.com/problems/largest-perimeter-triangle/solution/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_N), spaceComplexity = ComplexityEnum.O_1)
  public static class SolutionBySort implements LargestPerimeterTriangle {

    /**
     * 思路：完整排序找出大小关系
     */
    @Override
    public int largestPerimeter(int[] A) {
      // 0. sort
      Arrays.sort(A);
      // 1. find max perimeter with reverse traverse
      for (int i = A.length - 1; i >= 2; i--)
      // 2. max < others sum
      {
        if (A[i] < A[i - 1] + A[i - 2]) {
          return A[i] + A[i - 1] + A[i - 2];
        }
      }
      return 0;
    }

  }

  @Author(value = "老虎", references = "https://leetcode-cn.com/problems/largest-perimeter-triangle/solution/mou-pao-jie-da-by-lao-hu-8/")
  @Submission(memory = 39.7, memoryBeatRate = 100, runtime = 6, runtimeBeatRate = 99.63, submittedDate = @DateTime("20191128"), url = "https://leetcode.com/submissions/detail/282168560/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(best = ComplexityEnum.O_1, worst = ComplexityEnum.O_N_POW_2), spaceComplexity = ComplexityEnum.O_1, inplace = true, stability = true)
  public static class SolutionByBubbleSort implements LargestPerimeterTriangle {

    /**
     * 思路：在bubble sort过程中找出满足最大周长条件的3个最大值
     * <pre>
     * Process:
     *
     * while count++ < A.length:
     *  while i++ < A.length:
     *    if A[i] < A[i-1] then swap i, j
     *  if count > 2 and A[cur_max] < A[cur_max-1] + A[cur_max-2] then
     *    find Largest Perimeter Triangle
     * </pre>
     * <p>复杂度
     * <ul>
     * <li>时间：冒泡排序为O(N^2)，存在的最好的情况是3次for后刚好max perimeter满足条件即O(1)，最差是所有的都不满足为O(N^2)
     * <li>空间：O(1)
     * </ul>
     */
    @Override
    public int largestPerimeter(int[] A) {
      // 0. 遍历n次
      int count = A.length;
      while (count-- > 0) {
        // 1. 比较相邻两个值大小，大的移动后面
        for (int i = 1; i < A.length; i++) {
          if (A[i] < A[i - 1]) {
            swap(A, i, i - 1);
          }
        }
        // 2. 存在3个最大值后则开始取最大周长
        if (A.length - count > 2 && A[count] + A[count + 1] > A[count + 2]) {
          return A[count] + A[count + 1] + A[count + 2];
        }
      }
      return 0;
    }

    private static void swap(int[] a, int i, int j) {
      int temp = a[i];
      a[i] = a[j];
      a[j] = temp;
    }
  }

  @Author("navyd")
  @Submission(memory = 39.1, memoryBeatRate = 100, runtime = 4, runtimeBeatRate = 99.72, submittedDate = @DateTime("20191128"), url = "https://leetcode.com/submissions/detail/282209580/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(best = ComplexityEnum.O_1, worst = ComplexityEnum.O_N_POW_2), spaceComplexity = ComplexityEnum.O_1, inplace = true, stability = true)
  public static class SolutionBySelectionSort implements LargestPerimeterTriangle {
    /**
     * 思路：selection sort过程中找最大的三个边长。
     * <p>复杂度
     * <p>时间：情况与{@linkplain LargestPerimeterTriangle.SolutionByBubbleSort#largestPerimeter(int[]) SolutionByBubbleSort}一致
     * <p>空间：O(1)
     * <p>可选优化：去除边界条件{@code A.length - lastIdx >= 3}，先找3个最大的到A最后
     */
    @Override
    public int largestPerimeter(int[] A) {
      // 0. traversal from last
      int lastIdx = A.length;
      while (lastIdx-- > 0) {
        int maxIdx = 0;
        // 1. find max index for each traversal
        for (int i = 1; i <= lastIdx; i++) {
          if (A[maxIdx] < A[i]) {
            maxIdx = i;
          }
        }
        // 2. swap max index and cur index
        swap(A, lastIdx, maxIdx);
        // 3. check Perimeter condition, if find frequency>=3
        if (A.length - lastIdx >= 3 && A[lastIdx] + A[lastIdx + 1] > A[lastIdx + 2]) {
          return A[lastIdx] + A[lastIdx + 1] + A[lastIdx + 2];
        }
      }
      return 0;
    }

    private static void swap(int[] a, int i, int j) {
      int temp = a[i];
      a[i] = a[j];
      a[j] = temp;
    }
  }
}
