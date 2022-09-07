package leetcode.AZInterview;

import java.util.Arrays;

//https://cybergeeksquad.co/2021/06/robot-bounded-in-circle-solution-amazon-oa.html
public class RobotBoundedInBox {
    public int maximumUnits(int[][] boxTypes, int truckSize) {

        Arrays.sort(boxTypes, (a, b)->{

          return  b[1]-a[1];

        });

return 0;

    }
    public boolean isRobotBounded(String instructions) {

        int[] position = new int[]{0, 0};
        // north east south west
        int[][] direction = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};

        int face = 0;

        char[] arr = instructions.toCharArray();

        for (char c : arr) {

            if (c == 'L') {

                //turn left
                face = face == 0 ? 3 : face - 1;


            } else if (c == 'R') {

                face = face == 3 ? 0 : face + 1;

            } else {
                // c==go

                position[0] = position[0]+direction[face][0];
                position[1] = position[1]+direction[face][1];

            }


        }

        return (position[0]==0 &&position[1]==0) ||face!=0;
    }

}
