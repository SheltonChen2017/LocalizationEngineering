package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class MinimumWindowSubstring {

    /**
     * Given two strings s and t of lengths m and n respectively, return the minimum window substring of s such that every character in t (including duplicates) is included in the window. If there is no such substring, return the empty string "".
     * <p>
     * The testcases will be generated such that the answer is unique.
     * <p>
     * A substring is a contiguous sequence of characters within the string.
     */

    public String minWindow(String s, String t) {

        if(t.length()>s.length()) return "";

       // System.out.println("fff");

        char[] tArray = t.toCharArray();

        ArrayList<String> tList = new ArrayList<>();

        for(char c: tArray){
            tList.add(Character.toString(c));
        }

        HashMap<String, Integer> map = new HashMap<>();

        int i=0, j=1;

        while(j<s.length() && i<j){
System.out.print(1);
System.out.println(tList.contains(s.substring(j)));
            if(tList.contains(s.substring(j))) tList.remove(s.substring(j));

i++;
j++;

        }


        return null;
    }



    public static void main(String[] args) {
        new MinimumWindowSubstring().minWindow("sdfetherger", "ert");
    }

}
