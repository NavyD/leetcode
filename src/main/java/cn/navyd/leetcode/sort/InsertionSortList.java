package cn.navyd.leetcode.sort;

import java.util.Arrays;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DerivedFrom;
import cn.navyd.annotation.leetcode.Optimal;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Submission.Status;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Unskilled;

/**
 * <pre>
 * Sort a linked list using insertion sort.
 * </pre>
 * <img alt="" src="https://upload.wikimedia.org/wikipedia/commons/0/0f/Insertion-sort-example-300px.gif" style="height:180px; width:300px">
 * <pre>
 * A graphical example of insertion sort. The partial sorted list (black) initially contains only the first element in the list.
 * With each iteration one element (red) is removed from the input data and inserted in-place into the sorted list
 *
 *
 * Algorithm of Insertion Sort:
 *
 * Insertion sort iterates, consuming one input element each repetition, and growing a sorted output list.
 * At each iteration, insertion sort removes one element from the input data, finds the location it belongs within the sorted list, and inserts it there.
 * It repeats until no input elements remain.
 *
 * Example 1:
 *
 * Input: 4->2->1->3
 * Output: 1->2->3->4
 * Example 2:
 *
 * Input: -1->5->3->4->0
 * Output: -1->0->3->4->5
 * </pre>
 *
 * @author navyd
 */
@Unskilled
@Problem(number = 147, difficulty = Difficulty.MEDIUM, tags = {
    Tag.SORT}, url = "https://leetcode.com/problems/insertion-sort-list/")
