package cn.navyd.leetcode.sort;

import java.util.Map.Entry;

import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;

import java.util.Arrays;
import java.util.NavigableMap;
import java.util.TreeMap;

/**
 * <pre>
 N cars are going to the same destination along a one lane road.  The destination is target miles away.

Each car i has a constant speed speed[i] (in miles per hour), and initial position position[i] miles towards the target along the road.

A car can never pass another car ahead of it, but it can catch up to it, and drive bumper to bumper at the same speed.

The distance between these two cars is ignored - they are assumed to have the same position.

A car fleet is some non-empty set of cars driving at the same position and same speed.  Note that a single car is also a car fleet.

If a car catches up to a car fleet right at the destination point, it will still be considered as one car fleet.


How many car fleets will arrive at the destination?
 * </pre>
 */
public interface CarFleet {
    /**
     * 思路：
     * 如何处理时间与距离：如果相邻两车中前车到达终点时间 < 后车到达时间，则后车不可能追上前车成一个车队。
     * 否则是一个fleet
     * 如何确定是一个车队：由于后面的车不能超过前面，一个车队的到达时间始终是该车队第1个时间。
     * 如果lead时间>后车时间 => 合并一个车队，注意下一次车应与lead车比较。
     * @param target
     * @param position
     * @param speed
     * @return
     */
    public int carFleet(int target, int[] position, int[] speed);

    @Author(value = "lee215", references = "https://leetcode.com/problems/car-fleet/discuss/139850/C%2B%2BJavaPython-Straight-Forward")
    public static class SolutionBySort implements CarFleet {
        /**
         * 
         * 时间复杂度：归并排序O(n log n)
         * 空间：O(N)
         */
        @Override
        public int carFleet(int target, int[] position, int[] speed) {
            final int n = position.length;
            if (n == 0)
                return 0;
            // 0. position and arrived time pair array
            final double[][] cars = new double[n][2];
            for (int i = 0; i < n; i++)
                cars[i] = new double[] { position[i], (target - position[i]) / (double) speed[i] };
            // 1. sort by position order
            Arrays.sort(cars, (a, b) -> Double.compare(a[0], b[0]));
            // fleet is the lead 
            double fleetTime = 0;
            int count = 0;
            for (int i = n - 1; i >= 0; i--)
                // 2. not a fleet if car.time > leadfleet.time, as a new fleet
                if (cars[i][1] > fleetTime) {
                    count++;
                    fleetTime = cars[i][1];
                }
            return count;
        }
    }

    public static void main(String[] args) {
        CarFleet p = new SolutionBySort();
        int target = 12;
        int[] position = {10,8,0,5,3}, speed = {2,4,1,1,3};
        System.out.println(p.carFleet(target, position, speed));;
    }
}