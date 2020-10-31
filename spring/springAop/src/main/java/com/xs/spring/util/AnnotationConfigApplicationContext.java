package com.xs.spring.util;

import com.xs.spring.anno.Xs;

import java.io.File;

/**
 * @program: learn_root
 * @description: 注入上下文
 * @author: xs-shuai.com
 * @create: 2020-07-11 21:10
 **/
public class AnnotationConfigApplicationContext {

    /****
     * 读取target后的class ,class.forName() 引入进来
     * @param basePackage
     */
    public void scan(String basePackage){
        String rootPath = this.getClass().getResource("/").getPath();
        String basePackagePath = basePackage.replaceAll("\\.","\\\\");
        File file = new File(rootPath + "\\" + basePackagePath);
        String[] names = file.list();
        for (String name : names) {
            name = name.replaceAll(".class", "");
            try {
                Class<?> aClass = Class.forName(basePackage + "." + name);
                //判断是否包含注解
                if (aClass.isAnnotationPresent(Xs.class)){
                    Xs xs = aClass.getAnnotation(Xs.class);
                    System.out.println(xs.value());
                    System.out.println(aClass.newInstance());
                }


            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InstantiationException e) {
                e.printStackTrace();
            }
        }


    }
}
