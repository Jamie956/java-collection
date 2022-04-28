package com.leetcode;

public class No268 {

    /**
     * 暴力解法，用一个数据标记存在的整数，再次遍历这个数组就知道哪个数字不存在
     */
    public static int missingNumber(int[] nums) {
        boolean[] arr = new boolean[nums.length + 1];
        for (int index : nums) {
            arr[index] = true;
        }
        for (int i = 0; i < arr.length; i++) {
            if (!arr[i]) {
                return i;
            }
        }
        return -1;
    }

    /**
     * O(1)空间解法，使用等比数学公式求和，用总和减去输入数组全部元素，减到最后的值就是输出
     */
    public static int missingNumber2(int[] nums) {
        int len = nums.length;
        int sum = (len * (len + 1)) / 2;
        for (int num : nums) {
            sum = sum - num;
        }
        return sum;
    }

    public static void main(String[] args) {
//        System.out.println(t.missingNumber(new int[]{0, 1}));
//        System.out.println(t.missingNumber(new int[]{3, 0, 1}));
//        System.out.println(t.missingNumber(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}));

        System.out.println(missingNumber2(new int[]{0, 1}));
        System.out.println(missingNumber2(new int[]{3, 0, 1}));
        System.out.println(missingNumber2(new int[]{9, 6, 4, 2, 3, 5, 7, 0, 1}));
    }
}