package leetcode;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LongestValidParenthesis {

    public int longestValidParentheses(String s) {

        Pattern p = Pattern.compile("\\(\\)");

        Matcher matcher = p.matcher(s);

        int i=0;

        while(matcher.find()){
            i++;
        }

        return i*2;
    }

    public static void main(String[] args) {
        LongestValidParenthesis l = new LongestValidParenthesis();
        int i = l.longestValidParentheses("()()()");
        System.out.println(i);
    }

}
