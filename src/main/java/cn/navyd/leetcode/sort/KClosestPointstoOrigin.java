package cn.navyd.leetcode.sort;

import java.util.ArrayList;
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
}
