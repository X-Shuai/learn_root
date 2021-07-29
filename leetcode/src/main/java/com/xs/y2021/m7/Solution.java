package com.xs.y2021.m7;

/**
 * @program: learn_root
 * @description: 二分法
 * url :https://leetcode-cn.com/problems/binary-search/
 *
 *给定一个 n 个元素有序的（升序）整型数组 nums 和一个目标值 target  ，写一个函数搜索 nums 中的 target，如果目标值存在返回下标，否则返回 -1。
 *
 * 示例 1:
 *
 * 输入: nums = [-1,0,3,5,9,12], target = 9
 * 输出: 4
 * 解释: 9 出现在 nums 中并且下标为 4
 * 示例2:
 *
 * 输入: nums = [-1,0,3,5,9,12], target = 2
 * 输出: -1
 * 解释: 2 不存在 nums 中因此返回 -1
 *载请联系官方授权，非商业转载请注明出处。
 * @author: xs-shuai.com
 * @create: 2021-07-29 17:14
 **/
public class Solution {
        public int search(int[] nums, int target) {

            int start= 0;
            int end= nums.length-1;
            int middle= (start+end)/2;
           return find(nums, target, start, end, middle);
        }

    private int find(int[] nums, int target, int start, int end, int middle) {
        if (start ==middle||middle==end){
            return -1;
        }
        if (nums[middle]== target){
            return middle;
        }
        if (nums[middle]> target){
            //
            end=middle;
            middle=(start+end)/2;
            return  find(nums,target,start,end,middle);
        }
        if (nums[middle]< target){
            start=middle;
            middle=(start+end)/2;
            return  find(nums,target,start,end,middle);
        }
        return -1;
    }

    public static void main(String[] args) {
        int[] nums = {-1,0,3,5,9,12};
                int target = 9;
        Solution solution=new Solution();
        int search = solution.search(nums, target);
        System.out.println(search);
    }
}
