package cn.navyd.leetcode.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.DifficultyEnum;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
You are given an integer array nums and you have to return a new counts array. The counts array has the property where counts[i] is the number of smaller elements to the right of nums[i].

Example:

Input: [5,2,6,1]
Output: [2,1,1,0] 
Explanation:
To the right of 5 there are 2 smaller elements (2 and 1).
To the right of 2 there is only 1 smaller element (1).
To the right of 6 there is 1 smaller element (1).
To the right of 1 there is 0 smaller element.
 * </pre>
 * 
 * @author navyd
 *
 */
@Problem(number = 315, difficulty = DifficultyEnum.HARD, url = "https://leetcode.com/problems/count-of-smaller-numbers-after-self/")
public interface CountOfSmallerNumbersAfterSelf {
  public List<Integer> countSmaller(int[] nums);

  @Author(value = "danyfang7",
      references = "https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/76583/11ms-JAVA-solution-using-merge-sort-with-explanation/143754")
  @Author(value = "lzyfriday",
      references = "https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/76583/11ms-JAVA-solution-using-merge-sort-with-explanation")
  @Submission(submittedDate = @DateTime("20190629"), memory = 39.1, memoryBeatRate = 89.77, runtime = 5,
      runtimeBeatRate = 90.75, url = "https://leetcode.com/submissions/detail/239393643/")
  public static class SolutionByMergeSort implements CountOfSmallerNumbersAfterSelf {

    /**
     * 思路：
     * <p>排序，只需要记录num位置在排序后的对应位置即可。如果排序nums的下标，而不是nums数组，则不需要找num在nums中的位置。
     * <p>假设要合并两个有序数组left[], right[]到一个数组中，使用一个变量count记录从right[]移动到新数组中的数量，同时使用counts[]记录
     * 对应left num的对应的count，即counts就是smaller count结果
     * <p>算法：
     * <p>对nums的indexes做归并排序。归并移动right时记录counts数组，在归并完成后写入新排序的indexes
     * <p>count变量记录的是在有序数组right[], left[]归并时当right被移动到left前时的right num的数量，
     * 即记录nums中小于指定num的数量。
     * <p>复杂度：
     * <p>时间复杂度：归并排序为O(log N)，在merge中遍历start--end。遍历counts，nums为O(N)即为O(N)
     * <p>空间复杂度：merge中创建了一个数组，长度为start-end+1，平均长度为N/2，归并总空间为O(N/2 * log N)即O(N log N)
     */
    @Override
    public List<Integer> countSmaller(int[] nums) {
      if (nums == null || nums.length == 0)
        return Collections.emptyList();
      if (nums.length == 1)
        return Arrays.asList(0);
      // 初始化 构造counts indexes数组
      final int[] 
          indexes = new int[nums.length], 
          counts = new int[nums.length];
      for (int i = 0; i < nums.length; i++)
        indexes[i] = i;
      
      // 归并排序 生成counts
      mergeSort(nums, indexes, 0, nums.length - 1, counts);
      // 结果
      List<Integer> result = new ArrayList<>(nums.length);
      for (int count : counts)
        result.add(count);
      return result;
    }

    /**
     * 归并排序。将nums排序使的indexes在nums的下标有序
     */
    private void mergeSort(int[] nums, int[] indexes, int start, int end, int[] counts) {
      if (start >= end)
        return;
      int mid = (start + end) / 2;
      mergeSort(nums, indexes, start, mid, counts);
      mergeSort(nums, indexes, mid + 1, end, counts);
      merge(nums, indexes, start, mid, end, counts);
    }

    /**
     * 合并两个数组
     * <p>
     * 对于右边的 i <= end时，如果是一开始就是最大的，则count就为0，不需要记录count。 如果不是最大的，但是在归并后成了最大的，之前将保存了对应下标的count
     */
    private void merge(int[] nums, int[] indexes, int start, int mid, int end, int[] counts) {
      // i为左边开始，j为右边开始，k迭代newIndexes
      int i = start, j = mid + 1, k = 0;
      // 记录归并时left num小于的数量
      int count = 0;
      // 临时的新有序的索引
      final int[] newIndexes = new int[end - start + 1];
      // 归并
      while (i <= mid && j <= end) {
        // 如果右边num < 左边num
        if (nums[indexes[j]] < nums[indexes[i]]) {
          // 右边 排到前面
          newIndexes[k++] = indexes[j++];
          count++;
        } else {
          // 统计 被移动的左边num的count
          counts[indexes[i]] += count;
          // 左边排到前面
          newIndexes[k++] = indexes[i++];
        }
      }
      // 左边的都大于 排序数组最大的
      while (i <= mid) {
        counts[indexes[i]] += count;
        newIndexes[k++] = indexes[i++];
      }
      // 右边的
      while (j <= end) {
        newIndexes[k++] = indexes[j++];
      }
      // 保存排序下标
      for (int h = start; h <= end; h++)
        indexes[h] = newIndexes[h - start];
    }
  }

