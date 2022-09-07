package leetcode;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Objects;

public class LongestSubstring {
    //Given a string s, find the length of the longest substring without repeating characters.

    public int lengthOfLongestSubstring(String s) {

        if(s.equals("")){
            return 0;
        }

        char[] chars = s.toCharArray();

        ArrayList<Integer> list = new ArrayList<Integer>();


        ArrayList<Character> set = new ArrayList<>();

        for (int i = 0; i < chars.length; i++) {

            set.add(chars[i]);

            for (int j = i + 1; j < chars.length; j++) {

                if (!set.contains(chars[j])) {

                    set.add(chars[j]);
//                    System.out.println(j+""+chars[j]);

                } else {

//                    list.add(set.size());

                    break;
                }

            }
            list.add(set.size());
//            System.out.println(set.size()+"---");
            set = new ArrayList<Character>();

        }

        Integer[] integers = list.toArray(new Integer[list.size()]);

        for (int i = integers.length; i > 0; i--) {
            for (int j = 0; j < i - 1; j++) {

                if (integers[j] > integers[j + 1]) {
                    int temp = integers[j];
                    integers[j] = integers[j + 1];
                    integers[j + 1] = temp;
                }

            }
        }

//        System.out.println(integers[integers.length-1]);

//        for(Integer i: integers){
//            System.out.println(i);
//        }

        System.out.println(integers[integers.length-1]);
        return integers[integers.length - 1];
//        return 0;
    }

    public static void main(String[] args) {
        LongestSubstring l = new LongestSubstring();

        l.lengthOfLongestSubstring("");

    }

}
