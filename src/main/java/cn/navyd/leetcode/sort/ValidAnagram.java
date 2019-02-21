package cn.navyd.leetcode.sort;

import java.util.Arrays;

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
public class ValidAnagram {
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
}
