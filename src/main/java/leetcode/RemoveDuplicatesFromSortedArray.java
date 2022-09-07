package leetcode;

public class RemoveDuplicatesFromSortedArray {

    public int removeDuplicates(int[] nums) {

        if (nums.length == 0) return 0;
        if (nums.length == 1) return 1;

        int i = 0, j = 1;

        for (j = 1; j < nums.length; j++) {

            if (nums[j] != nums[i]) {
                i++;
                nums[i] = nums[j];

            }

        }


        return i+1;
    }

    public static void main(String[] args) {

        int[] nums = {1, 1, 2};

        new RemoveDuplicatesFromSortedArray().removeDuplicates(nums);

    }

}
