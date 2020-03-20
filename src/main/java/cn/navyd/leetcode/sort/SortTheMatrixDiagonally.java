package cn.navyd.leetcode.sort;

import java.util.PriorityQueue;
import java.util.Queue;

import javax.tools.Diagnostic;

import cn.navyd.annotation.algorithm.ComplexityEnum;
import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
Given a m * n matrix mat of integers, sort it diagonally in ascending order from the top-left to the bottom-right then return the sorted array.

 

Example 1:


Input: mat = [[3,3,1,1],[2,2,1,2],[1,1,1,2]]
Output: [[1,1,1,1],[1,2,2,2],[1,2,3,3]]

Constraints:

m == mat.length
n == mat[i].length
1 <= m, n <= 100
1 <= mat[i][j] <= 100
 * </pre>
 */
public interface SortTheMatrixDiagonally {
    /**
     * 矩阵对角sort转化为
     * <p>如何取出对角元素
     * <p>如何排序
     * @param mat
     * @return
     */
    public int[][] diagonalSort(int[][] mat);

    @Author(value = "lee215", references = "https://leetcode.com/problems/sort-the-matrix-diagonally/discuss/489749/JavaPython-Straight-Forward")
    @Submission(memory = 41.5, memoryBeatRate = 100, runtime = 6, runtimeBeatRate = 75.32, submittedDate = @DateTime("20200319"), url = "https://leetcode.com/submissions/detail/313729638/")
    public static class SolutionBySort implements SortTheMatrixDiagonally {
        /**
         * 如何取出对角元素：直接从对角取出很麻烦。不妨用i-j表示一个对角线，顺序扫描下去
         * i-j相同的是在同个对角线上，只需添加元素
         * <p>
         * 如何保存对角元素：mat矩阵的length是固定的，对角线数量是m+n-1，用priorityqueue[]，使用
         * offset
         * <p>
         * 如何使mat有序：对角线排序后，只需以i-j取出并放在[m][n]位置
         * 
         * <p>时间复杂度：O(m*n*log(max(m, n))
         * 空间复杂度：O(m + n)
         */
        @Override
        @SuppressWarnings("unchecked")
        public int[][] diagonalSort(int[][] mat) {
            final int m = mat.length, n = mat[0].length, offset = n - 1;
            final Queue<Integer>[] diagonals = new PriorityQueue[m + n - 1];
            for (int i = 0; i < diagonals.length; i++)
                diagonals[i] = new PriorityQueue<>();
            // 1. get diagonal elements
            for (int i = 0; i < m; i++)
                for (int j = 0; j < n; j++)
                    diagonals[i - j + offset].offer(mat[i][j]);
            // 2. to put mat[]
            for (int i = 0; i < m; i++)
                for (int j = 0; j < n; j++)
                    mat[i][j] = diagonals[i - j + offset].poll();
            return mat;
        }
    }

    @Author(value = "0", references = "https://leetcode.com/submissions/api/detail/1253/java/0/")
    @Submission(memory = 41.9, memoryBeatRate = 100, runtime = 1, runtimeBeatRate = 99.95, submittedDate = @DateTime("20200320"), url = "https://leetcode.com/submissions/detail/314028874/")
    @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_K), spaceComplexity = ComplexityEnum.O_1, inplace = true)
    public static class SolutionByCount implements SortTheMatrixDiagonally {
        /**
         * 如何取出对角元素：将matrix分两边，一边定位到最后mat[m-1][y]，固定了m-1，
         * y-- => 1就是一边对角线的终点位置 y==0时只有1个元素不需取出;
         * 只要同时减小m-1--, y-- => 0则是一条对角线的所有元素下标
         * 
         * 如何排序：由于mat的长度、值均限制在100，可以用counting sort。统计好后
         * 从小到大（或反之）把统计值放入
         */
        @Override
        public int[][] diagonalSort(int[][] mat) {
            final int m = mat.length, n = mat[0].length;
            final int[] counts = new int[101];
            // the below diagonal
            for (int i = 1; i < n; i++)
                sortDiagonalElements(mat, counts, m - 1, i);
            // the up diagonal
            for (int i = 1; i < m; i++) 
                sortDiagonalElements(mat, counts, i, n - 1);
            return mat;
        }

        static void sortDiagonalElements(int[][] mat, int[] counts, final int xEnd, final int yEnd) {
            // count diagonal
            int x = xEnd, y = yEnd;
            while (x >= 0 && y >= 0) 
                counts[mat[x--][y--]]++;
            // put val from large to small
            int val = -1;
            while (++val <= 100)
                // to guarantee counts clear 0
                while (counts[val] > 0) {
                    mat[++x][++y] = val;
                    counts[val]--;
                }
        }
    }
}