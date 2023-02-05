package com.android.prj.voicegame.public_classes.dialogs;

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
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.car_game.CarGameActivity;
import com.android.prj.voicegame.databinding.NextPlayerDialogBinding;
import com.android.prj.voicegame.public_classes.PlaySound;
import com.android.prj.voicegame.public_classes.PublicSetting;

import java.util.Objects;

public class NextPlayerDialog extends Dialog {

    private final Dialog dialog;
    private Finish finish;
    private com.android.prj.voicegame.databinding.NextPlayerDialogBinding bindingCu;


    public NextPlayerDialog(@NonNull Context context,
                            Finish finishContext,
                            String message,
                            boolean isPlayerRobot) {
        super(context);

        bindingCu = NextPlayerDialogBinding.inflate(
                LayoutInflater.from(getContext()), null, false);
        dialog = new Dialog(context);
        dialog.setContentView(bindingCu.getRoot());
        dialog.setCancelable(false);
        finish = finishContext;

        // message was finish prepare page for finish game
        if (message.equals(getContext().getString(R.string.finishMsg))){
            bindingCu.message2.setText(R.string.see_result);
            bindingCu.playerNameOrFinish.setText(R.string.finishMsg);
        }
        // if message was player set name to message text
        else {
            bindingCu.playerNameOrFinish.setText(message);
        }

        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
        PublicSetting.hideSystemNavigation(dialog);

        // if click play button go next player or go final result
        bindingCu.yesBtn.setOnClickListener(view -> {
            dialog.dismiss();
            if (message.equals(getContext().getString(R.string.finishMsg))){
                finish.startTheGame(isPlayerRobot, getContext().getString(R.string.finishMsg));
            }
            else{
                finish.startTheGame(isPlayerRobot, getContext().getString(R.string.possitiveBtnTxt));
            }
        });

        bindingCu.restartBtn.setOnClickListener(view -> {
            PlaySound.playSound(getContext(), R.raw.click_sound, false);
            finish.restartFromNextPlayerDialog(dialog);
        });

        bindingCu.mainMenuBtn.setOnClickListener(view -> {
            PlaySound.playSound(getContext(), R.raw.click_sound, false);
            dialog.dismiss();
            finish.goMenuFromNextPlayerDialog();
        });
    }

    // this class implement every class that want to show
    public interface Finish{
        void startTheGame(boolean isPlayerRobot, String messageTxt);
        void restartFromNextPlayerDialog(Dialog dialog);
        void goMenuFromNextPlayerDialog();
    }

}
