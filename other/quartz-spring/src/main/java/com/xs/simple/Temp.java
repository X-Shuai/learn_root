package com.xs.simple;

/**
 * @program: quartz
 * @description: tmp
 * @author: xs-shuai.com
 * @create: 2020-10-30 11:13
 **/
public class Temp {

    private int number;

    private static  Temp instance;

    public static Temp getTmp(int number) {
    if (instance==null){
        instance= new Temp(number);
    }
        return instance;

    }

    private Temp(int number) {
        this.number = number;
    }

    private Temp() {
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }
}
