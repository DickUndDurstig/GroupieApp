package de.android.apptemplate2;

import java.util.HashMap;
import java.util.Random;

public class Roulette {

    public static final Integer[] rouletteNumbersFR = {0,32,15,19,4,21,2,25,17,34,
            6,27,13,36,11,30,8,23,10,
            5,24,16,33,1,20,14,31,9,22,
            18,29,7,28,12,35,3,26};

    public static final int[] numberPositionFR = {0,23,6,35,4,19,10,31,16,27,18,14,33,12,25,2,21,8,
            29,3,24,5,28,17,20,7,36,11,32,30,15,26,1,22,9,34,13};

    public static final Integer[] rouletteNumbersUS = {0,28,9,26,30,11,7,20,32,17,5,22,34,
            15,3,24,36,13,1,37,27,10,25,29,12,8,19,31,18,6,21,
            33,16,4,23,35,14,2};

    public static final int[] numberPositionUS = {0,18,37,14,33,10,29,6,25,2,21,5,24,17,36,24,32,9,28,26,7,10,11,34,15,
            22,3,20,1,23,4,27,8,31,12,35,16,19};


    public static final HashMap<Integer, Integer> rouletteNumsFR = new HashMap<Integer, Integer>() {

        {
            put(0, 0);
            put(23, 32);
            put(6,15);
            put(35,19);
            put(4,4);
            put(19,21);
            put(10,2);
            put(31,25);
            put(31,25);
            put(16,17);
            put(27,34);
            put(18,6);
            put(14,27);
            put(33,13);
            put(12,36);
            put(25,11);
            put(2, 30);
            put(21, 8);
            put(8,23);
            put(29,10);
            put(3,5);
            put(24,24);
            put(5,16);
            put(28,33);
            put(17,1);
            put(20,20);
            put(7,14);
            put(36,31);
            put(11,9);
            put(32,22);
            put(30,18);
            put(15,29);
            put(26,7);
            put(1,28);
            put(22,12);
            put(9,35);
            put(34,3);
            put(13,26);
        }
    };

    public static int getRndNumberFR(){
        Random rnd = new Random();
        return rnd.nextInt(37);
    }

    public static int getRndNumberUS(){
        Random rnd = new Random();
        return rnd.nextInt(38);
    }

    public static int[] getNumberWithNeighborsUS(int num, int neighborCount){

        int arrayLength = 38;

        int[] result = new int[neighborCount*2+1];

        int posOfNumber = numberPositionUS[num];

        int currentPos = posOfNumber-neighborCount;

        for(int i = 0; i < result.length; i++){
            if(currentPos < 0)
                result[i] = rouletteNumbersUS[arrayLength+currentPos++];
            else if(currentPos >= arrayLength)
                result[i] = rouletteNumbersUS[currentPos++ - arrayLength];
            else
                result[i] = rouletteNumbersUS[currentPos++];
        }

        return result;

    }

    public static int[] getNumberWithNeighborsFR(int num, int neighborCount){

        int arrayLength = 37;

        int[] result = new int[neighborCount*2+1];

        int posOfNumber = numberPositionFR[num];

        int currentPos = posOfNumber-neighborCount;

        for(int i = 0; i < result.length; i++){
            if(currentPos < 0)
                result[i] = rouletteNumbersFR[arrayLength+currentPos++];
            else if(currentPos >= arrayLength)
                result[i] = rouletteNumbersFR[currentPos++ - arrayLength];
            else
                result[i] = rouletteNumbersFR[currentPos++];
        }

        return result;

    }
}
