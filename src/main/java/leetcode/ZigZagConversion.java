package leetcode;

public class ZigZagConversion {
    /**
     * The string "PAYPALISHIRING" is written in a zigzag pattern on a given number
     * of rows like this: (you may want to display this pattern in a fixed font for better legibility)
     ***/

    public String convert(String s, int numRows) {
//paypal is hiring

        String a[][] = new String[numRows][7];

        int count = 0;

        int i = 0;
        int j = 0;
        while (count < s.length()) {

            if (i == 0) {

                for (i = 0; i < numRows; i++) {
                    a[i][j] = s.substring(count++);
                    System.out.println("---");
                }

                i--;

            }
            System.out.println(count);
            if (i == numRows - 1) {
//

                for (i = numRows - 1; i >= 0; i--) {

                    a[i][++j] = s.substring(count++);
                    System.out.println("....");

                }

                i++;

            }

        }


        return null;

    }

    public static void main(String[] args) {
        new ZigZagConversion().convert("PAYPALISHIRING", 3);
    }


}
