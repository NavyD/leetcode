package cn.navyd.leetcode.heap;

import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DerivedFrom;
import cn.navyd.annotation.leetcode.Optimal;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
Find the kth largest element in an unsorted array. Note that it is the kth largest element in the sorted order, not the kth distinct element.

Example 1:

Input: [3,2,1,5,6,4] and k = 2
Output: 5
Example 2:

Input: [3,2,3,1,2,4,5,5,6] and k = 4
Output: 4
Note: 
You may assume k is always valid, 1 ≤ k ≤ array's length.
 * </pre>
 * 
 * @author navyd
 *
 */
@Problem(number = 215, difficulty = Difficulty.MEDIUM, tags = Tag.HEAP,
    url = "https://leetcode.com/problems/kth-largest-element-in-an-array/")
public interface KthLargestElementinanArray {
  /**
   * 从未排序的数组中找出第k大（不去重）的元素
   * 
   * @param nums
   * @param k
   * @return
   */
  public int findKthLargest(int[] nums, int k);

  @Author(name = "jmnarloch", significant = true,
      referenceUrls = "https://leetcode.com/problems/kth-largest-element-in-an-array/discuss/60294/Solution-explained")
  @Submission(date = "2019-05-08", runtime = 3, runtimeBeatRate = 84.91, memory = 36.2,
      memoryBeatRate = 95.99, url = "https://leetcode.com/submissions/detail/227467498/")
  @Solution(tags = Tag.SORT, spaceComplexity = Complexity.O_1,
      timeComplexity = Complexity.O_N_LOG_N)
  public static class SolutionBySort implements KthLargestElementinanArray {
    /**
     * 排序后直接访问数组最后第k个元素即可。适用于小数组
     */
    @Override
    public int findKthLargest(int[] nums, int k) {
      Arrays.sort(nums);
      int index = nums.length - k;
      return nums[index];
    }
  }
  
  @Author(name = "jmnarloch", significant = true,
      referenceUrls = "https://leetcode.com/problems/kth-largest-element-in-an-array/discuss/60294/Solution-explained")
  @Submission(date = "2019-05-08", runtime = 6, runtimeBeatRate = 63.22, memory = 36.9,
      memoryBeatRate = 94.64, url = "https://leetcode.com/submissions/detail/227582269/")
  @Solution(spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_N_LOG_N)
  public static class SolutionByPriorityQueue implements KthLargestElementinanArray {

    /**
     * 使用优先队列保存前k个元素。在添加num到队列时，如果当前queue.size>k，则移除最大的一个元素以保证O(K)个队列元素
     * <p>时间复杂度：O(K log N). 删除最大元素O(log N)，可能有k次删除操作
     * <p>注意：当k>nums.length/2时，使用max priorityqueue能够减少poll操作，相反 k<nums.length/2，min priority queue同样减少poll
     */
    @Override
    public int findKthLargest(int[] nums, int k) {
      // 避免频繁扩容操作
      Queue<Integer> queue = new PriorityQueue<>(k+1);
      for (int num : nums) {
        queue.offer(num);
        // 保证k个元素
        if (queue.size() > k)
          queue.poll();
      }
      return queue.peek();
    }
  }
  
  @Author(name = "jmnarloch", significant = true,
      referenceUrls = "https://leetcode.com/problems/kth-largest-element-in-an-array/discuss/60294/Solution-explained")
  @Submission(date = "2019-05-08", runtime = 5, runtimeBeatRate = 67.82, memory = 36.1,
      memoryBeatRate = 96.13, url = "https://leetcode.com/submissions/detail/227586012/")
  @Solution(tags = Tag.SORT, spaceComplexity = Complexity.O_1, timeComplexity = Complexity.O_N)
  public static class SolutionByPartition implements KthLargestElementinanArray {

    /**
     * 通过 快速选择 将数组nums的前k个元素小，后k个元素大的结构，第k个元素则是第k大
     * <p>如果不使用shuffle 时间复杂度将会出现最坏情况，runtime=23 ms
     */
    @Override
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
    
    static final Random RAND = new Random();
    
    void shuffle(int[] nums) {
      for (int i = 0; i < nums.length; i++)
        swap(nums, i, RAND.nextInt(nums.length));
    }
     
    int partition(int[] nums, int lo, int hi) {
      final int pivot = nums[lo];
      int i = lo, j = hi+1;
      while (true) {
        while (nums[++i] <= pivot)
          if (i == hi)
            break;
        while (nums[--j] > pivot)
          ;
        if (i >= j)
          break;
        swap(nums, i, j);
      }
      swap(nums, lo, j);
      return j;
    }
    
    static void swap(int[] nums, int i, int j) {
      int tmp = nums[i];
      nums[i] = nums[j];
      nums[j] = tmp;
    }
  }
  
  @Optimal
  @Author(name = "cdai",
      referenceUrls = "https://leetcode.com/problems/kth-largest-element-in-an-array/discuss/60312/AC-Clean-QuickSelect-Java-solution-avg.-O(n)-time/61582")
  @Author(name = "yuanb10", significant = true,
      referenceUrls = "https://leetcode.com/problems/kth-largest-element-in-an-array/discuss/60294/Solution-explained/267106")
  @DerivedFrom(SolutionByPartition.class)
  @Submission(date = "2019-05-08", runtime = 3, runtimeBeatRate = 84.91, memory = 36.4,
      memoryBeatRate = 95.45, url = "https://leetcode.com/submissions/detail/227588334/")
  @Solution(tags = Tag.SORT, spaceComplexity = Complexity.O_1, timeComplexity = Complexity.O_N)
  public static class SolutionByEasierPartition extends SolutionByPartition {
    /**
     * 一个简洁易读的partition。
     * 在一个for中将<pivot的交换到前段lo，>pivot则暂时不动，等到遇到新的<pivot则交换，导致>pivot的移动到右边，
     * <p>注意，这样有可能导致以移动>pivot的元素再次被后边的<pivot元素交换，导致额外的开销，对于大数组不可取
     */
    @Override
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
  }
}


