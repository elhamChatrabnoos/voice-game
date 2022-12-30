package com.android.prj.voicegame.public_classes;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class SoundDetector {

    private MediaRecorder recorder;

    public MediaRecorder startVoiceListening(Context context) {
        // prepare recording sound
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // save recording
        File file = null;
        try {
            file = File.createTempFile("prefix", ".extensions", context.getCacheDir());

            // set audio to android 8 and above
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                recorder.setOutputFile(file);
            }
            else{
                File audioFile = File.createTempFile("prefix", ".extensions", file);
                recorder.setOutputFile(audioFile.getAbsolutePath());
            }
        } catch (IOException e) {
            Log.d("2020", "startVoiceListening1: " + e.getMessage());
        }
        try {
            recorder.prepare();
            recorder.start();
        } catch (Exception e) {
            Log.d("2020", "startVoiceListening2: " + e.getMessage());
        }
        return recorder;
    }
}
