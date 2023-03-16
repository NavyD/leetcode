package cn.navyd.leetcode.problem;

import cn.navyd.leetcode.annotation.Problem;
import cn.navyd.leetcode.annotation.Submission;
import cn.navyd.leetcode.annotation.Problem.DifficultyEnum;

@Problem(number = 206, difficulty = DifficultyEnum.EASY)
public interface ReverseLinkedList {

  ListNode reverseList(ListNode head);

  @Submission(date = "20200825", memory = 39.6, memoryBeatRate = 60.23, runtime = 0, runtimeBeatRate = 100, url = "https://leetcode-cn.com/submissions/detail/101694613/")
  public static class SolutionIterative implements ReverseLinkedList {
    @Override
    public ListNode reverseList(ListNode head) {
      ListNode pre = null;
      if (head != null) {
        while (head != null) {
          ListNode next = head.next;
          head.next = pre;
          pre = head;
          head = next;
        }
      }
      return pre;
    }
  }

  /**
   * 参考：
   *
   * <li>https://leetcode-cn.com/problems/reverse-linked-list/solution/dong-hua-yan-shi-206-fan-zhuan-lian-biao-by-user74/
   */
  @Submission(date = "20200825", memory = 40, memoryBeatRate = 9.5, runtime = 0, runtimeBeatRate = 100, url = "https://leetcode-cn.com/submissions/detail/101700482/")
  public static class SolutionRecursive implements ReverseLinkedList {
    @Override
    public ListNode reverseList(ListNode head) {
      if (head == null || head.next == null) {
        return head;
      }
      ListNode cur = reverseList(head.next);
      // next = pre
      head.next.next = head;
      head.next = null;
      return cur;
    }
  }


  public class ListNode {
    int val;
    ListNode next;

    ListNode(int x) {
      val = x;
    }
  }
}
