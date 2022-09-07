package leetcode;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexMatching {

/**
 * Given an input string (s) and a pattern (p), implement wildcard pattern matching with support for '?' and '*' where:
 *
 * '?' Matches any single character.
 * '*' Matches any sequence of characters (including the empty sequence).
 * The matching should cover the entire input string (not partial).
 * **/

    public boolean isMatch(String s, String p) {

        if(!p.contains("\\*")&&!p.contains("\\?")){
            return s.equals(p);
        }

        List<String> sCharacters = Arrays.asList(s);

        List<String> pCharacters = Arrays.asList(p);


        ArrayList<String> newList = new ArrayList<>();

        int i=0, j=0;

        while(j<p.length()){

       if(pCharacters.get(j).equals("\\*")){



       }




        }

        return true;
    }


}
