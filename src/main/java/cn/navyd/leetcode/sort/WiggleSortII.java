package cn.navyd.leetcode.sort;

import java.util.Arrays;
import java.util.Random;

import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.DifficultyEnum;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Submission;

/**
<pre>
Given an unsorted array nums, reorder it such that nums[0] < nums[1] > nums[2] < nums[3]....

Example 1:

Input: nums = [1, 5, 1, 1, 6, 4]
Output: One possible answer is [1, 4, 1, 5, 1, 6].
Example 2:

Input: nums = [1, 3, 2, 2, 3, 1]
Output: One possible answer is [2, 3, 1, 3, 1, 2].
Note:
You may assume all input has valid answer.

Follow Up:
Can you do it in O(n) time and/or in-place with O(1) extra space?
</pre>
 *
 */
@Problem(number = 324, difficulty = DifficultyEnum.MEDIUM)
public interface WiggleSortII {
  /**
   * wiggle sorted：任意的odd（下标）元素大于其两个even neighbor元素
   * <p>基本思路：
   * 
   * 将数据分为odd,even两组保存对应的元素，任意的odd元素大于其两个even neighbor，该关系仅为局部适用，
   * 任意的odd元素大于even元素是不能直接全局成立的。
   * 
   * <p>尝试构建全局关系，如果所有的odd元素都大于所有的even元素则全局成立
   * 
   * <p>由于nums.length不一定为偶数，必须保证even group的长度为(n+1)/2，因为少了一个even会导致连续的odd元素，
   * 但是少了一个odd不影响，即even的长度可能大于odd group
   * 
   * <p>注意对于可能存在相等元素，必须错开odd even下标位置 否则将导致连续的等值元素
   * 
   * @param nums
   */
  public void wiggleSort(int[] nums);

 




public static class SolutionBy implements WiggleSortII {
  @Override
  public void wiggleSort(int[] nums) {
    // sort 
    Arrays.sort(nums);
    //  index of big nums
    final int mid = (nums.length/2);
    // put position
    int i = nums.length - 1, j = -1;
    while (i >= mid) 
      swap(nums, i--, j+=2);
  }

  static void swap(int[] nums, int i, int j) {
    int t = nums[i];
    nums[i] = nums[j];
    nums[j] = t;
  }
}


public static void main(String[] args) {
  WiggleSortII p = new SolutionBy();
  int[] nums = {1,5,1,1,6,4};
  p.wiggleSort(nums);
  
}
























  
  @Author(value = "fun4LeetCode",  
      references = "https://leetcode.com/problems/wiggle-sort-ii/discuss/77684/Summary-of-the-various-solutions-to-Wiggle-Sort-for-your-reference")
  @Submission(submittedDate = @DateTime("20190504"), runtime = 3, runtimeBeatRate = 99.76, memory = 42.2,
      memoryBeatRate = 27.27, url = "https://leetcode.com/submissions/detail/226655576/")
  public static class SolutionBySort implements WiggleSortII {

    /**
     * 思路：
     * 辅助数组aux排序后使用middle将nums分为odd, even group两部分。该middle值依赖输入，如果输入合法，则middle合法，否则需要在数组中找到何时的middle
     * 将aux数组前段部分[0-mid)小的数 移动到nums的even部分，[mid-n)大的数移动到nums的odd部分
     * 注意，在移动时应该从注意相等的元素不能在一起，遍历时注意分离
     */
    @Override
    public void wiggleSort(int[] nums) {
      // mid为n+1/2保证移动even group时[0-mid)的元素比[mid-n)更多
      final int n = nums.length, mid = (n + 1) / 2;
      int[] aux = Arrays.copyOf(nums, n);
      Arrays.sort(aux);

      // 将[0-mid)的小元素移动到even group
      for (int e = 0, o = mid - 1; o >= 0; o--, e += 2)
        nums[e] = aux[o];
      // 将[mid-n)大的元素移动到odd group
      for (int e = n - 1, o = 1; e >= mid; e--, o += 2)
        nums[o] = aux[e];
    }
  }
  
