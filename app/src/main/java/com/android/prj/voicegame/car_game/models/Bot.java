package com.android.prj.voicegame.car_game.models;

import java.util.Random;

public class Bot {

    //?? commend all class
    private static final String TAG = "2020";
    private int[] randomNumbers;
    private Random random;

    private int robotSound;
    private int loosePoint;
    private boolean loopVar = true;
    int number = 0;


    public int getLoosePoint() {
        return loosePoint;
    }

    public void setLoosePoint(int loosePoint) {
        this.loosePoint = loosePoint;
    }

    public void setRobotField(int redPercent, int yellowPercent, int green1Percent, int green2Percent, int orangePercent) {
        randomNumbers = new int[30];
        random = new Random();

        // when player bot receive this point make it loose
        loosePoint = 9000;

        int number1 = 0;
        int number2 = 0;
        int number3 = 0;
        int number4 = 0;
        int number5 = 0;

        // generate random
        for (int i = 0; i < randomNumbers.length; i++) {
            // generate random number between 1 to 5 until generate suitable number
            while (loopVar){
                number = random.nextInt(5) + 1;
                if (number == 1 && number1 < redPercent){
                    number1 ++;
                    break;
                }
                else if (number == 2 && number2 < yellowPercent){
                    number2 ++;
                    break;
                }
                else if (number == 3 && number3 < green1Percent){
                    number3 ++;
                    break;
                }
                else if (number == 4 && number4 < green2Percent){
                    number4 ++;
                    break;
                }
                else if (number == 5 && number5 < orangePercent){
                    number5 ++;
                    break;
                }
            }
            randomNumbers[i] = number;
        }
    }

    public int generateNumber(){
        int number = random.nextInt(29) + 1;
        return randomNumbers[number];
    }

    public int generateSound(){
        switch (generateNumber()){
            case 1:
                robotSound = 300;
                break;
            case 2:
                robotSound = 1000;
                break;
            case 3:
                robotSound = 3000;
                break;
            case 4:
                robotSound = 5000;
                break;
            case 5:
                robotSound = 7000;
                break;
        }

        return robotSound;
    }

    public void prepareBotField() {
        Random random = new Random();
        // generate random number to set robot difficulty
        int randomBotPlayer = random.nextInt(4) + 1;
        switch (randomBotPlayer){
            case 1:
                setRobotField(1,3,12,11,3);
                break;
            case 2:
                setRobotField(1,4,11,11,3);
                break;
            case 3:
                setRobotField(1,3,11,12,3);
                break;
            case 4:
                setRobotField(1,4,9,13,3);
                break;
        }
    }

}
