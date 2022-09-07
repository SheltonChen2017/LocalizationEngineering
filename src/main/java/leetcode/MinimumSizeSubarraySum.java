package leetcode;

import java.util.Arrays;

public class MinimumSizeSubarraySum {
    public int minSubArrayLen(int target, int[] nums) {

        Arrays.sort(nums);

        int remain = target;

        int count = 0;

        for (int i = nums.length - 1; i > 0; i--) {

            int r = i;
            while (remain > 0) {

                int index = this.findMax(remain, nums, r);


                remain = remain - nums[index];

                count++;

                if(index!=0) {
                    r = index - 1;
                } else {
                    r=0;
                }


                if (r < 0) {

                    // return count;
                    count = 0;
                    remain = target;
                    break;
                }

            }


            remain = target;

            count = 0;

        }


        return 0;


    }

    public int findMax(int target, int[] nums, int r) {

        int l = 0, middle = 0;

        while (l < r) {

//            System.out.println("l is " + l + " while r is " + r);
// [12, 28, 83, 4, 25, 26, 25, 2, 25, 25, 25, 12]
//  [0 , 1,  2,  3, 4,  5,  6,  7, 8,  9,  10, 11]


            middle = (r + l) / 2;

            if (nums[middle] == target) {

                return middle;

            } else if (nums[middle] < target) {
                l = middle + 1;
            } else if (nums[middle] > target) {
                r = middle - 1;
            }
            // 1 2 4 5 7 9 10 12
            // 0 1 2 3 4 5 6  7
        }
        System.out.println("l is " + l + " while r is " + r);
        return r;

    }

    public static void main(String[] args) {

        int target = 213;
        int nums[] = {12,28,83,4,25,26,25,2,25,25,25,12};
// 2, 4,12 ,12,25,25,25,25,25,26,28,83
//        Arrays.sort(nums);


//        new MinimumSizeSubarraySum().findMax(target, nums, nums.length-1);

        new MinimumSizeSubarraySum().minSubArrayLen(target, nums);

    }

}
