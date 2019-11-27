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
 * 
 * If it is impossible to form any triangle of non-zero area, return 0.
 * 
 * 
 * 
 * Example 1:
 * 
 * Input: [2,1,2] Output: 5 Example 2:
 * 
 * Input: [1,2,1] Output: 0 Example 3:
 * 
 * Input: [3,2,3,4] Output: 10 Example 4:
 * 
 * Input: [3,6,2,3] Output: 8
 * 
 * 
 * Note:
 * 
 * 3 <= A.length <= 10000 1 <= A[i] <= 10^6
 * 
 * @author navyd
 *
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
   * <li>完整排序
   * <li>排序中，满足条件后不需要剩下的值。可用的算法：bubble, selection, max heap, 
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
        if (A[i] < A[i-1] + A[i-2])
          return A[i] + A[i-1] + A[i-2];
      return 0;
    }
    
  }

  public static class SolutionByBubble implements LargestPerimeterTriangle {

    @Override
    public int largestPerimeter(int[] A) {
      // TODO Auto-generated method stub
      return 0;
    }
    
  }
}
