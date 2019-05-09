package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.Random;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Solution.Status;
import cn.navyd.annotation.leetcode.Unskilled;
import cn.navyd.leetcode.heap.KthLargestElementinanArray; 

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
@Problem(number = 324, tags = Tag.SORT, difficulty = Difficulty.MEDIUM, url = "https://leetcode.com/problems/wiggle-sort-ii/")
public interface WiggleSortII {
  /**
   * wiggle sorted：任意的odd（下标）元素大于其两个even neighbor元素
   * <p>基本思路：
   * 
   * 将数据分为odd,even两组保存对应的元素，任意的odd元素大于其两个even neighbor，该关系仅为局部适用，
   * 任意的odd元素大于even元素是不能直接全局成立的。
   * 
   * <p>尝试构建全局关系，如果所有的odd元素都大于所有的even元素则全局成立
   * 
   * <p>由于nums.length不一定为偶数，必须保证even group的长度为(n+1)/2，因为少了一个even会导致连续的odd元素，
   * 但是少了一个odd不影响，即even的长度可能大于odd group
   * 
   * <p>注意对于可能存在相等元素，必须错开odd even下标位置 否则将导致连续的等值元素
   * 
   * @param nums
   */
  public void wiggleSort(int[] nums);
  
  @Solution(status = Status.ACCEPTED, 
      spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_N_LOG_N, 
      runtime = 3, runtimeBeatRate = 99.76, memory = 42.2, memoryBeatRate = 27.27, 
      dates = "2019-05-04", 
      submissionUrl = "https://leetcode.com/submissions/detail/226655576/",
      referenceUrls = "https://leetcode.com/problems/wiggle-sort-ii/discuss/77684/Summary-of-the-various-solutions-to-Wiggle-Sort-for-your-reference")
  public static class SolutionBySort implements WiggleSortII {

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
  
  @Solution(status=Status.ACCEPTED,
      timeComplexity = Complexity.O_N_LOG_N, spaceComplexity = Complexity.O_N,
      runtime = 4, runtimeBeatRate = 69.32, memory = 41, memoryBeatRate = 56.82,
      dates = "2019-05-04", 
      submissionUrl = "https://leetcode.com/submissions/detail/226646664/",
      referenceUrls = {
          "https://leetcode.com/problems/wiggle-sort-ii/discuss/77682/Step-by-step-explanation-of-index-mapping-in-Java",
          "https://leetcode.com/problems/wiggle-sort-ii/discuss/77682/Step-by-step-explanation-of-index-mapping-in-Java/254983"})
  public static class SolutionBySortII implements WiggleSortII {

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
  
  @Solution(status=Status.ACCEPTED,
      timeComplexity = Complexity.O_N, spaceComplexity = Complexity.O_N,
      runtime = 70, runtimeBeatRate = 23.57, memory = 41, memoryBeatRate = 55.11,
      dates = "2019-05-09", 
      submissionUrl = "https://leetcode.com/submissions/detail/227709646/",
      referenceUrls = "https://leetcode.com/problems/wiggle-sort-ii/discuss/77684/Summary-of-the-various-solutions-to-Wiggle-Sort-for-your-reference")
  public static class SolutionByMedianPartition implements WiggleSortII {
    /**
     * 使用一个中值median将nums划分两部分。
     * <p>使用{@link KthLargestElementinanArray#findKthLargest(int[], int)}寻找中间值
     * <p>对辅助数组aux划分s group（odd下标）左边元素<median，l group（even下标）右边元素>median。数组大小s.length >= l.length
     * <p>读取aux左边元素到nums odd下标，右边到even下标 
     * <p>对于parition 可能存在相等元素，可能导致正常的partition操作无法正确，使用3-way-partition将相等值划分到中间，
     * 保证s, l group的length合法（至多相差1个元素） 
     * <p>复制到nums时，注意不能将中间相等的元素放到nums 相邻位置，为此需要从aux median两边往前遍历
     * 
     */
    @Override
    public void wiggleSort(int[] nums) {
      final int n = nums.length;
      // 寻找中间值median
      int midIndex = (n+1) / 2;
//      int median = kth.findKthLargest(nums, midIndex);
      int median = findKthLargest(nums, midIndex);
      int[] aux = Arrays.copyOf(nums, n);
      // meidan 3-way-partition划分
      for (int i = 0, lt = 0, gt = n-1; i <= gt;) {
        if (aux[i] < median)
          swap(aux, i++, lt++);
        else if (aux[i] > median)
          swap(aux, i++, gt--);
        else 
          i++;
      }
      // 组合
      // 将aux左边元素复制到nums的even group。注意size > odd group，因为有midIndex个而midIndex超过一半(n+1)/2
      for (int i = 0, j = midIndex-1; j >= 0; i+=2, j--)
        nums[i] = aux[j];
      // 将aux右边元素复制到nums odd group
      for (int i = 1, j = n-1; j >= midIndex; i+=2, j--)
        nums[i] = aux[j];
    }
    
    /**
     * 使用{@link KthLargestElementinanArray#findKthLargest(int[], int)}寻找中间值
     */
    
    void shuffle(int[] nums) {
      Random RAND = new Random();
      for (int i = 0; i < nums.length; i++)
        swap(nums, i, RAND.nextInt(nums.length));
    }
    
    public int findKthLargest(int[] nums, int k) {
      shuffle(nums);
      int lo = 0, hi = nums.length-1;
      // 将第k大 转化为 数组下标k
      k = nums.length - k;
      while (lo < hi) {
        int index = partition(nums, lo, hi);
        if (index < k)
          lo = index + 1;
        else if (index > k)
          hi = index - 1;
        else 
          break;
      }
      return nums[k];
    }
    
    int partition(int[] nums, int lo, int hi) {
      final int pivot = nums[hi];
      int i = lo;
      for (int j = lo; j < hi; j++) {
        if (nums[j] <= pivot)
          swap(nums, i++, j);
      }
      swap(nums, i, hi);
      return i;
    }
    
    static void swap(int[] nums, int i, int j) {
      int tmp = nums[i];
      nums[i] = nums[j];
      nums[j] = tmp;
    }
  }
}
