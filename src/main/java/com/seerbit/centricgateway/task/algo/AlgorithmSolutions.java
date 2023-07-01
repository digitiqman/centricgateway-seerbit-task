package com.seerbit.centricgateway.task.algo;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Stack;
import java.util.HashSet;



class IntervalPairs {
    int end;
    int begin;

    IntervalPairs(int begin, int end) {
        this.begin = begin;
        this.end = end;
    }
}

public class AlgorithmSolutions {

    public static void main(String args[]) {
        //Pairs<Integer, Integer> arr[] = new Pairs[4];
        IntervalPairs arr1[] = new IntervalPairs[3];

        arr1[1] = new IntervalPairs(1, 7);
        arr1[2] = new IntervalPairs(3, 4);
        arr1[2] = new IntervalPairs(2, 5);
        mergeOverlappingIntervals(arr1);

        
        int arr2[] = {1,2,3,4};
        int n = 4; //arr2.length
        System.out.println("Maximumly XORed Array value is " +
                                 maximumlyXORSubArray(arr2, n));
    }

    /*
    You are given an array (list) of interval pairs as input where each interval has a start
    and end timestamp. The input array is sorted by starting timestamps. You are
    required to merge overlapping intervals and return output array (list).
    */
    public static void mergeOverlappingIntervals(IntervalPairs arr[])
    {

        if (arr.length <= 0)
            return; /*Array does not have a minimum of one interval pair*/

        // Initialize stack to hold interval pairs
        Stack<IntervalPairs> stack = new Stack<>();

        // sort interval pairs with starting time ascending order
        Arrays.sort(arr, new Comparator<IntervalPairs>() {
            public int compare(IntervalPairs ip1, IntervalPairs ip2) {
                return ip1.begin - ip2.begin;
            }
        });

        // since after sorting in ascending order, the first will have the lowest begin value
        //Push intial value
        stack.push(arr[0]);

        // Loop through remaining interval pairs
        for (int i = 1; i < arr.length; i++) {
            // use the latest top as reference of merging positioning 
            IntervalPairs top = stack.peek();

            if (top.end < arr[i].begin) { // current interval pair does not overlap with the latest top interval pair
                stack.push(arr[i]);
            } else if (top.end < arr[i].end) // current interval pair overlap with the latest top interval pair
            {
                //If current interval pair of end value is greater than top interval pair end
                top.end = arr[i].end;
                stack.pop();
                stack.push(top);
            }
        }

        System.out.print("Output after merging intervalPairs is: ");
        while (!stack.isEmpty()) {
            IntervalPairs IP = stack.pop();
            System.out.print("IntervalPairs[" + IP.begin + "," + IP.end + "] ");
        }

    }


    /*
    Given an array arr[] of size, N. Find the subarray with maximum XOR. A subarray is a
    contiguous part of the array. 
    */
    public static int maximumlyXORSubArray(int array[], int n)
    {

        //initialize xored value with the most minimum integer value ever (since arr is from a contiguous array...)
        int xoredmax = Integer.MIN_VALUE; //0 || Integer.MIN_VALUE;
      
        /* int arr2[] = {1,2,3,4}; int n = 4; */
        // Loop through the array 
        for (int i=0; i<n; i++)
        {
            // initialize xored value 
            int xored = 0;
      
            // Loop through the last point of the array 
            for (int j = i; j < n; j++) //i=0,j=1 | i=0,j=2 | ...
            {
                xored = xored ^ array[j]; // 0^1 = 1; 1^2; 1^3; 1^4; ... 3^4
                xoredmax = Math.max(xored, xoredmax); //1; 3; 2; 5 ... 7
            }
        }
        return xoredmax;
    }

    
}

