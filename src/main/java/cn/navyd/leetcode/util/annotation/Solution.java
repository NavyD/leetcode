package cn.navyd.leetcode.util.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示是一个算法的解决方案
 * @see Problem
 * @author navyd
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.SOURCE)
@Inherited
@Documented
public @interface Solution {
  /**
   * 表示该solution解决的问题class定义。默认从注解class中提取super class 或 super interface的class
   * @return
   */
  Class<?> problem() default Object.class;
  
  /**
   * 表示该solution的状态。如已通过，错误，正在做
   * @return
   */
  SolutionStatus status();
  
  /**
   * 时间复杂度。默认值表示未定义
   * @return
   */
  Complexity timeComplexity() default Complexity.NONE;
  
  /**
   * 空间复杂度。默认值表示未定义
   * @return
   */
  Complexity spaceComplexity() default Complexity.NONE;
  
  /**
   * 运行时间。单位 ms。默认值表示未定义
   * @return
   */
  int runtime() default -1;
  
  /**
   * 运行时间超过的百分比
   * @return
   */
  double runtimeBeats() default -1;
  
  /**
   * 使用内存。单位 MB。默认值表示未定义
   * @return
   */
  double memory() default -1;
  
  /**
   * 内存超过百分比
   * @return
   */
  double memoryBeats() default -1;
  
  /**
   * 解决方案提供的时间。格式：{@linkplain java.time.format.DateTimeFormatter#ISO_LOCAL_DATE yyyy-MM-dd}
   * @return
   */
  String date() default "";
  
  /**
   * 参考的url地址
   * @return
   */
  String[] referenceUrls() default "";
  
  String[] comments() default "";
  
  public static enum SolutionStatus {
    /**
     * 表示正在完成，不是标准的leetcode status
     */
    DOING,
    WRONG,
    ACCEPTED;
  }
  
  public static enum Complexity {
    /**
     * 表示未设置复杂度
     */
    NONE("none"),
    O_1("O(1)"),
    O_N("O(N)"),
    O_LOG_N("O(log N)"),
    O_N_LOG_N("O(N log N)"),
    O_N_POW_2("O(N^2)"),
    ;
    private final String description;
    
    private Complexity(String description) {
      this.description = description;
    }
    
    public String getDescription() {
      return description;
    }
  }
}
