package com.android.prj.voicegame.public_classes.activities;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.databinding.ActivitySelectGameBinding;
import com.android.prj.voicegame.public_classes.model.Game;
import com.android.prj.voicegame.public_classes.PublicSetting;
import com.android.prj.voicegame.public_classes.SoundPermission;
import com.android.prj.voicegame.public_classes.services.BackgroundMusicService;

import java.util.ArrayList;

public class SelectGameActivity extends AppCompatActivity {

    private ActivitySelectGameBinding binding;
    private boolean selectAnyGame = false;
    private int number = 4;
    private boolean[] gamesCheck = new boolean[number];
    public static ArrayList<Game> gamesList;
    private Game carGame;
    private Game fishGame;
    private MediaPlayer clickSound;
    public static Intent intentBackgroundSound;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        clickSound = MediaPlayer.create(this, R.raw.click_sound);

        // start background music
        intentBackgroundSound = new Intent(this, BackgroundMusicService.class);
        startService(intentBackgroundSound);
        gamesList = new ArrayList<>();

        // get requirement of sound permission when activity start
        SoundPermission soundPermission = new SoundPermission(this, SelectGameActivity.this);
        soundPermission.getPermission();
        PublicSetting.setAppLanguage(getApplicationContext().getResources());
        PublicSetting.hideBars(this);
    }

    @Override
    public void onBackPressed() {

    }


    private void startPlayerActivity() {
        clickSound.start();
        finish();
        startActivity(new Intent(SelectGameActivity.this, PlayerSelectionActivity.class));
    }

    // if select any game add or remove it from list
    public void carClick(View view) {
        if (gamesCheck[0]){
            runOnUiThread(() -> addOrRemoveGameToList(carGame, binding.carCheck, 0, false));
        }
        else{
            carGame = new Game(getString(R.string.carGameTitle), false);
            addOrRemoveGameToList(carGame, binding.carCheck, 0,true);
        }

        clickSound.start();
    }

    //
    public void fishClick(View view) {
        if (gamesCheck[3]){
            addOrRemoveGameToList(fishGame, binding.fishCheck, 3, false);
        }
        else{
            fishGame = new Game(getString(R.string.fishGameTitle), false);
            addOrRemoveGameToList(fishGame, binding.fishCheck, 3, true);
        }
        clickSound.start();
    }

    // when click on a game image add it to games list or remove it from game list
    private void addOrRemoveGameToList(Game game, ImageView checkImage, int index, boolean addGame) {
        selectAnyGame = addGame;
        gamesCheck[index] = addGame;

        // add game to list
        if (addGame){
            runOnUiThread(() -> checkImage.setVisibility(View.VISIBLE));
            gamesList.add(game);
        }

        // remove game from list
        else{
            runOnUiThread(() -> checkImage.setVisibility(View.INVISIBLE));
            checkAllGameSelected();
            for (int i = 0; i < gamesList.size(); i++) {
                if (gamesList.get(i) == game){
                    gamesList.remove(i);
                }
            }
        }
    }

    // check list of game to know which game selected
    private void checkAllGameSelected() {
        for (boolean games : gamesCheck) {
            if (games) {
                selectAnyGame = true;
                break;
            }
        }
    }

    public void stickManClick(View view) {
    }

    public void fruitClick(View view) {
    }

    public void exitGame(View view) {
        clickSound.start();
        stopService(intentBackgroundSound);
        finish();
    }


    public void startButton(View view) {
        // when any game selected go do its work
        if(selectAnyGame){
            startPlayerActivity();
        }
    }
}