package leetcode;

public class ReverseInteger {

    /**
     * Given a signed 32-bit integer x, return x with its digits reversed.
     * If reversing x causes the value to go outside the signed 32-bit integer range [-231, 231 - 1], then return 0.
     *
     * Assume the environment does not allow you to store 64-bit integers (signed or unsigned).
     * */

    public int reverse(int x) {


        // java int  ： -2147483648 <=x <= 2147483647

        // or  -2^31 <= x <= 2^31-1

        int temp = 0;

        if(x<0){
            temp = -x;
        } else {
            temp = x;
        }
        int remain = 0;

        int res=0;
        while(temp>0){



            remain = temp%10; //1 -> 2 -> 3 - >4

            res = 10*res+ remain ; //1 -> 12 -> 123 ->1234

            if(res>Integer.MAX_VALUE/10|| (res==Integer.MAX_VALUE/10 && remain>7)) return 0;

            if(res<Integer.MIN_VALUE/10|| (res==Integer.MIN_VALUE/10 && remain<-8))return 0;

            temp = temp/10; //432 -> 43 - > 4 ->0

        }



        if(x<0) res = -res;


        return res;



    }

}
