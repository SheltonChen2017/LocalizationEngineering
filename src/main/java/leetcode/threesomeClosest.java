package leetcode;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Set;

public class threesomeClosest {
    HashMap<Integer, Integer> integers = new HashMap<>();

//    ArrayList<Integer> integers =new ArrayList<Integer>();

    public int threeSumClosest(int[] nums, int target) {

        Arrays.sort(nums);

        for (int i = 0; i < nums.length; i++) {

            int current = nums[i];
            int dif = target - current;

            int[] restArr = this.findRest(nums, i, dif);


            int sum = current + restArr[0];

            int absMin = restArr[1];

            this.integers.put(absMin, sum);

        }

        Set<Integer> keys = this.integers.keySet();

        Integer minKey = Collections.min(keys);

        Integer resultSum = this.integers.get(minKey);

        return resultSum;
    }

    private int[] findRest(int[] nums, int n, int dif) {

        int i = n;
        int j = nums.length - 1;

        int minDif = dif - nums[i] - nums[j];

        int absMinDif = Math.abs(minDif);

        int result = 0;


        while (i < nums.length && i < j) {

            if (minDif > 0 && i + 1 < j && absMinDif >= Math.abs(dif - nums[i + 1] - nums[j])) {
                i++;
            } else if (minDif < 0 && j - 1 > i && absMinDif >= Math.abs(dif - nums[i] - nums[j - 1])) {
                j--;
            }
            minDif = dif - nums[i] - nums[j];

            absMinDif = Math.abs(minDif);

            if (minDif == 0) {
                result = nums[i] + nums[j];

                break;
            }

            result = nums[i] + nums[j];

        }

        int[] arr = new int[2];

        arr[0] = result;
        arr[1] = absMinDif;

        return arr;

    }
}
