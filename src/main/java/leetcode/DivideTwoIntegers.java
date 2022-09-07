package leetcode;

public class DivideTwoIntegers {
    /**
     * Given two integers dividend and divisor, divide two integers without using multiplication, division, and mod operator.
     * <p>
     * Return the quotient after dividing dividend by divisor.
     * <p>
     * The integer division should truncate toward zero, which means losing its fractional part.
     * For example, truncate(8.345) = 8 and truncate(-2.7335) = -2.
     * <p>
     * Note: Assume we are dealing with an environment that could only store integers within the 32-bit signed integer range: [−231, 231 − 1]. For this problem, assume that your function returns 231 − 1 when the division result overflows.
     */
// -10 3
    public int divide(int dividend, int divisor) {

        if (dividend == 0) return 0;

        if (dividend == Integer.MIN_VALUE && divisor == -1) return Integer.MAX_VALUE;

        int negatives = 2;

        if (dividend > 0) {
            negatives--;
            dividend = -dividend;
        }

        if (divisor > 0) {
            negatives--;
            divisor = -divisor;
        }

        int quotient = 0;

        while(dividend-divisor<=0){
            dividend -= divisor;
            quotient ++;
        }

        if(negatives!=1) return -quotient;

        return quotient;

    }

    public static void main(String[] args) {

        new DivideTwoIntegers().divide(-2147483648,
                -1);

    }

}
