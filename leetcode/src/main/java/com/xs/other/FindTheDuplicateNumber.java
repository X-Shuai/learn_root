package com.xs.other;

import com.xs.util.CommonUtil;

/**
 * @program: learn_root
 * @description: 寻找重复的数
 * <p>
 * <p>
 * 给定一个包含 n + 1 个整数的数组 nums，其数字都在 1 到 n 之间（包括 1 和 n），可知至少存在一个重复的整数。假设只有一个重复的整数，找出这个重复的数。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/find-the-duplicate-number
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * <p>
 * <p>
 * 输入: [1,3,4,2,2]
 * 输出: 2
 * <p>
 * 不能更改原数组（假设数组是只读的）。
 * 只能使用额外的 O(1) 的空间。
 * 时间复杂度小于 O(n2) 。
 * 数组中只有一个重复的数字，但它可能不止重复出现一次。
 * <p>
 * 说明：
 * @author: xs-shuai.com
 * @create: 2020-10-31 16:52
 **/
public class FindTheDuplicateNumber {
    public static void main(String[] args) {
        int[] num = new int[]{1, 3, 4, 2, 2};
        int result = findDuplicate2(num);
        System.out.println(result);
    }

    /**
     * 第一种 暴力破解
     *
     * @param nums
     * @return
     */
    public static int findDuplicate1(int[] nums) {

        for (int i = 0; i < nums.length; i++) {
            for (int j = i + 1; j < nums.length; j++) {
                if (nums[i] == nums[j]) {
                    return nums[i];
                }
            }
        }
        return 0;
    }

    /***
     * 修改值的时候
     * @param nums
     * @return
     */
    public static int findDuplicate2(int[] nums) {
        for (int i = 0; i < nums.length; i++) {
            while (nums[i] != i + 1) {
                if (nums[nums[i-1]] == nums[i]) {
                    return nums[i];
                }
                CommonUtil.swap(i, nums[i-1], nums);

            }
        }
        return 0;
    }


}
