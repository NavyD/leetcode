package cn.navyd.leetcode.sort;

import java.util.Arrays;

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
public interface HIndex {
  int hIndex(int[] citations);
}

@SuppressWarnings("unused")
class MySolutionHIndex implements HIndex {

  @Override
  public int hIndex(int[] citations) {
    
    // 排序
    Arrays.sort(citations);
    // 设置h 与 min min条件一直成立min >= h
    final int len = citations.length;
    for (int i = 0; i < len; i++) {
      int hIndex = citations[i], max = len - hIndex, remainingPaper;
      
      for (int j = len - 1; j >= i; j--) {
        int citation = citations[j];
      }
    }
    // h=a[0] min = h, max = N - h;
    
    // 比较max <= arr.max则成立 否则减小max
    return 0;
  }
}

class SolutionByBucketSort implements HIndex {

  /**
   * 思路：由于h-index被定义的最大为论文数量n。使用桶排序buckets时定义length=n+1，下标为[0,n]，论文citation
   * 可以对应buckets[citation]，重复则++，对于citation>=n的则仅buckets[n]++，不区分>n，因为h-index不可能>n
   * 然后从最后迭代buckets并计数count，当遇到第一个count>=i即找到h-index=i
   * 
   * 时间复杂度：O(N)
   * 空间复杂度：O(N)
   */
  @Override
  public int hIndex(int[] citations) {
    // 入桶
    final int n = citations.length;
    // 最大桶为数组length，h-index不会超过length，citation>length的仅++即可
    int[] buckets = new int[n+1];
    for (int citation : citations)
      if (citation >= n)
        buckets[n]++;
      else 
        buckets[citation]++;
    // 遍历桶 寻找当bucket index <= count桶计数时，h-index被找到
    int count = 0;
    for (int i = n; i >= 0; i--) {
      count += buckets[i];
      if (count >= i)
        return i;
    }
    return 0;
  }
  
}