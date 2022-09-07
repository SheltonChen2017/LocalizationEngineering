package leetcode;

public class SearchInRotatedSortedArray {

    /**
     * There is an integer array nums sorted in ascending order (with distinct values).
     * <p>
     * Prior to being passed to your function, nums is possibly rotated at an unknown pivot index k
     * (1 <= k < nums.length) such that the resulting array is [nums[k], nums[k+1], ..., nums[n-1], nums[0], nums[1], ..., nums[k-1]]
     * (0-indexed). For example, [0,1,2,4,5,6,7] might be rotated at pivot index 3 and become [4,5,6,7,0,1,2].
     * <p>
     * Given the array nums after the possible rotation and an integer target, return the index of target if it is in nums,
     * or -1 if it is not in nums.
     * <p>
     * You must write an algorithm with O(log n) runtime complexity.
     */

    public int search(int[] nums, int target) {

        if(nums.length==1){
            if(nums[0]==target){
                return 0;
            } else {
                return -1;
            }
        }

        int l = 0, r = nums.length - 1;

        int middle = 0;

        int rotatePoint = this.findRotatePoint(nums);

        if (target == nums[nums.length - 1]) return nums.length - 1;
        if (target > nums[nums.length - 1]) return this.ifTargetLeft(target, rotatePoint, nums);
        if (target < nums[nums.length - 1]) return this.ifTargetRight(target, rotatePoint, nums);

        return -1;
    }

    private int ifTargetLeft(int target, int rotatePoint, int[] nums) {

        int l = 0, r = rotatePoint, middle = 0;

        while (l <= r) {
            middle = (l + r) / 2;

            if (nums[middle] == target) return middle;
            if (nums[middle] > target) {
                r = middle - 1;
            } else {
                l = middle + 1;
            }
        }

        return -1;
    }

    private int ifTargetRight(int target, int rotatePoint, int[] nums) {

        int l = rotatePoint + 1, r = nums.length - 1, middle = 0;

        while (l <= r) {
            middle = (l + r) / 2;

            if (nums[middle] == target) return middle;
            if (nums[middle] > target) {
                r = middle - 1;
            } else {
                l = middle + 1;
            }

        }


        return -1;
    }

    public int findRotatePoint(int[] nums) {

        int l = 0, r = nums.length - 1;

        int middle = 0;

        while (l < r) {
            middle = (l + r) / 2;

            if (nums[middle] > nums[nums.length - 1]) {

                l = middle + 1;

            } else if (nums[middle] < nums[nums.length - 1]) {
                r = middle - 1;
            }

        }

        int pivot = (r + l) / 2;

        // System.out.println ("r is "+r + " l is "+ l + " pivot is "+ pivot);


        if(pivot == nums.length-1) {
            System.out.println(pivot-1);
            return pivot-1;
        }
        if (nums[pivot] > nums[pivot + 1]) {
            System.out.println(pivot);
            return pivot;
        }
        if (nums[pivot] < nums[pivot + 1]) {
            System.out.println(pivot -1);
            return pivot - 1;
        }

        return 0;

    }

    public static void main(String[] args) {

        int nums[] = {4, 5, 6, 0, 1, 2, 3};
        int target = 0;
        new SearchInRotatedSortedArray().findRotatePoint(nums);
//        new SearchInRotatedSortedArray().search(nums, target);
    }

}
