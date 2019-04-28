package com.nbd.ocp.common.log.anotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要业务日志的方法注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LogBeanConfig {

	/**
	 * 业务代码，必填
	 * @return
	 */
	String busiCode() ;
}
