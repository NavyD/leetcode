package cn.navyd.leetcode.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示这是一个算法问题定义class
 * <p>规则
 * <ol>
 * <li>仅能被定义在interface
 * </ol>
 * @see Solution
 * @author navyd
 *
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Problem {
  /**
   * 问题的标题，默认为class的名称
   * @return
   */
  String name() default "";
  
  /**
   * 问题的链接url。默认的表示从interface文件名获取
   * @return
   */
  String url() default "";
  
  /**
   * 问题的编号。
   * @return
   */
  short number();
  
  /**
   * 问题的难度
   * @return
   */
  DifficultyEnum difficulty();

  enum DifficultyEnum {
    EASY, MEDIUM, HARD,;
  }
  
}

