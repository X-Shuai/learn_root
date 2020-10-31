package com.xs.arrays;

/**
 * @program: learn_root
 * @description: 爬楼梯
 * @author: xs-shuai.com
 * @create: 2020-09-14 00:16
 **/

/***
 * 假设你正在爬楼梯。需要 n 阶你才能到达楼顶。
 *
 * 每次你可以爬 1 或 2 个台阶。你有多少种不同的方法可以爬到楼顶呢？
 *
 * 注意：给定 n 是一个正整数。
 */
public class ClimbStairs {

    /***
     * 1. 1
     * 2. 2
     * 3. 3
     * 4. 5
     * 斐波拉切数列
     * @param n
     * @return
     */
    public int climbStairs(int n) {

        int first = 1;
        int second = 2;
        int step=0;
        if(n<second){
            return n;
        }
        for (int i = 3 ; i <= n; i++) {
            step = second + first;
            first = second;
            second = step;
        }
        return second;
    }
}
