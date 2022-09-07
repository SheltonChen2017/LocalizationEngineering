package RecursionLearning;

public class GCD {

    public int greatestCommonDevisor(int m, int n) {

        if(n==0) return m;

        int r = m%n;

        return greatestCommonDevisor(n,r);

    }

    public boolean isPrime(long n){

        if(n<2) return false;
        for(long i=2; i*i<=n;i++){
            if(n%i==0) return false;
        }
        return true;
    }

    public static void main(String[] args) {

//        System.out.println(new GCD().greatestCommonDevisor(2,3));

    }


}
