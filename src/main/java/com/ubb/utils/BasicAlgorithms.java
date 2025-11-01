package com.ubb.utils;

public final class BasicAlgorithms {

    public static int findMax(int[] numbers){

        if ( numbers == null || numbers.length == 0 )
            return -1;

        int maxNumber =  numbers[0];
        for ( int number : numbers ) {
            if ( number > maxNumber ) {
                maxNumber = number;
            }
        }

        return maxNumber;
    }
}
