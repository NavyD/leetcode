package cn.navyd.leetcode.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
We have a list of points on the plane.  Find the K closest points to the origin (0, 0).

(Here, the distance between two points on a plane is the Euclidean distance.)

You may return the answer in any order.  The answer is guaranteed to be unique (except for the order that it is in.)

 

Example 1:

Input: points = [[1,3],[-2,2]], K = 1
Output: [[-2,2]]
Explanation: 
The distance between (1, 3) and the origin is sqrt(10).
The distance between (-2, 2) and the origin is sqrt(8).
Since sqrt(8) < sqrt(10), (-2, 2) is closer to the origin.
We only want the closest K = 1 points from the origin, so the answer is just [[-2,2]].
Example 2:

Input: points = [[3,3],[5,-1],[-2,4]], K = 2
Output: [[3,3],[-2,4]]
(The answer [[-2,4],[3,3]] would also be accepted.)
 

Note:

1 <= K <= points.length <= 10000
-10000 < points[i][0] < 10000
-10000 < points[i][1] < 10000
 * @author navyd
 *
 */
public class KClosestPointstoOrigin {
  
  /**
   * 思路：遍历计算每个point的距离distance，并将distance作为key，points作为value保存到SortedMap中，最后遍历k个最小距离的元素即可
   * @param points
   * @param K
   * @return
   */
  public int[][] kClosest(int[][] points, int K) {
    // original 0,0
    // |AB| = √[(x₁-x₂)²+(y₁-y₂)²]
    Map<Double, List<int[]>> map = new TreeMap<>();
    // 计算距离
    for (int i = 0; i < points.length; i++) {
      int[] point = points[i];
      double powVal = Math.pow(point[0], 2) + Math.pow(point[1], 2);
      double distance = Math.sqrt(powVal);
      if (!map.containsKey(distance)) {
        map.put(distance, new ArrayList<>());
      }
      List<int[]> indexs = map.get(distance);
      indexs.add(point);
    }
    int[][] r = new int[K][];
    int i = 0;
    out: for (Map.Entry<Double, List<int[]>> entry : map.entrySet()) {
      List<int[]> indexs = entry.getValue();
      for (int[] point : indexs) {
        r[i++] = point;
        if (i >= K)
          break out;
      }
      
    }
    return r;
  }
  
  static class Solution {
    public int[][] kClosest(int[][] points, int K) {
      return kClosestByDivideAndConquer(points, K);
    }
    /**
     * 思路：通过将距离排序获取k范围内的最大距离maxDist，然后遍历获取<=maxDist的point
     * 时间复杂度：O(N log N)主要在排序中。未保存distances对应的points下标，而是使用遍历的方式重新计算获取，减少空间复杂度
     * 空间复杂度：O(N)
     * @param points
     * @param K
     * @return
     */
    public int[][] kClosestBySort(int[][] points, int K) {
      // 获取distance数组
      int len = points.length;
      int[] distances = new int[len];
      for (int i = 0; i < len; i++)
        distances[i] = distance(points[i]);
      // 排序
      Arrays.sort(distances);
      // 获取distance[k-1]最大的距离
      int maxDistance = distances[K-1];
      // 遍历points比较距离，<=最大的添加
      int[][] r = new int[K][2];
      int i = 0;
      for (int[] point : points) {
        if (distance(point) <= maxDistance)
          r[i++] = point;
      }
      return r;
    }
    
    private int distance(int[] point) {
      return point[0] * point[0] + point[1] * point[1];
    }
    
    // Divide and Conquer
    /**
     * 思路：基于快速排序的quick select。不需要对所有元素排序，只需要选择指定的范围即可
     * 每个数组元素使得在切分点pivot左边元素都<=pivot，右边都>pivot，然后返回的切分点下标mid，
     * 如果[0-mid]的元素数量q<K，元素较少则继续切分[mid+1,hi]。
     * 如果q>K，元素过多继续切分[lo, mid-1]。
     * 如果q==K则刚好满足停止。
     * 时间复杂度：与快排类似，平均为O(N)，最坏为N^2/2
     * 空间复杂度：O(N)
     * @param points
     * @param K
     * @return
     */
    public int[][] kClosestByDivideAndConquer(int[][] points, int K) {
      sort(points, 0, points.length-1, K);
      return Arrays.copyOf(points, K);
    }
    
    private void sort(int[][] points, int lo, int hi, int K) {
      // K 表示选择多少个元素
      K -=1;
      // 切分元素直到满足K个元素
      while (lo <= hi) {
        int mid = partition(points, lo, hi);
        if (mid == K)
          break;
        else if (mid < K)
          lo = mid + 1;
        else if (mid > K)
          hi = mid-1;
      }
    }
    
    private int partition(int[][] points, int lo, int hi) {
      int pivot = distance(points[lo]);
      int i = lo, j = hi+1;
      while (true) {
        while (distance(points[++i]) <= pivot && i < hi);
        while (distance(points[--j]) > pivot);
        if (i >= j)
          break;
        swap(points, i, j);
      }
      swap(points, lo, j);
      return j;
    }
    
    private void swap(int[][] a, int i, int j) {
      int[] tmp = a[i];
      a[i] = a[j];
      a[j] = tmp;
    }
  }
  
  public static void main(String[] args) {
    Solution s = new Solution();
//    int[][] a = {{1,3},{-2,2}};
    int[][] a = {{-2,10},{-4,-8},{10,7},{-4,-7}};
    System.err.println(Arrays.deepToString(s.kClosest(a, 3)));;
  }
}