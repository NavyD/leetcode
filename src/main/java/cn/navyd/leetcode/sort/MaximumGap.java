package cn.navyd.leetcode.sort;

import java.util.Arrays;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
Given an unsorted array, find the maximum difference between the successive elements in its sorted form.

Return 0 if the array contains less than 2 elements.

Example 1:

Input: [3,6,9,1]
Output: 3
Explanation: The sorted form of the array is [1,3,6,9], either
             (3,6) or (6,9) has the maximum difference 3.
Example 2:

Input: [10]
Output: 0
Explanation: The array contains less than 2 elements, therefore return 0.
Note:

You may assume all elements in the array are non-negative integers and fit in the 32-bit signed integer range.
Try to solve it in linear time/space.
 * </pre>
 * @author navyd
 *
 */
@Problem(number = 164, difficulty = Difficulty.HARD, tags = Tag.SORT,
    url = "https://leetcode.com/problems/maximum-gap/")
public interface MaximumGap {
  public int maximumGap(int[] nums);

  @Author(name = "navyd")
  @Submission(date = "2019-06-04", memory = 35.9, memoryBeatRate = 99.89, runtime = 5,
      runtimeBeatRate = 33.99, url = "https://leetcode.com/submissions/detail/233472726/")
  @Solution(spaceComplexity = Complexity.O_1, timeComplexity = Complexity.O_N_LOG_N)
  public static class SolutionBySimple implements MaximumGap {

    /**
     * 思路：将nums排序，然后遍历相邻元素的gap找到最大值。
     * <p>该方法不符合问题的线性时间空间要求
     */
    @Override
    public int maximumGap(int[] nums) {
      if (nums == null || nums.length < 2)
        return 0;
      // 快速排序
      Arrays.sort(nums);
      final int n = nums.length;
      // 寻找最大值
      int maxGap = 0;
      for (int i = 1; i < n; i++) {
        int gap = nums[i] - nums[i - 1];
        if (gap > maxGap)
          maxGap = gap;
      }
      return maxGap;
    }
  }
  
  // 解释为何比较相邻间隔
  @Author(name = "hot13399", referenceUrls = "https://leetcode.com/problems/maximum-gap/discuss/50643/bucket-sort-JAVA-solution-with-explanation-O(N)-time-and-space/51216")
  // 解释为何最大间隔为ceiling((max-min)/(n-1))
  @Author(name = "sherryli", referenceUrls = "https://leetcode.com/problems/maximum-gap/discuss/50643/bucket-sort-JAVA-solution-with-explanation-O(N)-time-and-space/200059")
  // 解释为何同一个桶不需要比较间隔
  @Author(name = "teddyyyy", referenceUrls = "https://leetcode.com/problems/maximum-gap/discuss/50643/bucket-sort-JAVA-solution-with-explanation-O(N)-time-and-space/51251")
  @Author(name = "zkfairytale", significant = true, 
    referenceUrls = "https://leetcode.com/problems/maximum-gap/discuss/50643/bucket-sort-JAVA-solution-with-explanation-O(N)-time-and-space")
  @Submission(date = "2019-06-09", memory = 37, memoryBeatRate = 99.42, runtime = 2, runtimeBeatRate = 99.34, url = "https://leetcode.com/submissions/detail/234650389/")
  @Solution(spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_N)
  public static class SolutionByBucketSort implements MaximumGap {

    /**
     * 思路：假设数组nums长度为n,最小、最大值为min, max，则整个nums的最大间隔{@code maxGap>=ceiling((max-min)/(n-1))}。
     * <p>将nums分为n-1个桶，第k个桶保存的num为{@code [min+(k-1)*maxGap, min+k*mapGap}
     * <p>由于nums最多存在n-2个num不为min，max，而只有n-1个桶，所以至少存在1个空的桶
     * <p>只需要保存每个桶的最大、最小值。然后寻找比较相邻的最大最小值的间隔
     * 注意：
     * <p>桶的大小为n-1，因为间隔为n-1个。如[1,3,4]共3个元素，间隔为[2,1]共2个
     * <p>同一个桶的gap不需要。由于{@code maxGap>=ceiling((max-min)/(n-1))}，而每个桶保存为gap间隔，
     * 所以在gap间隔的都在同一个桶中，超过的需要在相邻的桶中寻找即可
     * <p>比较相邻桶的间隔，由于同一个桶的间隔为maxGap，要比较相邻桶的间隔，仅需要比较相邻桶的最大最小值，
     * <p>桶不保存nums最大最小值max, min. 
     * <p>为什么最大最小值不入桶，且最后更新maxGap为何使用max-prev？
     */
    @Override
    public int maximumGap(int[] nums) {
      if (nums == null || nums.length < 2)
        return 0;
      final int n = nums.length;
      // 找到最大 最小值
      int min = nums[0], max = nums[0];
      for (int i = 0; i < n; i++) {
        int num = nums[i];
        if (num < min)
          min = num;
        else if (num > max)
          max = num;
      }
      // 全部值都相等 直接返回
      if (max == min)
        return 0;
      // 计算gap
      final int gap = (int) Math.ceil((double) (max - min) / (n-1));
      final int bucketLength = n - 1;
      // 创建buckets
      final int[] 
          // 保存桶的最小值
          minBuckets = new int[bucketLength],
          // 保存最大值
          maxBuckets = new int[bucketLength];
      // 填充bucket
      Arrays.fill(minBuckets, Integer.MAX_VALUE);
      Arrays.fill(maxBuckets, Integer.MIN_VALUE);
      // 入桶
      for (int num : nums) {
        // 最大最小值 不入桶
        if (num == min || num == max)
          continue;
        int idx = (num - min) / gap;
        minBuckets[idx] = Math.min(num, minBuckets[idx]);
        maxBuckets[idx] = Math.max(num, maxBuckets[idx]);
      }
      // 比较
      int maxGap = gap, prevBucketMax = min;
      for (int i = 0; i < bucketLength; i++) {
        // 空桶
        if (minBuckets[i] == Integer.MAX_VALUE || maxBuckets[i] == Integer.MIN_VALUE)
          continue;
        int curBucketMin = minBuckets[i];
        // 第一次min=nums最小值 此时max第一个桶的最小值 可能!=nums.min，min max没有入桶，即0<=max-min<=gap
        // 相邻桶的gap = cur bucket min - prev bucket max 
        maxGap = Math.max(maxGap, curBucketMin - prevBucketMax);
        prevBucketMax = maxBuckets[i];
      }
      // 更新最后一个 桶的gap
      maxGap = Math.max(maxGap, max - prevBucketMax);
      return maxGap;
    }
    
  }
}
