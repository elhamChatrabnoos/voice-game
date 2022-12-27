package com.android.prj.voicegame.model;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.android.prj.voicegame.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.droidsonroids.gif.GifImageView;

public class Barriers {

    public static final String HOOKNAME = "hook";
    public static final String ROCKNAME = "rock";
    public static final String HAMMERNAME = "hammer";
    public static final String SWORD_FISH = "swordFish";
    private final List<Rock> rockList;
    private final List<Hook> hookList;
    private final List<HammerFish> hammerFishList;
    private final List<SwordFish> swordFishList;
    private final Context context;
    private final ConstraintLayout layout;
    private int screenHeight = 0;
    private int screenWidth = 0;
    private final Random random;

    public List<SwordFish> getSwordFishList() {
        return swordFishList;
    }

    public List<Hook> getHookList() {
        return hookList;
    }

    public List<Rock> getRockList() {
        return rockList;
    }

    public List<HammerFish> getHammerFishList() {
        return hammerFishList;
    }

    public Barriers(Context context, ConstraintLayout layout, int screenHeight, int screenWidth) {
        this.context = context;
        this.layout = layout;
        this.screenHeight = screenHeight;
        this.screenWidth = screenWidth;

        rockList = new ArrayList<>();
        hookList = new ArrayList<>();
        hammerFishList = new ArrayList<>();
        swordFishList = new ArrayList<>();
        random = new Random();
    }

    private int imageX = 0, imageY = 0;
    int xDistance = 1000;

    public void generateObjects() {
        // 400
        int hook1Height = (int) (screenHeight * 0.4);
        // 100
        int hook1Width = (int) (screenWidth * 0.08);
        // 500
        int rockHeight = (int) (screenHeight * 0.46);
        // 500
        int rockWidth = (int) (screenHeight * 0.46);
        // 100
        int iconsSize = (int) (screenWidth * 0.05);

        int[] randomNumbers = new int[100];
        for (int i = 0; i < randomNumbers.length; i++) {
            randomNumbers[i] = random.nextInt(6) + 1;
        }

        // add images to related list depend on generated number
        for (int randomNumber : randomNumbers) {
            imageX += xDistance;
            switch (randomNumber) {
                case 1:
                case 2:
                    imageY = 0;
                    addImage(R.drawable.hook2, hook1Width, hook1Height, HOOKNAME);
                    break;
                case 3:
                case 6:
                    imageY = screenHeight - rockHeight - 100;
                    addImage(R.drawable.rock, rockWidth, rockHeight, ROCKNAME);
                    break;
                case 4:
                    addImage(R.drawable.sword_fish, iconsSize, iconsSize, SWORD_FISH);
                    // to create icons also bottom of page
                    imageY = screenHeight - iconsSize * 3;
                    break;
                case 5:
                    addImage(R.drawable.hammerhead_img, iconsSize, iconsSize, HAMMERNAME);
                    break;
            }
        }
    }

    private void addImage(int imageResource, int width, int height, String imageKind) {
        // make custom image
        GifImageView imageView = new GifImageView(context);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(width, height);
        imageView.setX(imageX);
        imageView.setY(imageY);
        imageView.setLayoutParams(params);
        imageView.setImageResource(imageResource);
        layout.addView(imageView);

        // add image to rocks list
        switch (imageKind) {
            case ROCKNAME:
                Rock rock = new Rock();
                rock.setImageView(imageView);
                rockList.add(rock);
                break;
            //add image to hook list
            case HOOKNAME:
                Hook hook = new Hook();
                hook.setImageView(imageView);
                hookList.add(hook);
                // add half of hook
//                imageView.setImageResource(R.drawable.tie);
//                layout.addView(imageView);
                break;
            //add image to hammer fish list
            case HAMMERNAME:
                HammerFish hammerFish = new HammerFish();
                hammerFish.setImageView(imageView);
                hammerFishList.add(hammerFish);
                break;
            case SWORD_FISH:
                SwordFish swordFish = new SwordFish();
                swordFish.setImageView(imageView);
                swordFishList.add(swordFish);
                break;
        }
    }

    public boolean checkCollision(View view1, View view2, int left, int right, int top, int bottom) {
        Rect r1 = new Rect();
        view1.getHitRect(r1);
        Rect r2 = new Rect();
        view2.getHitRect(r2);
        return r1.intersect(r2.left + left, r2.top + top,
                r2.right + right, r2.bottom + bottom);
    }

}
