package cn.navyd.leetcode.sort;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.navyd.annotation.algorithm.ComplexityEnum;
import cn.navyd.annotation.algorithm.SortAlgorithm;
import cn.navyd.annotation.algorithm.TimeComplexity;
import cn.navyd.annotation.leetcode.Author;
import cn.navyd.annotation.leetcode.DateTime;
import cn.navyd.annotation.leetcode.Submission;

/**
 * <pre>
 * Given the array restaurants where  restaurants[i] = [idi, ratingi, veganFriendlyi, pricei, distancei]. You have to filter the restaurants using three filters.
 *
 * The veganFriendly filter will be either true (meaning you should only include restaurants with veganFriendlyi set to true) or false (meaning you can include any restaurant). In addition, you have the filters maxPrice and maxDistance which are the maximum value for price and distance of restaurants you should consider respectively.
 *
 * Return the array of restaurant IDs after filtering, ordered by rating from highest to lowest. For restaurants with the same rating, order them by id from highest to lowest. For simplicity veganFriendlyi and veganFriendly take value 1 when it is true, and 0 when it is false.
 *
 *
 *
 * Example 1:
 *
 * Input: restaurants = [[1,4,1,40,10],[2,8,0,50,5],[3,8,1,30,4],[4,10,0,10,3],[5,1,1,15,1]], veganFriendly = 1, maxPrice = 50, maxDistance = 10
 * Output: [3,1,5]
 * Explanation:
 * The restaurants are:
 * Restaurant 1 [id=1, rating=4, veganFriendly=1, price=40, distance=10]
 * Restaurant 2 [id=2, rating=8, veganFriendly=0, price=50, distance=5]
 * Restaurant 3 [id=3, rating=8, veganFriendly=1, price=30, distance=4]
 * Restaurant 4 [id=4, rating=10, veganFriendly=0, price=10, distance=3]
 * Restaurant 5 [id=5, rating=1, veganFriendly=1, price=15, distance=1]
 * After filter restaurants with veganFriendly = 1, maxPrice = 50 and maxDistance = 10 we have restaurant 3, restaurant 1 and restaurant 5 (ordered by rating from highest to lowest).
 * Example 2:
 *
 * Input: restaurants = [[1,4,1,40,10],[2,8,0,50,5],[3,8,1,30,4],[4,10,0,10,3],[5,1,1,15,1]], veganFriendly = 0, maxPrice = 50, maxDistance = 10
 * Output: [4,3,2,1,5]
 * Explanation: The restaurants are the same as in example 1, but in this case the filter veganFriendly = 0, therefore all restaurants are considered.
 * Example 3:
 *
 * Input: restaurants = [[1,4,1,40,10],[2,8,0,50,5],[3,8,1,30,4],[4,10,0,10,3],[5,1,1,15,1]], veganFriendly = 0, maxPrice = 30, maxDistance = 3
 * Output: [4,5]
 *
 *
 * Constraints:
 *
 * 1 <= restaurants.length <= 10^4
 * restaurants[i].length == 5
 * 1 <= idi, ratingi, pricei, distancei <= 10^5
 * 1 <= maxPrice, maxDistance <= 10^5
 * veganFriendlyi and veganFriendly are 0 or 1.
 * All idi are distinct.
 * </pre>
 */
public interface FilterRestaurantsByVeganFriendlyPriceAndDistance {
  public List<Integer> filterRestaurants(int[][] restaurants, int veganFriendly, int maxPrice,
                                         int maxDistance);

  @Author(value = "hiepit", references = "https://leetcode.com/problems/filter-restaurants-by-vegan-friendly-price-and-distance/discuss/491469/Java-Straight-Forward-HashMap")
  @Submission(memory = 48.7, memoryBeatRate = 100, runtime = 10, runtimeBeatRate = 24.82, submittedDate = @DateTime("20200327"), url = "https://leetcode.com/submissions/detail/316279312/")
  @SortAlgorithm(timeComplexity = @TimeComplexity(average = ComplexityEnum.O_N_LOG_N), spaceComplexity = ComplexityEnum.O_N)
  public static class SolutionBySort implements FilterRestaurantsByVeganFriendlyPriceAndDistance {
    /**
     * 注意要先通过rating和id一起降序排序，如果仅排rating，然后反向add id是不行的
     */
    @Override
    public List<Integer> filterRestaurants(int[][] restaurants, int veganFriendly, int maxPrice,
                                           int maxDistance) {
      // 0. descendant sort by rating and id
      Arrays.sort(restaurants, (a, b) -> b[1] == a[1] ? b[0] - a[0] : b[1] - a[1]);
      // 1. filter with conditions
      List<Integer> res = new ArrayList<>(restaurants.length);
      for (int[] r : restaurants) {
        if (r[2] >= veganFriendly && r[3] <= maxPrice && r[4] <= maxDistance) {
          res.add(r[0]);
        }
      }
      return res;
    }
  }

  public static void main(String[] args) {
    int[][] restaurants = {{1, 4, 1, 40, 10}, {2, 8, 0, 50, 5}, {3, 8, 1, 30, 4}, {4, 10, 0, 10, 3},
        {5, 1, 1, 15, 1}};
    FilterRestaurantsByVeganFriendlyPriceAndDistance p = new SolutionBySort();
    int veganFriendly = 0, maxPrice = 50, maxDistance = 10;
    p.filterRestaurants(restaurants, veganFriendly, maxPrice, maxDistance);
  }
}