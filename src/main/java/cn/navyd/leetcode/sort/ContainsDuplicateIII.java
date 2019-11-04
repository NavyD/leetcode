package cn.navyd.leetcode.sort;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeSet;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Optimal;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Skilled;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Submission.Status;
import cn.navyd.annotation.leetcode.Unskilled;

@Unskilled
@Problem(number = 220, tags = Tag.SORT, difficulty = Difficulty.MEDIUM, url = "https://leetcode.com/problems/contains-duplicate-iii/")
public interface ContainsDuplicateIII {
  /**
   * 要求nums两个下标i,j的绝对值在k范围内对应的nums[i], nums[j]的绝对值在t范围内
   * <p>k表示两个下标的间隔。如k=1需要使用两个相邻元素比较，k=2需要在三个相邻元素间比较该范围内的num的绝对差值是否在<=t范围内
   * @param nums
   * @param k
   * @param t
   * @return
   */
  public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t);

  @Skilled
  @Author(name = "navyd", significant = true)
  @Submission(runtime = 405, runtimeBeatRate = 11.46, memory = 37.2, memoryBeatRate = 71.16,
      date = "2019-05-12", 
      url = "https://leetcode.com/submissions/detail/228294328/")
  @Solution(timeComplexity = Complexity.O_N_K, spaceComplexity = Complexity.O_1)
  public static class SolutionByIteration implements ContainsDuplicateIII {

    /**
     * 遍历迭代，暴力破解
     */
    @Override
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
      for (int i = 0; i < nums.length; i++) {
        final int limit = i+k;
        for (int next = i+1; next <= limit && next < nums.length; next++) {
          long val = nums[i], nextVal = nums[next];
          // 可能导致int溢出
          long abs = Math.abs(val - nextVal);
          // 比较绝对值 <= t则返回true
          if (abs <= t)
            return true;
        }
      }
      return false;
    }
  }
  
  @Optimal
  @Author(name = "jmnarloch", referenceUrls = "https://leetcode.com/problems/contains-duplicate-iii/discuss/61655/Java-O(N-lg-K)-solution")
  @Author(name = "wzrthhj", significant = true, 
      referenceUrls = "https://leetcode.com/problems/contains-duplicate-iii/discuss/61645/AC-O(N)-solution-in-Java-using-buckets-with-explanation/62992")
  @Author(name = "lx223", significant = true, 
  referenceUrls = "https://leetcode.com/problems/contains-duplicate-iii/discuss/61645/AC-O(N)-solution-in-Java-using-buckets-with-explanation")
  @Submission(date = "2019-05-13", status = Status.ACCEPTED,
      runtime = 8, runtimeBeatRate = 95.30, memory = 36.9, memoryBeatRate = 72.56,
      url = "https://leetcode.com/submissions/detail/228570208/")
  @Solution(tags = Tag.SORT_BUCKET, timeComplexity = Complexity.O_N, spaceComplexity = Complexity.O_K)
  public static class SolutionByBucketSort implements ContainsDuplicateIII {
    /**
     * 对nums使用桶排序的思路解决：就是将t作为一个区分重复元素的范围值
     * <ol>
     * <li>桶的数量最多为k+1个，将nums值在t差值之内都分配到同一个桶中，作为重复元素处理，
     * <li>如果同一个桶存在多个元素，表示在k范围内nums存在 <= t的两个元素
     * <li>如果桶仅存在一个元素（由上一条保证，多个元素的桶一定不会出现），则需要考虑相邻桶的差值是否在 <=t
     * <li>当迭代元素超过k+1个元素后，删除最前面的桶，保证k+1个桶，就像一个滑动的k+1大的window
     * </ol>
     * 实现：
     * <ul>
     * <li>由于处理负数比较麻烦（处理绝对值），直接重新映射num-Integer.MIN_VALUE不会为负数
     * <li>由于两个数差值t可能为包含0，使用t+1分配桶，防止除0异常
     * <li>map保存k个元素，第k+1个元素先计算桶并比较结果后才移动window移除最前的桶并添加k+1桶保证k size
     * </ul>
     * 时间复杂度：{@link Complexity#O_N}。
     * 由于buckets是一个hash map，get, containsKey, remove等操作都是{@link Complexity#O_1}，迭代nums是{@link Complexity#O_N}
     */
    @Override
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
      if (k < 1 || t < 0)
        return false;
      final long offset = Integer.MIN_VALUE, range = (long)t + 1;
      Map<Long, Long> buckets = new HashMap<>(k);
      for (int i = 0; i < nums.length; i++) {
        final long 
          // 重新映射num
          remappedNum = (long)nums[i] - offset,
          // 将num 分配bucket
          bucket = remappedNum / range;
        final long prev = bucket - 1, next = bucket + 1;
        // 判断buckets是否满足<=t
        if (buckets.containsKey(bucket) // 同一个桶存在多个元素
            // 当前bucket与前一个桶的元素值（都是单元素桶）相差在t范围内，关于 prev < bucket 可以推出 prev num < bucket num
            || (buckets.containsKey(prev) && remappedNum - buckets.get(prev) <= t)
            || (buckets.containsKey(next) && buckets.get(next) - remappedNum <= t))
          return true;
        // 移动k window，删除最前的bucket
        if (buckets.size() >= k) {
          long foremostBucket = ((long)nums[i-k] - offset) / range;
          buckets.remove(foremostBucket);
        }
        // 插入bucket
        buckets.put(bucket, remappedNum);
      }
      return false;
    }
  }
  
  @Author(name = "user8982", 
      referenceUrls = "https://leetcode.com/problems/contains-duplicate-iii/discuss/61655/Java-O(N-lg-K)-solution/144810")
  @Author(name = "jmnarloch", significant = true, 
    referenceUrls = "https://leetcode.com/problems/contains-duplicate-iii/discuss/61655/Java-O(N-lg-K)-solution")
  @Submission(runtime = 27, runtimeBeatRate = 30.72, memory = 36.3, memoryBeatRate =  82.33,
      date = "2019-05-14", status = Status.ACCEPTED,
      url = "https://leetcode.com/submissions/detail/228760292/")
  @Solution(spaceComplexity = Complexity.O_K, timeComplexity = Complexity.O_N_LOG_K)
  public static class SolutionByBST implements ContainsDuplicateIII {

    /**
     * 思路：将指定大小k的元素排序，新的元素进来时与最接近的值比较是否满足<=t即可
     * <p>实现：使用binary search tree数据结构，高效的实现对k个元素排序的 同时不断的移动
     */
    @Override
    public boolean containsNearbyAlmostDuplicate(int[] nums, int k, int t) {
      if (k < 1 || t < 0 || nums.length == 0)
        return false;
      // 保存k个有序的nums元素，并不断的向前移动window
      java.util.NavigableSet<Long> sortedNums = new TreeSet<>();
      for (int i = 0; i < nums.length; i++) {
        final long num = nums[i];
        // 在k范围内存在相同的值
        if (sortedNums.contains(num))
          return true;
        // 只要获取sortedNums存在比num大的，不需要最大的
        Long ceilingNum = sortedNums.ceiling(num);
        if (ceilingNum != null && ceilingNum - num <= t)
          return true;
        Long floorNum = sortedNums.floor(num);
        if (floorNum != null && num - floorNum <= t)
          return true;
        
        // 当i>=k时 set sortedNums必定size >= k
//        if (i >= k) {
        // 相比i>=k 更可读
        // 移动 保证k window的大小为k个元素
        if (sortedNums.size() >= k) {
          long foremostNum = nums[i - k];
          sortedNums.remove(foremostNum);
        }
        sortedNums.add(num);
      }
      return false;
    }
  }
}