  @Author(value = "thallam",
      references = "https://leetcode.com/problems/wiggle-sort-ii/discuss/77682/Step-by-step-explanation-of-index-mapping-in-Java/254983")
  @Author(value = "shuoshankou", 
      references = "https://leetcode.com/problems/wiggle-sort-ii/discuss/77682/Step-by-step-explanation-of-index-mapping-in-Java")
  @Submission(submittedDate = @DateTime("20190504"), runtime = 4, runtimeBeatRate = 69.32, memory = 41,
      memoryBeatRate = 56.82, url = "https://leetcode.com/submissions/detail/226646664/")
  public static class SolutionBySortII implements WiggleSortII {

    /**
     * 该方法与{@link SolutionBySort}一致，仅改变两个循环到一个循环，多使用了两个下标
     */
    @Override
    public void wiggleSort(int[] nums) {
      final int n = nums.length;
      int[] copy = Arrays.copyOf(nums, n);
      Arrays.sort(copy);
      
      // left为虚拟的even group下标
      int left = (n + 1)/2 - 1, right = n - 1;
      for (int i = 0; i < n; i++) {
        // 移动到odd group
        if (isOdd(i))
          nums[i] = copy[right--];
        // 移动到even group
        else
          nums[i] = copy[left--];
      }
    }
    private static boolean isOdd(int num) {
      return (num & 1) == 1;
    }
  }
  
  @Author(value = "fun4LeetCode", 
      references = "https://leetcode.com/problems/wiggle-sort-ii/discuss/77684/Summary-of-the-various-solutions-to-Wiggle-Sort-for-your-reference")
  @Submission(submittedDate = @DateTime("20190509"), runtime = 70, runtimeBeatRate = 23.57, memory = 41,
      memoryBeatRate = 55.11, url = "https://leetcode.com/submissions/detail/227709646/")
  public static class SolutionByMedianPartition implements WiggleSortII {
    /**
     * 使用一个中值median将nums划分两部分。
     * <p>使用{@link KthLargestElementinanArray#findKthLargest(int[], int)}寻找中间值
     * <p>对辅助数组aux划分s group（odd下标）左边元素<median，l group（even下标）右边元素>median。数组大小s.length >= l.length
     * <p>读取aux左边元素到nums odd下标，右边到even下标 
     * <p>对于parition 可能存在相等元素，可能导致正常的partition操作无法正确，使用3-way-partition将相等值划分到中间，
     * 保证s, l group的length合法（至多相差1个元素） 
     * <p>复制到nums时，注意不能将中间相等的元素放到nums 相邻位置，为此需要从aux median两边往前遍历
     * 
     */
    @Override
    public void wiggleSort(int[] nums) {
      final int n = nums.length;
      // 寻找中间值median
      int midIndex = (n+1) / 2;
//      int median = kth.findKthLargest(nums, midIndex);
      int median = findKthLargest(nums, midIndex);
      int[] aux = Arrays.copyOf(nums, n);
      // meidan 3-way-partition划分
      for (int i = 0, lt = 0, gt = n-1; i <= gt;) {
        if (aux[i] < median)
          swap(aux, i++, lt++);
        else if (aux[i] > median)
//          swap(aux, i++, gt--);
          // 注意不能i++，会导致交换过来的跳过不检查了，小于的能++是因为已经检查过的小于则跳过++
          // 不过居然通过了leetcode测试。。。
          swap(aux, i, gt--);
        else 
          i++;
      }
      // placement
      // 将aux左边元素复制到nums的even group。注意size > odd group，因为有midIndex个而midIndex超过一半(n+1)/2
      for (int i = 0, j = midIndex-1; j >= 0; i+=2, j--)
        nums[i] = aux[j];
      // 将aux右边元素复制到nums odd group
      for (int i = 1, j = n-1; j >= midIndex; i+=2, j--)
        nums[i] = aux[j];
    }
    
    /**
     * 使用{@link KthLargestElementinanArray#findKthLargest(int[], int)}寻找中间值
     */
    
    void shuffle(int[] nums) {
      Random RAND = new Random();
      for (int i = 0; i < nums.length; i++)
        swap(nums, i, RAND.nextInt(nums.length));
    }
    
