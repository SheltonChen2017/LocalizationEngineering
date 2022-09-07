package leetcode;

public class FindMinimumInRotatedSortedArayII {
    public int findMin(int[] nums) {
        if (nums.length == 1) return nums[0];
        int l = 0, r = nums.length - 1, middle = 0;
        while (l < r) {
            middle = (l + r) / 2;
            if (nums[middle] < nums[nums.length - 1]) {
                r = middle - 1;
            } else if (nums[middle] > nums[nums.length - 1]) {
                l = middle + 1;
            } else {
                for (int i = middle; i < nums.length; i++) {
                    if (nums[i] != nums[nums.length - 1]) {
                        // left area. If the middle is in the right area, then the nums[i] should be the same all the way down
                        l = i;
                        break;
                    }
                    if (i == nums.length - 1) {
// here, it indicates that variable i is exhausted to the end of the array nums[], which in turn suggests the middle value is on the right section.
                        r = middle - 1;
                    }
                }
            }
        }

        int pivot = (r + l) / 2;
        if (pivot == 0) {

            if (nums[pivot] > nums[pivot + 1]) {
                return nums[pivot + 1];
            }

            return nums[pivot];

        } else {
            if (nums[pivot] < nums[pivot - 1]) {
                return nums[pivot];
            }

            return nums[pivot + 1];
        }
    }
}
