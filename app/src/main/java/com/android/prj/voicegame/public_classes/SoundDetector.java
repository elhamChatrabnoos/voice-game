package com.android.prj.voicegame.public_classes;

import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.IOException;

public class SoundDetector {

    public MediaRecorder startVoiceListening(Context context) {
        // we are here initializing our filename variable
        // with the path of the recorded audio file.

        MediaRecorder recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                File file = File.createTempFile("prefix", ".extensions", context.getCacheDir());
                recorder.setOutputFile(file);
            }
            else{
                String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
                mFileName += "/AudioRecording.3gp";
                recorder.setOutputFile(mFileName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            recorder.prepare();
            recorder.start();
        } catch (IOException e) {
            Log.e("4040", "prepare() failed");
        }

        return recorder;
    }
}
