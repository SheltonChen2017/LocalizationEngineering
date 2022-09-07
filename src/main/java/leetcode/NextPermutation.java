package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class NextPermutation {
    public void nextPermutation(int[] nums) {

        int length = nums.length;

        if (length == 1) return;

        int m;

        int temp=nums[0];

        int index2 = 0;

        for(m=0; m<nums.length; m++){

            // temp = Math.min

            if(nums[m]<= temp) {
                temp = nums[m];
                index2 = m;
            }


        }

        if(index2==length-1){

             ArrayList<Integer> list = new ArrayList();

//            List<Integer> list = Arrays.asList(nums);

            for(int i: nums){
                list.add(i);
            }

            Collections.sort(list);

            int info = 0;

            for(int i=length-1; i>=0; i--){

                nums[info++] = list.get(i);

            }


        }


        for (int i = length - 2; i >= 0; i--) {

            int current = nums[i];

            int next = nums[i + 1];

            if (current < next) {
                //37654->43567->43576->43657->43675->43756->43765->45367
                //43762 -> 46237
                //6987654321 -?7123456689
                int index = 0;
                for (int j = i + 1; j < nums.length; j++) {

                    if (nums[j] > current) {
                        nums[i] = nums[j];
                        index = j;
                    }

                }

                ArrayList<Integer> integers = new ArrayList<>();

                for (int j = i + 1; j < nums.length; j++) {

                    if (j != index) integers.add(nums[j]);

                }
                integers.add(current);

                Collections.sort(integers);
                int count = 0;
                for (int j = i + 1; j < nums.length; j++) {
                    nums[j] = integers.get(count++);
                }

                return;

            }


        }


    }
}
