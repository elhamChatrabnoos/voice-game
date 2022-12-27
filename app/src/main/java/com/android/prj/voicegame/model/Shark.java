package com.android.prj.voicegame.model;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.prj.voicegame.publicClasses.SeekBarMoving;

public class Shark {
    private int movingSharkStep = 18;
    private int movingStep = 20;
    private int windowWidth;
    private float sharkPositionX;
    private float sharkPositionY;
    public static boolean followFish;
    private int screenHeight;
    private boolean finishGame;
    private int rotationValue = 10;
    private int duration = 40;
    private int fishPosition;
    private boolean downAnimation = true;
    private boolean upAnimation = true;
    private boolean frontAnimation = true;
    private View topLine;

    public Shark(int backgroundWidth, int screenHeight, int layoutHeight, View topLine) {
        windowWidth = backgroundWidth;
        this.screenHeight = screenHeight;
        this.topLine = topLine;
        followFish = false;
        finishGame = false;
    }

    public void determineSharkPosition(ImageView sharkImage, int seekBarThumbPos, int fishPosition, int height) {
        this.fishPosition = fishPosition;
        // when fish moved shark start moving
        if (followFish) {
            moveShark(sharkImage, true, false, false);
        }

        // when seekbar go above of 100 shark start moving
        if (seekBarThumbPos >= 100 && !followFish) {
            followFish = true;
        }

        // move shark depend on fish position and
        // shark dosent hit the score layout or button line and game was started before
        // go up when fish is above of middle page
        if (fishPosition <= height/2 && sharkPositionY > sharkImage.getHeight()/6 && SeekBarMoving.startGame) {
            moveShark(sharkImage, false, true, false);
            downAnimation = true;
        }
        // go down if fish is below of middle page
        if (fishPosition > height / 2 && sharkPositionY < screenHeight
                - sharkImage.getHeight()*1.5 && followFish && SeekBarMoving.startGame) {
            moveShark(sharkImage, false, false, true);
            upAnimation = true;
        }
    }

    public void moveShark(ImageView sharkImage, boolean front, boolean up, boolean down) {
        sharkPositionY = sharkImage.getY();
        sharkPositionX = sharkImage.getX();

        // until player image receive target point don t move it anymore
        if (sharkPositionX < windowWidth) {
            if (front) {
                // set x to take image front
                sharkPositionX += movingSharkStep;
                sharkImage.setX(sharkPositionX);
            }

            // if shark is in page
            if (up && !finishGame) {
                // set y to take image up
                sharkPositionY -= movingSharkStep - 2;
                sharkImage.setY(sharkPositionY);
                Log.d("5656", "go up shark image y is " + sharkPositionY);
                // rotate shark head up
                if (upAnimation) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        sharkImage.animate().rotation(-rotationValue).setDuration(duration);
                    });
                    upAnimation = false;
                }
            } else if (down && !finishGame) {
                // set y to take image down
                sharkPositionY += movingSharkStep - 2;
                sharkImage.setY(sharkPositionY);
                // rotate shark head down
                if (downAnimation) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        Log.d("3r3r", "down animation: ");
                        sharkImage.animate().rotation(rotationValue).setDuration(duration);
                    });
                    downAnimation = false;
                }
            }

            // when shark is in straight way make its rotation 0
            else {
                if (frontAnimation) {
                    new Handler(Looper.getMainLooper()).post(() -> {
                        sharkImage.animate().rotation(0).setDuration(duration);
                        Log.d("3r3r", "front animation: ");
                    });
                    frontAnimation = false;
                }
            }
        }

    }

    public void changePosition(ImageView sharkImage, int fishPosition) {
        // when sound cut speed up shark moving and change its y depend on fish y
        movingSharkStep = 50;
        finishGame = true;
        // change shark y position depend on fish image position up or down
        // if fish y is down page
        if (fishPosition >= screenHeight - sharkImage.getHeight()) {
            sharkImage.setY(fishPosition - sharkImage.getHeight() / 2);
        }
        // if fish y is up page
        else if (fishPosition <= sharkImage.getHeight()) {
            sharkImage.setY(fishPosition - sharkImage.getHeight() / 3);
        }
    }

}
