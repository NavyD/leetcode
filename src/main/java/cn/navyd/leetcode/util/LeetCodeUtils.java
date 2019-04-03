package cn.navyd.leetcode.util;

public class LeetCodeUtils {
  /**
   * 将字符串数组形式{@code [3,30,34,5,9]}转换为int[]
   */
  public static final Converter<String, int[]> CONVERTER_STRING_TO_INT_ARRAY = in -> {
    String sub = in.substring(1, in.length()-1);
    String[] digits = sub.split(",");
    int[] result = new int[digits.length];
    for (int i = 0; i < digits.length; i++)
      result[i] = Integer.valueOf(digits[i]);
    return result;
  };
}
