package com.xs.arrays;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @program: learn_root
 * @description: 三数的和
 * @author: xs-shuai.com
 * @create: 2020-09-14 00:34
 **/
public class ThreeSum {
    /**
     * 给你一个包含 n 个整数的数组 nums，判断 nums 中是否存在三个元素 a，b，c ，使得 a + b + c = 0 ？请你找出所有满足条件且不重复的三元组。
     * <p>
     * 注意：答案中不可以包含重复的三元组。
     * <p>
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/3sum
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * <p>
     * 1.三重循环
     * 2. 哈希
     * 3.排序后,指针方式
     */
    public List<List<Integer>> threeSum(int[] nums) {
        //todo 未解答对
        List<List<Integer>> targetArray = new ArrayList();

        Arrays.sort(nums);

        for (int targetIndex = 0; targetIndex <= nums.length-2; targetIndex++) {
            int minIndex = targetIndex+1;
            int maxIndex = nums.length - 1;
            if(targetIndex>0 && nums[targetIndex] == nums[targetIndex-1]) {
                continue;
            }

            while (minIndex<maxIndex){

                if ( nums[targetIndex]==nums[minIndex]+nums[maxIndex]){
                    targetArray.add(Arrays.asList(nums[targetIndex], nums[minIndex], nums[maxIndex]));
                    if (nums[minIndex] == nums[minIndex+1]){
                        minIndex++;
                    }
                    if (nums[maxIndex] == nums[maxIndex-1]){
                        maxIndex--;
                    }
                    minIndex++;
                    maxIndex--;
                    continue;
                }
                if ( nums[targetIndex]<nums[minIndex]+nums[maxIndex]){
                    maxIndex--;
                }else
                {
                    minIndex++;
                }

            }

        }
        return targetArray;
    }

}
