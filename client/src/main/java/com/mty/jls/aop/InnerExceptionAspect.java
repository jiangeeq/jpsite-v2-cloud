//package com.mty.jls.aop;
//
//import lombok.extern.slf4j.Slf4j;
//import org.aspectj.lang.JoinPoint;
//import org.aspectj.lang.annotation.After;
//import org.aspectj.lang.annotation.AfterThrowing;
//import org.aspectj.lang.annotation.Aspect;
//import org.aspectj.lang.annotation.Before;
//import org.aspectj.lang.annotation.Pointcut;
//import org.slf4j.LoggerFactory;
//import org.springframework.aop.support.AopUtils;
//import org.springframework.stereotype.Component;
//
//import java.io.IOException;
//import java.lang.reflect.Field;
//
///**
// * @author jiangpeng
// * @date 2020/10/1410:58
// */
//@Aspect
//@Slf4j
//@Component
//public class InnerExceptionAspect {
//
//    @Pointcut("execution(* com.mty.jls.config.security.filter.ValidateCodeFilter.*(..))")
//    private void filterPointcut(){}
//
////    @Pointcut("execution(* com.mty.jls.controller.HelloController.*(..))")
////    private void filterPointcut(){}
//
//    @Before("filterPointcut()")
//    public void beforeProcess(JoinPoint joinPoint)  {
//        String className = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
//        log.info("进入 before filterPointcut");
//    }
//
//    @After("filterPointcut()")
//    public void afterProcess(JoinPoint joinPoint)  {
//        Object  proxy  = joinPoint.getThis();
//        Object  target   = joinPoint.getTarget();
//        if(AopUtils.isAopProxy(proxy)){
//            try {
//                Class<?> proxyClass = proxy.getClass();
//                Class<?> targetClass = target.getClass();
//
//                Field f = proxyClass.getDeclaredField("logger");
//                if(f != null) {
//                    f.setAccessible(true);
//                    f.set(proxy, LoggerFactory.getLogger(proxyClass));
//                }
//            } catch (Exception e) {
//                e.printStackTrace();//记录好异常进行处理
//            }
//        }
//    }
//
//
//    /**
//     *
//     * @param joinPoint 拦截包或者子包中定义的方法
//     * @param ex 目标方法抛出的异常
//     */
//    @AfterThrowing(throwing = "ex", pointcut = "filterPointcut()")
//    public void filterException(JoinPoint joinPoint, Throwable ex) throws IOException {
//        String className = joinPoint.getTarget().getClass().getName();
//        String methodName = joinPoint.getSignature().getName();
//        Object[] args = joinPoint.getArgs();
////        response.setContentType("application/json;charset=utf-8");
////        PrintWriter out = response.getWriter();
////        out.write(ex.getMessage());
////        out.flush();
////        out.close();
//
//    }
//}
