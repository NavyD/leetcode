package cn.navyd.leetcode.sort;

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
 * @author navyd
 *
 */
public class SortList {
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

  public ListNode sortList(ListNode head) {
    return sortListByMergeSortBU(head);
  }

  /**
   * 链表的归并排序 自底向上，
   * @param head
   * @return
   */
  public ListNode sortListByMergeSortBU(ListNode head) {
    // 虚拟链表head
    ListNode dummy = new ListNode(0);
    dummy.next = head;
    // 链表长度
    final int length = getLength(head);
    // 遍历 每个长度分别遍历
    for (int size = 1; size < length; size <<= 1) {
      // 内循环
      ListNode 
        cur = dummy.next,
        prev = dummy;
      while (cur != null) {
        // 按size分离子链表
        ListNode 
          left = cur,
          // 分离left子链表 使得left仅有size个node，并返回第size个node
          right = splitNode(left, size);
        // 分离right链表，使得right只有size个node，并返回第size个node 作为下一个归并的left起点node
        cur = splitNode(right, size);
        // 归并子链表
        prev = merge(left, right, prev);
      }
    }
    return dummy.next;
  }

  private static int getLength(ListNode head) {
    if (head == null)
      return 0;
    int len = 1;
    ListNode cur = head;
    while ((cur = cur.next) != null)
      len++;
    return len;
  }

  /**
   * 从链表head开始分离出size个node，最后的(size-1)node.next=null，并返回第size个node（起点node）
   * <p>会改变head链表结构
   * @param start
   * @param size
   * @return
   */
  private static ListNode splitNode(ListNode head, int size) {
    if (head == null)
      return null;
    while (head.next != null && --size > 0)
      head = head.next;
    ListNode next = head.next;
    head.next = null;
    return next;
  }

  /**
   * 将子链表left right归并链表节点prev后，并返回新链表的最后一个节点prev
   * @param left
   * @param right
   * @param prev
   * @return
   */
  private static ListNode merge(ListNode left, ListNode right, ListNode prev) {
    // 比较left与right val并链接
    while (left != null && right != null) {
      if (left.val < right.val) {
        prev.next = left;
        left = left.next;
      } else {
        prev.next = right;
        right = right.next;
      }
      prev = prev.next;
    }
    prev.next = left == null ? right : left;
    // 遍历到归并链表的最后节点
    while (prev.next != null)
      prev = prev.next;
    return prev;
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
//     a = {4,2,1,3};
    a = {-1, 5, 3, 4, 0};
    
    ListNode head = new ListNode(a[0]), node = head;
    for (int i = 1; i < a.length; i++) {
      node.next = new ListNode(a[i]);
      node = node.next;
    }
    SortList sl = new SortList();
//    SL sl = new SL();
    System.err.println(iterate(sl.sortList(head)));;
  }
}
