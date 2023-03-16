package cn.navyd.leetcode.sort;

import java.util.Arrays;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.DifficultyEnum;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
 * Given an unsorted array, find the maximum difference between the successive elements in its sorted form.
 *
 * Return 0 if the array contains less than 2 elements.
 *
 * Example 1:
 *
 * Input: [3,6,9,1]
 * Output: 3
 * Explanation: The sorted form of the array is [1,3,6,9], either
 * (3,6) or (6,9) has the maximum difference 3.
 * Example 2:
 *
 * Input: [10]
 * Output: 0
 * Explanation: The array contains less than 2 elements, therefore return 0.
 * Note:
 *
 * You may assume all elements in the array are non-negative integers and fit in the 32-bit signed integer range.
 * Try to solve it in linear time/space.
 * </pre>
 *
 * @author navyd
 */
@Problem(number = 164, difficulty = DifficultyEnum.HARD, url = "https://leetcode.com/problems/maximum-gap/")
public interface MaximumGap {
  public int maximumGap(int[] nums);

  @Author(value = "navyd")
  @Submission(submittedDate = @DateTime("20190604"), memory = 35.9, memoryBeatRate = 99.89, runtime = 5,
      runtimeBeatRate = 33.99, url = "https://leetcode.com/submissions/detail/233472726/")
  public static class SolutionBySimple implements MaximumGap {

    /**
     * 思路：将nums排序，然后遍历相邻元素的gap找到最大值。
     * <p>该方法不符合问题的线性时间空间要求
     */
    @Override
    public int maximumGap(int[] nums) {
      if (nums == null || nums.length < 2) {
        return 0;
      }
      // 快速排序
      Arrays.sort(nums);
      final int n = nums.length;
      // 寻找最大值
      int maxGap = 0;
      for (int i = 1; i < n; i++) {
        int gap = nums[i] - nums[i - 1];
        if (gap > maxGap) {
          maxGap = gap;
        }
      }
      return maxGap;
    }
  }

