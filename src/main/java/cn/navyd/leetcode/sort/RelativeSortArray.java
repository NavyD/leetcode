package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
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
Given two arrays arr1 and arr2, the elements of arr2 are distinct, and all elements in arr2 are also in arr1.

Sort the elements of arr1 such that the relative ordering of items in arr1 are the same as in arr2.  Elements that don't appear in arr2 should be placed at the end of arr1 in ascending order.

 

Example 1:

Input: arr1 = [2,3,1,3,2,4,6,7,9,2,19], arr2 = [2,1,4,3,9,6]
Output: [2,2,2,1,4,3,3,9,6,7,19]
 

Constraints:

arr1.length, arr2.length <= 1000
0 <= arr1[i], arr2[i] <= 1000
Each arr2[i] is distinct.
Each arr2[i] is in arr1.
 * </pre>
 * @author navyd
 *
 */
@Tag(TagEnum.SORT)
@Problem(difficulty = DifficultyEnum.EASY, number = 1122)
public interface RelativeSortArray {
  public int[] relativeSortArray(int[] arr1, int[] arr2);

  @Author(value = "gthor10", references = "https://leetcode.com/problems/relative-sort-array/discuss/335217/Java-O(n*lgn)-1-ms-beats-100")
  @Submission(memory = 35.9, memoryBeatRate = 100, runtime = 2, runtimeBeatRate = 69.81, submittedDate = @DateTime("20191018"), url = "https://leetcode.com/submissions/detail/270909428/")
  @SortAlgorithm(
      timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_N, best = ComplexityEnum.O_1, worst = ComplexityEnum.O_N_LOG_N), 
      spaceComplexity = ComplexityEnum.O_N, 
      inputDependency = true)
  public static class SolutionByHash implements RelativeSortArray {

    /**
     * 思想：统计
     * <pre>
     * Process:
     * 
     * 1. create variable counts with hash arr1 and counting 
     * 
     * 2. iterate arr2 for arr1 sorting: count=arr2.element
     *      
     *      while (count-- > 0)
     *          replace into arr1[i++]
     *      update counts.count
     * 
     * 3. merge in arr1 elements only for ascending order
     * 
     *      if counts.count > 0 then replace into arr1[i++] 
     * 4. sort in arr1 elements only
     * </pre>
     * <p>优化
     * <ul>
     * <li>如果arr2中重合的元素在arr1中占比很大，可以考虑使用hashmap，在merge arr1时将in arr1的元素单独
     * 排序。减少前2步的时间至O(1)</li>
     * </ul>
     * <p>复杂度分析：设arr1 N, arr2 K
     * <ul>
     * <li>时间：创建counts hashmap是O(N)，arr2排序O(N)，merge arr1是O(N)。最后的sort当输入仅在arr1的元素过多时为O(N log N)，最好O(1)。即O(N log N)
     * <li>空间：counts hashmap O(N)
     * </ul>
     */
    @Override
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
      // 1.create counts and counting by hash arr1
      final Map<Integer, Integer> counts = new HashMap<>();
      for (int n : arr1)
        counts.put(n, counts.getOrDefault(n, 0) + 1);
      // arr1 idx for sorting
      int i = 0;
      // 2.sorting by arr2
      for (int n : arr2) {
        int count = counts.get(n);
        while (count-- > 0)
          arr1[i++] = n;
        counts.put(n, count);
      }
      final int sortIdx = i;
      // 3.merge arr1
      for (Map.Entry<Integer, Integer> e : counts.entrySet()) {
        int count = e.getValue();
        while (count-- > 0)
          arr1[i++] = e.getKey();
      }
      // 4. sort sortIdx...
      Arrays.sort(arr1, sortIdx, arr1.length);
      return arr1;
    }
    
    
    public int[] relativeSortArray1(int[] arr1, int[] arr2) {
      Map<Integer, Integer> map = new HashMap<>(arr2.length);
      // 1.arr2 作为key 入hash
      for (int i = 0; i < arr2.length; i++)
        map.put(arr2[i], 0);
      // 2.统计arr1 key次数
      for (int i = 0; i < arr1.length; i++)
        map.put(arr1[i], map.getOrDefault(arr1[i], 0)+1);
      // 3.以arr2模板对arr1 sorting
      
      // 4.处理arr1中不存在的key
      
      return arr1;
    }
  }
  
  @Submission(memory = 36.1, memoryBeatRate = 100, runtime = 0, runtimeBeatRate = 100, submittedDate = @DateTime("20191018"), url = "https://leetcode.com/submissions/detail/270857630/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N), spaceComplexity = ComplexityEnum.O_1, inplace = true)
  public static class SolutionByCounting implements RelativeSortArray {

    /**
     * 思想：统计，计数排序，下标有序
     * <pre>
     * 1. create counts[1001] and counting for arr1
     * 
     * 2. sorting for arr2 iterative
     * 
     * 3. merge arr1 by in arr1 elments only
     * </pre>
     */
    @Override
    public int[] relativeSortArray(int[] arr1, int[] arr2) {
      // 1. counts[1001]
      final int[] counts = new int[1001];
      for (int n : arr1)
        counts[n]++;
      int i = 0;
      // 2. sorting
      for (int n : arr2)
        while (counts[n]-- > 0)
          arr1[i++] = n;
      // 3. merge
      for (int n = 0; n < counts.length; n++)
        while (counts[n]-- > 0)
          arr1[i++] = n;
      return arr1;
    }
    
  }
}