public interface InsertionSortList {
  public static class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
      val = x;
    }
  }

  /**
   * 对链表head使用插入排序
   *
   * @param head
   * @return
   */
  public ListNode insertionSortList(ListNode head);

  @Author(name = "sbvictory", significant = true,
      referenceUrls = "https://leetcode.com/problems/insertion-sort-list/discuss/46420/An-easy-and-clear-way-to-sort-(-O(1)-space-)")
  @Submission(date = "2019-05-20", status = Status.ACCEPTED,
      runtime = 30, runtimeBeatRate = 60.33, memory = 36.7, memoryBeatRate = 99.54,
      url = "https://leetcode.com/submissions/detail/230066826/")
  @Submission(date = "2019-03-18", status = Status.ACCEPTED,
      runtime = 32, runtimeBeatRate = 28.1, memory = 38, memoryBeatRate = 59.01,
      url = "https://leetcode.com/submissions/detail/215740064/")
  @Solution(timeComplexity = Complexity.O_N_POW_2, spaceComplexity = Complexity.O_1)
  public static class SolutionByDummyNode implements InsertionSortList {

    /**
     * 思路：
     * <p>使用一个dummy节点作为开始链接已排序的nodes，要插入一个node到已排序的 单链表 list中，
     * 需要保存一个prev节点链接新的节点，使用节点prev.next.val与当前节点cur.val比较，一旦找到
     * {@code prev.next.val >= cur.val}时，才能将cur插入到prev的后面
     * <p>关键点在于使用prev.next与cur比较，插入 prev->cur
     */
    @Override
    public ListNode insertionSortList(ListNode head) {
      if (head == null || head.next == null) {
        return head;
      }
      // 虚拟节点，链接已排序的nodes
      final ListNode dummy = new ListNode(0);
      ListNode prev = dummy;
      for (ListNode cur = head; cur != null; ) {
        // 找到cur在prev的位置
        while (prev.next != null && prev.next.val < cur.val) {
          prev = prev.next;
        }
        ListNode next = cur.next;
        // 链接节点prev -> cur
        cur.next = prev.next;
        prev.next = cur;
        cur = next;
        // 重置prev，重新遍历
        prev = dummy;
      }
      return dummy.next;
    }

  }

  @Optimal
  @Author(name = "Xing_",
      referenceUrls = "https://leetcode.com/problems/insertion-sort-list/discuss/46420/An-easy-and-clear-way-to-sort-(-O(1)-space-)/45957")
  @Author(name = "zrythpzhl", significant = true,
      referenceUrls = "https://leetcode.com/problems/insertion-sort-list/discuss/46420/An-easy-and-clear-way-to-sort-(-O(1)-space-)/45974")
  @Submission(date = "2019-05-20", status = Status.ACCEPTED,
      runtime = 2, runtimeBeatRate = 98.80, memory = 37.8, memoryBeatRate = 87.09,
      url = "https://leetcode.com/submissions/detail/230071106/")
  @DerivedFrom(SolutionByDummyNode.class)
  @Solution(timeComplexity = Complexity.O_N_POW_2, spaceComplexity = Complexity.O_1)
  public static class SolutionByDummyNodeII implements InsertionSortList {

    /**
     * 思路：
     * <p>与{@link SolutionByDummyNode}唯一的不同在于：
     * 对于插入节点后尝试使用上一次的插入位置节点开始比较，而不是简单的重新开始head比较。
     * <p>对于有序的list时间复杂度：{@link Complexity#O_N}
     * <p>实现：
     * <ol>
     * <li>保存上次插入的prev节点，不再直接在循环后简单的置为prev=dummy
     * <li>在开始时判断{@code prev.val >= cur.val}。
     * <li>如果为true，说明cur节点在prev前面，prev=dummy开始从头遍历
     * <li>如果为false，说明cur节点在prev后面，直接从prev开始遍历，减少{@code dummy--->prev}遍历的时间
     * <li>不一定需要使用{@code prev.next != null || prev.next.val >= cur.val}比较，因为while会保证后面的节点能够比较到，
     * 该代码还需要检查null（dummy.next==null第一次遍历插入，后续都是非null）
     * </ol>
     */
    @Override
    public ListNode insertionSortList(ListNode head) {
      if (head == null || head.next == null) {
        return head;
      }
      final ListNode dummy = new ListNode(0);
      ListNode prev = dummy;
      for (ListNode cur = head; cur != null; ) {
        // 如果cur在上次插入的prev节点前，从dummy开始遍历寻找，否则在prev之后
        if (prev.val >= cur.val) {
          prev = dummy;
        }
        // 找到cur节点插入位置prev
        while (prev.next != null && prev.next.val < cur.val) {
          prev = prev.next;
        }
        ListNode next = cur.next;
        // 链接节点prev->cur
        cur.next = prev.next;
        prev.next = cur;
        // 比较下一个
        cur = next;
      }
      return dummy.next;
    }
  }

  @Author(name = "Han_V", significant = true,
      referenceUrls = "https://leetcode.com/problems/insertion-sort-list/discuss/46429/Thoughts-from-a-Google-interviewer")
  @Submission(date = "2019-05-20", status = Status.ACCEPTED,
      runtime = 1, runtimeBeatRate = 100, memory = 38.5, memoryBeatRate = 33.29,
      url = "https://leetcode.com/submissions/detail/230074732/")
  @Solution(timeComplexity = Complexity.O_LOG_N, spaceComplexity = Complexity.O_N)
  public static class SolutionByArraySort implements InsertionSortList {

    /**
     * 思路：将链表排序转换为数组排序。再将排序的数组值修改回链表
     */
    @Override
    public ListNode insertionSortList(ListNode head) {
      // 获取数组大小
      int nodeCount = 0;
      for (ListNode cur = head; cur != null; cur = cur.next) {
        nodeCount++;
      }
      int[] vals = new int[nodeCount];
      // 创建vals数组
      int i = 0;
      for (ListNode cur = head; cur != null; cur = cur.next) {
        vals[i++] = cur.val;
      }
      // 快速排序
      Arrays.sort(vals);
      // 修改node.val为有序的vals值
      i = 0;
      for (ListNode cur = head; cur != null; cur = cur.next) {
        cur.val = vals[i++];
      }
      return head;
    }
  }
}
