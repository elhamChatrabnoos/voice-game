package com.android.prj.voicegame;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.prj.voicegame.activities.PlayerActivity;
import com.android.prj.voicegame.databinding.ActivitySelectGameBinding;
import com.android.prj.voicegame.model.Game;
import com.android.prj.voicegame.model.Player;
import com.android.prj.voicegame.publicClasses.SoundPermission;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;


public class SelectGameActivity extends AppCompatActivity {

    public static final String SELECTED_GAME = "selected game";
    private ActivitySelectGameBinding binding;
    private boolean selectAnyGame = false;
    private int number = 4;
    private boolean[] gamesCheck = new boolean[number];
    public static ArrayList<Game> gamesList;
    private Game carGame;
    private Game fishGame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySelectGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        gamesList = new ArrayList<>();

        // get requirement of sound permission when activity start
        SoundPermission soundPermission = new SoundPermission(this, SelectGameActivity.this);
        soundPermission.getPermission();

        checkClickableObject();

        setEnglishLocale();
    }

    // use just english language
    private void setEnglishLocale() {
        Configuration configuration = getResources().getConfiguration();
        configuration.setLayoutDirection(new Locale("en"));
        getResources().updateConfiguration(configuration, getResources().getDisplayMetrics());
    }

    private void checkClickableObject() {
        binding.startBtn.setOnClickListener(view -> {
            // when any game selected go do its work
            if(selectAnyGame){
                startPlayerActivity();
            }
        });
    }

    private void startPlayerActivity() {
        startActivity(new Intent(SelectGameActivity.this, PlayerActivity.class));
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
//        enableStartButton();
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
}