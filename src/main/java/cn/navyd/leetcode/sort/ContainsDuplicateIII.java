package cn.navyd.leetcode.sort;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;

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
 * Given an array of integers, find out whether there are two distinct indices i and j in the array such that the absolute difference between nums[i] and nums[j] is at most t and the absolute difference between i and j is at most k.
 *
 * Example 1:
 *
 * Input: nums = [1,2,3,1], k = 3, t = 0
 * Output: true
 * Example 2:
 *
 * Input: nums = [1,0,1,1], k = 1, t = 2
 * Output: true
 * Example 3:
 *
 * Input: nums = [1,5,9,1,5,9], k = 2, t = 3
 * Output: false
 * </pre>
 */
@Problem(number = 220, difficulty = DifficultyEnum.MEDIUM)
public interface ContainsDuplicateIII {
  /**
   * 要求nums两个下标i,j的绝对值在k范围内对应的nums[i], nums[j]的绝对值在t范围内
   * <p>k表示两个下标的间隔。如k=1需要使用两个相邻元素比较，k=2需要在三个相邻元素间比较该范围内的num的绝对差值是否在<=t范围内
   *
   * @param nums
   * @param k
   * @param t
   * @return
   */
  public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t);

  @Author(value = "力扣 (LeetCode)", references = "https://leetcode-cn.com/problems/contains-duplicate-iii/solution/cun-zai-zhong-fu-yuan-su-iii-by-leetcode/")
  @Submission(memory = 37.6, memoryBeatRate = 86.36, runtime = 9, runtimeBeatRate = 91.85, submittedDate = @DateTime("20200105"), url = "https://leetcode.com/submissions/detail/291238159/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_K)
  public static class SolutionByBucket implements ContainsDuplicateIII {
    /**
     * <p>思路：在k window中找是否有元素差值<=t。把元素分为[-t-1,-1][0,t],[t+1,2t+1]...
     * <p>问题
     * <ul>
     * <li>为何要检查bucket prev, next两个相邻位置，为何能快速的检查prev,next
     * <p>由于bucket窗口内的元素必定是在<=t的，即bucket仅一个值put一次。相邻的元素差值1<=diff<=2t+1，k窗口中只出现一次bucket（两次则diff<=t），
     * 不需要考虑bucket多个值的问题。bucket间仅检查两个元素
     * <li>bucket=num/t+1与num/t有无区别：有区别
     * <p>如果t=3最大差值为3，一个bucket= 0 <=> [0,3]刚好，3/3=1不在bucket=0这个桶，而3/(3+1)刚好
     * <p>另外，t=0是可能的，3/0将不可行的，num/t+1刚好避开了这个问题
     * <li>有没有可能同一个bucket由于put覆盖接近的元素被移除导致get-num>t：不可能。同一个bucket表示满足<=t直接返回
     * <li>为何使用{@code Map<Long, Long>}：由于nums[i], t都会进行运算可能会int溢出
     * <li>为何num<0时{@code bucket= (num + 1) / range - 1}：由于[0,t]/(t+1)占了0的位置，[-t-1, -1]只有能占-1的位置。
     * 当t=3，[-4,-1]要使bucket=-1，-4/4=-1, -3/4=0，==> (num+1)/range - 1 => (-4+1) / 4 - 1 = -1
     * </ul>
     */
    @Override
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
      if (k <= 0 || t < 0) {
        return false;
      }
      // 0. create hash map for k window
      final Map<Long, Long> buckets = new HashMap<>(k);
      // be careful of int overflow
      final long range = t + 1L;
      for (int i = 0; i < nums.length; i++) {
        // 1. get bucket with num and t
        final long bucket = getBucket(nums[i], range);
        // 3. check bucket existed or adjacent bucket
        if (buckets.containsKey(bucket)) {
          return true;
        }
        if (buckets.containsKey(bucket - 1) && Math.abs(nums[i] - buckets.get(bucket - 1)) <= t) {
          return true;
        }
        if (buckets.containsKey(bucket + 1) && Math.abs(buckets.get(bucket + 1) - nums[i]) <= t) {
          return true;
        }
        // 2. put bucket
        buckets.put(bucket, (long) nums[i]);
        // remove last bucket if window > k
        if (buckets.size() > k) {
          buckets.remove(getBucket(nums[i - k], range));
        }
      }
      return false;
    }

    static long getBucket(long num, long range) {
      return num >= 0 ? num / range : (num + 1) / range - 1;
    }
  }

  @Author(value = "力扣 (LeetCode)", references = "https://leetcode-cn.com/problems/contains-duplicate-iii/solution/cun-zai-zhong-fu-yuan-su-iii-by-leetcode/")
  @Author(value = "user8982", references = "https://leetcode.com/problems/contains-duplicate-iii/discuss/61655/Java-O(N-lg-K)-solution/144810")
  @Author(value = "jmnarloch", references = "https://leetcode.com/problems/contains-duplicate-iii/discuss/61655/Java-O(N-lg-K)-solution")
  @Submission(runtime = 27, runtimeBeatRate = 30.72, memory = 36.3, memoryBeatRate = 82.33, submittedDate = @DateTime("20190514"), url = "https://leetcode.com/submissions/detail/228760292/")
  @Submission(runtime = 21, runtimeBeatRate = 55.9, memory = 36.2, memoryBeatRate = 97.73, submittedDate = @DateTime("20200108"), url = "https://leetcode.com/submissions/detail/228760292/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_K), spaceComplexity = ComplexityEnum.O_K)
  public static class SolutionByBST implements ContainsDuplicateIII {
    /**
     * 思路：在k window中使用Binary Search Tree检查是否存在与当前值差为t
     * <p>问题
     * <ul>
     * <li>为何floor/ceiling num一个就够了：要求是将nums有没有差值在t值内，而不是所有值在t内
     * </ul>
     */
    @Override
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
      if (k < 1 || t < 0 || nums.length == 0) {
        return false;
      }
      // 1. create bst
      final java.util.NavigableSet<Long> set = new TreeSet<>();
      for (int i = 0; i < nums.length; i++) {
        final long num = nums[i];
        final Long floorNum = set.floor(num);
        // 2. check equal or floor num and t difference
        if (floorNum != null && num - floorNum <= t) {
          return true;
        }
        final Long ceilingNum = set.ceiling(num);
        // 3. check equal ceiling num and t difference
        if (ceilingNum != null && ceilingNum - num <= t) {
          return true;
        }
        set.add(num);
        // 4. window elements is k+1, set must is k size
        if (set.size() > k) {
          set.remove((long) nums[i - k]);
        }
      }
      return false;
    }
  }

  @Author("navyd")
  @Submission(runtime = 405, runtimeBeatRate = 11.46, memory = 37.2, memoryBeatRate = 71.16, submittedDate = @DateTime("20190512"), url = "https://leetcode.com/submissions/detail/228294328/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_POW_2), spaceComplexity = ComplexityEnum.O_1)
  public static class SolutionByIteration implements ContainsDuplicateIII {

    /**
     * 遍历迭代，暴力破解
     */
    @Override
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
      for (int i = 0; i < nums.length; i++) {
        final int limit = i + k;
        for (int next = i + 1; next <= limit && next < nums.length; next++) {
          long val = nums[i], nextVal = nums[next];
          // 可能导致int溢出
          long abs = Math.abs(val - nextVal);
          // 比较绝对值 <= t则返回true
          if (abs <= t) {
            return true;
          }
        }
      }
      return false;
    }
  }

  @Author(value = "0", references = "https://leetcode.com/submissions/api/detail/220/java/0/")
  public static class ErrorSolutionBySlidingWindow implements ContainsDuplicateIII {
    /**
     * 思路：sliding window 暴力破解
     * <p>错误答案：官方所有用例可以通过，但下面的测试无法通过。
     * <p>原因：当t==0时end=end的做法将跳过start-end中间元素不比较
     *
     * <pre>
     * [8,1,1,6,7,5,9]
     * 3
     * 0
     * </pre>
     * 解决方案：取消t==0做法正常比较，但是时间复杂度是O(N^2)
     */
    @Override
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
      if (k < 1 || t < 0 || nums.length <= 1) {
        return false;
      }
      int start = 0, end = 1;
      final int n = nums.length, endIndex = n - 1;
      // out loop 
      while (start < endIndex) {
        // compare
        if (Math.abs((long) nums[start] - nums[end]) <= t) {
          return true;
        }
        // out of k window or to end index
        if ((end - start) >= k || end >= endIndex) {
          start++;
          end = t != 0 ? start + 1 : end;
        }
        // [start,end] in k range
        else {
          end++;
        }
      }
      return false;
    }
  }

  public static void main(String[] args) {
    int[] nums = {8, 1, 1, 6, 7, 5, 9};
    int k = 3, t = 0;
    ContainsDuplicateIII p = new ErrorSolutionBySlidingWindow();
    System.out.println(p.containsNearbyAlmostDuplicate(nums, k, t));
  }
}

  