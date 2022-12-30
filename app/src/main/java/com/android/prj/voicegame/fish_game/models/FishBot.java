package com.android.prj.voicegame.fish_game.models;

import java.util.Random;

public class FishBot {

    private int[] randomNumbers;
    private Random random;
    private int robot;
    private int loosePoint;


    public void setRobotField(int downPercent, int frontPercent, int upPercent, int soundCutPercent) {
        randomNumbers = new int[30];
        random = new Random();

        // when player bot receive this point make it loose

        int number1 = 0;
        int number2 = 0;
        int number3 = 0;
        int number4 = 0;

        // generate random numbers
        int number;
        for (int i = 0; i < randomNumbers.length; i++) {
            while (true){
                // generate random number between 1 to 5
                number = random.nextInt(5) + 1;
                if (number == 1){
                    if (number1 < downPercent){
                        number1 ++;
                        break;
                    }
                }
                else if (number == 2){
                    if (number2 < frontPercent){
                        number2 ++;
                        break;
                    }
                }
                else if (number == 3){
                    if (number3 < upPercent){
                        number3 ++;
                        break;
                    }
                }
                else if (number == 4){
                    if (number4 < soundCutPercent){
                        number4 ++;
                        break;
                    }
                }
            }
            randomNumbers[i] = number;
        }
    }

    public int generateNumber(){
        int number = random.nextInt(30);
        return randomNumbers[number];
    }

    public int generateSound(){
        switch (generateNumber()){
            case 1:
                robot = 2000;
                break;
            case 2:
                robot = 3000;
                break;
            case 3:
                robot = 7000;
                break;
            case 4:
                robot = 800;
                break;
        }
        return robot;
    }

    public void prepareBotField() {
        Random random = new Random();
        // generate random number to set robot difficulty
        int randomBotPlayer = random.nextInt(4) + 1;
        switch (randomBotPlayer){
            case 1:
                setRobotField(4,13,13, 0);
                break;
            case 2:
                setRobotField(3,10,16, 1);
                break;
            case 3:
                setRobotField(2,11,17,0);
                break;
            case 4:
                setRobotField(1,7,21, 1);
                break;
        }
    }
}
