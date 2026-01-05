package com.sky.aspect;

import com.sky.annotation.AutoFill;
import com.sky.constant.CommonFieldSetterConstant;
import com.sky.context.BaseContext;
import com.sky.enumeration.SqlOperationType;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.LocalDateTime;

/**
 * 自定义切面类，统一拦截并处理带有@AutoFill注解的方法
 * 对多张表出现的公共字段进行自动填充
 */
@Aspect
@Component
@Slf4j
public class AutoFillAspect {

    // 切入点
    // @Pointcut("@annotation(com.sky.annotation.AutoFill)")这种写法也行，但是由于会扫描所有包，导致效率低下
    @Pointcut("execution(* com.sky.mapper.*.*(..)) && @annotation(com.sky.annotation.AutoFill)")
    public void autoFillPointCut(){}

    // 前置通知，在通知中对公共字段赋值
    @Before("autoFillPointCut()")
    public void autoFill(JoinPoint joinPoint) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        log.info("开始对公共字段进行自动填充...");

        // 1.获取被拦截方法的SQL操作类型
        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        AutoFill autoFill = signature.getMethod().getAnnotation(AutoFill.class);
        SqlOperationType sqlOperationType=autoFill.value();

        // 2.获取被拦截方法的入参，也就是实体对象
        Object[] args = joinPoint.getArgs();
        if(args==null){
            return;
        }
        Object entity=args[0];

        // 3.准备即将赋给实体对象公共字段的数据
        LocalDateTime now = LocalDateTime.now();
        Long currentId = BaseContext.getCurrentId();

        // 4.根据SQL操作类型，通过反射为对应的公共字段赋值
        Class c=entity.getClass();
        Method setCreateTime = c.getDeclaredMethod(CommonFieldSetterConstant.SET_CREATE_TIME, LocalDateTime.class);
        Method setCreateUser = c.getDeclaredMethod(CommonFieldSetterConstant.SET_CREATE_USER, Long.class);
        Method setUpdateTime = c.getDeclaredMethod(CommonFieldSetterConstant.SET_UPDATE_TIME, LocalDateTime.class);
        Method setUpdateUser = c.getDeclaredMethod(CommonFieldSetterConstant.SET_UPDATE_USER, Long.class);

        // 若是INSERT操作，则为create_time, create_user, update_time, update_user这4个字段赋值
        if(sqlOperationType==SqlOperationType.INSERT){
            setCreateTime.invoke(entity,now);
            setCreateUser.invoke(entity,currentId);
            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,currentId);
        }
        // 若是UPDATE操作，则只为update_time, update_user这2个字段赋值
        if(sqlOperationType==SqlOperationType.UPDATE){
            setUpdateTime.invoke(entity,now);
            setUpdateUser.invoke(entity,currentId);
        }
    }
}
