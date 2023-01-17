package com.android.prj.voicegame.public_classes.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.databinding.SensitiveSettingLayoutBinding;
import com.android.prj.voicegame.fish_game.SeekBarMoving;
import com.android.prj.voicegame.public_classes.PlaySound;
import com.android.prj.voicegame.public_classes.SoundDetector;

import java.util.Objects;

public class SensorSettingDialog extends DialogFragment {

    private SensitiveSettingLayoutBinding binding;
    private int seekBarThumbPos;
    private float soundVolume;
    private int movingThumbStep = 0;
    private MediaRecorder recorder;
    private boolean loopVar = true;
    private final int threadDelay = 50;
    private float sliderValue = 0;
    private SensitiveSetting check;
    //?? remove
    private SeekBarMoving seekBarMoving;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        check = (SensitiveSetting) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        // make alert dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        binding = SensitiveSettingLayoutBinding.inflate(getLayoutInflater());
        builder.setCancelable(false);
        builder.setView(binding.getRoot());

        // get sound from micro
        SoundDetector soundDetector = new SoundDetector();
        recorder = soundDetector.startVoiceListening(getContext());

        setSeekBarSetting();
        startThread();
        onClickObject();

        return builder.create();
    }

    private void moveSpeedPointer(){
        seekBarThumbPos = binding.mainSeekbar.getProgress();

        // change speed pointer position
        if (seekBarThumbPos >= 0 && seekBarThumbPos < 50){
            moveSeekbar(100, 1000);
        }
        else if (seekBarThumbPos >= 50 && seekBarThumbPos < 150){
            moveSeekbar(1000, 2000);
        }
        else if (seekBarThumbPos >= 150 && seekBarThumbPos < 300){
            moveSeekbar(2000, 3000);
        }
        else if (seekBarThumbPos >= 300 && seekBarThumbPos < 400){
            moveSeekbar(3000, 4000);
        }
        else if (seekBarThumbPos >= 400 && seekBarThumbPos <= 500){
            moveSeekbar(4000, 6000);
        }

        binding.mainSeekbar.setProgress(movingThumbStep);
    }

    private void moveSeekbar(int minSound, int maxSound) {
        float sound = 1;

        if (soundVolume > 10){
            sound = (soundVolume/((sliderValue +1)*0.3f));
        }
        if (sound >= minSound && sound <= maxSound){
            movingThumbStep += 0;
        }
        else if(sound < minSound && seekBarThumbPos >= 1){
            movingThumbStep -= 10;
        }
        else if(sound > maxSound && seekBarThumbPos < 500){
            movingThumbStep += 5;
        }
    }

    private void startThread() {
        Thread recorderThread = new Thread(() -> {
            while (loopVar) {
                if (recorder != null) {
                    soundVolume = recorder.getMaxAmplitude();
                    moveSpeedPointer();
                }
                try {
                    Thread.sleep(threadDelay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        recorderThread.start();
    }

    private void onClickObject() {
        binding.startGame.setOnClickListener(view -> {
            // start game activity
            PlaySound.playSound(getContext(), R.raw.click_sound, false);
            loopVar = false;
            check.getSensInfo(sliderValue, this);
        });

        binding.sensitiveSeekbar.addOnChangeListener((slider, value, fromUser) -> sliderValue = value);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setSeekBarSetting() {
        int seekBarWidth = 500;
        binding.mainSeekbar.setMax(seekBarWidth);
        binding.mainSeekbar.setOnTouchListener((view, motionEvent) -> true);
    }

    // to prevent close dialog by click on page
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).requestWindowFeature(STYLE_NO_TITLE);
        getDialog().setCancelable(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface SensitiveSetting {
        void getSensInfo(float sliderValue, DialogFragment dialogFragment);
    }

}
