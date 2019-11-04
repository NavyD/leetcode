# Sorting

## Algorithms

### Bubble Sort

```java
  public static void sort(int[] arr) {
    for (int i = 0; i < arr.length; i++) {
      final int len = arr.length-i;
      for (int next = 1; next < len; next++) {
        int prev = next-1;
        if (arr[next] < arr[prev]) {
          int tmp = arr[prev];
          arr[prev] = arr[next];
          arr[next] = tmp;
        }
      }
    }
  }
```

### Merge Sort

Merge

```java
void merge(int a[], int low, int mid, int high) {
  // subarray1 = a[low..mid], subarray2 = a[mid+1..high], both sorted
  int N = high-low+1;
  int b[N]; // discuss: why do we need a temporary array b?
  int left = low, right = mid+1, bIdx = 0;
  while (left <= mid && right <= high) // the merging
    b[bIdx++] = (a[left] <= a[right]) ? a[left++] : a[right++];
  while (left <= mid) b[bIdx++] = a[left++]; // leftover, if any
  while (right <= high) b[bIdx++] = a[right++]; // leftover, if any
  for (int k = 0; k < N; k++) a[low+k] = b[k]; // copy back
}
```

> 为何递归时间复杂度为O(log N)

递归是对半的，也就是一个二叉树，结点数n与高度h的关系:`h<=n<=(2^h)-1` ==> h = lg N

即 对于长度N的数组，递归的时间复杂度为O(log N)

> 为何基于比较的排序算法时间复杂度下界是`O(N log N)`

完全二叉树证明

## Apply

### 检查有序

```java
public boolean isSorted(T[] array) {
  for (int i = 1; i < array.length; i++)
    if (array[i-1].compareTo(array[i]) > 0)
      return false;
  return true;
}
```

时间复杂度：O(N);worst为array已经有序，best为array逆序

idea:如果array特别巨大，可以考虑并行算法，特别是检查文件

