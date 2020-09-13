package com.xs.arrays;

/**
 * @program: learn_root
 * @description: 盛最多数的容器
 * @author: xs-shuai.com
 * @create: 2020-09-13 23:36
 **/
public class LoadMaxWater {

    /***
     * 给你 n 个非负整数 a1，a2，...，an，每个数代表坐标中的一个点 (i, ai) 。在坐标内画 n 条垂直线，
     * 垂直线 i 的两个端点分别为 (i, ai) 和 (i, 0)。找出其中的两条线，使得它们与 x 轴共同构成的容器可以容纳最多的水。
     *
     * 说明：你不能倾斜容器，且 n 的值至少为 2。
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/container-with-most-water
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param height
     * @return
     */


    /***
     * 循环 一次 length -1 ,寻找之后的数 求得最大值
     * @param height
     * @return
     */
    public int maxArea(int[] height) {

        int max = 0;
        int area = 0;
        for (int i = 0; i < height.length - 1; i++) {
            for (int j = i + 1; j < height.length; j++) {
                area = (j - i) * Math.min(height[i], height[j]);
                max = Math.max(max, area);
            }
        }
        return max;

    }

    /***
     * 第二种:寻找面积最大的前提,两个数的高度比我现在的来个数都都小大,两个数中小的那个移动寻找比当前数还大的数
     *
     *
     * @param height
     * @return
     */
    public int maxArea2(int[] height) {

        int i = 0;
        int j = height.length - 1;
        int max = 0;
        int area = 0;

        while (i < j ) {
            area=(j - i) * Math.min(height[i], height[j]);
            if (height[i]<height[j]){
                i++;
            }else
            {
                j--;
            }
            max = Math.max(max, area);

        }


        return max;
    }
}
