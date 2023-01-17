package com.android.prj.voicegame.public_classes.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

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
    
    private GifImageView[] playersImageView;
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
        showPlayersImage();

        binding.btnBack.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, SelectGameActivity.class));
        });
        PublicSetting.setAppLanguage(getApplicationContext().getResources());
        PublicSetting.hideBars(this);
    }

    @Override
    public void onBackPressed() {

    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    private void showPlayersImage() {
        playersImageView = new GifImageView[]{binding.firstPlayer, binding.secondPlayer,
                binding.thirdPlayer, binding.fourthPlayer};

        int numberOfPlayer = PlayerSelectionActivity.numberOfPlayer;

        for (int i = 0; i < numberOfPlayer; i++) {
            String playerColor = playerList.get(i).getPlayerColor();
            if (playerColor.equals(getString(R.string.blue_color))) {
                Log.d("2020", "showPlayersImage: ");
                playersImageView[i].setBackgroundColor(getColor(R.color.blue_color));
            }
            if (playerColor.equals(getString(R.string.green_color))) {
                Log.d("2020", "showPlayersImage2: ");
                playersImageView[i].setBackgroundColor(getColor(R.color.green_color));
            }
            if (playerColor.equals(getString(R.string.red_color))) {
                Log.d("2020", "showPlayersImage3: ");
                playersImageView[i].setBackgroundColor(getColor(R.color.red_color));
            }
            if (playerColor.equals(getString(R.string.yellow_color))) {
                Log.d("2020", "showPlayersImage4: ");
                playersImageView[i].setBackgroundColor(getColor(R.color.yellow_color));
            }
            playersImageView[i].setVisibility(View.VISIBLE);
        }

    }

    
    private void sortPlayers() {
        Collections.sort(PlayerSelectionActivity.playerList, new Comparator<Player>(){
            public int compare(Player obj1, Player obj2) {
                return Integer.compare(obj2.getPlayerTotalScore(), obj1.getPlayerTotalScore());
            }
        });

        playerList = PlayerSelectionActivity.playerList;
    }


    public void home_button_click(View view) {
        PlaySound.stopSound();
        finish();
        startActivity(new Intent(this, SelectGameActivity.class));
    }
}