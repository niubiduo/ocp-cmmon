package com.nbd.ocp.common.repository.multiTenancy.discriminator.aspects;

import com.nbd.ocp.core.context.threadlocal.InvocationInfoProxy;
import com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpCurrentTenant;
import com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpMultiTenancy;
import com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpWithoutTenant;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.hibernate.Session;
import org.springframework.context.annotation.Conditional;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;

@Aspect
@Component
@Conditional(OcpMultiTenancyDiscriminatorCondition.class)
public class OcpMultiTenancyDiscriminatorAspect {

  @PersistenceContext
  private  EntityManager em;

  @Pointcut(value = "(@within(com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpCurrentTenant) || @annotation(com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpCurrentTenant))")
  void hasCurrentTenantAnnotation() {}

  @Pointcut(value = "@within(com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpMultiTenancy) || @annotation(com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpMultiTenancy)")
  void hasTenantAnnotation() {}

  @Pointcut(value = "@within(com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpWithoutTenant) || @annotation(com.nbd.ocp.common.repository.multiTenancy.discriminator.annotations.OcpWithoutTenant)")
  void hasWithoutTenantAnnotation() {}

  @Pointcut(value = "@annotation(org.springframework.data.jpa.repository.Query)")
  void hasNativeQueryAnnotation() {}

  @Pointcut(value = "hasCurrentTenantAnnotation() || hasTenantAnnotation() || hasWithoutTenantAnnotation()")
  void hasMultiTenantAnnotation() {}


  @PersistenceContext
  public EntityManager entityManager;

  @Around("execution(public * *(..)) && hasMultiTenantAnnotation() ")
  public Object aroundExecution(ProceedingJoinPoint pjp) throws Throwable {
    Method method=getMethod(pjp);
    Annotation multiTenantAnnotation = getTenantAnnotation(method);
    if (multiTenantAnnotation != null && !(multiTenantAnnotation instanceof OcpWithoutTenant)) {
      String tenantId = InvocationInfoProxy.getTenantId();
//      if (multiTenantAnnotation instanceof OcpMultiTenancy) {
//        tenantId = ((OcpMultiTenancy) multiTenantAnnotation).value();
//      }
      org.hibernate.Filter filter = entityManager.unwrap(Session.class).enableFilter("tenantFilter");
      filter.setParameter("tenantId", tenantId);
      filter.validate();
    }
    return pjp.proceed();
  }

//  @Around(value="execution(public * *(..)) && hasMultiTenantAnnotation() && hasNativeQueryAnnotation() ")
//  public Object aroundNativeExecution(ProceedingJoinPoint pjp) throws Throwable {
//    Method method=getMethod(pjp);
//    Annotation multiTenantAnnotation = getTenantAnnotation(method);
//    if (multiTenantAnnotation != null && !(multiTenantAnnotation instanceof OcpWithoutTenant) ) {
//      String tenantId = OcpTenantContextHolder.getContext().getTenantId();
////      if (multiTenantAnnotation instanceof OcpMultiTenancy) {
////        tenantId = ((OcpMultiTenancy) multiTenantAnnotation).value();
////      }
//
////      Query queryAnnotation = method.getAnnotation(Query.class);
////      if (queryAnnotation != null&&queryAnnotation.nativeQuery()){
////        InvocationHandler invocationHandler = Proxy.getInvocationHandler(queryAnnotation);
////        Field value = invocationHandler.getClass().getDeclaredField("memberValues");
////        value.setAccessible(true);
////        Map<String, Object> memberValues = (Map<String, Object>) value.get(invocationHandler);
////        memberValues.put("value","test");
////      }
//    }
//    return pjp.proceed();
//  }

  private Method getMethod(ProceedingJoinPoint pjp){
    final MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    Method method = methodSignature.getMethod();
    return method;
  }

  private Annotation getTenantAnnotation(Method method){

    Annotation multiTenantAnnotation = getMultiTenantAnnotation(method);
    if (multiTenantAnnotation == null) {
      multiTenantAnnotation = getMultiTenantAnnotation(method.getDeclaringClass());
    }
    return multiTenantAnnotation;
  }

  private Annotation getMultiTenantAnnotation(AnnotatedElement element) {
    Annotation annotation = element.getAnnotation(OcpCurrentTenant.class);
    if (annotation != null) {
      return annotation;
    }
    annotation = element.getAnnotation(OcpMultiTenancy.class);
    if (annotation != null) {
      return annotation;
    }
    annotation = element.getAnnotation(OcpWithoutTenant.class);
    if (annotation != null) {
      return annotation;
    }
    return null;
  }
}
