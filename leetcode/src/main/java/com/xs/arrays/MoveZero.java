package com.xs.arrays;

/**
 * @program: learn_root
 * @description: 移动零
 * @author: xs-shuai.com
 * @create: 2020-09-13 20:14
 **/

/***
 * 给定一个数组 nums，编写一个函数将所有 0 移动到数组的末尾，同时保持非零元素的相对顺序。
 *
 * 示例:
 *
 * 输入: [0,1,0,3,12]
 * 输出: [1,3,12,0,0]
 * 说明:
 *
 * 必须在原数组上操作，不能拷贝额外的数组。
 * 尽量减少操作次数。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/move-zeroes
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 */
public class MoveZero {

    public void moveZeroes(int[] nums) {
        if (nums == null || nums.length <= 1) {
            return;
        }
        for (int i = 0; i < nums.length; i++) {
         if (nums[i]==0){
             //寻找下一个不为零
             for (int j = i+1; j < nums.length; j++){
                 if (nums[j]!=0){
                     //交换
                     swap(i,j,nums);
                     break;
                 }
             }

         }
        }
    }
    public void swap(int i,int j,int[] nums){
        int a = nums[i];
        nums[i] = nums[j];
        nums[j] = a;
    }

    /**
     * 第二种
     * @param nums
     */
    public void moveZeroes2(int[] nums) {

        }

}




