package leetcode;

public class MaxPointsOnAlINE {

    /**
     * Given an array of points where points[i] = [xi, yi]
     * represents a point on the X-Y plane, return the maximum number of points that lie on the same straight line.
     * */


    /**
     * e.g.
     * points = [[1,1],[3,2],[5,3],[4,1],[2,3],[1,4]]
     */
    public int maxPoints(int[][] points) {

        for (int i = points.length - 1; i >= 0; i--) {
            for (int j = 0; j < i; j++) {
                if (points[j][0] >= points[j + 1][0]) {
                    int temp = points[j][0];
                    points[j][0] = points[j + 1][0];
                    points[j + 1][0] = temp;
                    int temp2 = points[j][1];
                    points[j][1] = points[j + 1][1];
                    points[j + 1][1] = temp2;
                }
            }
        }

        for(int i=0; i<points.length; i++){




        }


        return 0;
    }

    public float getSlope(int y1, int y2, int x1, int x2){


        float y1F = new Integer(y1).floatValue();
        float y2F = new Integer(y2).floatValue();
        float x1F = new Integer(x1).floatValue();
        float x2F = new Integer(x2).floatValue();

      return x1!=x2?(y1F-y2F) / (x1F - x2F):0;


    }

    public static void main(String[] args) {

        new MaxPointsOnAlINE().getSlope(5,2,3,1);

    }

}
