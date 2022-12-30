package com.android.prj.voicegame.public_classes.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.car_game.CarGameActivity;
import com.android.prj.voicegame.databinding.ActivityResultBinding;
import com.android.prj.voicegame.fish_game.FishGameActivity;
import com.android.prj.voicegame.public_classes.model.Player;
import com.android.prj.voicegame.public_classes.PublicSetting;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ResultActivity extends AppCompatActivity {

    //??? comment
    private ActivityResultBinding binding;
    private int numberOfPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        numberOfPlayer = PlayerActivity.numberOfPlayer;
        getInfoOfPlayers(PlayerActivity.playerList);
        seFieldOfTextViews();

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

    // fill textViews on page with players score and name
    private void seFieldOfTextViews() {
        if (numberOfPlayer == 2) {
            setTwoPlayerField();
        } else if (numberOfPlayer == 3) {
            setThreePlayerField();
        } else if (numberOfPlayer == 4) {
            setFourPlayerField();
        }
    }

    private void setFourPlayerField() {
        setThreePlayerField();
        showResult(PlayerActivity.playerList.get(3), 8000);
        binding.fourthPlayerName.setText(PlayerActivity.playerList.get(3).getPlayerName());
        binding.fourthPlayerScore.setText(String.valueOf(PlayerActivity.playerList.get(3).getPlayerScore()));
    }

    private void setThreePlayerField() {
        setTwoPlayerField();
        showResult(PlayerActivity.playerList.get(2), 6000);
        if (numberOfPlayer == 3) {
            binding.wholeLayout.removeView(binding.fourthPlayerLayout);
        }
        binding.thirdPlayerName.setText(PlayerActivity.playerList.get(2).getPlayerName());
        binding.thirdPlayerScore.setText(String.valueOf(PlayerActivity.playerList.get(2).getPlayerScore()));
    }

    private void setTwoPlayerField() {
        if (numberOfPlayer == 2) {
            binding.wholeLayout.removeView(binding.fourthPlayerLayout);
            binding.wholeLayout.removeView(binding.thirdPlayerLayout);
        }

        showResult(PlayerActivity.playerList.get(0), 2000);
        showResult(PlayerActivity.playerList.get(1), 4000);

        // set first player info
        binding.firstPlayerName.setText(PlayerActivity.playerList.get(0).getPlayerName());
        binding.firstPlayerScore.setText(String.valueOf(PlayerActivity.playerList.get(0).getPlayerScore()));

        // set player 2 info
        binding.secondPlayerName.setText(PlayerActivity.playerList.get(1).getPlayerName());
        binding.secondPlayerScore.setText(String.valueOf(PlayerActivity.playerList.get(1).getPlayerScore()));
    }

    private void showResult(Player player, int targetTime) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // show sticker , name and score
                runOnUiThread(() -> {
                    switch (targetTime) {
                        case 2000:
                            binding.firstPlayerLayout.setBackgroundResource(player.getPlayerResultStyle());
                            // if player get any score add to his score
                            // show player score on textview  // change image resources of star
                            if (player.getPlayerScore() > 0){
                                player.setPlayerTotalScore(player.getPlayerTotalScore() + 3);
                                binding.firstPlayerTotalScore.setText(String.valueOf(player.getPlayerTotalScore()));
                                binding.firstPersonSticker.setImageResource(R.drawable.three_star);
                            }
                            break;
                        case 4000:
                            binding.secondPlayerLayout.setBackgroundResource(player.getPlayerResultStyle());
                            // show player score on textview
                            if (player.getPlayerScore() > 0){
                                player.setPlayerTotalScore(player.getPlayerTotalScore() + 2);
                                binding.secondPlayerTotalScore.setText(String.valueOf(player.getPlayerTotalScore()));
                                binding.secondPersonSticker.setImageResource(R.drawable.two_star);
                            }
                            break;
                        case 6000:
                            binding.thirdPlayerLayout.setBackgroundResource(player.getPlayerResultStyle());
                            // show player score on textview
                            if (player.getPlayerScore() > 0){
                                player.setPlayerTotalScore(player.getPlayerTotalScore() + 1);
                                binding.thirdPlayerTotalScore.setText(String.valueOf(player.getPlayerTotalScore()));
                                binding.thirdPersonSticker.setImageResource(R.drawable.one_star);
                            }
                            break;
                        case 8000:
                            binding.fourthPlayerLayout.setBackgroundResource(player.getPlayerResultStyle());
                            // show player score on textview
                            player.setPlayerTotalScore(player.getPlayerTotalScore());
                            binding.fourthPlayerTotalScore.setText(String.valueOf(player.getPlayerTotalScore()));
                            binding.fourthPersonSticker.setImageResource(R.drawable.no_star);
                            break;
                    }
                });
            }
        }, targetTime);
    }

    //sort playerList depend on each player score
    public static void getInfoOfPlayers(List<Player> targetList) {
        // sort score of player
        Collections.sort(targetList, (obj1, obj2) -> Integer.compare(obj2.getPlayerScore(), obj1.getPlayerScore()));
    }

    public void NextGameButton(View view) {
        boolean allGamesDone = true;

        // start next game depend on game list
        for (int i = 0; i < SelectGameActivity.gamesList.size(); i++) {
            String gameName = SelectGameActivity.gamesList.get(i).getGameName();
            if (!SelectGameActivity.gamesList.get(i).isGameDone()) {
                if (SelectGameActivity.gamesList.get(i).getGameName().equals(getString(R.string.carGameTitle))){
                    startActivity(new Intent(this, CarGameActivity.class));
                    allGamesDone = false;
                    break;
                }
                else if (gameName.equals(getString(R.string.fishGameTitle))){
                    startActivity(new Intent(ResultActivity.this, FishGameActivity.class));
                    allGamesDone = false;
                    break;
                }
            }
        }

        if (allGamesDone){
            finish();
            startActivity(new Intent(ResultActivity.this, FinalActivity.class));
        }
    }
}