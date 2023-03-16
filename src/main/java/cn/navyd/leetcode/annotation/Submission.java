package cn.navyd.leetcode.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 表示一个solution的提交结果
 *
 * @author navyd
 */
@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(Submissions.class)
public @interface Submission {
  String date();

  /**
   * 运行时间。单位 ms。默认值表示未定义
   *
   * @return
   */
  int runtime();

  /**
   * 运行时间击败率
   *
   * @return
   */
  double runtimeBeatRate();

  /**
   * 使用内存。单位 MB。默认值表示未定义
   *
   * @return
   */
  double memory();

  /**
   * 内存击败比率
   *
   * @return
   */
  double memoryBeatRate();

  /**
   * 提交结果页面url。
   * <p>
   * 注意：该功能是关联用户的，需要leetcode 用户登录
   *
   * @return
   */
  String url();
}