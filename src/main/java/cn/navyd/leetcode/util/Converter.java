package cn.navyd.leetcode.util;

public interface Converter<I, O> {
  O convert(I in);
}
