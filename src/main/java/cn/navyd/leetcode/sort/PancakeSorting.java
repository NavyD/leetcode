package cn.navyd.leetcode.sort;

import java.util.ArrayList;
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

Given an array A, we can perform a pancake flip: We choose some positive integer k <= A.length, then reverse the order of the first k elements of A.  We want to perform zero or more pancake flips (doing them one after another in succession) to sort the array A.

Return the k-values corresponding to a sequence of pancake flips that sort A.  Any valid answer that sorts the array within 10 * A.length flips will be judged as correct.

 

Example 1:

Input: [3,2,4,1]
Output: [4,2,4,3]
Explanation: 
We perform 4 pancake flips, with k values 4, 2, 4, and 3.
Starting state: A = [3, 2, 4, 1]
After 1st flip (k=4): A = [1, 4, 2, 3]
After 2nd flip (k=2): A = [4, 1, 2, 3]
After 3rd flip (k=4): A = [3, 2, 1, 4]
After 4th flip (k=3): A = [1, 2, 3, 4], which is sorted. 
Example 2:

Input: [1,2,3]
Output: []
Explanation: The input is already sorted, so there is no need to flip anything.
Note that other answers, such as [3, 3], would also be accepted.
 

Note:

1 <= A.length <= 100
A[i] is a permutation of [1, 2, ..., A.length]
 * </pre>
 * 
 * @author navyd
 *
 */
@Problem(number = 969, difficulty = DifficultyEnum.MEDIUM)
public interface PancakeSorting {
  /**
   * 对数组A使用煎饼排序。返回翻转元素的下标+1集合
   * <p>思路：两次翻转最大的到最后面
   * @param A
   * @return
   */
  public List<Integer> pancakeSort(int[] A);
  
  // 解释煎饼排序
  @Author(value = "CMU", references = "http://www.cs.cmu.edu/afs/cs.cmu.edu/academic/class/15251-f06/Site/Materials/Lectures/Lecture01/lecture01.pdf")
  @Author(value = "wangzi6147", references = "https://leetcode.com/problems/pancake-sorting/discuss/214200/Java-flip-the-largest-number-to-the-tail")
  @Submission(memory = 35.4, memoryBeatRate = 97.60, runtime = 1, runtimeBeatRate = 100.00, url = "https://leetcode.com/submissions/detail/232792533/", submittedDate = @DateTime("20190601"))
  @SortAlgorithm(timeComplexity = @TimeComplexity(worst = ComplexityEnum.O_N_POW_2), spaceComplexity = ComplexityEnum.O_N, inplace = false)
  public static class SolutionByBringToTop implements PancakeSorting {
    
    /**
     * 思路：bring to top。将大的翻转到最前面，然后翻转到后面，如此即可。
     * 
     * <p>实现：
     * 
     * 简单的找到当前值的下标，然后翻转两次 
     * 
     * <p>
     * 时间复杂度：O(N^2)
     * 空间复杂度：O(N^2)
     */
    @Override
    public List<Integer> pancakeSort(int[] A) {
      final int n = A.length;
      final List<Integer> answer = new ArrayList<>(2*n);
      // A[i] is a permutation of [1, 2, ..., A.length]
      int largest = n;
      while (largest > 0) {
        int largestIndex = indexOf(A, largest);
        // 翻转最大的元素到最前面
        flip(A, largestIndex);
        // 翻转最大元素到最后
        flip(A, largest-1);
        // 添加翻转的结果
        answer.add(largestIndex+1);
        answer.add(largest--);
      }
      return answer;
    }
    
    /**
     * 返回指定value在数组中首次出现的位置
     * @param a
     * @param value
     * @return
     */
    static int indexOf(int[] a, int value) {
      for (int i = 0; i < a.length; i++)
        if (a[i] == value)
          return i;
      return -1;
    }
    /**
     * 将数组在下标index之前的所有元素做一次翻转如：[1,3,2] index=2, ==>[2,3,1]。index元素将会被交换到最前面
     * @param a
     * @param index
     */
    static void flip(int[] a, int index) {
      for (int i = 0, j = index; i < j; i++, j--) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
      }
    }
  }

  @Author(value = "大力王", references = "https://leetcode-cn.com/problems/pancake-sorting/solution/c-jian-dan-shu-xue-fa-by-da-li-wang/")
  @Author("navyd")
  @Submission(memory = 36.7, memoryBeatRate = 94.74, runtime = 1, runtimeBeatRate = 100, submittedDate = @DateTime("20191210"), url = "https://leetcode.com/submissions/detail/284892386/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_POW_2), spaceComplexity = ComplexityEnum.O_N, inplace = true)
  public static class SolutionByFlipToButtom implements PancakeSorting {
    /**
     * 思路：与{@link PancakeSorting.SolutionByBringToTop#pancakeSort(int[])}一致，不同的是flip直接的下标，
     * 而不是特殊largest与A的关系，更直接可读
     * <p>优化
     * <ul>
     * <li>当maxIdx已在最后时，不需要两次flip。对于有序数组有用
     * <li>当maxIdx==0时，不需要flip to top
     * <li>利用input A的特殊性A[i++]=i可减少find max idx的时间：i+1(max) == A[j]; break;
     * </ul>
     * <p>复杂度
     * <li>时间：O(N^2)
     * <li>空间：O(N)
     */
    @Override
    public List<Integer> pancakeSort(int[] A) {
      final List<Integer> res = new ArrayList<>(2*A.length);
      // 0. repeat until buttom=1|0 
      for (int i = A.length - 1; i > 0; i--) {
        // 1. find max value indx
        int maxIdx = 0;
        for (int j = 1; j <= i; j++) 
          if (A[maxIdx] < A[j])
            maxIdx = j;
        // has been in the last 
        if (maxIdx == i)
          continue;
        // 2. flip to top
        if (maxIdx > 0) {
          flip(A, maxIdx);
          res.add(maxIdx+1);
        }
        // 3. flip to buttom
        flip(A, i);
        res.add(i+1);
      }
      return res;
    }

    static void flip(int[] a, int index) {
      for (int i = 0, j = index; i < j; i++, j--) {
        int temp = a[i];
        a[i] = a[j];
        a[j] = temp;
      }
    }
  }
}
