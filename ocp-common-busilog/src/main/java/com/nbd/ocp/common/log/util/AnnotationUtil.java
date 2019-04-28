package com.nbd.ocp.common.log.util;
/*
                       _ooOoo_
                      o8888888o
                      88" . "88
                      (| -_- |)
                      O\  =  /O
                   ____/`---'\____
                 .'  \\|     |//  `.
                /  \\|||  :  |||//  \
               /  _||||| -:- |||||-  \
               |   | \\\  -  /// |   |
               | \_|  ''\---/''  |   |
               \  .-\__  `-`  ___/-. /
             ___`. .'  /--.--\  `. . __
          ."" '<  `.___\_<|>_/___.'  >'"".
         | | :  `- \`.;`\ _ /`;.`/ - ` : | |
         \  \ `-.   \_ __\ /__ _/   .-` /  /
    ======`-.____`-.___\_____/___.-`____.-'======
                       `=---='
    ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
             佛祖保佑       永无BUG
*/


import org.aspectj.lang.JoinPoint;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.ProxyMethodInvocation;
import org.springframework.aop.aspectj.MethodInvocationProceedingJoinPoint;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author jin
 */

public class AnnotationUtil {
    private final static Logger logger= LoggerFactory.getLogger(AnnotationUtil.class);

    public static <T extends Annotation> T getMethodLogConfig(Method method,Class<T> clazz){
        if (method.isAnnotationPresent(clazz)) {
            return method.getAnnotation(clazz);
        }
        return null;
    }
    public static <T extends Annotation> T getMethodLogConfig(JoinPoint joinPoint,Class<T> clazz){
        Method method=invocationMethod(joinPoint);
        return getMethodLogConfig(method,clazz);
    }
    public static Method invocationMethod(JoinPoint joinPoint) {
        try {
            Field methodInvocationField = MethodInvocationProceedingJoinPoint.class.getDeclaredField("methodInvocation");
            methodInvocationField.setAccessible(true);
            ProxyMethodInvocation methodInvocation = (ProxyMethodInvocation) methodInvocationField.get(joinPoint);
            return methodInvocation.getMethod();
        } catch (NoSuchFieldException e) {
            logger.error("invocationMethod NoSuchFieldException return null" + e.getMessage());
            return null;
        } catch (IllegalAccessException e) {
            logger.error("invocationMethod IllegalAccessException return null" + e.getMessage());
            return null;
        }
    }

    public static <T extends Annotation> T getClassLogConfig(JoinPoint joinPoint, Class<T> clazz) {
        Class<?> targetClazz=joinPoint.getTarget().getClass();
        Annotation annotation = targetClazz.getAnnotation(clazz);
        if(annotation==null){
            return null;
        }else{
            return (T) annotation;
        }
    }
}
