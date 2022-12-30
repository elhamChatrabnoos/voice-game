package com.android.prj.voicegame.fish_game;
import android.annotation.SuppressLint;
import android.view.MotionEvent;
import android.view.View;
import android.widget.SeekBar;

public class SeekBarMoving {

    private int seekBarThumbPos;
    private int movingThumbStep;
    public static float sound;
    private int seekBarWidth;
    public static boolean gameFinished;
    public static boolean startGame;
    public static int minimumSound = 3000, maximumSound = 5000, looseSound = 900;

    @SuppressLint("ClickableViewAccessibility")
    public void setSeekbar(SeekBar seekBar) {
        seekBarWidth = 500;
        seekBar.setMax(seekBarWidth);
        // cant touch seekbar thumb
        seekBar.setOnTouchListener((View view, MotionEvent motionEvent) -> true);
        gameFinished = false;
        startGame = false;
    }

    public int moveSpeedPointer(SeekBar seekBar, float soundVolume, int minSound) {
        seekBarThumbPos = seekBar.getProgress();
        sound = soundVolume;

        if (seekBarThumbPos >= 100) {
            startGame = true;
        }

        // keep seekbar thumb in its place if below situation was true
        if (seekBarThumbPos >= 0 && seekBarThumbPos < 200) {
            moveSeekbar(900, 3000);
        } else if (seekBarThumbPos >= 200 && seekBarThumbPos < 400) {
            moveSeekbar(3000, 5000);
        } else if (seekBarThumbPos >= 400 && seekBarThumbPos <= 500) {
            moveSeekbar(5000, 5500);
        }

        seekBar.setProgress(movingThumbStep);
        return seekBarThumbPos;
    }

    private void moveSeekbar(int minSound, int maxSound) {
        if (sound < minSound && seekBarThumbPos >= 10) {
            movingThumbStep -= 10;
        }
        else if (sound > maxSound && seekBarThumbPos <= seekBarWidth - 10) {
            movingThumbStep += 10;
        }


        // if sound was higher than 5000 take up seekBarThumbPos
        if (sound >= maximumSound && seekBarThumbPos <= seekBarWidth - 50) {
            movingThumbStep += 50;
        }
        if (sound <= minimumSound && seekBarThumbPos >= 50) {
            movingThumbStep -= 50;
        }

        // if sound go lower than 900 loose game
        if (sound <= looseSound && startGame) {
            gameFinished = true;
        }
    }
}
