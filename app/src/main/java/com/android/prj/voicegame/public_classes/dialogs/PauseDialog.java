package com.android.prj.voicegame.public_classes.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.databinding.PauseDialogBinding;
import com.android.prj.voicegame.public_classes.PlaySound;
import com.android.prj.voicegame.public_classes.PublicSetting;

public class PauseDialog extends Dialog {

    private PauseDialogBinding binding;
    private Actions actions;

    public PauseDialog(@NonNull Context context, Actions actions) {
        super(context);

        this.actions = actions;
        binding = PauseDialogBinding.inflate(getLayoutInflater());
        Dialog dialog = new Dialog(context);
        dialog.setContentView(binding.getRoot());
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        PublicSetting.hideSystemNavigation(dialog);

        binding.btnBack.setOnClickListener(view -> {
            PlaySound.playSound(getContext(), R.raw.click_sound, false);
            dismiss();
            actions.continueGame();
        });

        binding.btnHome.setOnClickListener(view -> {
            PlaySound.playSound(getContext(), R.raw.click_sound, false);
            dismiss();
            actions.goMainMenu();
        });

        binding.btnRetry.setOnClickListener(view -> {
            PlaySound.playSound(getContext(), R.raw.click_sound, false);
            dismiss();
            actions.restartGame();
        });

    }

//    @Override
//    public void onAttach(@NonNull Context context) {
//        super.onAttach(context);
//        actions = (Actions) context;
//    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        binding = PauseDialogBinding.inflate(getLayoutInflater());
//        builder.setCancelable(false);
//        builder.setView(binding.getRoot());
//
//        binding.btnBack.setOnClickListener(view -> {
//            PlaySound.playSound(getContext(), R.raw.click_sound, false);
//            dismiss();
//            actions.continueGame();
//        });
//
//        binding.btnHome.setOnClickListener(view -> {
//            PlaySound.playSound(getContext(), R.raw.click_sound, false);
//            dismiss();
//            actions.goMainMenu();
//        });
//
//        binding.btnRetry.setOnClickListener(view -> {
//            PlaySound.playSound(getContext(), R.raw.click_sound, false);
//            dismiss();
//            actions.restartGame();
//        });
//
//        return builder.create();
//    }

//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        getDialog().requestWindowFeature(STYLE_NO_TITLE);
//        getDialog().setCancelable(false);
//        return super.onCreateView(inflater, container, savedInstanceState);
//    }

    public interface Actions{
        void continueGame();
        void goMainMenu();
        void restartGame();
    }

}
