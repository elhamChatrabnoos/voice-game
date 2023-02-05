package com.android.prj.voicegame.public_classes.activities;

import static java.security.AccessController.getContext;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.car_game.CarGameActivity;
import com.android.prj.voicegame.databinding.ActivityPlayerSelectionBinding;
import com.android.prj.voicegame.fish_game.FishGameActivity;
import com.android.prj.voicegame.public_classes.PlaySound;
import com.android.prj.voicegame.public_classes.PublicSetting;
import com.android.prj.voicegame.public_classes.dialogs.SensorSettingDialog;
import com.android.prj.voicegame.public_classes.model.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PlayerSelectionActivity extends AppCompatActivity implements SensorSettingDialog.SensitiveSetting {

    String[] robotsName;
    String[] colors;
    private ActivityPlayerSelectionBinding binding;
    public static int numberOfPlayer;
    private final boolean[] players = new boolean[4];
    public static String[] playersName;
    public static List<Player> playerList;
    private int humanCount = 0;
    private final boolean[] playerColor = new boolean[4];
    private final boolean[] aboveColors = new boolean[4];
    private boolean enableStartButton;
    ImageView[] colorBoxes;
    int[] playerStyleColor;
    int[] boxStyleColor;
    int[] playerResultStyle;
    private ImageView[] playerColorBoxes;
    private int[] playerCurrentStyle;
    private int robotHumanCount;
    private int playerColorCount;
    private long humanCountForShowingBorder;
    public static float soundSensitive = 2;
    private int humanColorToShowBorder;
    private EditText[] playersNameEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityPlayerSelectionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        fillField();

        PublicSetting.setAppLanguage(getApplicationContext().getResources());

        PublicSetting.hideBars(this);
        hideKeyboardWhenEnterClick();

    }

    private void hideKeyboardWhenEnterClick() {
        /// set feature when click enter on open keyboard close it
        playersNameEditText = new EditText[]{
                binding.player1NameEdt,
                binding.player2NameEdt,
                binding.player3NameEdt,
                binding.player4NameEdt};

        for (EditText playerName : playersNameEditText) {
            playerName.setOnKeyListener((view, i, keyEvent) -> {
                if (keyEvent.getAction() == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    InputMethodManager manager = (InputMethodManager) getSystemService(
                            Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    return true;
                }
                return false;
            });
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void fillField() {
        numberOfPlayer = 0;
        robotHumanCount = 0;
        playerColorCount = 0;

        playersName = new String[]{
                getString(R.string.player1_txt),
                getString(R.string.player2_txt),
                getString(R.string.player3_txt),
                getString(R.string.player4_txt)};

        robotsName = new String[]{
                getString(R.string.robot1),
                getString(R.string.robot2),
                getString(R.string.robot3),
                getString(R.string.robot4)};

        colors = new String[]{
                getString(R.string.red_color),
                getString(R.string.green_color),
                getString(R.string.yellow_color),
                getString(R.string.blue_color)};

        Arrays.fill(aboveColors, true);
        Arrays.fill(playerColor, false);

        playerCurrentStyle = new int[4];

        colorBoxes = new ImageView[]{binding.redColor, binding.greenColor,
                binding.yellowColor, binding.blueColor};

        playerColorBoxes = new ImageView[]{binding.player1Color, binding.player2Color,
                binding.player3Color, binding.player4Color};

        playerStyleColor = new int[]{
                R.drawable.selection_red,
                R.drawable.selection_green,
                R.drawable.selection_yellow,
                R.drawable.selection_blue};

        boxStyleColor = new int[]{
                R.drawable.red_circle,
                R.drawable.green_circle,
                R.drawable.yellow_circle,
                R.drawable.blue_circle};

        playerResultStyle = new int[]{R.drawable.result_image_red, R.drawable.result_image_green,
                R.drawable.result_image_yellow, R.drawable.result_image_blue};

        playerList = new ArrayList<>();

        // define each player feature
        Player player;
        for (int i = 0; i < 4; i++) {
            player = new Player();
            player.setId(0);
            playerList.add(player);
        }
    }

    public void botClick(View view) {
        PlaySound.playSound(this, R.raw.click_sound, false);
        setImagesResources(R.drawable.robot_image, getString(R.string.robot));
    }

    public void humanClick(View view) {
        PlaySound.playSound(this, R.raw.click_sound, false);
        setImagesResources(R.drawable.human_image, getString(R.string.human));
    }

    public void startGameClick(View view) {
        if (enableStartButton) {
            setPlayersName();
            stopService(SelectGameActivity.intentBackgroundSound);
            PlaySound.playSound(this, R.raw.click_sound, false);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // show setting for sound detective sensitive
                new SensorSettingDialog(this, this);
            } else {
                startActivity();
            }
        } else {
            Toast.makeText(this, R.string.select_players_feild, Toast.LENGTH_SHORT).show();
        }
    }

    private void setPlayersName() {
        for (int i = 0; i < playerList.size(); i++) {
            if (!playersNameEditText[i].getText().toString().equals("")){
                playerList.get(i).setPlayerName(playersNameEditText[i].getText().toString());
            }
        }
    }


    private void startActivity() {

        int playerListSize = playerList.size();
        int playerListSizeTmp = playerList.size();

        // remove items that have 0 id in players list
        for (int i = 0; i < playerListSize; i++) {
            for (int j = 0; j < playerListSizeTmp; j++) {
                if (playerList.get(j).getId() == 0) {
                    playerList.remove(playerList.get(j));
                    playerListSizeTmp = playerList.size();
                }
            }
        }

        PlaySound.stopSound();
        finish();
        // start game activity depend on game selected
        for (int i = 0; i < SelectGameActivity.gamesList.size(); i++) {
            String gameName = SelectGameActivity.gamesList.get(i).getGameName();
            if (gameName.equals(getString(R.string.carGameTitle))) {
                startActivity(new Intent(PlayerSelectionActivity.this, CarGameActivity.class));
                break;
            } else if (gameName.equals(getString(R.string.fishGameTitle))) {
                startActivity(new Intent(PlayerSelectionActivity.this, FishGameActivity.class));
                break;
            }
        }
    }

    public void backButtonClick(View view) {
        finish();
        stopService(SelectGameActivity.intentBackgroundSound);
        startActivity(new Intent(PlayerSelectionActivity.this, SelectGameActivity.class));
        PlaySound.playSound(this, R.raw.click_sound, false);
    }

    public void player1Onclick(View view) {
        PlaySound.playSound(this, R.raw.click_sound, false);
        setPlayerField(0, binding.player1Img, binding.player1NameEdt, 0, "");
    }

    public void player2Onclick(View view) {
        PlaySound.playSound(this, R.raw.click_sound, false);
        setPlayerField(1, binding.player2Img, binding.player2NameEdt, 0, "");
    }

    public void player3Onclick(View view) {
        PlaySound.playSound(this, R.raw.click_sound, false);
        setPlayerField(2, binding.player3Img, binding.player3NameEdt, 0, "");
    }

    public void player4Onclick(View view) {
        PlaySound.playSound(this, R.raw.click_sound, false);
        setPlayerField(3, binding.player4Img, binding.player4NameEdt, 0, "");
    }

    // set image resources of each player when click on each human or bot image
    private void setImagesResources(int imagesResource, String playerType) {
        if (!players[0]) {
            // if clicked image is robot send robot name else send human name
            if (playerType.equals(getString(R.string.robot))) {
                setPlayerField(0, binding.player1Img, binding.player1NameEdt, imagesResource, robotsName[0]);
            } else {
                setPlayerField(0, binding.player1Img, binding.player1NameEdt, imagesResource, playersName[0]);
            }
        } else if (!players[1]) {
            if (playerType.equals(getString(R.string.robot))) {
                setPlayerField(1, binding.player2Img, binding.player2NameEdt, imagesResource, robotsName[1]);
            } else {
                setPlayerField(1, binding.player2Img, binding.player2NameEdt, imagesResource, playersName[1]);
            }
        } else if (!players[2]) {
            if (playerType.equals(getString(R.string.robot))) {
                setPlayerField(2, binding.player3Img, binding.player3NameEdt, imagesResource, robotsName[2]);
            } else {
                setPlayerField(2, binding.player3Img, binding.player3NameEdt, imagesResource, playersName[2]);
            }
        } else if (!players[3]) {
            if (playerType.equals(getString(R.string.robot))) {
                setPlayerField(3, binding.player4Img, binding.player4NameEdt, imagesResource, robotsName[3]);
            } else {
                setPlayerField(3, binding.player4Img, binding.player4NameEdt, imagesResource, playersName[3]);
            }
        }
    }

    // change field to null when every player layout click
    private void setPlayerField(int index, ImageView playerType, EditText playerName,
                                int targetImage, String targetName) {

        // if player is robot disable its editName
        playerName.setEnabled(!targetName.contains(getString(R.string.robot)));

        // if player index is true clear player field else add player to field and view
        if (players[index]) {
            // make textview and image view empty
            playerType.setImageResource(targetImage);
            playerName.setText("");
            players[index] = false;
            robotHumanCount--;

            // for showing border around human
            if (!playerList.get(index).isPlayerRobot()) {
                humanCountForShowingBorder--;
                if (playerColor[index]) {
                    humanColorToShowBorder--;
                }
            }

            // if color also selected remove from players
            if (playerColor[index]) {
                // if human selected to remove
                if (!playerList.get(index).isPlayerRobot()) {
                    humanCount--;
                }
                numberOfPlayer--;
                playerList.get(index).setId(0);

                // make index of player list false
                playerList.get(index).setPlayerRobot(false);
            }
            playerType.setEnabled(false);
        } else {
            playerType.setImageResource(targetImage);
            playerName.setText(targetName);
            robotHumanCount++;

            // if color selected for player add player to player number
            players[index] = true;

            // add the player to list
            playerList.get(index).setPlayerRobot(!targetName.contains(getString(R.string.player)));
            playerList.get(index).setPlayerName(targetName);

            // if color selected first
            if (playerColor[index]) {
                playerList.get(index).setId(1);
                numberOfPlayer++;
                // if human selected add one number to humanCount
                if (!playerList.get(index).isPlayerRobot()) {
                    if (humanCount < 4) {
                        humanCount++;
                    }
                }
            }
            if (!playerList.get(index).isPlayerRobot()) {
                // for showing border around robot and human
                humanCountForShowingBorder++;
                if (playerColor[index]) {
                    humanColorToShowBorder++;
                }
            }
            playerType.setEnabled(true);
        }
        checkEnableStartButton();
    }

    private void checkEnableStartButton() {
        enableStartButton = humanCount > 0 && numberOfPlayer >= 2;
        // if user doesn't select player color show border around color box else remove this border
        if (playerColorCount < 2 || humanColorToShowBorder < 1) {
            binding.colorBorder.setVisibility(View.VISIBLE);
        } else {
            binding.colorBorder.setVisibility(View.INVISIBLE);
        }
        // if user doesn't select human or robot show border around it
        if (robotHumanCount < 2) {
            binding.humanRobotBorder.setVisibility(View.VISIBLE);
        } else {
            binding.humanRobotBorder.setVisibility(View.INVISIBLE);
        }
        // if user doesn't select any human player show border around human image
        if (humanCountForShowingBorder < 1) {
            binding.humanBorder.setVisibility(View.VISIBLE);
        } else {
            binding.humanBorder.setVisibility(View.INVISIBLE);
        }
    }

    public void player1ColorClick(View view) {
        returnColorToBoxes(0);
    }

    public void player2ColorClick(View view) {
        returnColorToBoxes(1);
    }

    public void player3ColorClick(View view) {
        returnColorToBoxes(2);
    }

    public void player4ColorClick(View view) {
        returnColorToBoxes(3);
    }

    private void returnColorToBoxes(int index) {
        if (playerColor[index]) {
            playerColorCount--;
            // search to find player color and return its color to its place
            for (int i = 0; i < playerStyleColor.length; i++) {
                if (playerCurrentStyle[index] == playerStyleColor[i]) {
                    // make above color clickable and return its back color
                    aboveColors[i] = true;
                    colorBoxes[i].setImageResource(boxStyleColor[i]);

                    // remove color from player color box
                    playerColor[index] = false;
                    playerColorBoxes[index].setImageResource(R.drawable.color_selection);

                    // when remove player color number of player reduce
                    if (players[index]) {
                        numberOfPlayer--;
                        if (!playerList.get(index).isPlayerRobot()) {
                            humanCount--;
                            humanColorToShowBorder--;
                        }
                        playerList.get(i).setId(0);
                    }
                    break;
                }
            }
        }
        checkEnableStartButton();
    }

    // clickable of color box above
    public void redColorClick(View view) {
        PlaySound.playSound(this, R.raw.click_sound, false);
        replaceColorToPlayer(0);
    }

    public void greenColorClick(View view) {
        PlaySound.playSound(this, R.raw.click_sound, false);
        replaceColorToPlayer(1);
    }

    public void yellowColorClick(View view) {
        PlaySound.playSound(this, R.raw.click_sound, false);
        replaceColorToPlayer(2);
    }

    public void blueColorClick(View view) {
        PlaySound.playSound(this, R.raw.click_sound, false);
        replaceColorToPlayer(3);
    }

    private void replaceColorToPlayer(int index) {
        // check if above color box is fill not empty
        if (aboveColors[index]) {
            // make it false to cant click it any more
            aboveColors[index] = false;
            // search empty player color
            for (int i = 0; i < playerColor.length; i++) {
                if (!playerColor[i]) {
                    playerColorCount++;
                    // fill player color box boolean
                    playerColor[i] = true;

                    // set color to player color box
                    playerColorBoxes[i].setImageResource(playerStyleColor[index]);

                    // set gray color to above color box
                    colorBoxes[index].setImageResource(R.drawable.gray_circle);

                    playerCurrentStyle[i] = playerStyleColor[index];

                    // set player field
                    playerList.get(i).setPlayerColor(colors[index]);
                    playerList.get(i).setPlayerResultStyle(playerResultStyle[index]);

                    // if human or bot image selected before
                    if (players[i]) {
                        numberOfPlayer++;
                        playerList.get(i).setId(1);
                    }
                    // if select color for human image
                    if (!playerList.get(i).isPlayerRobot() && players[i]) {
                        humanCount++;
                        playerList.get(i).setPlayerRobot(false);
                        humanColorToShowBorder++;
                    }
                    break;
                }
            }
        }
        checkEnableStartButton();
    }

    @Override
    public void getSensInfo(float sliderValue, Dialog dialogFragment) {
        soundSensitive = sliderValue;
        dialogFragment.dismiss();
        startActivity();
    }

}