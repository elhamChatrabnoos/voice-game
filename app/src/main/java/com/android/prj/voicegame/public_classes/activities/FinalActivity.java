package com.android.prj.voicegame.public_classes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.prj.voicegame.databinding.ActivityFinalBinding;
import com.android.prj.voicegame.public_classes.model.Player;
import com.android.prj.voicegame.public_classes.PublicSetting;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

        binding.btnBack.setOnClickListener(view -> {
            finish();
            startActivity(new Intent(this, SelectGameActivity.class));
        });
        PublicSetting.setAppLanguage(getApplicationContext().getResources());
//        PublicSetting.hideBars(this);
    }

    @Override
    public void onBackPressed() {

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