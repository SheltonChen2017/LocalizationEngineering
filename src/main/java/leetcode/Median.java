package leetcode;

import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Median {

    /**
     * Given two sorted arrays nums1 and nums2 of size m and n respectively, return the median of the two sorted arrays.
     */

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {

//        ArrayList<Integer> list = new ArrayList<Integer>();
        IntBuffer intBuf = IntBuffer.allocate(nums1.length + nums2.length);

        for (int num : nums1) {

            intBuf.put(num);

        }

        for (int num : nums2) {
            intBuf.put(num);
        }

        int[] array = intBuf.array();

        for (int i = array.length; i > 0; i--) {

            for (int j = 0; j < i - 1; j++) {

                if (array[j] > array[j + 1]) {

                    int temp = array[j];

                    array[j] = array[j + 1];

                    array[j + 1] = temp;


                }


            }

        }

//        for(int n: array){
//            System.out.println(n);
//        }

        if (array.length == 1) {
            return array[0];
        }

        if (array.length == 2) {

           float num = array[0] + array[1];

            float f = num / 2;

            return f;

        }

        if (array.length % 2 == 0) {

            System.out.println("ou shu");

            System.out.println(array[array.length/2-1]);
            System.out.println(array[array.length/2]);

            float num = array[array.length/2]+array[array.length/2-1];
        System.out.println(num);

         float f = num/2;

            System.out.println(f);


            return f;

        }

        if(array.length%2==1){
            int num = array[array.length/2];

            System.out.println("ji shu");

            float f = num;

            System.out.println(f);

            return f;

        }

        // 2 3 4 5 6 7 8

        return 0;


    }

    public static void main(String[] args) {
        Median median = new Median();

        int[] nums1 = {1,2};

        int[] nums2 = {3,4};

        median.findMedianSortedArrays(nums1, nums2);

    }


}
