package com.android.prj.voicegame.public_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaRecorder;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.databinding.SensitiveSettingLayoutBinding;
import com.android.prj.voicegame.public_classes.PlaySound;
import com.android.prj.voicegame.public_classes.PublicSetting;
import com.android.prj.voicegame.public_classes.SoundDetector;

public class SensorSettingDialog extends Dialog {

    private final Dialog dialog;
    private SensitiveSettingLayoutBinding binding;
    private int seekBarThumbPos;
    private float soundVolume;
    private int movingThumbStep = 0;
    private MediaRecorder recorder;
    private boolean loopVar = true;
    private final int threadDelay = 50;
    private float sliderValue = 2;
    private SensitiveSetting sensitiveSetting;

    public SensorSettingDialog(@NonNull Context context, SensitiveSetting sensitiveSetting) {
        super(context);
        this.sensitiveSetting = sensitiveSetting;

        dialog = new Dialog(getContext());
        binding = SensitiveSettingLayoutBinding.inflate(getLayoutInflater());
        dialog.setContentView(binding.getRoot());
        dialog.setCancelable(false);
        dialog.show();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        PublicSetting.hideSystemNavigation(dialog);

        // get sound from micro
        SoundDetector soundDetector = new SoundDetector();
        recorder = soundDetector.startVoiceListening(getContext());

        setSeekBarSetting();
        startThread();
        onClickObject();
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
            dialog.dismiss();
            // start game activity
            PlaySound.playSound(getContext(), R.raw.click_sound, false);
            loopVar = false;
            sensitiveSetting.getSensInfo(sliderValue, this);
        });
        binding.sensitiveSeekbar.addOnChangeListener((slider, value, fromUser) -> sliderValue = value);
    }

    private void setSeekBarSetting() {
        int seekBarWidth = 500;
        binding.mainSeekbar.setMax(seekBarWidth);
        binding.mainSeekbar.setOnTouchListener((view, motionEvent) -> true);
    }

    public interface SensitiveSetting {
        void getSensInfo(float sliderValue, Dialog dialogFragment);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
            } catch (Exception e) {
                Log.d("2020", "onDestroy: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }
}
