package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import cn.navyd.annotation.algorithm.ComplexityEnum;
import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
 * In a special ranking system, each voter gives a rank from highest to lowest to all teams participated in the competition.
 *
 * The ordering of teams is decided by who received the most position-one votes. If two or more teams tie in the first position, we consider the second position to resolve the conflict, if they tie again, we continue this process until the ties are resolved. If two or more teams are still tied after considering all positions, we rank them alphabetically based on their team letter.
 *
 * Given an array of strings votes which is the votes of all voters in the ranking systems. Sort all teams according to the ranking system described above.
 *
 * Return a string of all teams sorted by the ranking system.
 *
 *
 *
 * Example 1:
 *
 * Input: votes = ["ABC","ACB","ABC","ACB","ACB"]
 * Output: "ACB"
 * Explanation: Team A was ranked first place by 5 voters. No other team was voted as first place so team A is the first team.
 * Team B was ranked second by 2 voters and was ranked third by 3 voters.
 * Team C was ranked second by 3 voters and was ranked third by 2 voters.
 * As most of the voters ranked C second, team C is the second team and team B is the third.
 * Example 2:
 *
 * Input: votes = ["WXYZ","XYZW"]
 * Output: "XWYZ"
 * Explanation: X is the winner due to tie-breaking rule. X has same votes as W for the first position but X has one vote as second position while W doesn't have any votes as second position.
 * Example 3:
 *
 * Input: votes = ["ZMNAGUEDSJYLBOPHRQICWFXTVK"]
 * Output: "ZMNAGUEDSJYLBOPHRQICWFXTVK"
 * Explanation: Only one voter so his votes are used for the ranking.
 * Example 4:
 *
 * Input: votes = ["BCA","CAB","CBA","ABC","ACB","BAC"]
 * Output: "ABC"
 * Explanation:
 * Team A was ranked first by 2 voters, second by 2 voters and third by 2 voters.
 * Team B was ranked first by 2 voters, second by 2 voters and third by 2 voters.
 * Team C was ranked first by 2 voters, second by 2 voters and third by 2 voters.
 * There is a tie and we rank teams ascending by their IDs.
 * Example 5:
 *
 * Input: votes = ["M","M","M","M"]
 * Output: "M"
 * Explanation: Only team M in the competition so it has the first rank.
 *
 *
 * Constraints:
 *
 * 1 <= votes.length <= 1000
 * 1 <= votes[i].length <= 26
 * votes[i].length == votes[j].length for 0 <= i, j < votes.length.
 * votes[i][j] is an English upper-case letter.
 * All characters of votes[i] are unique.
 * All the characters that occur in votes[0] also occur in votes[j] where 1 <= j < votes.length.
 * </pre>
 */
public interface RankTeamsByVotes {
  public String rankTeams(String[] votes);
}

@Author(value = "ToffeeLu", references = "https://leetcode.com/problems/rank-teams-by-votes/discuss/524853/Java-O(26n%2B(262-*-log26))-Sort-by-high-rank-vote-to-low-rank-vote")
@Submission(memory = 39.6, memoryBeatRate = 100, runtime = 13, runtimeBeatRate = 41.97, submittedDate = @DateTime("20200417"), url = "https://leetcode.com/submissions/detail/325483774/")
@SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_1)
class SolutionBySort implements RankTeamsByVotes {
  /**
   * 如何对team排序：如果在1位置中votes大的则放在前面，相等的比较下一位置如果最后都相等，则用字母顺序
   * <p>如何取得team的position并适用排序：利用二维数组关系ranks[team][position]=[vote]统计team的对应位置的投票数，
   * 对team排序时用[position]=[votes]可比出大小。使用HashMap关联team与position数组减少get时间
   * <p>如何构造ranks数组：由于team数与vote长度一样，遍历votes数组并对每个vote统计其team的position
   * <p>时间复杂度：由于votes[0].length最大是26常数，创建ranks数组时为26N。sort时的comparison是26^2
   * 要进行log26次比较即O(26N + 26^2 log 26) => O(N)
   * <p>空间复杂度：ranks使用的map和int[]都是固定的26*26，teams也是26，即为O(1)
   */
  @Override
  public String rankTeams(String[] votes) {
    // 0. create rank array
    final int posSize = votes[0].length();
    final Map<Character, int[]> ranks = new HashMap<>(posSize);
    for (String v : votes)
    // count vote of position of team by votes
    {
      for (int i = 0; i < v.length(); i++) {
        char team = v.charAt(i);
        ranks.putIfAbsent(team, new int[posSize]);
        ranks.get(team)[i]++;
      }
    }
    // 2. sort with team positions
    Character[] teams = ranks.keySet().toArray(new Character[0]);
    Arrays.sort(teams, (a, b) -> {
      for (int i = 0; i < posSize; i++) {
        // get max
        int cmp = ranks.get(b)[i] - ranks.get(a)[i];
        if (cmp != 0) {
          return cmp;
        }
      }
      return a - b;
    });
    StringBuilder sb = new StringBuilder();
    for (char c : teams) {
      sb.append(c);
    }
    return sb.toString();
  }
}

@Author(value = "3", references = "https://leetcode.com/submissions/api/detail/1483/java/3/")
@Submission(memory = 39.4, memoryBeatRate = 100, runtime = 5, runtimeBeatRate = 82.81, submittedDate = @DateTime("20200417"), url = "https://leetcode.com/submissions/detail/325914227/")
@SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_1)
class SolutionByCounting implements RankTeamsByVotes {
  /**
   * rank[team][position]
   * 与{@link SolutionBySort}不同的一点在于使用数组和offset替代hashmap
   */
  @Override
  public String rankTeams(String[] votes) {
    // 0. create ranks with votes
    final int offset = 'A', teamSize = votes[0].length();
    final int[][] ranks = new int[26][teamSize];
    for (String v : votes) {
      for (int i = 0; i < v.length(); i++) {
        ranks[v.charAt(i) - offset][i]++;
      }
    }
    // 1. sort team by ranks with comparator
    Character[] teams = new Character[teamSize];
    for (int i = 0; i < teamSize; i++) {
      teams[i] = votes[0].charAt(i);
    }
    Arrays.sort(teams, (a, b) -> {
      for (int i = 0; i < teamSize; i++) {
        int cmp = ranks[b - offset][i] - ranks[a - offset][i];
        if (cmp != 0) {
          return cmp;
        }
      }
      return a - b;
    });
    StringBuilder sb = new StringBuilder();
    for (char c : teams) {
      sb.append(c);
    }
    return sb.toString();
  }

}