package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DerivedFrom;
import cn.navyd.annotation.leetcode.Optimal;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Submission.Status;
import cn.navyd.annotation.leetcode.Unskilled;

/**
 * <pre>
 *
 * Given a string S, check if the letters can be rearranged so that two characters that are adjacent to each other are not the same.
 *
 * If possible, output any possible result.  If not possible, return the empty string.
 *
 * Example 1:
 *
 * Input: S = "aab"
 * Output: "aba"
 * Example 2:
 *
 * Input: S = "aaab"
 * Output: ""
 * Note:
 *
 * S will consist of lowercase letters and have length in range [1, 500].
 * </pre>
 *
 * @author navyd
 */
@Unskilled
@Problem(number = 767, difficulty = Difficulty.MEDIUM, tags = Tag.SORT, url = "https://leetcode.com/problems/reorganize-string/")
public interface ReorganizeString {
  /**
   * 将字符串S重新排列，使相邻字符不是在重复的字符
   * <p>复杂度：假设S的长度为N，平均每个字符的数量为K
   *
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
  @Solution(timeComplexity = Complexity.O_N_LOG_K, spaceComplexity = Complexity.O_N)
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
     * <p>时间复杂度：
     * 重排需要循环次N次，每次队列操作为log K。即O(N log K)
     * <p>空间复杂度：将S转为char[], stringbuilder需要O(N)，queue和map常数26
     */
    @Override
    public String reorganizeString(String S) {
      // 统计重复次数
      final Map<Character, Integer> counts = new HashMap<>();
      for (char c : S.toCharArray()) {
        counts.put(c, counts.getOrDefault(c, 0) + 1);
      }
      // 构建priority queue
      final Queue<Map.Entry<Character, Integer>> queue =
          new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
      queue.addAll(counts.entrySet());
      // 重排
      final StringBuilder reorganized = new StringBuilder();
      while (!queue.isEmpty()) {
        final Map.Entry<Character, Integer> curEntry = queue.poll();
        final int length = reorganized.length();
        // 当前字符与重排最后字符不同
        if (length == 0 || reorganized.charAt(length - 1) != curEntry.getKey()) {
          reorganized.append(curEntry.getKey());
          curEntry.setValue(curEntry.getValue() - 1);
        }
        // 相同
        else {
          // 获取下一个
          Map.Entry<Character, Integer> nextEntry = queue.poll();
          // 如果不存在下一个，无法重排
          if (nextEntry == null) {
            return "";
          }
          reorganized.append(nextEntry.getKey());
          nextEntry.setValue(nextEntry.getValue() - 1);
          // 下一个字符没有用完
          if (nextEntry.getValue() > 0) {
            queue.offer(nextEntry);
          }
        }
        // 当前字符没有用完
        if (curEntry.getValue() > 0) {
          queue.offer(curEntry);
        }
      }
      return reorganized.toString();
    }
  }

  @DerivedFrom(SolutionByPriorityQueue.class)
  @Author(name = "FionaFang", significant = true,
      referenceUrls = "https://leetcode.com/problems/reorganize-string/discuss/128907/Java-solution-99-similar-to-358")
  @Submission(date = "2019-05-28", runtime = 35, runtimeBeatRate = 38.92, memory = 35.2,
      memoryBeatRate = 92.10, url = "https://leetcode.com/submissions/detail/231887633/")
  @Solution(timeComplexity = Complexity.O_N_LOG_K, spaceComplexity = Complexity.O_N)
  public static class SolutionByPriorityQueueII implements ReorganizeString {

    /**
     * 与{@link SolutionByPriorityQueue}主要不同在于在重排时，
     * 避免在最后一个字符相同时，避免再次poll,offer操作。
     * <p>重排时优先队列两次poll必然是不同的字符，不再需要判断是否与之前的一致
     * <p>使用引用prev保存之前poll的值，交替发送到stringbuiler
     * <p>交替-1不会导致相同的字符被重复poll发送到结果中
     * <p>如果不是可重排的，则queue 重复的字符prev不会被offer，
     * 提前为空，导致prev的剩余字符数量不会被发送到结果中
     * <p>时间复杂度：priority queue的offer poll操作为 O(log K)，重排需要重复N次，则为O(N log K)
     * <p>
     * 空间复杂度：queue, map都是固定空间26，S toCharArray与结果stringbuilder需要O(N)的空间
     */
    @Override
    public String reorganizeString(String S) {
      final int countLimit = 26;
      // 统计
      final Map<Character, Integer> charCounts = new HashMap<>(countLimit);
      for (char c : S.toCharArray()) {
        charCounts.put(c, charCounts.getOrDefault(c, 0) + 1);
      }
      // 构建priory queue
      final Queue<Map.Entry<Character, Integer>> queue =
          new PriorityQueue<>((a, b) -> b.getValue() - a.getValue());
      queue.addAll(charCounts.entrySet());
      // 重排
      final StringBuilder reorganized = new StringBuilder();
      // 交替poll
      Map.Entry<Character, Integer> prev = null;
      while (!queue.isEmpty()) {
        Map.Entry<Character, Integer> cur = queue.poll();
        // 将上一个字符重入队列
        if (prev != null && prev.getValue() > 0) {
          queue.offer(prev);
        }
        reorganized.append(cur.getKey());
        cur.setValue(cur.getValue() - 1);
        prev = cur;
      }
      // 如果不是可重排的 字符数量与输入不一致
      return (reorganized.length() == S.length()) ? reorganized.toString() : "";
    }
  }

  @Optimal
  @Author(name = "fangbiyi", significant = true,
      referenceUrls = "https://leetcode.com/problems/reorganize-string/discuss/232469/Java-O(N)-3ms-beat-100")
  @Submission(date = "2019-05-28", runtime = 1, runtimeBeatRate = 96.72, memory = 33.8,
      memoryBeatRate = 100.00, url = "https://leetcode.com/submissions/detail/231903207/")
  @Solution(timeComplexity = Complexity.O_N, spaceComplexity = Complexity.O_N)
  public static class SolutionByCountingSort implements ReorganizeString {

    /**
     * 思路：使用计数排序统计每个字符出现的次数，重排时不依赖Priority queue，优先将重复次数最多的字符
     * 间隔的插入结果字符串中。然后将其余字符也间隔插入剩下的位置中，如果超过了数组长度，则从头开始插入那些间隔
     * <p>即重排时，间隔的插入大的字符，保证相邻的不会重复，然后将其他字符也间隔插入即可
     * <p>时间复杂度：遍历counts常数时间，S字符遍历O(N)，重排时counts常数时间，每次遍历K次，故最大为S遍历O(N)
     * <p>空间复杂度：S的输出结果数组O(N)
     */
    @Override
    public String reorganizeString(String S) {
      // 统计计数
      final int n = S.length();
      final int[] counts = new int[26];
      for (char c : S.toCharArray()) {
        counts[index(c)]++;
      }
      // 找到重复次数最多的字符
      char mostLetter = letter(0);
      final int letterCountLimit = (S.length() + 1) / 2;
      for (char i = 0; i < counts.length; i++) {
        if (counts[index(mostLetter)] < counts[i]) {
          mostLetter = letter(i);
        }
      }
      // 检查数量是否合法
      if (counts[index(mostLetter)] > letterCountLimit) {
        return "";
      }
      // 重排 最多的
      final char[] reorganized = new char[n];
      // 从0开始
      int intervalIndex = 0;
      while (counts[index(mostLetter)]-- > 0) {
        reorganized[intervalIndex] = mostLetter;
        intervalIndex += 2;
      }
      // 重排 剩余的
      for (int i = 0; i < counts.length; i++) {
        while (counts[i]-- > 0) {
          // 如果超过　则从１开始 不能使用%n，因为10%10=0，从0开始了
          if (intervalIndex >= n) {
            intervalIndex = 1;
          }
          reorganized[intervalIndex] = letter(i);
          intervalIndex += 2;
        }
      }
      return String.valueOf(reorganized);
    }

    static char letter(int index) {
      return (char) (index + 'a');
    }

    static int index(char c) {
      return c - 'a';
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
     * Input:"vvvlo"
     * Output:"lvvvo"
     * Expected:"vlvov"
     * </pre>
     */
    @Override
    public String reorganizeString(String S) {
      // 排序
      final char[] chars = S.toCharArray();
      Arrays.sort(chars);
      // 检查是否合法
      if (!isEligible(chars)) {
        return "";
      }
      // 重排
      for (int i = 1, j = chars.length - 1; i <= j; i += 2, j -= 2) {
        swap(chars, i, j);
      }
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
        if (c == checkingChar) {
          count++;
        }
        // 检查不合法
        else if (count > limit) {
          return false;
        }
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
