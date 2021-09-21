package com.lucas.common.util;


import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author: ren
 * @program: jump-spider-rpc
 * @description: 反射工具类
 * @create: 2021-09-12 18:04
 **/
@Slf4j
public class ReflectUtil {
    /**
     * @Author: ren
     * @Description: 通过反射的方式执行方法
     * @Param: [targetObject, method, arg]
     * @return: java.lang.Object
     * @Date: 2021/9/12
     */
    public static Object reflectHandle(Object targetObject,String methodName,Class[] argsClass,Object[] args) throws Exception{
        if (targetObject == null) {
            log.warn("目标对象为null，不能反射执行相应的方法：{}", methodName);
            return null;
        }
        // 通过反射执行对象方法
        Method method = targetObject.getClass().getMethod(methodName, argsClass);
        return method.invoke(targetObject,args);
    }
}
