package leetcode.GoogleInterview;

import java.util.ArrayList;

public class Question1 {

    /**
     * You are given a license key represented as a string s that consists of only alphanumeric characters and dashes. The string is separated into n + 1 groups by n dashes. You are also given an integer k.
     * <p>
     * We want to reformat the string s such that each group contains exactly k characters, except for the first group, which could be shorter than k but still must contain at least one character. Furthermore, there must be a dash inserted between two groups, and you should convert all lowercase letters to uppercase.
     * <p>
     * Return the reformatted license key.
     */
    public String licenseKeyFormatting(String s, int k) {

        if (s.length() == 1) return s;

        ArrayList<Character> list = new ArrayList<>();

        char[] chars = s.toCharArray();

        for (Character ch : chars) {

            if (ch != '-') {
                list.add(ch);
            }

        }

        this.reformat(list, k);

        return null;
    }

    private void reformat(ArrayList<Character> list, int k) {
        int size = list.size();
        ArrayList<Character> newList = new ArrayList<>();
        int remain = size%k;

        if(remain==0){

            int count=0;

            for(int i=0;i<size;i++){
                count++;
                newList.add(list.get(i));
                if(count==k){
                    newList.add('-');
                    count=0;
                }
            }

            if(newList.get(newList.size()-1)=='-') newList.remove(newList.size()-1);

        } else {

            int remainCount = 0;

            int count=0;
            for(int i=0 ; i<size;i++){

                remainCount++;
                count++;
                newList.add(list.get(i));
                if(remainCount==remain) {
                    newList.add('-');
                    count=0;
                }

                if(count==k){
                    newList.add('-');
                    count=0;
                }


            }

            if(newList.get(newList.size()-1)=='-') newList.remove(newList.size()-1);

        }

        for(Character c: newList){
            System.out.print(c);
        }

    }


    public static void main(String[] args) {

        Question1 question1 = new Question1();

        question1.licenseKeyFormatting("12-3456-7-89-0", 1);

    }

}
