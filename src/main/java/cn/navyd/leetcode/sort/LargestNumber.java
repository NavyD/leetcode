package cn.navyd.leetcode.sort;

import java.util.Arrays;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Unskilled;

/**
 * <pre>
Given a list of non negative integers, arrange them such that they form the largest number.

Example 1:

Input: [10,2]
Output: "210"
Example 2:

Input: [3,30,34,5,9]
Output: "9534330"
Note: The result may be very large, so you need to return a string instead of an integer.
 * </pre>
 * 
 * @author navyd
 *
 */
@Unskilled
@Problem(number = 179, difficulty = Difficulty.MEDIUM, tags = Tag.SORT, url = "https://leetcode.com/problems/largest-number/")
public interface LargestNumber {

  public String largestNumber(int[] nums);
  
  @Author(name = "ran3", significant = true, 
      referenceUrls = "https://leetcode.com/problems/largest-number/discuss/53158/My-Java-Solution-to-share")
  @Submission(date = "2019-05-23", runtime = 35, runtimeBeatRate = 22.50, memory = 35.4, memoryBeatRate = 95.94,
      url = "https://leetcode.com/submissions/detail/230755456/")
  @Submission(date = "2019-04-03", runtime = 36, runtimeBeatRate = 20.10, memory = 37.4, memoryBeatRate = 67.86,
      url = "https://leetcode.com/submissions/detail/219763015/")
  @Solution(timeComplexity = Complexity.O_N_LOG_N, spaceComplexity = Complexity.O_N)
  public static class SolutionByConcatenation implements LargestNumber {
    /**
     * 思路：转换为string比较。当两个数连接时，连接后的值只有两种情况如：
     * <pre>
     * 9,20
     * ==>
     * 1. 920
     * 2. 209
     * </pre>
     * 显然，只需要获取最大的连接后的值即可
     * <p>默认的排序顺序会导致忽略最高位如：9, 20 ==> 209，正确的应该为920
     * 使用连接两个string int值的两种情况比较顺序：（a, b表示int的字符串形式）
     * a + b > b + a 若 b + c > c + b 推出==> a + c > c + a，即基于连接的比较是正确的
     * <p>关键在于比较时两个数前面部分都相等时，另一个数多出的一位应该超过另一个的首位
     * 
     * 时间复杂度：假设num平均长度为K，两个字符串num比较为O(K)，排序为O(N log N)，则为O(KN log N)
     */
    @Override
    public String largestNumber(int[] nums) {
      if (nums==null || nums.length == 0)
        return "";
      final int len = nums.length;
      // 字符串数据
      String[] strNums = new String[len];
      for (int i = 0; i < len; i++)
        strNums[i] = String.valueOf(nums[i]);
      // 降序排列
      Arrays.sort(strNums, (val1, val2) -> (val2 + val1).compareTo(val1 + val2));
      // 如果nums数据都为0
      if (strNums[0].equals("0"))
        return "0";
      StringBuilder largestNum = new StringBuilder();
      for (String s : strNums)
        largestNum.append(s);
      return largestNum.toString();
    }
  }
}


