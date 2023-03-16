package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.List;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Optimal;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Unskilled;

/**
 * <pre>
 * Given a string and a string dictionary, find the longest string in the dictionary that can be formed by deleting some characters of the given string. If there are more than one possible results, return the longest word with the smallest lexicographical order. If there is no possible result, return the empty string.
 *
 * Example 1:
 * Input:
 * s = "abpcplea", d = ["ale","apple","monkey","plea"]
 *
 * Output:
 * "apple"
 * Example 2:
 * Input:
 * s = "abpcplea", d = ["a","b","c"]
 *
 * Output:
 * "a"
 * Note:
 * All the strings in the input will only contain lower-case letters.
 * The size of the dictionary won't exceed 1,000.
 * The length of all the strings in the input won't exceed 1,000.
 * </pre>
 *
 * @author navyd
 */
@Unskilled
@Problem(number = 524, difficulty = Difficulty.MEDIUM, tags = Tag.SORT,
    url = "https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/")
public interface LongestWordInDictionarythroughDeleting {
  /**
   * 在字典中找到字符串s匹配的 最长、字典顺序的 单词
   *
   * @param s
   * @param d
   * @return
   */
  public String findLongestWord(String s, List<String> d);

  @Author(name = "compton_scatter", significant = true,
      referenceUrls = "https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/discuss/99588/Short-Java-Solutions-Sorting-Dictionary-and-Without-Sorting")
  @Submission(date = "2019-05-24", runtime = 51, runtimeBeatRate = 22.38, memory = 36.9,
      memoryBeatRate = 99.16, url = "https://leetcode.com/submissions/detail/230978469/")
  @Solution(timeComplexity = Complexity.O_N_K, spaceComplexity = Complexity.O_N)
  public static class SolutionBySort implements LongestWordInDictionarythroughDeleting {

    /**
     * 思路：由于需要找到的单词是最长的、字符升序的，首先对于长度排序，最长的放到最后，忽略字符顺序。然后对同等长度的
     * 使用字符升序。
     * <p>比较时从最长的开始，字符顺序小的开始，如果单词字符都在字符串中被顺序找到，则直接返回该单词。
     * <p>
     * 实现：
     * <ul>
     * <li>排序比较时使用长度降序、字符升序，从最长的开始找
     * <li>word字符匹配的顺序应该与字符串s字符的顺序保持一致。
     * 如abpcplea --> apple。当第一个p在匹配index 2时，下一个p只能从index 3开始匹配
     * </ul>
     * 假设字符串s长k：
     * <p>时间复杂度：排序O(NlogN)，遍历O(NK)。如果k非常小，则使用排序的复杂度，但是一般使用O(NK)
     * 空间复杂度：归并排序O(N)，字符数组O(K)。所以为O(N)
     */
    @Override
    public String findLongestWord(String s, List<String> d) {
      // 比较器排序
      String[] dictionary = d.toArray(new String[0]);
      final char[] chars = s.toCharArray();
      // 长度降序、字符升序
      Arrays.sort(dictionary, (a, b) -> {
        int diff = a.length() - b.length();
        return diff != 0 ? -diff : a.compareTo(b);
      });
      // 遍历寻找
      for (String word : dictionary) {
        int i = 0;
        // 在chars中顺序匹配word单词字符
        for (char c : chars) {
          if (i < word.length() && c == word.charAt(i)) {
            i++;
          }
        }
        // 所有字符顺序匹配
        if (i == word.length()) {
          return word;
        }
      }
      return "";
    }
  }

  @Optimal
  @Author(name = "hot13399",
      referenceUrls = "https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/discuss/99588/Short-Java-Solutions-Sorting-Dictionary-and-Without-Sorting/103703")
  @Author(name = "compton_scatter", significant = true,
      referenceUrls = "https://leetcode.com/problems/longest-word-in-dictionary-through-deleting/discuss/99588/Short-Java-Solutions-Sorting-Dictionary-and-Without-Sorting")
  @Submission(date = "2019-05-25", runtime = 13, runtimeBeatRate = 93.62, memory = 39.8,
      memoryBeatRate = 78.67, url = "https://leetcode.com/submissions/detail/231170569/")
  @Solution(timeComplexity = Complexity.O_N_K, spaceComplexity = Complexity.O_K)
  public static class SolutionByLongestWord implements LongestWordInDictionarythroughDeleting {

    /**
     * 思路：遍历找到最长、字符顺序最小的单词longestWord。如果遍历过程中遇到更好的，则更新longestWord
     * <p>类似与冒泡排序，只不过从单一顺序变为了双重顺序，并且需要满足指定条件（单词能被顺序找到）
     * <p>实现时注意：遍历的判断条件的顺序很重要，能够减少许多不必要的迭代遍历
     * <p>空间复杂度：由于word.toCharArray()复制一个数组，假设字符串的平均长度为k，额外的空间为O(K)。
     */
    @Override
    public String findLongestWord(String s, List<String> d) {
      String longestWord = "";
      final char[] sources = s.toCharArray();
      for (String word : d) {
        int diff = word.length() - longestWord.length();
        // 仅当长度大于longestword 或 等于时 word小于longestword 尝试寻找新的最长单词
        if ((diff > 0 || (diff == 0 && word.compareTo(longestWord) < 0))
            // 单词在s中找到
            && isSubsequence(sources, word.toCharArray())) {
          longestWord = word;
        }
      }
      return longestWord;
    }

    /**
     * 如果单词在s中顺序被找到，则返回true
     *
     * @param s
     * @param words
     * @return
     */
    static boolean isSubsequence(char[] s, char[] word) {
      int i = 0;
      for (char c : s) {
        if (i < word.length && word[i] == c) {
          i++;
        }
      }
      return i == word.length;
    }
  }
}
