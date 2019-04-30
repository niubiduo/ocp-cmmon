package com.nbd.ocp.common.busilog.aspect;

import com.nbd.ocp.common.busilog.process.BusiLogManager;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;




/**
 * @author jhb
 */
@Component
@Aspect
public class LogAspect {

    private Logger logger = LoggerFactory.getLogger(LogAspect.class);


    @Autowired
    BusiLogManager busiLogManager;

    @Pointcut(value = "@annotation(com.nbd.ocp.common.busilog.anotation.LogConfig)")
    void hasLogAnnotation() {}


    @AfterReturning(value="execution(public * *(..)) && hasLogAnnotation() ",returning="retValue")
    public void aroundExecution(JoinPoint joinPoint, Object retValue){
        busiLogManager.log(joinPoint,retValue,null);
    }
    @AfterThrowing(value="execution(public * *(..)) && hasLogAnnotation() ",throwing="error")
    public void aroundExecution(JoinPoint joinPoint, Throwable error){
        busiLogManager.log(joinPoint,null,error);
    }



}