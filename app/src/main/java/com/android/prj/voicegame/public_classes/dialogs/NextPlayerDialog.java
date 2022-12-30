package com.android.prj.voicegame.public_classes.dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.car_game.CarGameActivity;
import com.android.prj.voicegame.databinding.NextPlayerDialogBinding;

import java.util.Objects;

public class NextPlayerDialog extends DialogFragment {

    private Finish finish;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        finish = (Finish) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        NextPlayerDialogBinding bindingCu;
        bindingCu = NextPlayerDialogBinding.inflate(LayoutInflater.from(getContext()));
        androidx.appcompat.app.AlertDialog.Builder dialog = new AlertDialog.Builder(requireContext());
        dialog.setView(bindingCu.getRoot());

        dialog.setCancelable(false);

        assert getArguments() != null;
        String message = getArguments().getString(CarGameActivity.MESSAGE);
        boolean isPlayerRobot = getArguments().getBoolean(CarGameActivity.IS_PLAYER_ROBOT);

        // message was finish prepare page for finish game
        if (message.equals(getString(R.string.finishMsg))){
            bindingCu.message2.setText(R.string.see_result);
            bindingCu.message.setText(R.string.finishMsg);
        }
        // if message was player set name to message text
        else {
            bindingCu.message.setText(message);
        }

        bindingCu.yesBtn.setOnClickListener(view -> {
            if (message.equals(getString(R.string.finishMsg))){
                finish.startTheGame(isPlayerRobot, getString(R.string.finishMsg));
            }
            else{
                finish.startTheGame(isPlayerRobot, getString(R.string.possitiveBtnTxt));
            }
            dismiss();
        });

        bindingCu.restartBtn.setOnClickListener(view -> {
            finish.restartFromNextPlayerDialog(this);
        });

        bindingCu.mainMenuBtn.setOnClickListener(view -> {
            dismiss();
            finish.goMainMenu();
        });

        return dialog.create();
    }


    // don t close the dialog when touch out of screen
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).requestWindowFeature(STYLE_NO_TITLE);
        getDialog().setCancelable(false);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    //?? commend
    public interface Finish{
        void startTheGame(boolean isPlayerRobot, String messageTxt);
        void restartFromNextPlayerDialog(DialogFragment dialogFragment);
        void goMainMenu();
    }

}