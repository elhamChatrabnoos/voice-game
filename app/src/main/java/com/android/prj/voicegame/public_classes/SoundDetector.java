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
        String mFileName = Environment.getExternalStorageDirectory().getAbsolutePath();
        mFileName += "/AudioRecording.3gp";
        // below method is used to initialize
        // the media recorder class
        MediaRecorder recorder = new MediaRecorder();

        // below method is used to set the audio
        // source which we are using a mic.
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);

        // below method is used to set
        // the output format of the audio.
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);

        // below method is used to set the
        // audio encoder for our recorded audio.
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);

        // below method is used to set the
        // output file location for our recorded audio
        recorder.setOutputFile(mFileName);
        try {
            recorder.prepare();
        } catch (IOException e) {
            Log.e("4040", "prepare() failed");
        }
        recorder.start();
        return recorder;
    }
}
