package com.xs.y2021.m7;

/**
 * @program: learn_root
 * @description: 二分法
 * url:https://leetcode-cn.com/problems/search-insert-position/
 *
 * @author: xs-shuai.com
 * @create: 2021-07-29 17:14
 **/
public class Solution2 {
    public int search(int[] nums, int target) {
        int start= 0;
        int end= nums.length-1;
        int middle= start + (end - start) / 2;
        return find(nums, target, start, end, middle);
    }

    private int find(int[] nums, int target, int start, int end, int middle) {
        if (nums[middle]== target){
            return middle;
        }
        if (start>=end){
           return nums[middle]>target? middle:middle+1;

        }
        if (nums[middle]> target){
            end=middle-1;
            middle=start + (end - start) / 2;
            return  find(nums,target,start,end,middle);
        }
        if (nums[middle]< target){
            start=middle+1;
            middle=start + (end - start) / 2;
            return  find(nums,target,start,end,middle);
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] nums = {2,5};
        int target = 3;
        Solution2 solution=new Solution2();
        int search = solution.search(nums, target);
        System.out.println(search);
    }
}
