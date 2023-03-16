package cn.navyd.leetcode.sort;

import cn.navyd.annotation.algorithm.ComplexityEnum;
import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.DifficultyEnum;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Tag;
import cn.navyd.annotation.leetcode.TagEnum;

/**
 * <pre>
 * Given an array A of non-negative integers, half of the integers in A are odd, and half of the integers are even.
 *
 * Sort the array so that whenever A[i] is odd, i is odd; and whenever A[i] is even, i is even.
 *
 * You may return any answer array that satisfies this condition.
 *
 *
 *
 * Example 1:
 *
 * Input: [4,2,5,7]
 * Output: [4,5,2,7]
 * Explanation: [4,7,2,5], [2,5,4,7], [2,7,4,5] would also have been accepted.
 *
 *
 * Note:
 *
 * 2 <= A.length <= 20000
 * A.length % 2 == 0
 * 0 <= A[i] <= 1000
 * </pre>
 *
 * @author navyd
 */
@Tag(TagEnum.SORT)
@Problem(difficulty = DifficultyEnum.EASY, number = 922)
public interface SortArrayByParityII {

  /**
   * 对数组A排序。保证数组奇、偶下标分别 对应的奇、偶值
   * <p>数组A的长度是偶数，即数组奇偶元素数量一致
   *
   * @param A
   * @return
   */
  public int[] sortArrayByParityII(int[] A);

  @Author("navyd")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_N, inplace = false)
  @Submission(memory = 41.7, memoryBeatRate = 81.48, runtime = 2, runtimeBeatRate = 99.68, submittedDate = @DateTime("20190921"),
      url = "https://leetcode.com/submissions/detail/262767574/")
  public class SolutionByTwoPass implements SortArrayByParityII {
    /**
     * 思路：遍历数组，当元素为奇数时，保存新数组的奇数位置，反之亦然。
     *
     * @param A
     * @return
     */
    public int[] sortArrayByParityII(int[] A) {
      final int n;
      if (A == null || (n = A.length) <= 1) {
        return A;
      }
      final int[] res = new int[n];
      // 奇数起始下标， 偶数起始下标
      int oIdx = 1, eIdx = 0;
      for (int i = 0; i < n; i++) {
        int val = A[i];
        if ((val & 1) == 1) {
          res[oIdx] = val;
          oIdx += 2;
        } else {
          res[eIdx] = val;
          eIdx += 2;
        }
      }
      return res;
    }
  }

  @Author("navyd")
  @Submission(memory = 40.3, memoryBeatRate = 100, runtime = 2, runtimeBeatRate = 99.66, submittedDate = @DateTime("20190922"),
      url = "https://leetcode.com/submissions/detail/263022300/")
  @SortAlgorithm(spaceComplexity = ComplexityEnum.O_1, timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), inplace = true, inputDependency = true)
  public class SolutionBySwap implements SortArrayByParityII {

    /**
     * 思想：原地排序，swap。该算法利用了数组A的特殊性：A.length%2=0, 奇偶元素各一半，swap保证才能有效
     * <p>Process
     * <pre>
     * Indexes eIdx is 0, 2, 4..., oIdx is 1, 3, 5...
     *
     * while (oIdx < n && eIdx < n)
     *
     * if eIdx_val is even, then eIdx += 2
     *
     * else eIdx_val is odd, then
     *  (improvable: find oIdx_val == even index)
     *  swap(eIdx, oIdx)
     *  oIdx += 2
     * done
     * </pre>
     * <p>优化</p>
     * <ul>
     * <li>如果只有一个偶数下标是奇数值，另一个在奇数下标在数组最后，中间的所有奇数位置
     * 都是奇数值，避免不必要的交换：找偶数值的，奇数下标进行swap
     * </ul>
     * <p>复杂度与输入分析
     * <ul>
     * <li>时间：遍历A时间为O(N)。若输入A是已经有序的，则不会进行任何的swap，仅访问数组N/2次
     * <li>空间：O(1)
     * </ul>
     * <p>稳定性分析：否
     */
    @Override
    public int[] sortArrayByParityII(int[] A) {
      final int n;
      if (A == null || (n = A.length) < 2) {
        return A;
      }
      for (int eIdx = 0, oIdx = 1; oIdx < n && eIdx < n; ) {
        // 偶数下标 奇数值
        if ((A[eIdx] & 1) == 1) {
          // 找偶数值的 奇数下标 避免对奇数下标值的swap
          while ((A[oIdx] & 1) == 1 && (oIdx += 2) < n) {
            ;
          }
          // swap 奇数下标
          int tmp = A[oIdx];
          A[oIdx] = A[eIdx];
          A[eIdx] = tmp;
          oIdx += 2;
        } else {
          eIdx += 2;
        }
      }
      return A;
    }
  }

  @Author("navyd")
  @Author(value = "1", references = "https://leetcode.com/submissions/api/detail/958/java/1/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_1)
  @Submission(memory = 40.8, memoryBeatRate = 100, runtime = 1, runtimeBeatRate = 100, submittedDate = @DateTime("20191207"), url = "https://leetcode.com/submissions/detail/284251305/")
  public static class SolutionBySwapII implements SortArrayByParityII {
    /**
     * 如果奇数位置出现偶数，则在偶数位置找奇数swap
     * <p>优化：去除边界条件for o < A.length, while o < A.length，由于奇偶数量是对称的，只要出现偶数位置出现一个
     * 奇数，则必定奇数位置有一个偶数
     */
    @Override
    public int[] sortArrayByParityII(int[] A) {
      // 0. traversal
      for (int e = 0, o = 1; e < A.length; e += 2) {
        // 1. 如果偶数位置是奇数
        if ((A[e] & 1) == 0) {
          continue;
        }
        // 2. 找奇数位置的偶数
        while ((A[o] & 1) == 1) {
          o += 2;
        }
        // 3. swap e and o
        int temp = A[e];
        A[e] = A[o];
        A[o] = temp;
      }
      return A;
    }
  }
}

