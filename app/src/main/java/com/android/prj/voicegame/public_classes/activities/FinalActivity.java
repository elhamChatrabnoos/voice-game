package com.android.prj.voicegame.public_classes.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.databinding.ActivityFinalBinding;
import com.android.prj.voicegame.public_classes.PlaySound;
import com.android.prj.voicegame.public_classes.model.Player;
import com.android.prj.voicegame.public_classes.PublicSetting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.droidsonroids.gif.GifImageView;

public class FinalActivity extends AppCompatActivity {

    private ActivityFinalBinding binding;
    private List<Player> playerList;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        PlaySound.playSound(this, R.raw.final_activity_sound, true);

        sortPlayers();
        setPlayerStickers();
        showPlayersImage();

        PublicSetting.setAppLanguage(getApplicationContext().getResources());
        PublicSetting.hideBars(this);
    }

    private void setPlayerStickers() {
        int[] playerStickers = new int[]{
                R.drawable.grade_one_player,
                R.drawable.grade_two_player,
                R.drawable.grade_three_player,
                R.drawable.grade_four_player,
        };

        // set players sticker depend on their sort
        for (int i = 0; i < playerList.size(); i++) {
            playerList.get(i).setPlayerResultSticker(playerStickers[i]);
        }

        //// search each player between all if they have same score make their sticker like each other
        for (int i = 0; i < playerList.size(); i++) {
            for (int j = 0; j < playerList.size(); j++) {
                if (playerList.get(i).getPlayerTotalScore() == playerList.get(j).getPlayerTotalScore()) {
                    playerList.get(j).setPlayerResultSticker(playerList.get(i).getPlayerResultSticker());
                }
            }
        }
    }

    @Override
    public void onBackPressed() {

    }


    private void showPlayersImage() {
        LinearLayout[] playersLayout = new LinearLayout[]{
                binding.firstPlayerLayout,
                binding.secondPlayerLayout,
                binding.thirdPlayerLayout,
                binding.fourthPlayerLayout};

        TextView[] playersScore = new TextView[]{
                binding.firstPlayerScore,
                binding.secondPlayerScore,
                binding.thirdPlayerScore,
                binding.fourthPlayerScore};

        GifImageView[] playersStickerGif = new GifImageView[]{
                binding.firstPlayer, binding.secondPlayer, binding.thirdPlayer, binding.fourthPlayer
        };

        int numberOfPlayer = PlayerSelectionActivity.numberOfPlayer;

        for (int i = 0; i < numberOfPlayer; i++) {
            String playerColor = playerList.get(i).getPlayerColor();
            if (playerColor.equals(getString(R.string.blue_color))) {
                playersLayout[i].setBackgroundResource(R.drawable.blue_result_back);
            }
            if (playerColor.equals(getString(R.string.green_color))) {
                playersLayout[i].setBackgroundResource(R.drawable.green_result_back);
            }
            if (playerColor.equals(getString(R.string.red_color))) {
                playersLayout[i].setBackgroundResource(R.drawable.red_result_back);
            }
            if (playerColor.equals(getString(R.string.yellow_color))) {
                playersLayout[i].setBackgroundResource(R.drawable.yellow_result_back);
            }
            playersLayout[i].setVisibility(View.VISIBLE);
            playersScore[i].setText(String.valueOf(playerList.get(i).getPlayerTotalScore()));
            playersStickerGif[i].setImageResource(playerList.get(i).getPlayerResultSticker());
        }

    }


    private void sortPlayers() {
        Collections.sort(PlayerSelectionActivity.playerList, (obj1, obj2) ->
                Integer.compare(obj2.getPlayerTotalScore(), obj1.getPlayerTotalScore()));
        playerList = PlayerSelectionActivity.playerList;
    }


    public void home_button_click(View view) {
        PlaySound.stopSound();
        finish();
        startActivity(new Intent(this, SelectGameActivity.class));
    }
}