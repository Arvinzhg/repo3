package com.aspect;

import com.alibaba.fastjson.JSON;
import com.annotation.Cache;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

@Component
public class CacheAspect {

    @Autowired
    private Jedis jedis;

    public Object roundMethod(ProceedingJoinPoint pj) {
        System.out.println("***************start********************");
         //获取被调用者(即哪个类,此处是StudentDao类的对象)
        Object target = pj.getTarget();
        Signature signature = pj.getSignature();
        //获取被调用的方法名称
        String methodName = signature.getName();
        //获取每一个调用的方法的参数
        Object[] args = pj.getArgs();
        Object res = null;

        Class[] argsClass = new Class[args.length];
        //构造参数类型
        for (int i = 0; i < args.length; i++) {
            //每一个方法的参数的字节码对象
            argsClass[i] = args[i].getClass();
        }
        //得到被调用类的字节码对象
        Class<?> targetClass = target.getClass();
        try {
            Method method = targetClass.getMethod(methodName, argsClass);
            //targetClass.getCanonicalName()得到的是:包名.target类的类名    com.dao.StudentDao
            String key = targetClass.getCanonicalName() + "." + methodName;
            System.out.println("key是:"+key);

            //判断方法是否加有注解Cache
            if (method.isAnnotationPresent(Cache.class)) {
                if (!jedis.exists(key)) {
                    System.out.println("没有缓存");
                    //调用方法,查询数据库
                    res = method.invoke(target, args);
                    //把查询结果转换成json类型的字符串,存到缓存中
                    jedis.set(key, JSON.toJSONString(res));
                } else {
                    System.out.println("缓存");
                    String resStr = jedis.get(key);
                    //如果返回值类型是list,   java.util.List
                    //java.lang.reflect.Method.getReturnType()方法返回一个Class对象，该对象表示此Method对象表示的方法的正式返回类型。
                    if (method.getReturnType().getCanonicalName().equals(List.class.getCanonicalName())) {
                        //获取被执行的方法返回值类型
                        Type genericReturnType = method.getGenericReturnType();
                         //getActualTypeArguments()是子类ParameterizedType的方法,需要向下强转才能调用
                        if (genericReturnType instanceof ParameterizedType) {
                            Type[] actualTypeArguments = ((ParameterizedType) genericReturnType).getActualTypeArguments();
                            Class<?> actulClass = Class.forName(actualTypeArguments[0].getTypeName());
                            res = JSON.parseArray(resStr, actulClass);
                        }
                    } else {
                        res = JSON.parseObject(resStr, method.getReturnType());
                    }
                }
            } else {
                System.out.println("normal execute...");
                res = pj.proceed();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } catch (Throwable throwable) {
            throwable.printStackTrace();
        }
        System.out.println("***************end********************");
        return res;
    }

}
