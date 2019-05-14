package cn.navyd.leetcode.sort;

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
 *
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
     * <p>使用桶排序，每个桶保存同citation的论文数量。
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
        // 统计论文数量 查找论文数量第一个超过citation的citation
        paperCount += buckets[citation];
        if (paperCount >= citation)
          return citation;
      }
      return 0;
    }
    
  }
}