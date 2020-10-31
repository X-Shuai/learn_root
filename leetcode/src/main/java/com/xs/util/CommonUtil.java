package com.xs.util;

/**
 * @program: learn_root
 * @description:
 * @author: xs-shuai.com
 * @create: 2020-10-31 17:02
 **/
public class CommonUtil {

    public  static void swap(int i,int j,int[] nums){
        int a = nums[i];
        nums[i] = nums[j];
        nums[j] = a;
    }
}
