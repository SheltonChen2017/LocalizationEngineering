package leetcode;

public class SearchAtwoDMatrix {

    /**
     * Write an efficient algorithm that searches for a value in an m x n matrix. This matrix has the following properties:
     * <p>
     * Integers in each row are sorted from left to right.
     * The first integer of each row is greater than the last integer of the previous row.
     */

    public boolean searchMatrix(int[][] matrix, int target) {

        int h = 0;

        int col = matrix[0].length - 1;

        for (h = 0; h < matrix.length; h++) {

            if (target == matrix[h][col]) return true;

            if (target < matrix[h][col]) break;

        }

        //with the above, we locate the target is in the h(th) row.

        int l = 0, r = matrix[h].length - 1, middle = 0;

        while (l <= r) {

            middle = (l + r) / 2;

            if (target == matrix[h][middle]) return true;

            if (target > matrix[h][middle]) {

                l = middle + 1;


            } else {

                r = middle - 1;

            }

        }


        return false;
    }


}
