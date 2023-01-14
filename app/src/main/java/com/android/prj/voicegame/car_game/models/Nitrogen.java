package com.android.prj.voicegame.car_game.models;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.car_game.CarGameActivity;
import com.android.prj.voicegame.public_classes.customs.CustomViews;

import java.util.Random;

public class Nitrogen {

    //?? commend all method
    private int[] randomPoints;
    private ImageView imageView;
    Context context;
    private boolean carCollision;
    public static int nitrogenProgress = 0;
    int carPosition;
    ConstraintLayout mainLayout;
    int numberOfNitrogen = 15;
    int nitrogenCount;
    private Random random = new Random();
    private boolean nitroCollision;
    private final int screenWidth;
    private final int screenHeight;
    private View nitrogenPoint;

    public boolean isNitroCollision() {
        return nitroCollision;
    }

    public void setNitroCollision(boolean nitroCollision) {
        this.nitroCollision = nitroCollision;
    }

    public int getNitrogenCount() {
        return nitrogenCount;
    }

    public void setNitrogenCount(int nitrogenCount) {
        this.nitrogenCount = nitrogenCount;
    }

    public int getNumberOfNitrogen() {
        return numberOfNitrogen;
    }

    public void setNumberOfNitrogen(int numberOfNitrogen) {
        this.numberOfNitrogen = numberOfNitrogen;
    }

    // generate random number
    public int robotNitroRandomNumber() {
        random = new Random();
        return random.nextInt(3) + 1;
    }

    public Nitrogen(Context context, ConstraintLayout mainLayout, int screenWidth, int screenHeight) {
        this.context = context;
        this.mainLayout = mainLayout;
        imageView = new ImageView(context);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;
    }

    // generate random nitrogen in robot game
    public void generateRandomNumber() {
        randomPoints = new int[numberOfNitrogen];
        randomPoints[0] = random.nextInt(1000) + 50;

        for (int i = 1; i < randomPoints.length; i++) {
            randomPoints[i] = random.nextInt(1000) + randomPoints[i - 1] + 1;
        }

    }

    public boolean checkCollision(View imageView, View view) {
        Rect r1 = new Rect();
        imageView.getHitRect(r1);
        Rect r2 = new Rect();
        view.getHitRect(r2);
        return r1.intersect(r2);
    }

    public boolean generateCapsule(int carPosition, int movingStep, ConstraintLayout playerLayout,
                                        View nitrogenPoint, int roadHeight) {
        carCollision = false;
        for (int randomPoint : randomPoints) {
            // when car position was between generated random numbers make capsule
            if (carPosition >= randomPoint && carPosition <= randomPoint + movingStep) {
                this.carPosition = carPosition;
                carCollision = true;
                nitrogenCount++;
                imageView = new ImageView(context);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int) (screenWidth * 0.15), (int) (screenHeight * 0.15));
                imageView.setX(randomPoint + CarGameActivity.screenWidth);
                imageView.setY((float) (roadHeight/4));
                imageView.setLayoutParams(params);
                imageView.setImageResource(R.drawable.caps_blue);
                playerLayout.addView(imageView);
                imageView.animate().rotation(360).translationX(-300).setDuration(4000);
                this.nitrogenPoint = nitrogenPoint;
                if (imageView.getX() < 0) {
                    playerLayout.removeView(imageView);
                }
            }
        }
        return carCollision;
    }

    public boolean checkNitrogenCrossCar(ConstraintLayout targetImage, boolean isPlayerRobot) {
        carCollision = !(imageView.getX() < targetImage.getX());
        // when nitrogen generated show car point and if nitrogen crossed the car remove car point
       if (nitrogenPoint != null){
           if (targetImage.getX() < imageView.getX() && !isPlayerRobot) {
               nitrogenPoint.setVisibility(View.VISIBLE);
           }
           else {
               nitrogenPoint.setVisibility(View.INVISIBLE);
           }
       }
        return carCollision;
    }

    // when nitrogen cross car remove it from page
    public void removeViewFrom(ConstraintLayout layout, ProgressBar progressBar, ConstraintLayout targetImage, boolean spoilCar) {
        if (checkCollision(targetImage, imageView) && !spoilCar) {
            layout.removeView(imageView);
            nitrogenProgress += 35;
            nitroCollision = true;
            progressBar.setProgress(nitrogenProgress);
        } else if (!checkNitrogenCrossCar(targetImage, true) && !CarGameActivity.playWithRobot) {
            CustomViews.showText(context.getString(R.string.late_click_txt)
                    , CarGameActivity.screenWidth / 2 - CarGameActivity.screenWidth / 7
                    , CarGameActivity.screenHeight / 2 - CarGameActivity.screenHeight / 7
                    , mainLayout
                    , context);
            nitroCollision = false;
        }
    }

}
