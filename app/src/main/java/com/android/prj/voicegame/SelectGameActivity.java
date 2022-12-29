package com.android.prj.voicegame;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.prj.voicegame.activities.PlayerActivity;
import com.android.prj.voicegame.databinding.ActivitySelectGameBinding;
import com.android.prj.voicegame.model.Game;
import com.android.prj.voicegame.publicClasses.PublicSetting;
import com.android.prj.voicegame.publicClasses.SoundPermission;

import java.util.ArrayList;


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
        try {
            PublicSetting.setAppLanguage(getResources());
            PublicSetting.hideActionBar(getActionBar());
        }
        catch (Exception e){
            Log.d("2323", "onCreate: " + e.getMessage());
        }
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

    public void exitGame(View view) {
        finish();
    }


}