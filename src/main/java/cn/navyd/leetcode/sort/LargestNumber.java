package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.Comparator;

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
 * Given a list of non negative integers, arrange them such that they form the largest number.
 *
 * Example 1:
 *
 * Input: [10,2]
 * Output: "210"
 * Example 2:
 *
 * Input: [3,30,34,5,9]
 * Output: "9534330"
 * Note: The result may be very large, so you need to return a string instead of an integer.
 * </pre>
 *
 * @author navyd
 */
@Problem(number = 179, difficulty = DifficultyEnum.MEDIUM)
public interface LargestNumber {

  /**
   * 在连接的nums所有元素中找最大数
   * <p>方案：连接两个num比较排序
   */
  public String largestNumber(int[] nums);

  @Author("navyd")
  @Submission(memory = 36, memoryBeatRate = 82.22, runtime = 4, runtimeBeatRate = 88.15, submittedDate = @DateTime("20200103"), url = "https://leetcode.com/submissions/detail/290654939/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_N), spaceComplexity = ComplexityEnum.O_N)
  @Author(value = "chellya", references = "https://leetcode.com/problems/largest-number/discuss/53195/Mathematical-proof-of-correctness-of-sorting-method")
  @Author(value = "ran3", references = "https://leetcode.com/problems/largest-number/discuss/53158/My-Java-Solution-to-share")
  @Submission(submittedDate = @DateTime("20190523"), runtime = 35, runtimeBeatRate = 22.50, memory = 35.4, memoryBeatRate = 95.94, url = "https://leetcode.com/submissions/detail/230755456/")
  @Submission(submittedDate = @DateTime("20190403"), runtime = 36, runtimeBeatRate = 20.10, memory = 37.4, memoryBeatRate = 67.86, url = "https://leetcode.com/submissions/detail/219763015/")
  public static class SolutionBySort implements LargestNumber {
    /**
     * 思路：转换为string比较。当两个数连接时，连接后的值只有两种情况如：
     *
     * <pre>
     * 9,20
     * ==>
     * 1. 920
     * 2. 209
     * </pre>
     * <p>
     * 显然，只需要获取最大的连接后的值即可
     * <p>
     * 默认的排序顺序会导致忽略最高位如：9, 20 ==> 209，正确的应该为920 使用连接两个string int值的两种情况比较顺序：（a,
     * b表示int的字符串形式） {@code a + b > b + a} 若 {@code b + c > c + b} 推出==>
     * {@code a + c > c + a}，即基于连接的比较是正确的
     * <p>
     * 关键在于比较时两个数前面部分都相等时，另一个数多出的一位应该超过另一个的首位
     *
     * <p>
     * 时间复杂度：假设num平均长度为K，两个字符串num比较为O(K)，排序为O(N log N)，则为O(KN log N)
     */
    @Override
    public String largestNumber(int[] nums) {
      // 0. create nums string array
      String[] numStrs = new String[nums.length];
      for (int i = 0; i < nums.length; i++) {
        numStrs[i] = String.valueOf(nums[i]);
      }
      // 1. sort with string concatenation
      Arrays.sort(numStrs, (a, b) -> b.concat(a).compareTo(a.concat(b)));
      if (numStrs[0].charAt(0) == '0') {
        return "0";
      }
      // 2. get result
      StringBuilder res = new StringBuilder();
      for (String s : numStrs) {
        res.append(s);
      }
      return res.toString();
    }

    // 初版
    public String largestNumberOriginal(int[] nums) {
      Comparator<Integer> cmp =
          (a, b) -> (b.toString() + a.toString()).compareTo(a.toString() + b.toString());
      Integer[] ns = new Integer[nums.length];
      for (int i = 0; i < nums.length; i++) {
        ns[i] = nums[i];
      }
      Arrays.sort(ns, cmp);
      if (ns[0] == 0) {
        return "0";
      }
      String s = "";
      for (Integer n : ns) {
        s += n;
      }
      return s;
    }
  }
}


