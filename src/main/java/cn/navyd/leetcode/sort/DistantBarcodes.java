package cn.navyd.leetcode.sort;

public interface DistantBarcodes {
  public int[] rearrangeBarcodes(int[] barcodes);

  public class SolutionByCounting implements DistantBarcodes {
    /**
     * 思路：
     * 如何证明先安排最大的barcode后，其它可以随便，而不是接着第2大，第3大。。
     * 假定数组元素是可以互不相邻的，即存在最大元素数量<=(length+1)/2成立，剩余
     * 元素数量可用位置>=length/2，只要从0,2,4...偶数下标放最大元素，那么都可以放下
     */
    @Override
    public int[] rearrangeBarcodes(int[] barcodes) {
      // 0. count index
      final int n = barcodes.length;
      final int[] counts = new int[10001];
      for (int i : barcodes) {
        counts[i]++;
      }
      // 1. find max index
      int maxBarcode = 0;
      for (int i : barcodes) {
        if (counts[i] > counts[maxBarcode]) {
          maxBarcode = i;
        }
      }
      // 2. arrange the max index
      final int[] res = new int[n];
      int idx = -2;
      while (counts[maxBarcode]-- > 0) {
        res[idx += 2] = maxBarcode;
      }
      // 3. arrange others
      for (int i : barcodes) {
        while (counts[i]-- > 0) {
          res[idx = (idx += 2) >= n ? 1 : idx] = i;
        }
      }
      return res;
    }
  }
}

