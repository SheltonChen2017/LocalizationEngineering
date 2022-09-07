package leetcode;

import java.util.Arrays;
import java.util.List;

public class GenerateParenthesis {
    public List<String> generateParenthesis(int n) {

// algorithm: insert () to every smalleset ()

        List<String> list = Arrays.asList("()");

        if (n == 1) return list;

        for (int i = 1; i <= n; i++) {


          list =  this.processStrings(list);

        }



        return null;
    }

    private List<String> processStrings(List<String> list) {
        return list;
    }
}
