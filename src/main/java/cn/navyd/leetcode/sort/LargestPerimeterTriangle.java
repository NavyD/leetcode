package cn.navyd.leetcode.sort;

import java.util.Arrays;

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
public class LargestPerimeterTriangle {
  // 该方法虽然能够给正确的accept，但是对三角形的性质不清楚，无法做出正确的方法
  /**
   * 思路：先将数组排序，然后从最大元素开始遍历，查找三个元素是否满足三角形定义：任意两边和大于第三边 对于三角形的性质
   * 然后不断查找最大的周长。
   * 
   * @param A
   * @return
   */
  public int largestPerimeter(int[] A) {
    // 排序
    Arrays.sort(A);
    int maxPerimeter = 0;
    final int len = A.length;
    for (int i = len - 3; i >= 0; i--) {
      final int a = A[i], b = A[i + 1], c = A[i + 2];
      final int perimeter;
      if (isTriangle(a, b, c) && maxPerimeter < (perimeter = a + b + c))
        maxPerimeter = perimeter;
    }
    return maxPerimeter;
  }

  static boolean isTriangle(int a, int b, int c) {
    return (a + b) > c;
  }

  static class Solution {
    /**
     * 思路：先将数组排序，然后从最大元素开始遍历，查找三个元素是否满足三角形定义：任意两边和大于第三边 
     * 对于三角形的性质：
     * <pre>
     * A[n-1] < A[n-2] + A[n-3] 
     * A[n-1] >= A[n-2] + A[n-3] >= A[i] + A[j]，不能从A[n-1]后续所有元素组合为三角形
     * </pre>
     * 时间复杂度：O(NlgN) 用于排序
     * 空间复杂度：O(1)
     * @param A
     * @return
     */
    public int largestPerimeterBySort(int[] A) {
      Arrays.sort(A);
      for (int i = A.length - 1; i > 1; --i)
        if (A[i] < A[i - 1] + A[i - 2])
          return A[i] + A[i - 1] + A[i - 2];
      return 0;
    }
  }
}
