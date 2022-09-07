package leetcode;

public class UglyNumberii {

    /**
     * An ugly number is a positive integer whose prime factors are limited to 2, 3, and 5.
     * <p>
     * Given an integer n, return the nth ugly number.
     */

    //3 pointers, dynamic programming
    public int hUglyNumber(int n) {

        int p2 = 1;
        int p3 = 1;
        int p5 = 1;

        int[] dp = new int[n + 1];

        dp[0] = 0;
        dp[1] = 1;

        for (int i = 2; i < dp.length; i++) {

            dp[i] = Math.min(dp[p2] * 2, Math.min(dp[p3] * 3, dp[p5] * 5));

            if (dp[i] == dp[p2] * 2) p2++;
            if (dp[i] == dp[p3] * 3) p3++;
            if (dp[i] == dp[p5] * 5) p5++;
        }

//        System.out.println(dp[n]);

        return dp[n];

    }

    public int nthUglyNumber(int n) {

        double i = 0, j = 0, k = 0;

        double num = Math.pow(2, i) * Math.pow(3, j) * Math.pow(5, k);

        int count = 0;

        double min = 1;

        for (count = 1; count <= n; count++) {

            System.out.println(min);

            double temp2 = Math.pow(2, i + 1) * Math.pow(3, j) * Math.pow(5, k);
            double temp3 = Math.pow(2, i) * Math.pow(3, j + 1) * Math.pow(5, k);
            double temp5 = Math.pow(2, i) * Math.pow(3, j) * Math.pow(5, k + 1);

            min = Math.min(temp2, Math.min(temp3, temp5));

            if (min == temp2) i++;
            if (min == temp3) j++;
            if (min == temp5) k++;

        }

        System.out.println(min);

        return 0;
    }

    public static void main(String[] args) {
        new UglyNumberii().hUglyNumber(5);
        new UglyNumberii().nthUglyNumber(5);
    }

}
