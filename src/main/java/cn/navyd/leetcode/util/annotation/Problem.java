package cn.navyd.leetcode.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示这是一个算法问题定义class
 * @see Solution
 * @author navyd
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Documented
public @interface Problem {
  /**
   * @see #title()
   * @return
   */
  String value() default "";
  
  /**
   * 问题的标题，默认为class的名称
   * @return
   */
  String title() default "";
  
  /**
   * 问题的链接url
   * @return
   */
  String url();
  
  /**
   * 标签
   * @return
   */
  Tag[] tags();
  
  /**
   * 问题的编号。
   * @return
   */
  short number();
  
  /**
   * 问题的难度
   * @return
   */
  Difficulty difficulty();
  
  /**
   * 解决的次数
   * @return
   */
  byte resolvedCount();
  
  public static enum Tag {
    NONE,
    SORT,
    ;
  }
  
  public static enum Difficulty {
    EASY,
    MEDIUM,
    HARD,
    ;
  }
}