  // 解释为何比较相邻间隔
  @Author(value = "hot13399", references = "https://leetcode.com/problems/maximum-gap/discuss/50643/bucket-sort-JAVA-solution-with-explanation-O(N)-time-and-space/51216")
  // 解释为何最大间隔为ceiling((max-min)/(n-1))
  @Author(value = "sherryli", references = "https://leetcode.com/problems/maximum-gap/discuss/50643/bucket-sort-JAVA-solution-with-explanation-O(N)-time-and-space/200059")
  // 解释为何同一个桶不需要比较间隔
  @Author(value = "teddyyyy", references = "https://leetcode.com/problems/maximum-gap/discuss/50643/bucket-sort-JAVA-solution-with-explanation-O(N)-time-and-space/51251")
  @Author(value = "zkfairytale",
      references = "https://leetcode.com/problems/maximum-gap/discuss/50643/bucket-sort-JAVA-solution-with-explanation-O(N)-time-and-space")
  @Submission(submittedDate = @DateTime("20190609"), memory = 37, memoryBeatRate = 99.42, runtime = 2, runtimeBeatRate = 99.34, url = "https://leetcode.com/submissions/detail/234650389/")
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
     * 最大值不入桶(max-min)/((max-min)/n-1)下标是n-1，超出界限buckets[n-1]
     * <p>为什么最大最小值不入桶，且最后更新maxGap为何使用max-prev？
     * 当元素存在有过大的gap时，入桶时会有多个元素到一个桶，导到其它桶存在空桶的情况，但是按实际排序来看，
     * 两个桶间存在者许多空桶时，这两个桶最大最小元素是相邻的，即可以减
     * <p>
     * 为何比较相邻桶最大小值就能使max gap：
     * <p>
     * 由于total_gap=max-min，平均avg_gap=total_gap/(n-1)总的gap为n-1个，且如果相邻gap有小就有大，
     * 总的gap是不变的，即最大gap是在相邻gap中出现的
     * <p>
     * 同个gap中的元素是不用比较的，其中最大gap就是gap，但相邻gap就要比较，
     * gap桶是另一个形式的排序：入桶时找每个num对应的下标需要相对位置(num-min)/gap比较n次，再找每个桶的
     * 最大最小值也是n次
     * <p>
     * 为何1<bucket_size<=(max-min)/(n-1)：大于了(max-min)/(n-1)将导致maxgap到一个桶，无法通过相邻比较出来
     */
    @Override
    public int maximumGap(int[] nums) {
      if (nums == null || nums.length < 2) {
        return 0;
      }
      final int n = nums.length;
      // 找到最大 最小值
      int min = nums[0], max = nums[0];
      for (int i = 0; i < n; i++) {
        int num = nums[i];
        if (num < min) {
          min = num;
        } else if (num > max) {
          max = num;
        }
      }
      // 全部值都相等 直接返回
      if (max == min) {
        return 0;
      }
      // 计算gap
      final int gap = (int) Math.ceil((double) (max - min) / (n - 1));
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
        if (num == min || num == max) {
          continue;
        }
        int idx = (num - min) / gap;
        minBuckets[idx] = Math.min(num, minBuckets[idx]);
        maxBuckets[idx] = Math.max(num, maxBuckets[idx]);
      }
      // 比较
      int maxGap = gap, prevBucketMax = min;
      for (int i = 0; i < bucketLength; i++) {
        // 空桶
        if (minBuckets[i] == Integer.MAX_VALUE || maxBuckets[i] == Integer.MIN_VALUE) {
          continue;
        }
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

  @Submission(submittedDate = @DateTime("20190613"), memory = 36.4, memoryBeatRate = 99.61, runtime = 2, runtimeBeatRate = 99.32, url = "https://leetcode.com/submissions/detail/235587536/")
  public static class SolutionByBucketSortII implements MaximumGap {
    /**
     * 思路：相对于{@link SolutionByBucketSort}而言，最大的不同是，将min入桶，max不入桶。{@link SolutionByBucketSort}则是min,max都不入桶
     * <p>由于buckets.length=n-1，gap=(max-min)/(n-1)，将max入桶时，idx = (max-min)/gap = n-1，数组上界溢出。
     * min入桶时，idx=(min-min)/gap=0，正常。
     * <p>假设buckets.length=n，保存max，那么最后一个桶只有一个元素max，最小最大值都是max
     * <p>如果最大值也入桶，idx=n-1，需要数组长度为n，最小值入桶无影响。最后更新时使用max-prev是将虚拟的只有max的最后一个桶与buckets的最后的桶比较
     */
    @Override
    public int maximumGap(int[] nums) {
      if (nums.length < 2) {
        return 0;
      }
      final int n = nums.length;
      // 寻找min, max
      int min = nums[0], max = nums[0];
      for (int num : nums) {
        min = Math.min(min, num);
        max = Math.max(max, num);
      }
      // 计算gap
      final int gap = (int) Math.ceil(((double) max - min) / (n - 1));
      // 桶
      final int[]
          minBuckets = new int[n - 1],
          maxBuckets = new int[n - 1];
      Arrays.fill(minBuckets, Integer.MAX_VALUE);
      Arrays.fill(maxBuckets, Integer.MIN_VALUE);
      // 入桶
      for (int num : nums) {
        // max不入桶
        if (num == max) {
          continue;
        }
        int i = (num - min) / gap;
        minBuckets[i] = Math.min(num, minBuckets[i]);
        maxBuckets[i] = Math.max(num, maxBuckets[i]);
      }
      // 计算桶间gap
      int maxGap = Integer.MAX_VALUE, prev = min;
      for (int i = 0; i < minBuckets.length; i++) {
        // 空桶
        if (maxBuckets[i] == Integer.MIN_VALUE) {
          continue;
        }
        maxGap = Math.max(maxGap, minBuckets[i] - prev);
        prev = maxBuckets[i];
      }
      // 最后一个桶的最小值 最大值就是max
      maxGap = Math.max(maxGap, max - prev);
      return maxGap;
    }
  }

  // 解释为何radixsort 后序重排
  @Author(value = "HauserZ", references = "https://leetcode.com/problems/maximum-gap/discuss/50642/Radix-sort-solution-in-Java-with-explanation/279550")
  @Author(value = "Alexpanda", references = "https://leetcode.com/problems/maximum-gap/discuss/50642/Radix-sort-solution-in-Java-with-explanation")
  @Submission(submittedDate = @DateTime("20190616"), memory = 37.1, memoryBeatRate = 99.33, runtime = 5, runtimeBeatRate = 34.17, url = "https://leetcode.com/submissions/detail/236199369/")
  public static class SolutionByRadixSort implements MaximumGap {

    /**
     * 思路：使用基数排序将数组排序，使得时间空间复杂度在O(n)。排序后，则取相邻gap的最大值即可
     * <p>
     * 时间复杂度：由于int最多10位数字，最多while循环10次，while中最大遍历重排aux和nums为O(N)，所以，平均为O(N)
     * <p>
     * 空间复杂度：最大数组为aux为O(N)
     */
    @Override
    public int maximumGap(int[] nums) {
      if (nums.length < 2) {
        return 0;
      }
      // 10 radix sort
      radixSort(nums, 10);
      // 相邻max gap
      int maxGap = Integer.MIN_VALUE;
      for (int i = 1; i < nums.length; i++) {
        maxGap = Math.max(maxGap, nums[i] - nums[i - 1]);
      }
      return maxGap;
    }

    /**
     * 对nums使用radix进制的基数排序
     * <p>如果重排aux时，不使用后序遍历，前序遍历会导致丢失稳定性
     * <pre>
     * 如 [90,45,75,70]
     * counts[0]=2 counts[5]=2
     * aux[--counts[0]] = nums[0] ==> aux[1]=90 aux[0] = 70 ==> [70, 90,..]
     * 显然与初始数组顺序[90, .., 70]不符
     * </pre>
     */
    void radixSort(int[] nums, int radix) {
      // 获取max num
      int maxNum = nums[0];
      for (int num : nums) {
        maxNum = Math.max(maxNum, num);
      }

      final int n = nums.length;
      final int[] aux = new int[n];
      // 1, 10, 100, 1000...
      // 防止进制radix过大溢出
      long exponent = 1;
      // maxNum指数不为0 循环
      while (maxNum / exponent > 0) {
        // 计数
        int[] counts = new int[radix];
        for (int i = 0; i < n; i++) {
          counts[getDigit(nums[i], exponent, radix)]++;
        }
        // 统计每个计数的长度 即counts对应的nums下标位置
        for (int i = 1; i < radix; i++) {
          counts[i] += counts[i - 1];
        }
        // 重排aux 将对应位数的顺序counts 对应到 整个nums，使nums当前digit有序 从后往前，保证稳定性
        for (int i = n - 1; i >= 0; i--) {
          int idx = --counts[getDigit(nums[i], exponent, radix)];
          aux[idx] = nums[i];
        }
        // 重排nums
        for (int i = 0; i < n; i++) {
          nums[i] = aux[i];
        }
        // 指数步进
        exponent *= radix;
      }
    }

    /**
     * 获取指定num在指数exp下的radix数字。如15的exp=1的10进制数为5，exp=10则为1.若exp=1,为16进制则为15(f)
     */
    int getDigit(int num, long exp, int radix) {
      return (int) (num / exp) % radix;
    }
  }
}
