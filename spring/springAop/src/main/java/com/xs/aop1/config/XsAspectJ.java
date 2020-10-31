package com.xs.aop1.config;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

/**
 * @program: learn_root
 * @description: AspectJ
 * @author: xs-shuai.com
 * @create: 2020-07-05 18:18
 **/
@Component
@Aspect
public class XsAspectJ {

    /**
     * 切点
     * (修饰符? 返回类型,  方法全路径 (参数)
     *
     */
    @Pointcut("execution(* com.xs.aop1.dao.*.*(..))")
     public void pointcutExecution(){

     }

    /***
     * 只能定位到类
     */
    @Pointcut("within(com.xs.aop1.dao.*)")
    public void pointcutWithin(){

    }
    /***
     * 定义参数类型
     */
    @Pointcut("args(java.lang.String)")
    public void pointcutArgs(){

    }
    /***
     * 注解的方法
     */
    @Pointcut("@annotation(com.xs.aop1.annotation.Xs)")
    public void pointcutAnnotation(){

    }
    /***
     * 注解的方法
     */
    @Pointcut("this(com.xs.aop1.dao.Dao)")
    public void pointcutThis(){

    }
    /***
     * 注解的方法
     */
    @Pointcut("target(com.xs.aop1.dao.Dao)")
    public void pointcutTarget(){

    }


    /**
     * 通知 在之前通知
     */
     @Before("pointcutTarget()")
     public void before(JoinPoint joinPoint){
         System.out.println("before");
         System.out.println(joinPoint.getThis());
     }

     @Around("pointcutExecution()")
    public void around(ProceedingJoinPoint proceedingJoinPoint){

         try {
             proceedingJoinPoint.proceed();
         } catch (Throwable throwable) {
             throwable.printStackTrace();
         }
     }

}
