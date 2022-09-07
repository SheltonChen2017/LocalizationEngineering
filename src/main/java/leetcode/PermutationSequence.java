package leetcode;

import java.util.ArrayList;

public class PermutationSequence {
    /**
     * The set [1, 2, 3, ..., n] contains a total of n! unique permutations.
     *
     * By listing and labeling all of the permutations in order, we get the following sequence for n = 3:
     *
     * "123"
     * "132"
     * "213"
     * "231"
     * "312"
     * "321"
     * Given n and k, return the kth permutation sequence.
     * */


    /**
     * 1 2 3 4 5 6 7 8      1   1 2 3 4 5 6 7 8 9
     * 1 2 3 4 5 6 8 7      2   1 2 3 4 5 6 7 9 8
     * 1 2 3 4 5 7 6 8      3   1 2 3 4 5 6 8 7 9
     * 1 2 3 4 5 7 8 6      4   1 2 3 4 5 6 8 9 7
     * 1 2 3 4 5 8 6 7      5   1 2 3 4 5 6 9 7 8
     * 1 2 3 4 5 8 7 6      6   1 2 3 4 5 6 9 8 7
     * 1 2 3 4 6 5 7 8      7   1 2 3 4 5 7 6 8 9
     * 1 2 3 4 6 5 8 7      8   1 2 3 4 5 7 6 9 8
     * 1 2 3 4 6 7 5 8      9   1 2 3 4 5 7 8 6 9
     * 1 2 3 4 6 7 8 5      10  1 2 3 4 5 7 8 9 6
     * 1 2 3 4 6 8 5 7      11  1 2 3 4 5 7 9 6 8
     * 1 2 3 4 6 8 7 5      12  1 2 3 4 5 7 9 8 6
     * 1 2 3 4 7 5 6 8      13  1 2 3 4 5 8 6 7 9
     * 1 2 3 4 7 5 8 6      14  1 2 3 4 5 8 6 9 7
     * 1 2 3 4 7 6 5 8      15  1 2 3 4 5 8 7 6 9
     * 1 2 3 4 7 6 8 5      16  1 2 3 4 5 8 7 9 6
     * 1 2 3 4 7 8 5 6
     * 1 2 3 4 7 8 6 5
     * 1 2 3 4 8 5 6 7
     * 1 2 3 4 8 5 7 6
     * 1 2 3 4 8 6 5 7
     * 1 2 3 4 8 6 7 5
     * 1 2 3 4 8 7 5 6
     * 1 2 3 4 8 7 6 5
     * 1 2 3 5 4 6 7 8
     * 1 2 3 5 4 6 8 7
     * 1 2 3 5 4 7 6 8
     * 1 2 3 5 4 7 8 6
     * 1 2 3 5 4 8 6 7
     * 1 2 3 5 4 8 7 6
     * 1 2 3 5 6 4 7 8
     * 1 2 3 5 6 4 8 7
     * 1 2 3 5 6 7 4 8
     * 1 2 3 5 6 7 8 4
     * 1 2 3 5 6 8 4 7
     * 1 2 3 5 6 8 7 4
     * 1 2 3 5 7 4 6 8
     * 1 2 3 5 7 4 8 6
     * 1 2 3 5 7 6 4 8
     * 1 2 3 5 7 6 8 4
     * 1 2 3 8 4 5 6 7
     *
     *
     *
     * 1 2 3 4
     * 1 2 4 3
     * 1 3 2 4
     * 1 3 4 2
     * 1 4 2 3
     * 1 4 3 2
     * 2 1 3 4
     * 2 1 4 3
     * 2 3 1 4
     * 2 3 4 1
     * 2 4 1 3
     * 2 4 3 1
     * 3 1 2 4
     * 3 1 4 2
     * 3 2 1 4
     * 3 2 4 1
     * 3 4 1 2
     * 3 4 2 1
     * 4 1 2 3
     * 4 1 3 2
     * 4 2 1 3
     * 4 2 3 1
     * 4 3 1 2
     * 4 3 2 1

     1 2 3 4 5
     1 2 3 5 4
     1 2 4 3 5
     1 2 4 5 3
     1 2 5 3 4
     1 2 5 4 3
     1 3 2 4 5
     1 3 2 5 4
     1 3 4 2 5
     1 3 4 5 2
     1 3 5 2 4
     1 3 5 4 2
     1 4 2 3 5
     1 4 2 5 3
     1 4 3 2 5
     1 4 3 5 2
     1 4 5 2 3
     1 4 5 3 2
     1 5 2 3 4
     1 5 2 4 3
     1 5 3 2 4
     1 5 3 4 2
     1 5 4 2 3
     1 5 4 3 2
     * */


    /**
     * The rule:
     * Full permutation after one character, increase the character by 1 until it reaches k.
     */

    public String getPermutation(int n, int k) {

        int j = 1;

        StringBuilder sb = new StringBuilder();

        ArrayList<Integer> integers = new ArrayList<>();

        int[] arr = new int[n + 1];

        arr[0] = 0;
        for (int i = 1; i <= n; i++) {
            arr[i] = i;
        }
        for (j = 1; n - j >= 0; j++) {
            int factorial = this.factorial(n - j);

            int round = k / factorial;
            k = k % factorial;

            int num = k == 0 ? round : round + 1;

            int count = 0;

            for (int m = 1; m <= n; m++) {

                if (arr[m] != 0) {
                    count++;
                }
                if (count == num && arr[m] != 0) {
//                    integers.add(arr[m]);
                    sb.append(arr[m]);
                    arr[m] = 0;
                }
            }

            if (k == 0) {

                for (int m = n; m >= 1; m--) {

                    if (arr[m] != 0) {
                        sb.append(arr[m]);
                    }

                }

                break;
            }
        }
        return sb.toString();
    }

    public int factorial(int n) {

        int res = 1;

        for (int i = 1; i <= n; i++) {
            res *= i;
        }

        return res;
    }

    public static void main(String[] args) {
        new PermutationSequence().getPermutation(4, 19);

    }
}
