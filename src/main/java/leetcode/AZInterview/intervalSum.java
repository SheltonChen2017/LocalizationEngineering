package leetcode.AZInterview;

public class intervalSum {

    public int solution(int[][] intervals){

        if(intervals.length==1) return intervals[0][1] - intervals[0][0];



        int val = intervals[intervals.length-1][1]-intervals[0][0];

        int dif=0;

        for(int i=1; i<intervals.length;i++){

            int[] cur = intervals[i];
            int[] prev = intervals[i-1];
            if(cur[0]-prev[1]>0) dif+=cur[0]-prev[1];
        }

        return val-dif;
    }

    public static void main(String[] args) {

        int[][] arr= {
                {1,2}, {3,4}, {3,5}, {4,6},{9,12}

        };

        System.out.println(new intervalSum().solution(arr));

    }

}
