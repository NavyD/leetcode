package cn.navyd.leetcode.tree;

import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
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
Given a binary tree, return the preorder traversal of its nodes' values.

Example:

Input: [1,null,2,3]
   1
    \
     2
    /
   3

Output: [1,2,3]
Follow up: Recursive solution is trivial, could you do it iteratively?
 * </pre>
 * 
 * @author navyd
 *
 */
@Problem(number = 114, difficulty = Difficulty.MEDIUM, tags = Tag.TREE,
    url = "https://leetcode.com/problems/binary-tree-preorder-traversal/")
public interface BinaryTreePreorderTraversal {
  public List<Integer> preorderTraversal(TreeNode root);

  @Author(name = "navyd")
  @Submission(date = "2019-07-02", memory = 34.7, memoryBeatRate = 99.96, runtime = 0,
      runtimeBeatRate = 100, url = "https://leetcode.com/submissions/detail/240101649/")
  @Solution(spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_LOG_N)
  public static class SolutionByRecursive implements BinaryTreePreorderTraversal {

    @Override
    public List<Integer> preorderTraversal(TreeNode root) {
      List<Integer> result = new LinkedList<>();
      preorderRecursive(root, result);
      return result;
    }

    void preorderRecursive(TreeNode node, List<Integer> nums) {
      if (node == null)
        return;
      nums.add(node.val);
      preorderRecursive(node.left, nums);
      preorderRecursive(node.right, nums);
    }
  }

  @Unskilled
  @Author(name = "fabrizio3", significant = true, referenceUrls = "https://leetcode.com/problems/binary-tree-preorder-traversal/discuss/45468/3-Different-Solutions")
  @Submission(date = "2019-07-02", memory = 34.7, memoryBeatRate = 99.96, runtime = 1, runtimeBeatRate = 56.33, url = "https://leetcode.com/submissions/detail/240119002/")
  @Solution(spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_N)
  public static class SolutionByIterative implements BinaryTreePreorderTraversal {

    @Override
    public List<Integer> preorderTraversal(TreeNode root) {
      List<Integer> result = new LinkedList<>();
      if (root == null)
        return result;
      Deque<TreeNode> stack = new LinkedList<>();
      stack.push(root);
      while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        if (node == null)
          continue;
        // 获取root节点值
        result.add(node.val);
        // 后遍历right
        if (node.right != null)
          stack.push(node.right);
        // 先遍历 left
        if (node.left != null)
          stack.push(node.left);
      }
      return result;
    }
  }
}
