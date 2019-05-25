package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Submission.Status;

/**
 * <pre>

Given a string S, check if the letters can be rearranged so that two characters that are adjacent to each other are not the same.

If possible, output any possible result.  If not possible, return the empty string.

Example 1:

Input: S = "aab"
Output: "aba"
Example 2:

Input: S = "aaab"
Output: ""
Note:

S will consist of lowercase letters and have length in range [1, 500].
 * </pre>
 * @author navyd
 *
 */
@Problem(number = 767, difficulty = Difficulty.MEDIUM, tags = Tag.SORT, url = "https://leetcode.com/problems/reorganize-string/")
public interface ReorganizeString {
  /**
   * 将字符串S重新排列，使相邻字符不是在重复的字符
   * @param S
   * @return
   */
  public String reorganizeString(String S);
  
  @Author(name = "wfei26", significant = true,
      referenceUrls = "https://leetcode.com/problems/reorganize-string/discuss/113440/Java-solution-PriorityQueue/211009")
  @Author(name = "shawngao",
      referenceUrls = "https://leetcode.com/problems/reorganize-string/discuss/113440/Java-solution-PriorityQueue")
  @Submission(date = "2019-05-25", runtime = 36, runtimeBeatRate = 33.31, memory = 35.2,
      memoryBeatRate = 92.22, url = "https://leetcode.com/submissions/detail/231188436/")
  @Solution(timeComplexity = Complexity.O_K_N_LOG_N, spaceComplexity = Complexity.O_N_K)
  public static class SolutionByPriorityQueue implements ReorganizeString {

    /**
     * 思路：找到每个字符的重复频率，按照频率从大到小重新排列。
     * <p>实现：
     * <ul>
     * <li>使用Map保存指定字符和该字符在字符串中的重复次数
     * <li>使用PriorityQueue按次数从大到小的排列字符
     * <li>使用StringBuilder保存重新排列的字符
     * <li>如果重排的最后一个字符与当前字符不一样，则直接插入最后。
     * <li>否则需要重新获取下一个字符作为当前插入字符比较
     * <li>如果不存在下一个则说明不能重排
     * </ul>
     * 假设S的字符种类为N，平均长度为K，（s.length相当于NK）一次优先队列操作为O(log N)。
     * <p>时间复杂度：
     * 重排需要循环S.length次即NK次，每次队列操作为log N。即O(KN log N)
     * <p>空间复杂度：将S转为char[]需要O(NK)，queue和map都需要O(N)
     */
    @Override
    public String reorganizeString(String S) {
      // 统计重复次数
      final Map<Character, Integer> counts = new HashMap<>();
      for (char c : S.toCharArray())
        counts.put(c, counts.getOrDefault(c, 0) + 1);
      // 构建priority queue
      final Queue<Map.Entry<Character, Integer>> queue = new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
      queue.addAll(counts.entrySet());
      // 重排
      final StringBuilder reorganized = new StringBuilder();
      while (!queue.isEmpty()) {
        final Map.Entry<Character, Integer> curEntry = queue.poll();
        final int length = reorganized.length();
        // 当前字符与重排最后字符不同
        if (length == 0 || reorganized.charAt(length-1) != curEntry.getKey()) {
          reorganized.append(curEntry.getKey());
          curEntry.setValue(curEntry.getValue()-1);
        } 
        // 相同
        else {
          // 获取下一个
          Map.Entry<Character, Integer> nextEntry = queue.poll();
          // 如果不存在下一个，无法重排
          if (nextEntry == null)
            return "";
          reorganized.append(nextEntry.getKey());
          nextEntry.setValue(nextEntry.getValue()-1);
          // 下一个字符没有用完
          if (nextEntry.getValue() > 0)
            queue.offer(nextEntry);
        }
        // 当前字符没有用完
        if (curEntry.getValue() > 0)
          queue.offer(curEntry);
      }
      return reorganized.toString();
    }
    
  }
  
  
  @Author(name = "navyd")
  @Submission(date = "2019-05-25", status = Status.WRONG, url = "https://leetcode.com/submissions/detail/231179919/")
  @Solution(timeComplexity = Complexity.O_N_LOG_N, spaceComplexity = Complexity.O_N)
  public static class SolutionBySort implements ReorganizeString {

    /**
     * 思路：先排序，然后将后面的字符插入前面
     * 判断某个字符重复是否超过长度一半。
     * <p>
     * 错误原因：字符种类多，大小不一，重复的字符不一致，简单的从后往前排不足以将中间重复的字符分开。即没有找到每个字符的重复频率
     * <pre>
Input:"vvvlo"
Output:"lvvvo"
Expected:"vlvov"
     * </pre>
     */
    @Override
    public String reorganizeString(String S) {
      // 排序
      final char[] chars = S.toCharArray();
      Arrays.sort(chars);
      // 检查是否合法
      if (!isEligible(chars))
        return "";
      // 重排
      for (int i = 1, j = chars.length - 1; i <= j; i += 2, j -= 2)
        swap(chars, i, j);
      return String.valueOf(chars);
    }
    
    static void swap(char[] chars, int i, int j) {
      char temp = chars[i];
      chars[i] = chars[j];
      chars[j] = temp;
    }
    
    static boolean isEligible(char[] chars) {
      final int limit = (chars.length + 1) / 2;
      char checkingChar = chars[0];
      int count = 0;
      for (char c : chars) {
        // 计数
        if (c == checkingChar)
          count++;
        // 检查不合法
        else if (count > limit)
          return false;
        // 该字符合法 重置
        else {
          count = 1;
          checkingChar = c;
        }
      }
      return true;
    }
    
  }
}
