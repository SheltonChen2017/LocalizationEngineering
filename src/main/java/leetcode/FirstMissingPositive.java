package leetcode;

import java.util.ArrayList;
import java.util.Arrays;

public class FirstMissingPositive {

    /**
     * Given an unsorted integer array nums, find the smallest missing positive integer.
     */


    public int firstMissingPositive(int[] nums) {

// sort the array
        for (int i = nums.length; i > 0; i--) {

            for (int j = 0; j < i - 1; j++) {

                if (nums[j] > nums[j + 1]) {
                    int temp = nums[j];
                    nums[j] = nums[j + 1];
                    nums[j + 1] = temp;
                }
            }
        }

        if (nums.length == 0) {
            return 1;
        }

        if (nums[nums.length - 1] <= 0) {
            return 1;
        }

        if (nums[0] > 1) {
            return 1;
        }


        for (int i = 0; i < nums.length - 1; i++) {

            if (nums[i + 1] - nums[i] > 1) {
            System.out.println("first layer");
               if(nums[i+1]>1){
                   if(nums[i]>0){
                       return nums[i]+1;
                   }else {
                       return 1;
                   }
               }

            }


        }

        return nums[nums.length - 1] + 1;
    }

    public static void main(String[] args) {
        FirstMissingPositive fmp = new FirstMissingPositive();

        int[] integers = {-1, 1,3,4};

//        System.out.println(integers.length);

        System.out.println(fmp.firstMissingPositive(integers));


    }

}
