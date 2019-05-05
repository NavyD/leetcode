package cn.navyd.leetcode.sort;

import java.util.Arrays;
import cn.navyd.leetcode.util.annotation.Problem;
import cn.navyd.leetcode.util.annotation.Problem.Difficulty;
import cn.navyd.leetcode.util.annotation.Problem.Tag;
import cn.navyd.leetcode.util.annotation.Solution;
import cn.navyd.leetcode.util.annotation.Unskilled;
import cn.navyd.leetcode.util.annotation.Solution.Complexity;
import cn.navyd.leetcode.util.annotation.Solution.SolutionStatus;

/**
<pre>
Given an unsorted array nums, reorder it such that nums[0] < nums[1] > nums[2] < nums[3]....

Example 1:

Input: nums = [1, 5, 1, 1, 6, 4]
Output: One possible answer is [1, 4, 1, 5, 1, 6].
Example 2:

Input: nums = [1, 3, 2, 2, 3, 1]
Output: One possible answer is [2, 3, 1, 3, 1, 2].
Note:
You may assume all input has valid answer.

Follow Up:
Can you do it in O(n) time and/or in-place with O(1) extra space?
</pre>
 *
 */
@Unskilled
@Problem(number = 324, tags = Tag.SORT, difficulty = Difficulty.MEDIUM, url = "https://leetcode.com/problems/wiggle-sort-ii/", resolvedCount = 1)
public interface WiggleSortII {
  /**
   * wiggle sorted：任意的odd元素大于其两个even neighbor元素
   * 基本思路：
   * 将数据分为odd,even两组保存对应的元素，任意的odd元素大于其两个even neighbor，该关系仅为局部适用，
   * 任意的odd元素大于even元素是不能直接全局成立的。
   * 
   * 尝试构建全局关系，如果所有的odd元素都大于所有的even元素则全局成立
   * 
   * 由于nums.length不一定为偶数，必须保证even group的长度为(n+1)/2，因为少了一个even会导致连续的odd元素，
   * 但是少了一个odd不影响，即even的长度可能大于odd group
   * 
   * 如果所有的
   * 
   * @param nums
   */
  public void wiggleSort(int[] nums);
}

@Solution(status = SolutionStatus.ACCEPTED, 
spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_N_LOG_N, 
runtime = 3, runtimeBeats = 99.76, memory = 42.2, memoryBeats = 27.27, 
date = "2019-05-04", 
referenceUrls = "https://leetcode.com/problems/wiggle-sort-ii/discuss/77684/Summary-of-the-various-solutions-to-Wiggle-Sort-for-your-reference")
class WiggleSortIISolutionBySort implements WiggleSortII {

  /**
   * 思路：
   * 辅助数组aux排序后使用middle将nums分为odd, even group两部分。该middle值依赖输入，如果输入合法，则middle合法，否则需要在数组中找到何时的middle
   * 将aux数组前段部分[0-mid)小的数 移动到nums的even部分，[mid-n)大的数移动到nums的odd部分
   * 注意，在移动时应该从注意相等的元素不能在一起，遍历时注意分离
   */
  @Override
  public void wiggleSort(int[] nums) {
    // mid为n+1/2保证移动even group时[0-mid)的元素比[mid-n)更多
    final int n = nums.length, mid = (n + 1) / 2;
    int[] aux = Arrays.copyOf(nums, n);
    Arrays.sort(aux);

    // 将[0-mid)的小元素移动到even group
    for (int e = 0, o = mid - 1; o >= 0; o--, e += 2)
      nums[e] = aux[o];
    // 将[mid-n)大的元素移动到odd group
    for (int e = n - 1, o = 1; e >= mid; e--, o += 2)
      nums[o] = aux[e];
  }
}

@Solution(status=SolutionStatus.ACCEPTED,
timeComplexity = Complexity.O_N_LOG_N, spaceComplexity = Complexity.O_N,
runtime = 4, runtimeBeats = 69.32, memory = 41, memoryBeats = 56.82,
date = "2019-05-04", 
referenceUrls = {
    "https://leetcode.com/problems/wiggle-sort-ii/discuss/77682/Step-by-step-explanation-of-index-mapping-in-Java",
    "https://leetcode.com/problems/wiggle-sort-ii/discuss/77682/Step-by-step-explanation-of-index-mapping-in-Java/254983"})
class WiggleSortIISolutionBySortAndMedian implements WiggleSortII {

  /**
   * 该方法与{@link WiggleSortIISolutionBySort}一致，仅改变两个循环到一个循环，多使用了两个下标
   */
  @Override
  public void wiggleSort(int[] nums) {
    final int n = nums.length;
    int[] copy = Arrays.copyOf(nums, n);
    Arrays.sort(copy);
    
    // left为虚拟的even group下标
    int left = (n + 1)/2 - 1, right = n - 1;
    for (int i = 0; i < n; i++) {
      // 移动到odd group
      if (isOdd(i))
        nums[i] = copy[right--];
      // 移动到even group
      else
        nums[i] = copy[left--];
    }
  }
  
  private static boolean isOdd(int num) {
    return (num & 1) == 1;
  }
}
