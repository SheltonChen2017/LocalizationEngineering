package leetcode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class LetterCombinationOfAPhoneNumber {
    /**
     * Given a string containing digits from 2-9 inclusive,
     * return all possible letter combinations that the number could represent. Return the answer in any order.
     * <p>
     * A mapping of digit to letters (just like on the telephone buttons) is given below. Note that 1 does not map to any letters.
     */

    /*
    Input: digits = "23"
Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
    * */
    public List<String> letterCombinations(String digits) {

        HashMap<Character, char[]> map = this.produceMap();

        if (digits.length() < 1) return null;

        ArrayList<String> result = new ArrayList<>();

        char[] characters = map.get(digits.toCharArray()[0]);

        for (char c : characters) {
            result.add(new StringBuilder().append(c).toString());
        }

        if (digits.length() == 1) return result;

        char[] digitsArray = digits.toCharArray();

        int length = digits.length();

        ArrayList<String> resultList = this.generateResult(result, length, map, digitsArray);

        for (String s : resultList) {
      System.out.println(s);
        }

        return resultList;
    }

    private ArrayList<String> generateResult(ArrayList<String> result, int length, HashMap<Character, char[]> map, char[] digitsArray) {

       ArrayList<String> resultList = new ArrayList<String>();

        for (int i = 1; i < digitsArray.length; i++) {

            char[] alphabetArray = map.get(digitsArray[i]);
//System.out.println("start");
            for (String string : result) {

//                int index = result.indexOf(string);
           //     resultList = new ArrayList<String>();
                for (char alphabet : alphabetArray) {



                    StringBuilder sb = new StringBuilder(string);

                    sb.append(alphabet);

//                    System.out.println(sb.toString());

                    resultList.add(sb.toString());

                }


            }

           result = new ArrayList<String>();

            for(String s: resultList){
                if(s.length()==i+1)
                result.add(s);
            }


        }
        return result;
    }


    private HashMap<Character, char[]> produceMap() {
        HashMap<Character, char[]> map = new HashMap<>();

        map.put('2', "abc".toCharArray());
        map.put('3', "def".toCharArray());
        map.put('4', "ghi".toCharArray());
        map.put('5', "jkl".toCharArray());
        map.put('6', "mno".toCharArray());
        map.put('7', "pqrs".toCharArray());
        map.put('8', "tuv".toCharArray());
        map.put('9', "wxyz".toCharArray());

        return map;

    }

    public static void main(String[] args) {
        new LetterCombinationOfAPhoneNumber().letterCombinations("23");
    }

}
