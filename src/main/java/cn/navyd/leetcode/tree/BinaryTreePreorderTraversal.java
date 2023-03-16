package cn.navyd.leetcode.tree;

import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedList;
import java.util.List;
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
 * Given a binary tree, return the preorder traversal of its nodes' values.
 *
 * Example:
 *
 * Input: [1,null,2,3]
 * 1
 * \
 * 2
 * /
 * 3
 *
 * Output: [1,2,3]
 * Follow up: Recursive solution is trivial, could you do it iteratively?
 * </pre>
 *
 * @author navyd
 */
@Problem(number = 114, difficulty = Difficulty.MEDIUM, tags = Tag.TREE,
    url = "https://leetcode.com/problems/binary-tree-preorder-traversal/")
public interface BinaryTreePreorderTraversal {
  public List<Integer> preorderTraversal(TreeNode root);

  @Author(name = "navyd")
  @Submission(date = "2019-07-02", memory = 34.7, memoryBeatRate = 99.96, runtime = 0,
      runtimeBeatRate = 100, url = "https://leetcode.com/submissions/detail/240101649/")
  @Solution(spaceComplexity = Complexity.O_N, timeComplexity = Complexity.O_N)
  public static class SolutionByRecursive implements BinaryTreePreorderTraversal {

    @Override
    public List<Integer> preorderTraversal(TreeNode root) {
      List<Integer> result = new LinkedList<>();
      preorderRecursive(root, result);
      return result;
    }

    void preorderRecursive(TreeNode node, List<Integer> nums) {
      if (node == null) {
        return;
      }
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
      if (root == null) {
        return result;
      }
      Deque<TreeNode> stack = new LinkedList<>();
      stack.push(root);
      while (!stack.isEmpty()) {
        TreeNode node = stack.pop();
        if (node == null) {
          continue;
        }
        // 获取root节点值
        result.add(node.val);
        // 后遍历right
        if (node.right != null) {
          stack.push(node.right);
        }
        // 先遍历 left
        if (node.left != null) {
          stack.push(node.left);
        }
      }
      return result;
    }
  }

  @Optimal
  @Author(name = "jianchao-li", referenceUrls = "https://leetcode.com/problems/binary-tree-preorder-traversal/discuss/45466/C%2B%2B-Iterative-Recursive-and-Morris-Traversal")
  @Submission(date = "2019-07-28", memory = 34.7, memoryBeatRate = 99.94, runtime = 0, runtimeBeatRate = 100, url = "https://leetcode.com/submissions/detail/246836307/")
  @Solution(spaceComplexity = Complexity.O_1, timeComplexity = Complexity.O_N)
  public static class SolutionByMorrisTraversal implements BinaryTreePreorderTraversal {

    /**
     * 与in-order不同点在于，按照原来的left指向前继，right指向后继，可以在移动left时先访问root，left作为root访问时，left.right==root，即转换时会导致之前的root移动到left作为右子节点
     * ，如果能够避免访问该节点就可以前序遍历
     * <p>如何root.left子节点转换后保持left.right==root，即能判断root.left.right == root？
     * <p>root.left作为前继转换时不能切断root.left=null，保持root.left连接，然后寻找已访问的root root.left.right == root 后继时判断表示已访问的节点跳过
     * 先访问root
     *
     * @see BinaryTreeInorderTraversal.SolutionByMorrisTraversal#inorderTraversal(TreeNode)
     */
    @Override
    public List<Integer> preorderTraversal(TreeNode root) {
      List<Integer> result = new ArrayList<>();
      while (root != null) {
        // left == null 访问root 后继right
        if (root.left == null) {
          result.add(root.val);
          root = root.right;
        }
        // left != null 寻找left.mostRight连接后继root
        else {
          TreeNode mostRight = root.left;
          // 如果 当前root已经被访问过则结束 
          while (mostRight.right != null && mostRight.right != root) {
            mostRight = mostRight.right;
          }
          // root未被访问过
          if (mostRight.right == null) {
            result.add(root.val);
            // right null 连接后继
            mostRight.right = root;
            root = root.left;
            // 不删除 root.left=null
          }
          // root已被访问  mostRight.right == root
          else {
            // 删除mostRight连接的root节点
            mostRight.right = null;
            root = root.right;
          }
        }
      }
      return result;
    }

  }
}
