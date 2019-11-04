package cn.navyd.leetcode.tree;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.Optimal;
import cn.navyd.annotation.leetcode.Problem;
import cn.navyd.annotation.leetcode.Skilled;
import cn.navyd.annotation.leetcode.Problem.Difficulty;
import cn.navyd.annotation.leetcode.Problem.Tag;
import cn.navyd.annotation.leetcode.Solution;
import cn.navyd.annotation.leetcode.Solution.Complexity;
import cn.navyd.annotation.leetcode.Submission;
import cn.navyd.annotation.leetcode.Unskilled;

/**
 * <pre>
Given a binary tree, return the inorder traversal of its nodes' values.

Example:

Input: [1,null,2,3]
   1
    \
     2
    /
   3

Output: [1,3,2]
Follow up: Recursive solution is trivial, could you do it iteratively?
 * </pre>
 * @author navyd
 *
 */
@Problem(number = 94, difficulty = Difficulty.MEDIUM, tags = Tag.TREE, url = "https://leetcode.com/problems/binary-tree-inorder-traversal/")
public interface BinaryTreeInorderTraversal {
  /**
   * 中序遍历：先遍历左子树，然后是root，最后是右子树
   */
  public List<Integer> inorderTraversal(TreeNode root);
  
  @Skilled
  @Author(name = "navyd")
  @Submission(date = "2019-07-06", memory = 34.5, memoryBeatRate = 99.98, runtime = 0, runtimeBeatRate = 100, url = "https://leetcode.com/submissions/detail/241069913/")
  @Solution(spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_N)
  public static class SolutionByRecursive implements BinaryTreeInorderTraversal {

    @Override
    public List<Integer> inorderTraversal(TreeNode root) {
      List<Integer> result = new LinkedList<>();
      inorderRecursive(root, result);
      return result;
    }
    
    private void inorderRecursive(TreeNode root, List<Integer> nums) {
      if (root == null)
        return;
      inorderRecursive(root.left, nums);
      nums.add(root.val);
      inorderRecursive(root.right, nums);
    }
  }
  
  @Submission(date = "2019-07-07", memory = 34.7, memoryBeatRate = 99.98, runtime = 1, runtimeBeatRate = 55.24, url = "https://leetcode.com/submissions/detail/241078212/")
  @Solution(spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_N)
  @Unskilled
  public static class SolutionByIterative implements BinaryTreeInorderTraversal {

    /**
     * 思路：中序遍历需要先访问左子树，将左子树全部入栈，然后弹出left，访问结果，
     * 最后将left.right右节点作为root访问左子树循环。
     * <p>
     * 先将left全部入栈，然后访问left.val，然后将right作为root节点，将right.left入栈如此循环
     */
    @Override
    public List<Integer> inorderTraversal(TreeNode root) {
      Deque<TreeNode> stack = new ArrayDeque<>();
      List<Integer> result = new LinkedList<>();
      TreeNode cur = root;
      while (cur != null || !stack.isEmpty()) {
        // 将left入栈
        while (cur != null) {
          stack.push(cur);
          cur = cur.left;
        }
        // root
        cur = stack.pop();
        result.add(cur.val);
        // 遍历right
        cur = cur.right;
      }
      return result;
    }
  }
  
  @Optimal
  @Author(name = "leetcode", significant = true, referenceUrls = "https://leetcode.com/problems/binary-tree-inorder-traversal/solution/")
  @Submission(date = "2019-07-17", memory = 34.7, memoryBeatRate = 99.98, runtime = 0, runtimeBeatRate = 100, url = "https://leetcode.com/submissions/detail/243958315/")
  @Solution(spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_N)
  public static class SolutionByMorrisTraversal implements BinaryTreeInorderTraversal {

    /**
     * Morris Traversal 方法：structure-Threaded Binary Tree 将树构造成线索二叉树结构
     * <p>
     * 思路：将原本为null的right引用指向该节点的在in-order序列上的后继successor，将原本为null的left引用指向该节点的in-order序列的前继predecessor
     * <pre>
     X
   /   \
  Y     Z
 / \   / \
A   B C   D
     * </pre>
     * <p>right指向后继，root.right在in-order顺序本身就是后继，但是当root.right!=null存在子节点时必须根据in-order先访问了子节点left。
     * 如果right==null不存在子节点，下一个访问的是root父节点（root在左边）或完成遍历
     * <p>right指向后继容易理解，子树root.left != null说明要遍历root需要将left子树遍历完，即可以将root连接到left.mostRight.root (mostRight表示最深的右子树节点)
     * <p>left指向predecessor不好理解是因为in-order最左节点A是没有predecessor，但是无论root这棵树是在哪个节点的子树中，访问了root节点的父节点（前继）后，要访问这棵树，必须访问root.left（后继）,
     * left引用就指向前继（首先被访问）。left=null只有当root=Z，root.left=C时left存在前继X
     * <pre>
 A
  \
   Y
  / \
(A)  B
      \
       X
      / \
    (Y)  Z
        / \
       C   D
     * </pre>
     * 当root=C时，C的前继是X，在访问Z时将会变成C-Z-D的顺序，上次遍历是X，下一个遍历的就是C。也就是将left=null时指向前继
     * <p>实现：
     * <ol>
     * <li>cur初始化为root，cur==null时退出
     * <li>如果cur.left == null 不存在left子树（cur指向前继，访问上一个node后直接访问cur），访问cur.val节点值 并 将cur.right子树作为新的cur树遍历
     * <li>如果cur.left != null 存在left子树（mostRight指向后继cur），找出mostRight并将mostRight指向cur，将cur.left子树 作为新的cur遍历
     * </ol>
     */
    @Override
    public List<Integer> inorderTraversal(TreeNode root) {
      List<Integer> result = new ArrayList<>();
      // 遍历节点
      TreeNode cur = root;
      // 如果cur != null
      while (cur != null) {
        // 如果cur.left == null不存在left subtree
        if (cur.left == null) {
          // 访问left.val
          result.add(cur.val);
          // 将cur.right作为新的cur 遍历subtree
          cur = cur.right;
        } 
        // 如果cur.left存在subtree
        else {
          // 寻找最right节点
          TreeNode mostRight = cur.left;
          while (mostRight.right != null)
            mostRight = mostRight.right;
          // 将cur节点移动到cur.left的最right节点上
          mostRight.right = cur;
          TreeNode temp = cur.left;
          cur.left = null;
          // 将left作为新的cur遍历
          cur = temp;
        }
      }
      return result;
    }
  }
  
  public static class SolutionByIterativeError implements BinaryTreeInorderTraversal {

    @Override
    public List<Integer> inorderTraversal(TreeNode root) {
      Deque<TreeNode> stack = new ArrayDeque<>();
      List<Integer> result = new LinkedList<>();
      stack.add(root);
      // 会导致无线循环。 root添加后。root.left != null处理后，root pop出来，left !=null push(left)两遍，后退出
      while (!stack.isEmpty()) {
        TreeNode node = stack.peek();
        if (node == null)
          continue;
        if (node.left != null) {
          stack.push(node.left);
          continue;
        } 
        node = stack.pop();
        result.add(node.val);
        if (node.right != null) {
          stack.push(node.right);
        }
        
      }
      return null;
    }
  }
}
