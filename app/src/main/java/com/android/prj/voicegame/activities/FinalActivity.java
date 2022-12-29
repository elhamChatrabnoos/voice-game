package com.android.prj.voicegame.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.prj.voicegame.SelectGameActivity;
import com.android.prj.voicegame.databinding.ActivityFinalBinding;
import com.android.prj.voicegame.model.Player;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class FinalActivity extends AppCompatActivity {
    
    private ImageView[] playersImageView;
    private ActivityFinalBinding binding;
    private List<Player> playerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFinalBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        sortPlayers();
        showPlayersImage();
        setEnglishLocale();

        binding.btnBack.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, SelectGameActivity.class));
        });
    }

    private void setEnglishLocale() {
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    private void showPlayersImage() {
        playersImageView = new ImageView[]{binding.firstPlayer, binding.secondPlayer,
                binding.thirdPlayer, binding.fourthPlayer};

        int numberOfPlayer = PlayerActivity.numberOfPlayer;

        for (int i = 0; i < numberOfPlayer; i++) {
//            switch (i){
//                case 0:
//                    playersImageView[i].setBackgroundColor(playerList.get(i).getPlayerImage());
//                    break;
//                case 1:
//                    playersImageView[i].setImageResource(R.drawable.second_player);
//                    break;
//                case 2:
//                    playersImageView[i].setImageResource(R.drawable.third_player);
//                    break;
//                case 3:
//                    playersImageView[i].setImageResource(R.drawable.fourth_player);
//                    break;
//            }
            playersImageView[i].setBackgroundColor(playerList.get(i).getPlayerImage());
            playersImageView[i].setVisibility(View.VISIBLE);
        }
    }

    
    private void sortPlayers() {
        Collections.sort(PlayerActivity.playerList, new Comparator<Player>(){
            public int compare(Player obj1, Player obj2) {
                return Integer.compare(obj2.getPlayerTotalScore(), obj1.getPlayerTotalScore());
            }
        });

        playerList = PlayerActivity.playerList;
    }
    
    
}