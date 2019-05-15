package cn.navyd.leetcode.sort;

import java.util.Arrays;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Submission.Status;
import cn.navyd.annotation.leetcode.Unskilled;

/**
<pre>
Given an array of citations (each citation is a non-negative integer) of a researcher, write a function to compute the researcher's h-index.

According to the definition of h-index on Wikipedia: "A scientist has index h if h of his/her N papers have at least h citations each, and the other N − h papers have no more than h citations each."

Example:

Input: citations = [3,0,6,1,5]
Output: 3 
Explanation: [3,0,6,1,5] means the researcher has 5 papers in total and each of them had 
             received 3, 0, 6, 1, 5 citations respectively. 
             Since the researcher has 3 papers with at least 3 citations each and the remaining 
             two with no more than 3 citations each, her h-index is 3.
Note: If there are several possible values for h, the maximum one is taken as the h-index.
</pre>
 * @author navyd
 * ps: 这个题目有点难懂,h-index的定义有点混乱
 */
@Unskilled
@Problem(title = "H-Index", number = 274, difficulty = Difficulty.MEDIUM, tags = Tag.SORT, 
url = "https://leetcode.com/problems/h-index/")
public interface HIndex {
  /**
   * h-index被定义为第一个 被引用的次数h >= 被引用论文的数量之和。
   * 也就是说 {@code h<=citations.length}
   * @param citations
   * @return
   */
  public int hIndex(int[] citations); 
  
  
  @Author(name = "yfcheng", significant = true, 
      referenceUrls = "https://leetcode.com/problems/h-index/discuss/70768/Java-bucket-sort-O(n)-solution-with-detail-explanation")
  @Submission(date = "2019-05-14", status = Status.ACCEPTED,
      runtime = 0, runtimeBeatRate = 100.00, memory = 34.9, memoryBeatRate = 93.48,
      url = "https://leetcode.com/submissions/detail/228785686/")
  // submission中无法查询到memoryBeatRate，超过能够显示的内存
  @Submission(date = "2019-04-12",
      runtime = 0, runtimeBeatRate = 100.00, memory = 37.7, memoryBeatRate = 0.00,
      url = "https://leetcode.com/submissions/detail/221903890/")
  @Solution(tags = Tag.SORT_BUCKET, timeComplexity = Complexity.O_N, spaceComplexity = Complexity.O_N)
  public static class SolutionByBucketSort implements HIndex {
    /**
     * 思路：统计每个citation对应的论文数量，然后从大的citation遍历，对应的数量之和超过citation时，这个citation就是h-index，
     * 保证了每个论文至少有引用citation个。
     * <p>实现：
     * <p>使用桶排序，每个桶保存同citation的论文数量。此时的citation不只是桶的下标，也是论文的数量
     * <p>将citation分为n+1个桶，从0桶开始。citation >= n的保存到n桶中，由于h不可能超过论文数量，统计超过的数量即可
     * <p>从前往后遍历，统计citation对应的论文数量之和count，如果count>=citation即得到h-index: citation
     */
    @Override
    public int hIndex(int[] citations) {
      final int n = citations.length;
      // 分配bucket
      int[] buckets = new int[n+1];
      // 统计每个citation桶的对应论文数量
      for (int citation : citations) {
        // 超过的引用数量 在一起统计
        if (citation >= n)
          buckets[n]++;
        else 
          buckets[citation]++;
      }
      int paperCount = 0;
      for (int citation = n; citation >= 0; citation--) {
        // 统计论文数量
        paperCount += buckets[citation];
        // 如果总的论文数量超过当前的citation，则返回数量citation个
        if (paperCount >= citation)
          return citation;
      }
      return 0;
    }
  }
  
  @Author(name = "han35", referenceUrls = "https://leetcode.com/problems/h-index/discuss/70808/Simple-Java-solution-with-sort/73010")
  @Author(name = "novice00", significant = true, referenceUrls = "https://leetcode.com/problems/h-index/discuss/70808/Simple-Java-solution-with-sort")
  @Submission(date = "2019-05-15", status = Status.ACCEPTED, 
  runtime = 1, runtimeBeatRate = 81.61, memory = 34.5, memoryBeatRate = 99.67,
  url = "https://leetcode.com/submissions/detail/228978719/")
  @Solution(timeComplexity = Complexity.O_N_LOG_N, spaceComplexity = Complexity.O_1)
  public static class SolutionBySort implements HIndex {

    /**
     * 思路：将citations排序，从前往后遍历，统计论文数量。
     * 假设所有论文都符合，即citations[0]>=n。
     * <p>如果遇到citation < count，说明该存在一个论文不符合，count--不计算（移除）最小的论文citation
     * <p>如果发现存在一个citation >= count，表示至少count个论文的引用都>=citation，之前的n-count <= citation
     */
    @Override
    public int hIndex(int[] citations) {
      Arrays.sort(citations);
      final int n = citations.length;
      // 假设所有论文 都符合
      int paperCount = n;
      for (int i = 0; i < n; i++) {
        // 如果第一个citation >= count表示以后的citation都>count 该count是最大的
        if (paperCount <= citations[i])
          break;
        // 该count不符合citation
        paperCount--;
      }
      return paperCount;
    }
  }
  
  @Author(name = "oreomilkshake", referenceUrls = "https://leetcode.com/problems/h-index/discuss/70808/Simple-Java-solution-with-sort/73008")
  @Submission(date = "2019-05-15", status = Status.ACCEPTED, 
  runtime = 1, runtimeBeatRate = 81.61, memory = 35.1, memoryBeatRate = 98.91,
  url = "https://leetcode.com/submissions/detail/228970601/")
  @Solution(timeComplexity = Complexity.O_N_LOG_N, spaceComplexity = Complexity.O_1, derived = SolutionBySort.class)
  public static class SolutionBySortEndToStart implements HIndex {

    /**
     * 假设所有论文都不符合，当从后往前遍历时，遇到count < citation，则说明符合h-index
     * 当遇到第一个count >= citation说明count是最大的
     */
    @Override
    public int hIndex(int[] citations) {
      Arrays.sort(citations);
      final int n = citations.length;
      // 假设所有论文 不都符合
      int paperCount = 0;
      for (int i = n - 1; i >= 0; i--) {
        // 引用数量大于论文数量
        if (paperCount < citations[i]) 
          paperCount++;
        else 
          break;
      }
      return paperCount;
    }
    
  }
}