package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class TwoSum {

    /***
     * Given an array of integers nums and an integer target, return indices of the two numbers such that they add up to target.
     *
     * You may assume that each input would have exactly one solution, and you may not use the same element twice.
     *
     * You can return the answer in any order.
     */

    public int[] twoSum(int[] nums, int target) {


        ArrayList<Integer> list = new ArrayList<>();

        for(int num: nums){
            list.add(num);
        }


        ArrayList<Integer> indices = new ArrayList<>();

        for(int i=0;i<list.size();i++){

            int other = target - list.get(i);

//            System.out.println(list.indexOf(4));

            if(list.contains(other)&&i!=list.lastIndexOf(other)){
                System.out.println("the other element is "+other+ " and the index of it is "+list.indexOf(other)) ;
                indices.add(i);
                indices.add(list.lastIndexOf(other));
            }

        }


       int[] array = new int[2];

       array[0] = indices.get(0);
       array[1] = indices.get(1);

       return array;


    }

    public static void main(String[] args) {

        int[] array = {2,7,11,5};
        int k = 9;

        int[] ints = new TwoSum().twoSum(array, k);

        for(int m: ints){
            System.out.println(m);
        }

    }

}
