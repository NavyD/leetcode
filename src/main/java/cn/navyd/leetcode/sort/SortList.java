package cn.navyd.leetcode.sort;

import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Optimal;
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

  @Optimal
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
  
  @Author(name = "jeantimex", significant = true, 
      referenceUrls = "https://leetcode.com/problems/sort-list/discuss/46714/Java-merge-sort-solution")
  @Submission(date = "2019-05-22", runtime = 3, runtimeBeatRate = 97.35, memory = 38.8, memoryBeatRate = 99.51,
    url = "https://leetcode.com/submissions/detail/230520224/")
  @Submission(date = "2019-03-27", runtime = 3, runtimeBeatRate = 97.35, memory = 40.7, memoryBeatRate = 77.82,
      url = "https://leetcode.com/submissions/detail/218038080/")
  @Solution(timeComplexity = Complexity.O_N_LOG_N, spaceComplexity = Complexity.O_LOG_N)
  public static class SolutionByMergeTD implements SortList {

    /**
     * 思路：使用merge sort top down。标准的数组归并排序使用数组的{@code mid=(hi-lo)/2 + lo}下标，
     * 将数组递归的分离由大到小size=1 {@code lo=mid}的数组，然后在栈空间由小到大归并数组。
     * <p>链表的归并排序使用一个mid node将链表递归的由大到小分离为独立的子链表，然后不断的归并子链表
     * <p>实现：
     * <ol>
     * <li>获取链表的mid节点使用*2的方式，即一个fast以slow两倍的数组迭代，当fast到达链表最后时，slow刚好在mid节点上。
     * 如：[1,5,3]，初始化：slow=fast=1。第一次：slow=5,fast=3，fast.next=null迭代完成。slow=5即mid节点
     * <li>在递归时使用prev指针保存slow即mid前一个node，分离链表，导致递归后（归并前）的链表被分离为一个个单节点
     * <li>归并两个子链表后将新链表head返回，递归导致新链表不断被归并链接，最终单节点被合并为有序的链表
     * </ol>
     * 空间复杂度：由于{@link #merge(ListNode, ListNode)}使用一个节点head保存新链表，而递归的深度为log N，
     * 则复杂度为O(log N)
     */
    @Override
    public ListNode sortList(ListNode head) {
      if (head == null || head.next == null)
        return head;
      // 分离链表
      ListNode prev = null, slow = head, fast = head;
      // 找到mid节点
      while (fast != null && fast.next != null) {
        prev = slow;
        slow = slow.next;
        fast = fast.next.next;
      }
      // 分离mid前后子链表
      prev.next = null;
      // 切分链表为单节点
      ListNode left = sortList(head), 
          right = sortList(slow);
      // 合并链表
      return merge(left, right);
    }
    
    /**
     * 归并两个链表并返回有序的新链表
     * @param left
     * @param right
     * @return
     */
    static ListNode merge(ListNode left, ListNode right) {
      final ListNode head = new ListNode(0);
      ListNode cur = head;
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
      cur.next = left != null ? left : right;
      return head.next;
    }
  }
}
