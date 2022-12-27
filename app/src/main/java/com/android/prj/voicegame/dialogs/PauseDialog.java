package com.android.prj.voicegame.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.prj.voicegame.databinding.PauseDialogBinding;
import com.android.prj.voicegame.databinding.SensitiveSettingLayoutBinding;
import com.google.android.material.slider.Slider;

import java.io.File;
import java.io.IOException;

public class PauseDialog extends DialogFragment {

    private PauseDialogBinding binding;
    private Actions actions;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        actions = (Actions) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        binding = PauseDialogBinding.inflate(getLayoutInflater());
        builder.setCancelable(false);
        builder.setView(binding.getRoot());

        binding.btnBack.setOnClickListener(view -> {
            dismiss();
            actions.continueGame();
        });

        binding.btnHome.setOnClickListener(view -> {
            dismiss();
            actions.goMainMenu();
        });

        binding.btnRetry.setOnClickListener(view -> {
            dismiss();
            actions.restartGame();
        });

        return builder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(STYLE_NO_TITLE);
        getDialog().setCancelable(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    public interface Actions{
        void continueGame();
        void goMainMenu();
        void restartGame();
    }

}
