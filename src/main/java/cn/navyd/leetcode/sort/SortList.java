package cn.navyd.leetcode.sort;

import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Unskilled;

/**
 * <pre>
Sort a linked list in O(n log n) time using constant space complexity.

Example 1:

Input: 4->2->1->3
Output: 1->2->3->4
Example 2:

Input: -1->5->3->4->0
Output: -1->0->3->4->5
 * </pre>
 * 
 * @author navyd
 *
 */
@Unskilled
@Problem(number = 148, difficulty = Difficulty.MEDIUM, tags = {Tag.SORT, Tag.LINKED_LIST},
    url = "https://leetcode.com/problems/sort-list/")
public interface SortList {
  public static class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
      val = x;
    }
  }

  public ListNode sortList(ListNode head);

  @Author(name = "qinlei515", 
      referenceUrls = "https://leetcode.com/problems/sort-list/discuss/46712/Bottom-to-up(not-recurring)-with-o(1)-space-complextity-and-o(nlgn)-time-complextity/151523")
  @Author(name = "zdwu", significant = true,
      referenceUrls = "https://leetcode.com/problems/sort-list/discuss/46712/Bottom-to-up(not-recurring)-with-o(1)-space-complextity-and-o(nlgn)-time-complextity")
  @Submission(date = "2019-05-21", runtime = 6, runtimeBeatRate = 22.01, memory = 40.6,
      memoryBeatRate = 81.30, url = "https://leetcode.com/submissions/detail/230301052/")
  @Submission(date = "2019-03-27", runtime = 6, runtimeBeatRate = 22.01, memory = 41.8,
      memoryBeatRate = 11.72, url = "https://leetcode.com/submissions/detail/217769848/")
  @Solution(timeComplexity = Complexity.O_N_LOG_N, spaceComplexity = Complexity.O_1)
  public static class SolutionByMergeBU implements SortList {

    /**
     * 思路：使用bottom to up merge。将链表按照size 1,2,4,8...细分小链表然后归并
     * <ol>
     * <li>统计链表长度，用于细分链表
     * <li>需要分离链表，分为两段size大小的链表，然后归并
     * <li>归并两个小链表后返回排序的最后一个链表，用于链接下一个链表
     * </ol>
     * <p>关键在于将链表分离2个size大小的链表。如何处理合并时之前已合并的链表
     * <p>
     * 实现：
     * <ul>
     * <li>使用dummy节点保存有序链表
     * <li>分离链表时，需要三个节点：left-->null，right-->null用于合并，right.right用于下次分离
     * <li>每次size迭代，合并时不要求与之前的已合并的链表比较，而是重新构造新的有序链表。
     * 当{@code size>=(length+1)/2}时，这最后一次构造的新链表必然有序
     * </ul>
     * <p>合并时如何确保有序，tail.节点上的是否与left right合并的一致？
     * <p>
     * 由于小链表时必然不是整体有序的，但是当到达size>=length/2时，必然会合并left与right，此时整体有序
     */
    @Override
    public ListNode sortList(ListNode head) {
      if (head == null || head.next == null)
        return head;
      // 保存有序链表
      final ListNode dummy = new ListNode(0);
      dummy.next = head;
      // 统计list length
      final int length = getLength(head);
      // 细分链表
      for (int size = 1; size < length; size *= 2) {
        ListNode cur = dummy.next, tail = dummy;
        // 对size个节点的两个链表不断合并 在head所有链表节点上
        while (cur != null) {
          // 分离链表 
          ListNode left = cur,
              // 分离出left子链表
              right = split(left, size);
          // 分离right子链表。并返回right.right作为下一个待合并的链表
          cur = split(right, size);
          // 合并
          tail = merge(left, right, tail);
        }
      }
      
      return dummy.next;
    }
    
    static int getLength(ListNode node) {
      int count = 0;
      while (node != null) {
        node = node.next;
        count++;
      }
      return count;
    }
    
    /**
     * 将node分离为两个链表，left最多可能由size个节点，right从第size+1个开始到最后。
     * <p>返回right节点。如果left少于size个节点，则返回null表示right不存在
     * @param node
     * @param size
     * @return
     */
    static ListNode split(ListNode node, int size) {
      // 如果size=1，则node不变
      while (node != null && --size > 0)
        node = node.next;
      // 不足size个
      if (node == null)
        return null;
      ListNode next = node.next;
      node.next = null;
      return next;
    }
    
    /**
     * 
     * 将left与right排序合并到已排序的tail上，并返回tail的最后一个节点tail
     * <p>直接将排序的合并到tail节点后面
     * @param left
     * @param right
     * @param tail
     * @return
     */
    static ListNode merge(ListNode left, ListNode right, ListNode tail) {
      ListNode cur = tail;
      // 合并left与right
      while (left != null && right != null) {
        // 如果left较小，合并left到有序链表
        if (left.val < right.val) {
          // 不需要额外操作，如:left.next = null, cur.next虽然链接后面的left，但是会被之后的合并覆盖
          cur.next = left;
          left = left.next;
        } 
        // 合并right
        else {
          cur.next = right;
          right = right.next;
        }
        cur = cur.next;
      }
      // 合并多出的节点
      cur.next = left != null ? left : right;
      // 迭代到最后返回有序链表的最后节点
      while (cur.next != null)
        cur = cur.next;
      return cur;
    }
  }
}

/**
 * 链表归并排序top down。 与数组下标mid分离子数组不同，将链表分离子链表需要遍历slow.next fast.next.next跳跃获取mid node然后mid
 * node.next=null切分。 时间复杂度：O(N log N) 空间复杂度：O(log N) 递归栈空间
 * 
 * @author navyd
 *
 */
class SolutionSortListByMergeSortTD implements SortList {

  public ListNode sortList(ListNode head) {
    if (head == null || head.next == null)
      return head;
    ListNode prev = null, slow = head, fast = head;
    // 将head链表对半分离，slow++ fast+=2导致对半分
    while (fast != null && fast.next != null) {
      prev = slow;
      slow = slow.next;
      fast = fast.next.next;
    }
    // head分离为prev slow两个链表
    prev.next = null;
    ListNode
    // 不断分离left链表
    left = sortList(head),
        // 不断分离right链表
        right = sortList(slow);
    // 将两个链表归并并返回新链表head
    return merge(left, right);
  }

  /**
   * 归并两个链表并返回新链表的head
   * 
   * @param left
   * @param right
   * @return
   */
  private static ListNode merge(ListNode left, ListNode right) {
    ListNode dummy = new ListNode(0), cur = dummy;
    while (left != null && right != null) {
      if (left.val < right.val) {
        cur.next = left;
        left = left.next;
      } else {
        cur.next = right;
        right = right.next;
      }
      cur = cur.next;
    }
    cur.next = left == null ? right : left;
    return dummy.next;
  }
}
