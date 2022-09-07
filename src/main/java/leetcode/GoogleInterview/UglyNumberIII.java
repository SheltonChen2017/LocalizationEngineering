package leetcode.GoogleInterview;

public class UglyNumberIII {
    public int nthUglyNumber(int n, int a, int b, int c) {

        int[] dp = new int[n+1];

        int pa=1;
        int pb=1;
        int pc=1;

        dp[0] = 0;
        dp[1] = 1;

        for(int i=2; i<=n;i++){

            dp[i] = Math.min(dp[pa]*a, Math.min(dp[pb]*b, dp[pc]*c));

            if(dp[i]==dp[pa]*a) pa++;
            if(dp[i]==dp[pb]*b) pb++;
            if(dp[i]==dp[pc]*c) pc++;



        }

        for(int i=0; i<dp.length;i++){
            System.out.println(dp[i]+" ");
        }

        return dp[n];
    }

    public static void main(String[] args) {
new UglyNumberIII().nthUglyNumber(8,2,3,5);
    }

}
