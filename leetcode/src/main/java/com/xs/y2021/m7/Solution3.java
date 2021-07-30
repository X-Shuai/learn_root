package com.xs.y2021.m7;

/**
 * @program: learn_root
 * @description: 二分法
 * url:https://leetcode-cn.com/problems/squares-of-a-sorted-array/
 * 给你一个按 非递减顺序 排序的整数数组 nums，返回 每个数字的平方 组成的新数组，要求也按 非递减顺序 排序。
 * @author: xs-shuai.com
 * @create: 2021-07-29 17:14
 **/
public class Solution3 {

    public int[] sortedSquares(int[] nums) {
        int[] numRe = new int[nums.length];
        int start = 0;
        int end = nums.length - 1;
        int i = nums.length - 1;
        while (start <= end) {
            int leftNumber = nums[start] * nums[start];
            int rightNumber = nums[end] * nums[end];
            if (rightNumber >= leftNumber) {
                numRe[i] = rightNumber;
                end--;
            }else {
                numRe[i] = leftNumber;
                start++;
            }
            i--;
        }
        return numRe;
    }

    /***
     * 1. 两头为最大往中间找(注意最后一个数)
     *
     * 挖坑使用二分法查询找
     *
     * @param args
     */
    public static void main(String[] args) {
        int[] nums = {-9, -4, -1, 2, 3, 10};
        Solution3 solution = new Solution3();
        int[] squares = solution.sortedSquares(nums);
        for (int square : squares) {
            System.out.println(square);
        }

    }
}