  // 动画详解
  @Author(value = "liweiwei1419", references = "https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/solution/gui-bing-pai-xu-suo-yin-shu-zu-python-dai-ma-java-/")
  // 下标
  @Author(value = "EdickCoding", references = "https://leetcode.com/problems/count-of-smaller-numbers-after-self/discuss/76584/Mergesort-solution/80276")
  @Submission(memory = 41.3, memoryBeatRate = 8.33, runtime = 4, runtimeBeatRate = 91.63, submittedDate = @DateTime("20200510"), url = "https://leetcode.com/submissions/detail/337082785/")
  public class SolutionByMerge implements CountOfSmallerNumbersAfterSelf {
    /**
     * <p>如何在排序中计算某个元素的右边元素个数？
     * <p>如果两个数组left,right分别有序，在合并时只要计算right移动到left前的+1就可。即当
     * right移动到前面时，此时left元素只要计好移动过来的数量，对每个left元素移动时，其小于的数量为之前count+这次的count。
     * 计数与下标的关系：我们用count记录right移动的次数，但是right++下标刚好是count的次数=right - (mid + 1)
     * 替换count => counts[i]+=right - (mid + 1)，当right比left都移完时，count=hi-mid=right-(mid+1) right=hi+1不变
     * <pre>
     *            5,2,6,1
     * =>         5,2  6,1
     * =>         5 | 2
     * => loop0:left 5 > right 2                                      idx_l=0 idx_r=0
     * =>       count=1 temp_arr:[2, null]
     * => loop1:counts[0](5)+=count = 1 temp_arr:[2, 5]               idx_l=0 idx_r=1
     * 
     * =>         6 | 1
     * => loop0:left 6 > right 1                                      idx_l=0 idx_r=0
     * =>       count=1 temp_arr:[1, null]
     * => loop1: counts[2](6)+=count = 1 temp_arr:[1, 6]              idx_l=0 idx_r=1
     * 
     * =>         2,5 | 1,6
     * => loop0:left 2 > right 1                                      idx_l=0 idx_r=0
     * =>       count=1 temp_arr:[1,null,null,null]
     * => loop1:left 2 < right 6                                      idx_l=0 idx_r=1
     * =>       counts[1](2)+=count = 1 temp_arr:[1,2,null,null]
     * => loop2:left 5 < right 6                                      idx_l=1 idx_r=1
     * =>       counts[0](5)+=count = 2 temp_arr:[1,2,5,null]
     * => loop3:right 6                                               idx_l=2 idx_r=1
     * => the right side is the largest, we dont need to count
     * </pre>
     * <p>如何定位元素
     * 由于在排序数组时不能保证找到同一个元素，在排序后要能定位，必须保留num对应的index，可通过
     * index排序保证nums不修改一样即nums[indexes[i]]
     */
    @Override
    public List<Integer> countSmaller(int[] nums) {
      final int n = nums.length;
      List<Integer> res = new ArrayList<>(n);
      if (n == 0)
        return res;
      if (n == 1) {
        res.add(0);
        return res;
      }
      // counts indexes
      final int[] counts = new int[n], indexes = new int[n], auxIndexes = new int[n];
      for (int i = 0; i < n; i++)
        indexes[i] = i;
      mergeSort(nums, counts, indexes, auxIndexes, 0, n - 1);
      for (int c : counts)
        res.add(c);
      return res;
    }

    static void mergeSort(int[] nums, int[] counts, int[] indexes, int[] auxIndexes, int lo, int hi) {
      // recursion terminated
      if (lo >= hi)
        return;
      // left
      int mid = lo + (hi - lo) / 2;
      mergeSort(nums, counts, indexes, auxIndexes, lo, mid);
      // right
      mergeSort(nums, counts, indexes, auxIndexes, mid + 1, hi);
      // merge from 1 to all
      mergeWithIdx(nums, counts, indexes, auxIndexes, lo, mid, hi);
    }

    static void mergeWithCount(int[] nums, int[] counts, int[] indexes, int[] auxIndexes, int lo, int mid, int hi) {
      for (int i = lo; i <= hi; i++)
        auxIndexes[i] = indexes[i];
      // compares left and right
      int left = lo, right = mid + 1, i = lo;
      int count = 0;
      while (left <= mid && right <= hi) {
        // not move right for stable equals
        // right side move
        if (nums[auxIndexes[left]] > nums[auxIndexes[right]]) {
          indexes[i++] = auxIndexes[right++];
          count++;
        } else {
          // left side move
          counts[auxIndexes[left]] += count;
          indexes[i++] = auxIndexes[left++];
        }
      }
      // rest of the right
      while (right <= hi)
        indexes[i++] = auxIndexes[right++];
      // rest of the left
      while (left <= mid) {
        counts[auxIndexes[left]] += count;
        indexes[i++] = auxIndexes[left++];
      }
    }

