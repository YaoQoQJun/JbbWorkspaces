package com.jybb.annotation;
/**
 * mybatis自定义标记
 * @author 姚俊
 *
 */
public @interface MybatisRepository {
	String value() default "";
}
