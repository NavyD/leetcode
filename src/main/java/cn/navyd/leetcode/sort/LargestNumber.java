package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.Comparator;

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
public abstract class LargestNumber {

  public abstract String largestNumber(int[] nums);
}


class MySolution extends LargestNumber {
  /**
   * 思路：使用最大值将每个数扩展
   * 
   */
  @Override
  public String largestNumber(int[] nums) {
    return null;
  }

}

class SolutionByStringComparator extends LargestNumber {
  /**
   * 思路：转换为string比较。默认的排序顺序会导致忽略最高位如：9, 20 ==> 209，正确的应该为920
   * 使用连接两个string int值的两种情况比较顺序：（a, b表示int的字符串形式）
   * a + b > b + a 若 b + c > c + b 推出==> a + c > c + a，即基于连接的比较是正确的
   * 时间复杂度：O(N log N) 排序
   * 空间复杂度：O(N) 字符串nums
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
    Comparator<String> largestStringComparator = (val1, val2) -> (val2 + val1).compareTo(val1 + val2);
    Arrays.sort(strNums, largestStringComparator);
    // 如果nums数据都为0
    if (strNums[0].equals("0"))
      return "0";
    StringBuilder largestNum = new StringBuilder();
    for (String s : strNums)
      largestNum.append(s);
    return largestNum.toString();
  }
  
}
