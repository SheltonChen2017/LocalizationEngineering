package leetcode;

public class SearchInRotatedSortedArrayII {

    public boolean search(int[] nums, int target) {

        // 3 4 5 6 7 8 8 8 8 8 8 8 8 8 8
        /**

         888888888888888834567888888

         */

        if (nums[0] == target) return true;


        int l = 0, r = nums.length - 1;
        int middle = 0;

        int pivot = this.findRotatePoint(nums);


        if (target == nums[nums.length - 1]) return true;

        if (target > nums[nums.length - 1]) return this.ifTargetLeft(target, pivot, nums);
        if (target < nums[nums.length - 1]) return this.ifTargetRight(target, pivot, nums);

        return false;
    }

    public boolean ifTargetLeft(int target, int pivot, int[] nums) {

        int l = 0, r = pivot, middle = 0;

        while (l <= r) {
            middle = (l + r) / 2;

            if (nums[middle] == target) return true;

            if (nums[middle] > target) {
                r = middle - 1;
            } else if (nums[middle] < target) {
                l = middle + 1;
            }

        }

        return false;

    }


    public boolean ifTargetRight(int target, int pivot, int[] nums) {

        int l = pivot + 1, r = nums.length - 1, middle = 0;

        while (l <= r) {
            middle = (l + r) / 2;

            if (nums[middle] == target) return true;
            if (nums[middle] > target) {
                r = middle - 1;
            } else {
                l = middle + 1;
            }

        }

        return false;

    }

    public int findRotatePoint(int[] nums) {

        int l = 0, r = nums.length - 1, middle = 0;

        while (l < r) {

            middle = (l + r) / 2;

            System.out.println(middle);

            if (nums[middle] < nums[nums.length - 1]) {
                // this indicates middle is in the right section
                r = middle - 1;

                // 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 6 7 8 8 8 3 4 5 6 6 6 6 6 6 6 6 6 6  6 6 6

                // 777777777777 8 3 4 56 7 7 7


            } else if (nums[middle] > nums[nums.length - 1]) {
                //this indicates middle is in the left section
                l = middle + 1;

            } else { // nums[middle] == nums[nums.length-1]
//System.out.println(r);


                // 2 2 2 2 2 2 2 2 2 2 2 2 3 3 3 4 1 2 2 2 2 2 2 2 2 2 2 2 2 2 2

                for (int i = middle; i < nums.length; i++) {

                    if (nums[i] != nums[nums.length - 1]) {
                        // left area

                        l = i;
                        break;

                    }

                    if (i == nums.length - 1) {
                        //right area
//                        System.out.println("right");
                        r = middle - 1;
                    }
                }

//                System.out.println("l is "+l + "r is " + r);

            }


        }


        int pivot = (r + l) / 2;
        System.out.println("pivot is " + pivot);

        if (pivot == 0) {

            return pivot;

        } else {

            if (nums[pivot] < nums[pivot - 1]) {
                System.out.println("the return value is " + (pivot - 1));
                return pivot - 1;
            }

        }

        System.out.println("the return value is " + pivot);
        return pivot;

    }

    public static void main(String[] args) {

        int nums[] = {3,3,1,3};
        int target = 13;

        new SearchInRotatedSortedArrayII().findRotatePoint(nums);


    }

}