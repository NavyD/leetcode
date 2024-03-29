package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cn.navyd.annotation.algorithm.ComplexityEnum;
import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.DifficultyEnum;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Tag;
import cn.navyd.annotation.leetcode.TagEnum;

/**
 * <pre>
 * Given two arrays, write a function to compute their intersection.
 *
 * Example 1:
 *
 * Input: nums1 = [1,2,2,1], nums2 = [2,2]
 * Output: [2]
 * Example 2:
 *
 * Input: nums1 = [4,9,5], nums2 = [9,4,9,8,4]
 * Output: [9,4]
 * Note:
 *
 * Each element in the result must be unique.
 * The result can be in any order.
 *
 * </pre>
 *
 * @author navyd
 */
@Tag(TagEnum.SORT)
@Problem(difficulty = DifficultyEnum.EASY, number = 349)
public interface IntersectionOfTwoArrays {

  /**
   * <p>
   * 思路
   * <p>
   * 查找：要想找到O(1) hash是首选，注意int hash不会存在冲突出链表，保证是O(1)
   * <p>
   * 去重：如何保证查找到的元素之后不会被add，或元素不会被重复找到
   */
  public int[] intersection(int[] nums1, int[] nums2);

  @Submission(memory = 36.8, memoryBeatRate = 89.19, runtime = 2, runtimeBeatRate = 97.05, submittedDate = @DateTime("20191121"), url = "https://leetcode.com/submissions/detail/280475605/")
  @Author(value = "divingboy89", references = "https://leetcode.com/problems/intersection-of-two-arrays/discuss/81969/Three-Java-Solutions")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_N)
  public static class SolutionByHashMap implements IntersectionOfTwoArrays {

    /**
     * <p>
     * 查找：要想找到O(1) hash是首选，注意int hash不会存在冲突出链表，保证是O(1)
     * <p>
     * 去重：hashmap每个键映射boolean，保证一次add
     * <p>复杂度
     * <ul>
     * <li>时间：int类型在hashset中不会出现碰撞，即保证hash查找O(1)，最大的迭代为O(N)</li>
     * <li>空间：创建hashset, 结果数组intersectionNums, copy返回均为O(N)</li>
     * </ul>
     */
    @Override
    public int[] intersection(int[] nums1, int[] nums2) {
      // 查找大小数组
      final int[] maxNums = nums1.length > nums2.length ? nums1 : nums2, minNums =
          maxNums != nums1 ? nums1 : nums2,
          // 交集最多为minNums
          intersectionNums = new int[minNums.length];
      // 1. hash maxNums
      final Map<Integer, Boolean> nums = new HashMap<>(maxNums.length);
      for (int n : maxNums) {
        nums.put(n, true);
      }
      int idx = 0;
      // 2. 在hash中查找minNums
      for (int n : minNums) {
        Boolean isExists = nums.get(n);
        // 3. 找到则入结果
        if (isExists != null && isExists) {
          intersectionNums[idx++] = n;
          nums.put(n, false);
        }
      }
      return Arrays.copyOf(intersectionNums, idx);
    }

  }

  @Author(value = "divingboy89", references = "https://leetcode.com/problems/intersection-of-two-arrays/discuss/81969/Three-Java-Solutions")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_N)
  @Submission(memory = 37.4, memoryBeatRate = 71.62, runtime = 1, runtimeBeatRate = 99.73, submittedDate = @DateTime("20191114"), url = "https://leetcode.com/submissions/detail/278607157/")
  public static class SolutionByHashSet implements IntersectionOfTwoArrays {

    /**
     * 思路：
     * <p>
     * 查找：hashset
     * 去重：set.remove()
     * <p>
     * 优化
     * <ul>
     * <li>找到大小数组减小开销 hash max, iterative min => 对于该int无效 但对于Object类型的hash
     * 重复能减少时间:O(min log Max)</li>
     * <li>@{@link System#arraycopy(Object, int, Object, int, int)}
     * </ul>
     * <p>
     * 复杂度
     * <ul>
     * <li>时间：int类型在hashset中不会出现碰撞，即保证hash查找O(1)，最大的迭代为O(N)</li>
     * <li>空间：创建hashset, 结果数组intersectionNums, copy返回均为O(N)</li>
     * </ul>
     */
    @Override
    public int[] intersection(int[] nums1, int[] nums2) {
      // 大小数组
      final int[] maxNums = nums1.length > nums2.length ? nums1 : nums2, minNums =
          maxNums != nums1 ? nums1 : nums2,
          intersectionNums = new int[minNums.length];
      // 1. hash maxNums
      final Set<Integer> nums = new HashSet<>(maxNums.length);
      for (int n : maxNums) {
        nums.add(n);
      }
      int idx = 0;
      // 2. 在hash中查找minNums
      for (int n : minNums)
      // 3. 查找并移除 防止重复查找
      {
        if (nums.remove(n)) {
          intersectionNums[idx++] = n;
        }
      }
      return Arrays.copyOfRange(intersectionNums, 0, idx);
    }
  }

  @Author(value = "divingboy89", references = "https://leetcode.com/problems/intersection-of-two-arrays/discuss/81969/Three-Java-Solutions")
  @Submission(memory = 36.4, memoryBeatRate = 90.54, runtime = 2, runtimeBeatRate = 97.13, submittedDate = @DateTime("20191118"), url = "https://leetcode.com/submissions/detail/279691075/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_N), spaceComplexity = ComplexityEnum.O_N)
  public static class SolutionBySortBinarySearch implements IntersectionOfTwoArrays {

    /**
     * 思路
     * <ul>
     * <li>查找：对有序数组使用binary search
     * <li>去重：结果保存为set，允许重复查找
     * </ul>
     * <p>
     * 复杂度：设大数组为N，小数组为K
     * <ul>
     * <li>时间：快排max nums为O(N log N)，min nums数组对max nums binary search为O(K log
     * N)，结果遍历O(K)。即O(N log N)
     * <li>空间：O(N)
     * </ul>
     * <p>
     * 优化
     * <ul>
     * <li>快排max nums比min nums O(K log K)更好：binary search时为O(N log K)较慢
     * </ul>
     */
    @Override
    public int[] intersection(int[] nums1, int[] nums2) {
      // 0. find min and max array
      final int[] maxNums = nums1.length > nums2.length ? nums1 : nums2, minNums =
          maxNums != nums1 ? nums1 : nums2;
      // 1. sort max array
      Arrays.sort(maxNums);
      final Set<Integer> intersection = new HashSet<>();
      // 2. max elements binary search in max array
      for (int n : minNums)
      // 3. to add res array
      {
        if (Arrays.binarySearch(maxNums, n) > -1) {
          intersection.add(n);
        }
      }
      final int[] res = new int[intersection.size()];
      int i = 0;
      for (int n : intersection) {
        res[i++] = n;
      }
      return res;
    }

  }

  @Submission(memory = 37.1, memoryBeatRate = 89.19, runtime = 2, runtimeBeatRate = 97.08, submittedDate = @DateTime("20191120"), url = "https://leetcode.com/submissions/detail/280205567/")
  @Author(value = "divingboy89", references = "https://leetcode.com/problems/intersection-of-two-arrays/discuss/81969/Three-Java-Solutions")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_N), spaceComplexity = ComplexityEnum.O_N)
  public static class SolutionBySortTwoPointers implements IntersectionOfTwoArrays {

    /**
     * 思路
     * <ul>
     * <li>查找：对两个有序数组使用two pinter比较查找
     * <li>去重：结果保存为set，允许重复查找
     * </ul>
     * 复杂度：设大数组为N，小数组为K
     * <ul>
     * <li>时间：快排nums1, nums2为O(N log N)，two pinter查找最差的情况是nums1与nums1刚对应位置刚好错开，为O(N+K)。
     * <li>空间：仅结果占用O(N)
     * </ul>
     */
    @Override
    public int[] intersection(int[] nums1, int[] nums2) {
      // 0. sort nums1 and nums2
      Arrays.sort(nums1);
      Arrays.sort(nums2);
      // 1. find iteritive with two pointers
      int i = 0, j = 0;
      final Set<Integer> intersection = new HashSet<>(Math.min(nums1.length, nums2.length));
      while (i < nums1.length && j < nums2.length) {
        if (nums1[i] < nums2[j]) {
          i++;
        } else if (nums1[i] > nums2[j]) {
          j++;
        } else {
          intersection.add(nums1[i]);
          i++;
          j++;
        }
      }
      // 2. to copy result
      final int[] res = new int[intersection.size()];
      int k = 0;
      for (int n : intersection) {
        res[k++] = n;
      }
      return res;
    }
  }
}
