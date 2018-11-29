package com.demo;

import com.entity.Student;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

public class Demo2 {

    public List methodDemo() {
        return null;
    }

    public static void main(String[] args) {
        try {
            Method methodDemo = Demo2.class.getMethod("methodDemo");
            System.out.println(methodDemo.getReturnType());
            System.out.println(methodDemo.getGenericReturnType());
            Type genericReturnType = methodDemo.getGenericReturnType();
            System.out.println(genericReturnType.toString());
            System.out.println("--------------------");
            if(genericReturnType instanceof ParameterizedType){
                Type[] actualTypeArguments = ((ParameterizedType)genericReturnType).getActualTypeArguments();
                System.out.println(actualTypeArguments[0]);
                System.out.println("---------------------------");
                for (Type type : actualTypeArguments) {
                    System.out.println(type.getTypeName());
                }
            }

        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }
}
