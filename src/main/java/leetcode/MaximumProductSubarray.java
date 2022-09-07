package leetcode;

import java.util.Arrays;

public class MaximumProductSubarray {
    /**
     * Given an integer array nums, find a contiguous non-empty subarray within the array that has the largest product, and return the product.
     * <p>
     * It is guaranteed that the answer will fit in a 32-bit integer.
     * <p>
     * A subarray is a contiguous subsequence of the array.
     */


    public int maxProduct(int[] nums) {

        if (nums.length < 2) {
            return nums[0];
        }

//        int a[] = new int[nums.length + 1];
//
//
//        for (int i = 0; i < nums.length; i++) {
//            a[i] = nums[i];
//        }

        int a[] = nums;

        for (int i = 0; i < nums.length; i++) {

            int temp = a[i];

            for (int j = 1; j < nums.length - i; j++) {
                a[i] = a[i] * nums[i + j];

                temp = Math.max(a[i], temp);

            }

            a[i] = temp;

        }

        int max = Arrays.stream(a).max().getAsInt();


        System.out.println(max);

        return max;
    }

    public static void main(String[] args) {

        int[] nums = {0, 2, 3};

        new MaximumProductSubarray().maxProduct(nums);


    }

}
