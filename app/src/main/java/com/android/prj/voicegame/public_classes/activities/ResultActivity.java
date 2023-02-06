package com.android.prj.voicegame.public_classes.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.car_game.CarGameActivity;
import com.android.prj.voicegame.databinding.ActivityResultBinding;
import com.android.prj.voicegame.fish_game.FishGameActivity;
import com.android.prj.voicegame.public_classes.PlaySound;
import com.android.prj.voicegame.public_classes.model.Player;
import com.android.prj.voicegame.public_classes.PublicSetting;

import java.util.Collections;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;
    private int numberOfPlayer;
    private MediaPlayer lightOnSound;
    private Timer timer;
    private boolean finishShowResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        defineFields();
        PublicSetting.hideBars(this);
        PublicSetting.keepScreenOn(this);
    }

    private void defineFields() {
        numberOfPlayer = PlayerSelectionActivity.numberOfPlayer;
        getInfoOfPlayers(PlayerSelectionActivity.playerList);
        seFieldOfTextViews();

        finishShowResult = false;

        binding.btnGoHome.setOnClickListener(view -> {
            if (finishShowResult) {
                PlaySound.stopSound();
                lightOnSound.stop();
                lightOnSound.release();
                timer.cancel();
                finish();
                startActivity(new Intent(this, SelectGameActivity.class));
            }
        });

        PlaySound.playSound(this, R.raw.main_background_sound, true);
        PublicSetting.setAppLanguage(getApplicationContext().getResources());
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
        showResult(PlayerSelectionActivity.playerList.get(3), 4000);
        showResult(PlayerSelectionActivity.playerList.get(3), 4100);
        binding.fourthPlayerName.setText(PlayerSelectionActivity.playerList.get(3).getPlayerName());
        binding.fourthPlayerScore.setText(String.valueOf(PlayerSelectionActivity.playerList.get(3).getPlayerScore()));
    }

    private void setThreePlayerField() {
        setTwoPlayerField();
        showResult(PlayerSelectionActivity.playerList.get(2), 3000);
        showResult(PlayerSelectionActivity.playerList.get(2), 4100);
        if (numberOfPlayer == 3) {
            binding.wholeLayout.removeView(binding.fourthPlayerLayout);
        }
        binding.thirdPlayerName.setText(PlayerSelectionActivity.playerList.get(2).getPlayerName());
        binding.thirdPlayerScore.setText(String.valueOf(PlayerSelectionActivity.playerList.get(2).getPlayerScore()));
    }

    private void setTwoPlayerField() {
        // when enter this method from two player do it
        if (numberOfPlayer == 2) {
            binding.wholeLayout.removeView(binding.fourthPlayerLayout);
            binding.wholeLayout.removeView(binding.thirdPlayerLayout);
        }

        showResult(PlayerSelectionActivity.playerList.get(0), 1000);
        showResult(PlayerSelectionActivity.playerList.get(1), 2000);
        showResult(PlayerSelectionActivity.playerList.get(1), 4100);

        // set first player info
        binding.firstPlayerName.setText(PlayerSelectionActivity.playerList.get(0).getPlayerName());
        binding.firstPlayerScore.setText(String.valueOf(PlayerSelectionActivity.playerList.get(0).getPlayerScore()));

        // set player 2 info
        binding.secondPlayerName.setText(PlayerSelectionActivity.playerList.get(1).getPlayerName());
        binding.secondPlayerScore.setText(String.valueOf(PlayerSelectionActivity.playerList.get(1).getPlayerScore()));
    }

    private void showResult(Player player, int targetTime) {
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                // show sticker, name and score
                runOnUiThread(() -> {
                    switch (targetTime) {
                        case 1000:
                            binding.firstPlayerLayout.setBackgroundResource(player.getPlayerResultStyle());
                            // if player get any score add to his score
                            // show player score on textview  // change image resources of star
                            if (player.getPlayerScore() > 0) {
                                player.setPlayerTotalScore(player.getPlayerTotalScore() + 3);
                                binding.firstPlayerTotalScore.setText(String.valueOf(player.getPlayerTotalScore()));
                                binding.firstPersonSticker.setImageResource(R.drawable.three_star);
                            }
                            startLightOnSound();
                            break;
                        case 2000:
                            binding.secondPlayerLayout.setBackgroundResource(player.getPlayerResultStyle());
                            // show player score on textview
                            if (player.getPlayerScore() > 0) {
                                player.setPlayerTotalScore(player.getPlayerTotalScore() + 2);
                                binding.secondPlayerTotalScore.setText(String.valueOf(player.getPlayerTotalScore()));
                                binding.secondPersonSticker.setImageResource(R.drawable.two_star);
                            }
                            startLightOnSound();
                            break;
                        case 3000:
                            binding.thirdPlayerLayout.setBackgroundResource(player.getPlayerResultStyle());
                            // show player score on textview
                            if (player.getPlayerScore() > 0) {
                                player.setPlayerTotalScore(player.getPlayerTotalScore() + 1);
                                binding.thirdPlayerTotalScore.setText(String.valueOf(player.getPlayerTotalScore()));
                                binding.thirdPersonSticker.setImageResource(R.drawable.one_star);
                            }
                            startLightOnSound();
                            break;
                        case 4000:
                            binding.fourthPlayerLayout.setBackgroundResource(player.getPlayerResultStyle());
                            // show player score on textview
                            player.setPlayerTotalScore(player.getPlayerTotalScore());
                            binding.fourthPlayerTotalScore.setText(String.valueOf(player.getPlayerTotalScore()));
                            binding.fourthPersonSticker.setImageResource(R.drawable.no_star);
                            startLightOnSound();
                            break;
                        case 4100:
                            finishShowResult = true;
                    }
                });
            }
        }, targetTime);
    }

    private void startLightOnSound() {
        lightOnSound = MediaPlayer.create(ResultActivity.this, R.raw.result_ding);
        lightOnSound.start();
    }

    //sort playerList depend on each player score
    public static void getInfoOfPlayers(List<Player> targetList) {
        // sort score of player
        Collections.sort(targetList, (obj1, obj2) -> Integer.compare(obj2.getPlayerScore(), obj1.getPlayerScore()));
    }

    public void NextGameButton(View view) {
        if (finishShowResult) {
            boolean allGamesDone = true;

            PlaySound.stopSound();
            lightOnSound.stop();
            lightOnSound.release();

            timer.cancel();
            finish();
            // start next game depend on game list
            for (int i = 0; i < SelectGameActivity.gamesList.size(); i++) {
                String gameName = SelectGameActivity.gamesList.get(i).getGameName();
                if (!SelectGameActivity.gamesList.get(i).isGameDone()) {
                    if (SelectGameActivity.gamesList.get(i).getGameName().equals(getString(R.string.carGameTitle))) {
                        allGamesDone = false;
                        startActivity(new Intent(this, CarGameActivity.class));
                        break;
                    } else if (gameName.equals(getString(R.string.fishGameTitle))) {
                        allGamesDone = false;
                        startActivity(new Intent(ResultActivity.this, FishGameActivity.class));
                        break;
                    }
                }
            }

            // start selection activity when all games finish
            if (allGamesDone) {
                startActivity(new Intent(ResultActivity.this, FinalActivity.class));
            }
        }

    }
}