package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.navyd.annotation.algorithm.ComplexityEnum;
import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.DateTimeFormat;
import cn.navyd.annotation.leetcode.DifficultyEnum;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
Given two strings s and t , write a function to determine if t is an anagram of s.

Example 1:

Input: s = "anagram", t = "nagaram"
Output: true
Example 2:

Input: s = "rat", t = "car"
Output: false
Note:
You may assume the string contains only lowercase alphabets.

Follow up:
What if the inputs contain unicode characters? How would you adapt your solution to such case?
 * </pre>
 * 
 * @author navyd
 *
 */
@Problem(difficulty = DifficultyEnum.EASY, number = 242)
public interface ValidAnagram {

  /**
   * 思路：要保证s与t的每个元素都存在，数量一致
   * <p>查找：元素存在
   * <p>计数：数量一致
   * <p>方案
   * <ul>
   * <li>hash/count查找O(1)：将char作为key/index，出现次数为value
   * <li>full sort O(n log n)：排序后比较index对应一致
   * <li>half sort O(N)：排序中比较index一致
   * </ul>
   * @param s
   * @param t
   * @return
   */
  public boolean isAnagram(String s, String t);

  @Author("navyd")
  @Submission(memory = 37.3, memoryBeatRate = 94.84, runtime = 2, runtimeBeatRate = 99.72, submittedDate = @DateTime("20191130"), url = "https://leetcode.com/submissions/detail/282654435/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_1, inplace = false)
  public static class SolutionByCount implements ValidAnagram {
    
    /**
     * 思路：计数。使用特别的条件s,t都是lowercase alphabets.即是26个字符
     * <p>复杂度:设s,t的元素为N
     * <p>时间：遍历s,t为O(N)
     * <p>空间：counts常数数组：O(1)
     * <p>优化：在对t de-count时，同时判断该元素是否存在/数量不同:@{@code --counts[c-offset]<0}
     */
    @Override
    public boolean isAnagram(String s, String t) {
      if (s.length() != t.length())
        return false;
      final char offset = 'a';
      // 0. counter for 26 lantin
      final int[] counts = new int[26];
      // 1. traverse and count for s
      for (char c : s.toCharArray())
        counts[c-offset]++;
      // 2. traverse and de-count for t
      for (char c : t.toCharArray())
        // 3. check counter is 0
        if (--counts[c-offset] < 0)
          return false;
      return true;
    }
  }

  @Author("navyd")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_N, inplace = false)
  @Submission(memory = 37.5, memoryBeatRate = 72.26, runtime = 11, runtimeBeatRate = 28.73, submittedDate = @DateTime("20191202"), url = "https://leetcode.com/submissions/detail/283069664/")
  public static class SolutionByHash implements ValidAnagram {
    /**
     * 思路：与{@link ValidAnagram.SolutionByCount#isAnagram(String, String)}一致
     */
    @Override
    public boolean isAnagram(String s, String t) {
      if (s.length() != t.length())
        return false;
      // 0. count for s
      final Map<Character, Integer> counts = new HashMap<>(s.length());
      for (Character c : s.toCharArray())
        counts.put(c, counts.getOrDefault(c, 0)+1);
      // 1. de-count for t
      for (Character c : t.toCharArray()) {
        Integer count = counts.get(c);
        // 2. check count is 0
        if (count == null || counts.put(c, count-1) <= 0)
          return false;
      }
      return true;
    }
  }

  /**
   * 解决方案1： 将字符串s,t转换为char[]，然后排序，比较这两个char[]对应下标char是否相等。
   * 
   * @author navyd
   *
   */
  static class MyFirstSolution {
    public boolean isAnagram(String s, String t) {
      if (s.length() != t.length())
        return false;
      char[] sourceChars = s.toCharArray(), targetChars = t.toCharArray();
      Arrays.sort(sourceChars);
      Arrays.sort(targetChars);
      final int length = sourceChars.length;
      for (int i = 0; i < length; i++)
        if (sourceChars[i] != targetChars[i])
          return false;
      return true;
    }
  }

  // 不同的解决方案

  /**
   * 与我的一致
   * 
   * @author navyd
   *
   */
  static class SortingSolution {
    public boolean isAnagram(String s, String t) {
      if (s.length() != t.length()) {
        return false;
      }
      char[] str1 = s.toCharArray();
      char[] str2 = t.toCharArray();
      Arrays.sort(str1);
      Arrays.sort(str2);
      return Arrays.equals(str1, str2);
    }
  }

  /**
   * 使用Hash table，使用一个长度为26的int[]，对于每个出现的char则相对与'a'的int值count++，即记录指定char出现的次数。
   * 对于另一个字符串，则count--，如果两个字符串出现的char相同，则最后所有的count为0.否则一定不同
   * @author navyd
   *
   */
  static class HashTableSolution {
    boolean isAnagramWithHash(String s, String t) {
      if (s.length() != t.length())
        return false;
      // 26个小写字母
      char[] counter = new char[26];
      int length = s.length();
      // 最小字符
      char minChar = 'a';
      for (int i = 0; i < length; i++) {
        counter[s.charAt(i) - minChar]++;
        counter[t.charAt(i) - minChar]--;
      }
      for (int count : counter)
        if (count != 0)
          return false;
      return true;
    }
  }
}
