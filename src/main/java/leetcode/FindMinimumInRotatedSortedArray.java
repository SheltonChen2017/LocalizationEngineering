package leetcode;

public class FindMinimumInRotatedSortedArray {

    /**
     * Suppose an array of length n sorted in ascending order is rotated between 1 and n times. For example, the array nums = [0,1,2,4,5,6,7] might become:
     *
     * [4,5,6,7,0,1,2] if it was rotated 4 times.
     * [0,1,2,4,5,6,7] if it was rotated 7 times.
     * Notice that rotating an array [a[0], a[1], a[2], ..., a[n-1]] 1 time results in the array [a[n-1], a[0], a[1], a[2], ..., a[n-2]].
     *
     * Given the sorted rotated array nums of unique elements, return the minimum element of this array.
     *
     * You must write an algorithm that runs in O(log n) time.
     *
     * */
    public int findMin(int[] nums) {
        int l = 0, r = nums.length - 1;

        int middle = 0;

        while (l < r) {
            middle = (l + r) / 2;

            if (nums[middle] > nums[nums.length - 1]) {

                l = middle +1;

            } else if (nums[middle] < nums[nums.length - 1]) {
                r = middle -1;
            }

        }

        int pivot = (r + l) / 2;

        if(pivot == nums.length-1) return nums[pivot];
        if (nums[pivot] > nums[pivot + 1]) return nums[pivot+1];
        if (nums[pivot] < nums[pivot + 1]) return nums[pivot];

        return 0;
    }

}
