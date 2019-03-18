package cn.navyd.leetcode.sort;

/**
Sort a linked list using insertion sort.

A graphical example of insertion sort. The partial sorted list (black) initially contains only the first element in the list.
With each iteration one element (red) is removed from the input data and inserted in-place into the sorted list
 

Algorithm of Insertion Sort:

Insertion sort iterates, consuming one input element each repetition, and growing a sorted output list.
At each iteration, insertion sort removes one element from the input data, finds the location it belongs within the sorted list, and inserts it there.
It repeats until no input elements remain.

Example 1:

Input: 4->2->1->3
Output: 1->2->3->4
Example 2:

Input: -1->5->3->4->0
Output: -1->0->3->4->5
Accepted
143,007
Submissions
391,278
 * @author navyd
 *
 */
public class InsertionSortList {
  public static class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
      val = x;
    }

    @Override
    public String toString() {
      return "ListNode [val=" + val + ", next=" + next + "]";
    }
  }

  public ListNode insertionSortList(ListNode head) {
    if (head == null)
      return null;
    for (ListNode node = head.next, preNode = head; node != null;) {
      // 找到插入节点
      ListNode last = findInsertPosition(head, node);
      // 插入到head前
      if (last == null) {
        preNode.next = node.next;
        node.next = head;
        head = node;
        node = preNode.next;
      } else {
        // 插入
        ListNode newLast = last.next;
        // 删除node
        preNode.next = node.next;
        last.next = node;
        node.next = newLast;
        node = preNode.next;
      }
    }
    return head;
  }

  static ListNode insert(ListNode head) {
    if (head == null)
      return null;
    ListNode helper = new ListNode(0), pre = helper, cur = head;
    while (cur != null) {
      // 找到插入点
      while (pre.next != null && pre.next.val < cur.val)
        pre = pre.next;
      // 将cur插入到pre后
      pre.next = cur;
      cur = cur.next;
      pre = helper;
    }
    return helper.next;
  }

  // 元素插入链表，需要从head开始遍历比较

  /**
   * 返回指定node在链表插入的前一个元素
   * 
   * @param head
   * @param node
   * @return
   */
  static ListNode findInsertPosition(ListNode head, ListNode node) {
    ListNode cur = head;
    ListNode last = null;
    while (cur != null) {
      if (cur.val > node.val || cur == node)
        break;
      last = cur;
      cur = cur.next;
    }
    return last;
  }

  static void build(int[] a) {
    ListNode node = new ListNode(a[0]);
    for (int i = 1; i < a.length; i++) {
      node.next = new ListNode(i);
    }
  }

  static String iterate(ListNode node) {
    String s = "";
    while (node != null) {
      s += node.val + ", ";
      node = node.next;
    }
    return s;
  }

  public static void main(String[] args) {
    int[]
    // a = {4,2,1,3};
    a = {-1, 5, 3, 4, 0};
    ListNode head = new ListNode(a[0]), node = head;
    for (int i = 1; i < a.length; i++) {
      node.next = new ListNode(a[i]);
      node = node.next;
    }
    insert(head);
    // InsertionSortList o = new InsertionSortList();
    //// System.err.println(iterate(head));
    // System.err.println(o.insertionSortList(head));
  }

  static class Solution {
    /**
     * 思路：使用一个fake head node保存排序的list，使用节点cur不断的在fake head查找插入点pre，
     * 然后插入到pre后面，主要要删除cur.next原来连接的head未排序的节点，应该连接新的fake head pre.next节点，
     * 保证有序
     * 时间复杂度：O(N^2)
     * 空间复杂度：O(1)
     * @param head
     * @return
     */
    public ListNode insertionSortListByFakeHead(ListNode head) {
      // 已排序的head
      ListNode helper = new ListNode(0),
          // 插入点
          pre = helper, cur = head;
      while (cur != null) {
        // 找到插入点
        while (pre.next != null && pre.next.val < cur.val)
          pre = pre.next;
        ListNode next = cur.next;
        // 将cur插入到pre后
        cur.next = pre.next;
        pre.next = cur;
        // 重置
        cur = next;
        pre = helper;
      }
      return helper.next;
    }
    /**
     * 思路：与{@link #insertionSortListByFakeHead(ListNode)}一样，不同的是优化了对插入到tail的处理，
     * 如果能够直接插入到tail节点，则不需要从head遍历到tail，节省了时间
     * 时间复杂度：O(N^2) 对于部分有序节点 list复杂度为O(N)
     * 空间复杂度：O(1)
     * @param head
     * @return
     */
    public ListNode insertionSortListByFakeHeadWithMax(ListNode head) {
      // 虚拟的排序list nodes。取最大值用于比较tail
      ListNode fakeHead = new ListNode(Integer.MIN_VALUE);
      ListNode tail = fakeHead, cur = head;
      while (cur != null) {
        ListNode next = cur.next;
        // 移除cur的后续节点
        cur.next = null;
        // 判断cur是否能直接插入最后
        if (tail.val <= cur.val) {
          tail.next = cur;
          tail = cur;
        }
        // 遍历插入
        else {
          // 从fakeHead开始遍历查找
          ListNode pre = fakeHead;
          while (pre.next != null && pre.next.val < cur.val) {
            pre = pre.next;
          }
          cur.next = pre.next;
          pre.next = cur;
        }
        cur = next;
      }
      return fakeHead.next;
    }
  }
}