    public int findKthLargest(int[] nums, int k) {
      shuffle(nums);
      int lo = 0, hi = nums.length-1;
      // 将第k大 转化为 数组下标k
      k = nums.length - k;
      while (lo < hi) {
        int index = partition(nums, lo, hi);
        if (index < k)
          lo = index + 1;
        else if (index > k)
          hi = index - 1;
        else 
          break;
      }
      return nums[k];
    }
    
    /**
     * 如果将<= 改为 <，运行时间还增加了，与{@link SolutionByCombiningPartitionAndPlacement#partition(int[], int, int)}的修改减少
     * 时间不符合
     * 修改为< : runtime = 88, submissionurl:https://leetcode.com/submissions/detail/228165594/
     * @param nums
     * @param lo
     * @param hi
     * @return
     */
    int partition(int[] nums, int lo, int hi) {
      final int pivot = nums[hi];
      int i = lo;
      for (int j = lo; j < hi; j++) {
        if (nums[j] <= pivot)
          swap(nums, i++, j);
      }
      swap(nums, i, hi);
      return i;
    }
    
    static void swap(int[] nums, int i, int j) {
      int tmp = nums[i];
      nums[i] = nums[j];
      nums[j] = tmp;
    }
  }
  
  @Author(value = "fun4LeetCode", 
      references = "https://leetcode.com/problems/wiggle-sort-ii/discuss/77684/Summary-of-the-various-solutions-to-Wiggle-Sort-for-your-reference")
  @Author(value = "shuoshankou", 
      references = "https://leetcode.com/problems/wiggle-sort-ii/discuss/77682/Step-by-step-explanation-of-index-mapping-in-Java")
  @Submission(submittedDate = @DateTime("20190511"), runtime = 33, runtimeBeatRate = 41.09, memory = 41,
      memoryBeatRate = 57.95, url = "https://leetcode.com/submissions/detail/228161720/")
  public static class SolutionByCombiningPartitionAndPlacement extends SolutionByMedianPartition {

    /**
     * 不同于{@link SolutionByMedianPartition}，该方案将partition与placement组合到一起，不需要辅助数组aux使in-place。
     * <p>由于需要保证每个元素有且仅有一次partition，原来的正常的遍历顺序将会导致nums元素被partition到
     * 未遍历的位置时可能被partition多次
     * <p>使用Virtual Indexing规则 {@code (1+2*(i)) % (n|1)}，首先遍历访问odd group:[1,3,5,...]，再访问even group:[0,2,4,...]，注意group size: even >= odd。
     * <p>(n|1)将偶数size与对应的奇数size一致，如 6|1==>7, 7|1 ==> 7。
     * 当i=3，n=6和n=7时不同的值：(1+2*3) % 6=1，(1+2*3) % 7=0，即当n为偶数时会导致再访问一遍之前的下标
     * <pre>
Original idx: 0    1    2    3    4    5  
Mapped idx:   1    3    5    0    2    4 
     * </pre>
     * <p>使用Virtual Indexing使每个元素仅被访问交换一次
     */
    @Override
    public void wiggleSort(int[] nums) {
      final int n = nums.length, midIndex = (n + 1) / 2;
      // 获取median
      final int median = findKthLargest(nums, midIndex); 
      // partition 和 placement
      // odd, even表示index()后的下标含义
      for (int i = 0, odd = 0, even = n - 1; i <= even;) {
        int num = nums[index(i, n)];
        // 将大的放到奇数下标上
        if (num > median) {
          swap(nums, index(odd++, n), index(i++, n));
        } 
        // 将小的放到偶数下标上。注意i不能++，因为交换过来的even没有比较
        else if (num < median) {
          swap(nums, index(even--, n), index(i, n));
        } else 
          i++;
      }
    }
    
    static int index(int i, int n) {
      return (1 + 2 * i) % (n | 1);
    }
    
    /**
     * 较少等值交换
     */
    @Override
    int partition(int[] nums, int lo, int hi) {
      final int pivot = nums[hi];
      int i = lo;
      for (int j = lo; j < hi; j++) {
        // <= 改为 <，减少相等值的交换
        if (nums[j] < pivot)
          swap(nums, i++, j);
      }
      swap(nums, i, hi);
      return i;
    }
  }
}
