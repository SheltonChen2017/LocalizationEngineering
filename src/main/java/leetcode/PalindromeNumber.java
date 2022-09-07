package leetcode;

public class PalindromeNumber {
    public boolean palindromic(int x) {
// 2147483647 是最大整数。直接排除掉
        if (x == 2147483647) return false;
        if (x < 0) return false;
        //temp 是“余数”，
        int temp = 0;
        int sum = 0;
        int count = 0;
        //xFuben is x “副本”.
        int xFuben = x;
        int xCopy = x;
        //Use the xFuben to calculate times of operations needed.
        while (xFuben > 0) {
            xFuben = xFuben / 10;
            count++;
        }

        //若x>0，
        while (x > 0) {
            //每次用x 对10 取余。 例：15551， 第一次取余就是1， 第二次是5， 第三次是5， 第四次是5， 第五次是1
            temp = x % 10;

            x = x / 10;
            //下面这儿过程，是让每次循环后的余数，与相应的乘方相加。
            //sum的值， 第一次是1 * 10000， 然后是1*10000+5*1000， 然后是1*10000+5*1000+5*100， 然后是1*10000+5*1000+5*100+5*10， 然后是
            //1*10000+5*1000+5*100+5*10+1 = 15551
            sum += temp * Math.pow(10, --count);
        }

        if (sum == xCopy) return true;
        return false;
    }
}