    static void mergeWithIdx(int[] nums, int[] counts, int[] indexes, int[] auxIndexes, int lo, int mid, int hi) {
      for (int i = lo; i <= hi; i++)
        auxIndexes[i] = indexes[i];
      // compares left and right
      int left = lo, right = mid + 1, i = lo;
      while (left <= mid && right <= hi) {
        // not move right for stable equals
        // right side move
        if (nums[auxIndexes[left]] > nums[auxIndexes[right]]) {
          indexes[i++] = auxIndexes[right++];
        } else {
          // left side move
          counts[auxIndexes[left]] += right - (mid + 1);
          indexes[i++] = auxIndexes[left++];
        }
      }
      // rest of the right
      while (right <= hi)
        indexes[i++] = auxIndexes[right++];
      // rest of the left
      while (left <= mid) {
        counts[auxIndexes[left]] += right - (mid + 1);
        indexes[i++] = auxIndexes[left++];
      }
    }
  }

  @Author(value = "liweiwei1419", references = "https://leetcode-cn.com/problems/count-of-smaller-numbers-after-self/solution/shu-zhuang-shu-zu-by-liweiwei1419/")
  @Submission(memory = 42.2, memoryBeatRate = 5.55, runtime = 10, runtimeBeatRate = 55.63, submittedDate = @DateTime("20200513"), url = "https://leetcode.com/submissions/detail/338657142/")
  public static class SolutionByFenwickTree implements CountOfSmallerNumbersAfterSelf {
    /**
     * 思路：使用FenwickTree树状数组计算每个元素的前缀和。
     * <p>FenwickTree如何计算nums数小的数量？
     * 
     * 由于tree的特殊性，让nums放在tree中可以快速计算指定元素前的所有元素之和。只要
     * 将nums从后往前入tree，就可以得到当前元素右边小的元素之和。
     * 
     * tree不能存储nums的值，因为要求的是数量个数。考虑到nums可能有重复值出现，
     * 而题意只要求小的数，那么重复的数就不应该占多余空间，直接存储nums的下标是会
     * 浪费空间的。tree应该存储的是去重后的set的下标与出现的次数。
     * 
     * <p>set与次数在tree如何工作？
     * 
     * 当从后往前nums时，将set下标rank和次数1更新到tree中，然后找比当前num小的数rank-1
     * 以前的所有和（出现次数）。如果nums遇到相同的num，set的下标rank是一样的，但更新到
     * tree时update rank,1使出现次数+1，可被统计到小于元素中
     * 
     * <p>如何找到set与次数？
     * 
     * 用TreeSet去重和排序，再遍历set时可用map关联 num,rank。这个叫离散化，将元素的值
     * 用相对排名处理，只关心大小顺序
     * 
     * 在后面遍历nums构造tree时，可通过num与map查到出同次数
     * 
     * <p>为何FenwickTree(set.size+1)？
     * 用的是set中的下标作为tree，由于tree数组0下标不用，则多加一个
     * 
     * <p>时间复杂度：FenwickTree.update getsum是logN，即为O(NlogN)
     * <p>空间：O(N)
     */
    @Override
    public List<Integer> countSmaller(int[] nums) {
      List<Integer> res = new ArrayList<>(nums.length);
      if (nums.length == 0)
        return res;
      if (nums.length == 1) {
        res.add(0);
        return res;
      }
      // 离散化 计算nums的排名ranks 减少tree空间
      Set<Integer> set = new TreeSet<>();
      // set去重 排序
      for (int num : nums) {
        set.add(num);
      }
      Map<Integer, Integer> ranks = new HashMap<>();
      int count = 1;
      for (Integer n : set) {
        ranks.put(n, count++);
      }
      // 从后往前更新线段树
      FenwickTree tree = new FenwickTree(set.size() + 1);
      for (int i = nums.length - 1; i >= 0; i--) {
        int rank = ranks.get(nums[i]);
        tree.update(rank, 1);
        res.add(tree.getSum(rank-1));
      }
      Collections.reverse(res);
      return res;
    }

    private static class FenwickTree {
      private final int[] bits;
      private final int length;

      public FenwickTree(int length) {
        this.length = length;
        this.bits = new int[length + 1];
      }

      public void update(int i, int val) {
        while (i <= length) {
          bits[i] += val;
          i += lowbit(i);
        }
      }

      public int getSum(int i) {
        int sum = 0;
        while (i > 0) {
          sum += bits[i];
          i -= lowbit(i);
        }
        return sum;
      }

      private int lowbit(int i) {
        return i&(-i);
      }
    }
  }

  

  public static void main(String[] args) {
    int[] a = {5, 2, 6, 1};
    CountOfSmallerNumbersAfterSelf test = new SolutionByFenwickTree();
    System.out.println("count: " + test.countSmaller(a));
  }
}
