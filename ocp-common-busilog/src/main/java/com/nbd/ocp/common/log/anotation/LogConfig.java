package com.nbd.ocp.common.log.anotation;

import com.nbd.ocp.common.log.writer.ILogWriter;
import com.nbd.ocp.common.log.writer.CloudRestLogWriter;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 需要业务日志的方法注解
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface LogConfig {

	/**
	 * 业务代码，必填
	 * @return
	 */
	String busiCode() default "" ;

	/**
	 * 操作的动作描述，必填
	 * @return
	 */
	String operationCode();

	/**
	 * 日志类型代码，非必填
	 * @return
	 */
	String logTypeCode() default "busiLog";

	/**
	 * 预留，使用方法参数标识动作
	 * @return
	 */
	String[] paramNames() default  {};

	/**
	 * 预留，自定义日志输出工具类
	 * @return
	 */
	Class<? extends ILogWriter>   writer() default CloudRestLogWriter.class;
}
