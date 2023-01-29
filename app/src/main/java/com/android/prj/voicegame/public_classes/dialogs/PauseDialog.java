package com.android.prj.voicegame.public_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;

import androidx.annotation.NonNull;

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
            dialog.dismiss();
            actions.continueGame();
        });

        binding.btnHome.setOnClickListener(view -> {
            PlaySound.playSound(getContext(), R.raw.click_sound, false);
            dialog.dismiss();
            actions.goMainMenu();
        });

        binding.btnRetry.setOnClickListener(view -> {
            PlaySound.playSound(getContext(), R.raw.click_sound, false);
            dialog.dismiss();
            actions.restartGame();
        });

    }

    public interface Actions{
        void continueGame();
        void goMainMenu();
        void restartGame();
    }

}
