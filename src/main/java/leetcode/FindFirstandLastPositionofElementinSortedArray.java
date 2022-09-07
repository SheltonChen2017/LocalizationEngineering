package leetcode;

import java.util.ArrayList;
import java.util.Collections;

public class FindFirstandLastPositionofElementinSortedArray {
    public int[] searchRange(int[] nums, int target) {

        int firstOccurence = this.findBound(nums, target, true);

        if(firstOccurence==-1) return new int[]{-1,-1};

        int lastOccurence = this.findBound(nums, target, false);

        return new int[]{firstOccurence, lastOccurence};
    }

    public int findBound(int[] nums, int target, boolean isFirst){

        int l =0, r = nums.length-1;

        while(l<=r){

            int m= l+(r-l)/2;

            if(nums[m]== target){

                if(isFirst){

                    if(m==l||nums[m-1]!=target) return m;

                    r=m-1;

                } else {

                    if(m==r||nums[m+1]!=target) return m;

                    l=m+1;

                }

            } else if (nums[m]>target){
                r = m-1;
            } else if(nums[m]<target){
                l=m+1;
            }


        }

        return -1;

    }

    public static void main(String[] args) {
        FindFirstandLastPositionofElementinSortedArray f = new FindFirstandLastPositionofElementinSortedArray();

        int[] arr = {5, 7, 7, 8, 8, 8, 8, 10};
        int tgt = 8;

        f.searchRange(arr, tgt);

    }
}
