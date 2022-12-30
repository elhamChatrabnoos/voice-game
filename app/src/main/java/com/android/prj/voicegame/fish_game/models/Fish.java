package com.android.prj.voicegame.fish_game.models;

import android.widget.HorizontalScrollView;
import android.widget.ImageView;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.fish_game.SeekBarMoving;

public class Fish {

    private int movingFishStep = 0;
    public static int movingStep = 20;
    private int windowWidth;
    private int fishPositionX;
    private int fishPositionY;
    private boolean startGame;
    private double scrollX;
    private int screenWidth, screenHeight;
    private int fishPosition;
    public static boolean rockDestroyer;
    public static boolean hookCutter;
    private ImageView fishImage;
    private ConstraintLayout fishLayout;

    public Fish(int backgroundWidth, int screenWidth,
                int screenHeight, ImageView fishImage, ConstraintLayout fishLayout) {
        windowWidth = backgroundWidth;
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
        this.fishImage = fishImage;
        this.fishLayout = fishLayout;
        rockDestroyer = false;
        hookCutter = false;
    }

    public int determineFishPosition(ConstraintLayout playerImg, int seekBarThumbPos) {
        if (startGame) {
            fishPosition = moveFish(playerImg, true, false, false);
            movingFishStep = movingStep;
        }

        // start fish moving when seekbar thumb go above 100
        if (seekBarThumbPos > 100){
            startGame = true;
        }

        // move fish depend on seekbar position
        if (seekBarThumbPos <= 150 && startGame && fishPositionY < screenHeight - playerImg.getHeight() * 2) {
            fishPosition = moveFish(playerImg, false, false, true);
        }
        else if (seekBarThumbPos >= 350 && seekBarThumbPos <= 500 && fishPositionY > playerImg.getHeight()/4) {
            fishPosition = moveFish(playerImg, false, true, false);
        }

        // when sound cut in seekbar class fish stopped
        if (SeekBarMoving.gameFinished) {
            movingFishStep = 0;
            startGame = false;
        }
        return fishPosition;
    }

    public int moveFish(ConstraintLayout playerImg, boolean front, boolean up, boolean down) {
        fishPositionY = (int) playerImg.getY();
        fishPositionX = (int) playerImg.getX();

        // until player image receive target point don t move it anymore
        if (fishPositionX < windowWidth) {
            if (front) {
                // set x to take image front
                fishPositionX += movingFishStep;
                playerImg.setX(fishPositionX);
            }
            if (up) {
                if (SeekBarMoving.sound >= SeekBarMoving.maximumSound) {
                    fishPositionY -= movingFishStep * 3;
                } else {
                    // set y to take image up
                    fishPositionY -= movingFishStep;
                }
                playerImg.setY(fishPositionY);
            }

            if (down) {
                // set y to take image down
                if (SeekBarMoving.sound <= SeekBarMoving.minimumSound) {
                    fishPositionY += movingFishStep * 3;
                } else {
                    fishPositionY += movingFishStep;
                }
                playerImg.setY(fishPositionY);
            }
        }
        return fishPositionY;
    }

    public void scrollBackground(HorizontalScrollView scrollView) {
        // target point for scroll background
        int scrollPoint = screenWidth/2;
        if (fishPositionX > scrollPoint) {
            if (scrollX < windowWidth) {
                // fish moving sooner background
                // when fish receive target point, scrolling background starting
                scrollX = fishPositionX - scrollPoint;

                // move background
                scrollView.scrollTo((int) scrollX, (int) scrollX);
            }
        }
    }

    // when fish have collision with rock stop fish and change its image
    public void stopFish() {
        if (!rockDestroyer) {
            movingStep -= 1;
            fishLayout.animate().translationX(fishPositionX - 3);
        }
    }

    public void changeToHammer() {
        rockDestroyer = true;
        fishImage.setImageResource(R.drawable.hammerhead_img);
    }

    public void destroyTheRock(ImageView rockImage) {
        rockImage.setImageResource(R.drawable.broken_rock);
    }

    public void goUp(ImageView hookImage) {
        hookImage.animate().translationY(-screenHeight).setDuration(500);
        fishLayout.animate().translationY(-screenHeight).setDuration(300);
    }

    public void cutTheHooks(ImageView hookImage) {
        hookImage.animate().translationY(screenHeight).setDuration(300);
    }

    // when fish collision with swordFish icon, image change to sword
    public void changeToSwordFish() {
        hookCutter = true;
        fishImage.setImageResource(R.drawable.sword_fish);
    }

}
