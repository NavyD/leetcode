package cn.navyd.leetcode.sort;

import static org.assertj.core.api.Assertions.assertThat;
import org.junit.jupiter.api.Test;
import cn.navyd.leetcode.util.LeetCodeUtils;

public class LargestNumberTest {
  LargestNumber largestNumber;
  
  @Test
  public void largestNumberTest() {
    largestNumber = new SolutionByStringComparator();
    final String in = "[3,30,34,5,9]", expectedResult = "9534330";
    int[] nums = LeetCodeUtils.CONVERTER_STRING_TO_INT_ARRAY.convert(in);
    assertThat(largestNumber.largestNumber(nums)).isEqualTo(expectedResult);
  }
}
