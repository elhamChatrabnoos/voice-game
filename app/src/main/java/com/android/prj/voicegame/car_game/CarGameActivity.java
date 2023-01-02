package com.android.prj.voicegame.car_game;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaRecorder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.DialogFragment;

import com.android.prj.voicegame.R;
import com.android.prj.voicegame.public_classes.PlaySound;
import com.android.prj.voicegame.public_classes.activities.SelectGameActivity;
import com.android.prj.voicegame.public_classes.activities.PlayerActivity;
import com.android.prj.voicegame.public_classes.activities.ResultActivity;
import com.android.prj.voicegame.public_classes.dialogs.NextPlayerDialog;
import com.android.prj.voicegame.public_classes.dialogs.PauseDialog;
import com.android.prj.voicegame.public_classes.model.Player;
import com.android.prj.voicegame.public_classes.PublicSetting;
import com.android.prj.voicegame.public_classes.ScreenRelative;
import com.android.prj.voicegame.public_classes.SoundDetector;

import java.util.Arrays;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import pl.droidsonroids.gif.GifImageView;

public class CarGameActivity extends AppCompatActivity
        implements NextPlayerDialog.Finish, PauseDialog.Actions {

    private static final String TAG = "3030";
    public static final String MESSAGE = "message";
    public static final String IS_PLAYER_ROBOT = "is player robot";
    private Handler mainHandler;
    private MediaRecorder recorder;
    private int numberOfPlayer;
    private int soundVolume = 0;
    private int threadDelay;
    private int handlerDelay;
    private int interruptNumber;
    private int scrollX;
    private int scrollY;
    private static int movingStep;
    private static int movingThumbStep;
    private static int seekBarThumbPos;
    private static int movingCarStep;
    private int carPosition;
    private int windowWidth;
    private int seekBarWidth;
    private int looseNumber;
    private int playerNumber;
    private boolean isNext;
    private com.android.prj.voicegame.databinding.ActivityCarGameBinding binding;
    private boolean startGame;
    private boolean carBoil;
    private int counter;
    private float sound;
    boolean loose = false;
    private float soundSensitive;
    private boolean carCollision;
    private Nitrogen nitrogen;
    private boolean nitroClick;
    private int progressValue;
    public static int screenWidth;
    private boolean enablePauseButton;
    private int carsFirstPosition;
    public static int screenHeight;
    private float robotSound;
    public static boolean playWithRobot;
    private Bot bot;
    private boolean tempBoolean;
    private final Handler robotHandler = new Handler();
    private int backgroundWidth;
    private int looseSound;
    private int[] numberOfNitrogen;
    private boolean getNitrogen;
    private boolean changeImageResource;
    private GifImageView smoke;
    private GifImageView speedImage;
    private ConstraintLayout currentLayout;
    private boolean showSpeed;
    private int looseTimer;
    private Timer timer;
    private ConstraintLayout[] playersCarLayout;
    private ImageView[] playerScoreImage;
    private int robotNitrogenCounter;
    private int robotNitrogenFeed;
    private int fastSpeedNumber;
    private HorizontalScrollView[] playersScrollView;
    private ConstraintLayout[] playersLayout;
    private LinearLayout[] playerScoreLayout;
    private TextView[] playersScoreTxt;
    private ConstraintLayout[] playersInnerLayout;
    private Thread seekbarPosThread;
    private ExecutorService executorService;
    private ImageView[] playersCar;
    private View[] carPoints;
    private ScreenRelative screenRelative;
    private int roadHeight;
    private int roadWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.android.prj.voicegame.databinding.ActivityCarGameBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getScreenSize();
        setHorizontalViews();
        setSeekBarSetting();
        getInformationOfPlayers();

        soundSensitive = PlayerActivity.soundSensitive;
        carsFirstPosition = (int) binding.car1.getX();
        startGameAtFirst();

        PublicSetting.setAppLanguage(getApplicationContext().getResources());
//        PublicSetting.hideBars(this);
    }

    // to remove action from back press bottom
    @Override
    public void onBackPressed() {

    }

    private void startGameAtFirst() {
        threadDelay = 50;
        handlerDelay = 50;
        playerNumber = 0;
        counter = 0;
        enablePauseButton = true;
        startGame = false;
        loose = false;
        looseSound = 900;
        tempBoolean = true;
        changeImageResource = false;
        playWithRobot = false;
        backgroundWidth = windowWidth - 200;
        showSpeed = true;
        looseTimer = 0;
        binding.fadeLayout.setVisibility(View.VISIBLE);

        // define threads with threadPool for execute parallel
        executorService = Executors.newFixedThreadPool(4);

        // check first player that is robot or not for starting the game
        if (PlayerActivity.playerList.get(playerNumber).isPlayerRobot()) {
            playWithRobot = true;
            tempBoolean = true;

            bot = new Bot();
            // when robot wants to play make random numbers for define difficulty
            bot.prepareBotField();
        } else {
            playWithRobot = false;
        }

        onClickItems();
        mainHandler = new Handler();
        startVoiceListening();

        // make nitrogen object and its random point
        generateNitrogen();

        // start game
        if (!playWithRobot) {
            runOnUiThread(() -> showAlertDialog(PlayerActivity.playerList.get(playerNumber).isPlayerRobot(), PlayerActivity.playerList.get(playerNumber).getPlayerName()));
        } else {
            startTheGame(PlayerActivity.playerList.get(playerNumber).isPlayerRobot(), PlayerActivity.playerList.get(playerNumber).getPlayerName());
        }
        binding.nitrogenProgressBar.setBackgroundResource(R.drawable.progressbar_background);
    }

    private void generateNitrogen() {
        nitrogen = new Nitrogen(this, binding.mainLayout, screenWidth, screenHeight);
        nitrogen.generateRandomNumber();
        robotNitrogenFeed = nitrogen.robotNitroRandomNumber();

        // generate random number when play with robot to generate nitrogen
        if (playWithRobot) {
            Random random = new Random();
            numberOfNitrogen = new int[4];
            for (int i = 0; i < numberOfNitrogen.length; i++) {
                numberOfNitrogen[i] = random.nextInt(nitrogen.getNumberOfNitrogen() - 8) + 1;
            }
            binding.nitrogenProgressBar.setClickable(false);
        }
    }

    private void onClickItems() {
        binding.pauseBtn.setOnClickListener(view -> {
            PlaySound.playClickSound(this, R.raw.click_sound);
            if (enablePauseButton && startGame) {
                mainHandler.removeCallbacksAndMessages(null);
                PauseDialog dialog = new PauseDialog();
                dialog.show(getSupportFragmentManager(), "pause dialog");
            }
        });
    }

    private void getScreenSize() {
        // get screen phone size
        screenRelative = new ScreenRelative();
        ScreenRelative.getScreenSize(this);
        screenHeight = ScreenRelative.screenHeight;
        screenWidth = ScreenRelative.screenWidth;
    }

    private void carOnCLickListener(ConstraintLayout carLayout, ConstraintLayout playerLayout) {
        carLayout.setOnClickListener(view -> nitrogen.removeViewFrom(playerLayout, binding.nitrogenProgressBar, carLayout, carBoil));

        // when a robot eat nitrogen
        if (playWithRobot) {
            carLayout.setClickable(false);
            if (getNitrogen) {
                nitrogen.removeViewFrom(playerLayout, binding.nitrogenProgressBar, carLayout, carBoil);
                if (nitrogen.isNitroCollision()) {
                    nitrogen.setNitroCollision(false);
                    robotNitrogenCounter++;
                }
            }
        }
    }

    private void nitrogenProgressClicked(ImageView playerImage, ConstraintLayout playerImgLayout, Player player, TextView playerTxtScore) {
        // when nitrogen full fill
        if (binding.nitrogenProgressBar.getProgress() >= binding.nitrogenProgressBar.getMax()) {
            binding.nitrogenProgressBar.setBackgroundResource(R.drawable.progressbar_background_full);
            binding.nitrogenTxt.setText(R.string.click_to_fast);
        }

        // when nitrogen of robot receive a number that we generate in nitrogen class
        if (binding.nitrogenProgressBar.getProgress() >= binding.nitrogenProgressBar.getMax() / 3) {
            if (playWithRobot && robotNitrogenCounter >= robotNitrogenFeed) {
                nitroClick = true;
                showSpeed = true;
                resetNitrogenProgressBar();
            }
        }

        // when nitrogen clicked set progressbar 0
        binding.nitrogenProgressBar.setOnClickListener(view -> {
            showSpeed = true;
            if (binding.nitrogenProgressBar.getProgress() > 0 && !carBoil
                    && !PlayerActivity.playerList.get(playerNumber).isPlayerRobot()) {
                nitroClick = true;
                resetNitrogenProgressBar();
            }
        });
        fastCarSpeed(playerImage, playerImgLayout, player, playerTxtScore);
    }

    private void resetNitrogenProgressBar() {
        runOnUiThread(() -> {
            binding.nitrogenProgressBar.setBackgroundResource(R.drawable.progressbar_background);
            binding.nitrogenTxt.setText(getString(R.string.nitrogen_txt));

            Nitrogen.nitrogenProgress = 0;
            progressValue = binding.nitrogenProgressBar.getProgress();
            binding.nitrogenProgressBar.setProgress(0);
        });
    }

    // features for not scrolling background
    @SuppressLint("ClickableViewAccessibility")
    private void setHorizontalViews() {
        binding.scrollViewP1.setOnTouchListener((view, motionEvent) -> true);

        binding.scrollViewP2.setOnTouchListener((view, motionEvent) -> true);

        binding.scrollViewP3.setOnTouchListener((view, motionEvent) -> true);

        binding.scrollViewP4.setOnTouchListener((view, motionEvent) -> true);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setSeekBarSetting() {
        seekBarWidth = 500;
        binding.seekBar.setMax(seekBarWidth);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.seekBar.setMinWidth(0);
        }
        binding.seekBar.setOnTouchListener((view, motionEvent) -> true);
    }

    private void getInformationOfPlayers() {
        numberOfPlayer = PlayerActivity.numberOfPlayer;
        // set image for each player depend on its color
        setColorToEachPlayer();
        // define playWithRobot
        playWithRobot = PlayerActivity.playerList.get(playerNumber).isPlayerRobot();
        getViews();
        readyPlayerBoard();
        PlayerActivity.playerList.get(playerNumber).setPlayerTurn(true);
    }

    private void getViews() {
        // put views in arrays to use later
        playersCarLayout = new ConstraintLayout[]{binding.car1Layout, binding.car2Layout, binding.car3Layout, binding.car4Layout};
        playersCar = new ImageView[]{binding.car1, binding.car2, binding.car3, binding.car4};
        playerScoreImage = new ImageView[]{binding.imgScore1, binding.imgScore2, binding.imgScore3, binding.imgScore4};
        playersScrollView = new HorizontalScrollView[]{binding.scrollViewP1, binding.scrollViewP2, binding.scrollViewP3, binding.scrollViewP4};
        playersLayout = new ConstraintLayout[]{binding.player1Layout, binding.player2Layout, binding.player3Layout, binding.player4Layout};
        playerScoreLayout = new LinearLayout[]{binding.player1ScoreLayout, binding.player2ScoreLayout, binding.player3ScoreLayout, binding.player4ScoreLayout};
        playersScoreTxt = new TextView[]{binding.player1Score, binding.player2Score, binding.player3Score, binding.player4Score};
        playersInnerLayout = new ConstraintLayout[]{binding.player1InnerLayout, binding.player2InnerLayout, binding.player3InnerLayout, binding.player4InnerLayout};
        carPoints = new View[]{binding.car1Point, binding.car2Point, binding.car3Point, binding.car4Point};
    }

    private void setColorToEachPlayer() {
        for (int i = 0; i < PlayerActivity.playerList.size(); i++) {
            PlayerActivity.playerList.get(i).setPlayerScore(0);
            String playerColor = PlayerActivity.playerList.get(i).getPlayerColor();
            if (playerColor.equals(getString(R.string.blue_color))) {
                PlayerActivity.playerList.get(i).setPlayerGif(R.drawable.blue_car_gif);
                PlayerActivity.playerList.get(i).setPlayerImage(R.drawable.blue_car_r);
            } else if (playerColor.equals(getString(R.string.green_color))) {
                PlayerActivity.playerList.get(i).setPlayerGif(R.drawable.green_car_gif);
                PlayerActivity.playerList.get(i).setPlayerImage(R.drawable.green_car_r);
            } else if (playerColor.equals(getString(R.string.red_color))) {
                PlayerActivity.playerList.get(i).setPlayerGif(R.drawable.red_car_gif);
                PlayerActivity.playerList.get(i).setPlayerImage(R.drawable.red_car_r);
            } else if (playerColor.equals(getString(R.string.yellow_color))) {
                PlayerActivity.playerList.get(i).setPlayerGif(R.drawable.yellow_car_gif);
                PlayerActivity.playerList.get(i).setPlayerImage(R.drawable.yellow_car_r);
            }
        }
    }

    private void readyPlayerBoard() {
        // save image of player and their score to a array
        for (int i = 0; i < PlayerActivity.playerList.size(); i++) {
            playersCar[i].setImageResource(PlayerActivity.playerList.get(i).getPlayerImage());
            playerScoreImage[i].setImageResource(PlayerActivity.playerList.get(i).getPlayerImage());
        }

        // remove additional layout
        for (int i = PlayerActivity.playerList.size(); i < 4; i++) {
            binding.allPlayersLayout.removeView(playersLayout[i]);
            binding.scoresLayout.removeView(playerScoreLayout[i]);
        }

        // get width and height of road to use later
        binding.player1InnerLayout.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        // Layout has happened here.
                        roadHeight = binding.player1InnerLayout.getHeight();
                        roadWidth = binding.player1InnerLayout.getWidth();
                        // change view size
                        for (int i = 0; i < PlayerActivity.playerList.size(); i++) {
                            screenRelative.makeViewResponsive((int)((int)roadWidth/25),
                                    (int)((int)roadHeight/1.5), playersCar[i]);
                        }
                        // remove listener when we are done with it.
                        binding.player1InnerLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                });
    }

    private void showAlertDialog(boolean isPlayerRobot, String messageTxt) {
        NextPlayerDialog nextPlayerDialog = new NextPlayerDialog();
        Bundle bundle = new Bundle();
        bundle.putString(MESSAGE, messageTxt);
        bundle.putBoolean(IS_PLAYER_ROBOT, isPlayerRobot);
        nextPlayerDialog.setArguments(bundle);
        nextPlayerDialog.getActivity();
        nextPlayerDialog.show(getSupportFragmentManager(), "dialog");
    }

    private void definePlayerTurn(int playerNumber) {
        // to define which player should started
        for (int i = 0; i < PlayerActivity.playerList.size(); i++) {
            PlayerActivity.playerList.get(i).setPlayerTurn(false);
            if (i == playerNumber) {
                PlayerActivity.playerList.get(i).setPlayerTurn(true);
            }
        }
    }


    private void showNumberToStart(int targetTime, String targetStr) {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    binding.reverseCount.setText(targetStr);
                    // set animation to reverse Number count
                    Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.zoom_in);
                    animation.setDuration(1000);
                    binding.reverseCount.startAnimation(animation);

                    // startThread for one time
                    if (targetTime == 0) {
                        moveSeekBar(playersCar[playerNumber], playersCarLayout[playerNumber], playersScrollView[playerNumber]
                                , PlayerActivity.playerList.get(playerNumber));
                    }

                    // after 4 second game started
                    if (targetTime == 4000) {
                        binding.reverseCount.setVisibility(View.INVISIBLE);
                        binding.fadeLayout.setVisibility(View.INVISIBLE);
                        startGame = true;
                    }
                });
            }
        }, targetTime);
    }


    private void moveSeekBar(ImageView playerCar, ConstraintLayout playerCarLayout, HorizontalScrollView scrollView, Player player) {
        interruptNumber = 0;
        looseNumber = 1;
        // size of road image
        windowWidth = 14000;
        movingCarStep = 0;
        carPosition = (int) playerCarLayout.getX();

        movingStep = 20;
        movingThumbStep = 5;
        scrollX = 0;
        scrollY = 0;
        threadDelay = 50;

        // thread for change seekbar thumb position
        seekbarPosThread = new Thread(new Runnable() {
            @Override
            public void run() {
                mainHandler.postDelayed(this, handlerDelay);
                // when bot playing
                if (recorder != null) {
                    Log.d(TAG, "sound: " + sound);
                    if (playWithRobot) {
                        getSoundVolume(soundVolume, true);
                    } else {
                        soundVolume = recorder.getMaxAmplitude();
                        getSoundVolume(soundVolume, false);
                    }
                    runOnUiThread(() -> moveSpeedPointer(playerCar, player));
                    scrollBackground(scrollView);
                    if (startGame) {
                        carPositionDependOnSeekbar();
                    }
                }
            }
        });
        executorService.execute(seekbarPosThread);
    }


    private void moveSpeedPointer(ImageView playerCar, Player player) {
        seekBarThumbPos = binding.seekBar.getProgress();

        // when car move show gif
        if (seekBarThumbPos > 150 && !changeImageResource && startGame) {
            // for not crashing
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            runOnUiThread(() -> playerCar.setImageResource(player.getPlayerGif()));
            changeImageResource = true;
        }

        // when car back to yellow point change imageResource to static one
        if (seekBarThumbPos < 150 && changeImageResource) {
            changeImageResource = false;
            runOnUiThread(() -> playerCar.setImageResource(player.getPlayerImage()));
        }

        // when seekbar is on red part
        if (seekBarThumbPos >= 0 && seekBarThumbPos < 50) {
            moveSeekbar(300, 1000);
        }
        // when seekbar is on yellow part
        if (seekBarThumbPos >= 50 && seekBarThumbPos < 150) {
            moveSeekbar(1000, 2000);
        }
        // when seekbar is on green1 part
        else if (seekBarThumbPos >= 150 && seekBarThumbPos < 300) {
            loose = true;
            enablePauseButton = false;
            binding.pauseBtn.setImageResource(R.drawable.disable_pause_icon);
            moveSeekbar(2000, 3000);
        }
        // when seekbar is on green2 part
        else if (seekBarThumbPos >= 300 && seekBarThumbPos < 400) {
            moveSeekbar(3000, 4000);
        }
        // when seekbar is on orange part
        else if (seekBarThumbPos >= 400) {
            if (seekBarThumbPos >= 480) {
                checkBoilCar(playerCar, player);
            } else {
                moveSeekbar(4000, 6000);
            }
        }
        binding.seekBar.setProgress(movingThumbStep);

        // when sound volume go below  900 player loose and when player receive target point of page in robot player
        if ((sound < looseSound && loose && startGame && !carBoil)
                || (playWithRobot && carPosition >= bot.getLoosePoint() && !carBoil)) {
            checkSoundCut(playerCar, player);
        }
    }

    private void moveSeekbar(int minSound, int maxSound) {
        // change seekbar position depend on robot or natural sound
        if (playWithRobot) {
            getSoundVolume(0, true);
        } else {
            getSoundVolume(soundVolume, false);
        }
        int average = (maxSound + minSound) / 2;
        // make seekbar thumb
        if (sound >= minSound && sound <= maxSound) {
            if (sound > average) {
                movingThumbStep++;
            } else {
                movingThumbStep--;
            }
        } else if (sound < minSound && seekBarThumbPos >= 1) {
            movingThumbStep -= 10;
        } else if (sound > maxSound && seekBarThumbPos < 500) {
            movingThumbStep += 5;
        }
    }

    private void checkBoilCar(ImageView carImage, Player player) {
        // if seekbar thumb go very up go back down
        carBoil = true;

        //increase handler delay to stop main thread
        handlerDelay = 1000;
        // check if sound cut
        // to suppressLint the 0 numbers sound
        Thread checkSpoilThread = new Thread(() -> {
            while (seekBarThumbPos > 210 && carBoil) {
                movingThumbStep -= 5;
                movingCarStep = 0;

                if (playWithRobot) {
                    getSoundVolume(0, true);
                } else {
                    // check if sound cut
                    soundVolume = recorder.getMaxAmplitude();
                    // to suppressLint the 0 numbers sound
                    if (soundVolume != 0) {
                        getSoundVolume(soundVolume, false);
                    }
                }
                if (sound < looseSound) {
                    checkSoundCut(carImage, player);
                }
                binding.seekBar.setProgress(movingThumbStep);
                seekBarThumbPos = binding.seekBar.getProgress();
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                if (seekBarThumbPos <= 210) {
                    handlerDelay = 50;
                }
            }
        });
        executorService.execute(checkSpoilThread);
    }

    private void getSoundVolume(int soundVolume, boolean robotPlaying) {
        sound = 1;
        if (soundVolume > 10 && !robotPlaying) {
            sound = (soundVolume / ((soundSensitive + 1) * 0.3f));
        }
        // when robot is playing
        else if (robotPlaying) {
            sound = robotSound;
            if (tempBoolean) {
                getRobotSound();
            }
        }
    }

    // generate sound for bot every 1 second
    public void getRobotSound() {
        Thread generateRobotSoundThread = new Thread(new Runnable() {
            @Override
            public void run() {
                tempBoolean = false;
                robotSound = bot.generateSound();
                robotHandler.postDelayed(this, 1000);
            }
        });
        executorService.execute(generateRobotSoundThread);
    }

    private void checkSoundCut(ImageView imageView, Player player) {
        interruptNumber++;

        // if sound cut for "looseNumber" time player lost ******
        if (interruptNumber >= looseNumber || carPosition >= backgroundWidth) {
            playerNumber++;

            // stop handlers and threads
            stopHandlers();

            Log.d("rfrf", "first: " + playerNumber);

            // if seekbar received spoil point and sound interrupt refresh variables
            runOnUiThread(() -> imageView.setImageResource(player.getPlayerImage()));

            // when last player play finish the game with show dialog
            if (playerNumber == numberOfPlayer) {
                runOnUiThread(() -> {
                    startGame = false;
                    showAlertDialog(false, getString(R.string.finishMsg));
                });
            } else {
                // when next player turn show suitable dialog depend on robot or human
                isNext = true;
                resetTheGame();

                // robot start playing
                if (!playWithRobot) {
                    runOnUiThread(() -> showAlertDialog(PlayerActivity.playerList.get(playerNumber).isPlayerRobot(), PlayerActivity.playerList.get(playerNumber).getPlayerName()));
                }
                // robot start playing
                else {
                    startTheGame(PlayerActivity.playerList.get(playerNumber).isPlayerRobot(), PlayerActivity.playerList.get(playerNumber).getPlayerName());
                }
            }
        }
    }

    private void resetTheGame() {
        if (carBoil) {
            carBoil = false;
            handlerDelay = 50;
        }

        executorService = Executors.newFixedThreadPool(4);
        resetNitrogenProgressBar();

        invisibleCarPoint();

        runOnUiThread(() -> {
            binding.seekBar.setProgress(0);
            binding.pauseBtn.setImageResource(R.drawable.pause_icon);
        });

        interruptNumber = 0;
        looseTimer = 0;
        timer.cancel();
        enablePauseButton = true;
        playWithRobot = false;
        nitroClick = false;
        fastSpeedNumber = 2;
        if (PlayerActivity.playerList.get(playerNumber).isPlayerRobot()) {
            playWithRobot = true;
            tempBoolean = true;
            bot = new Bot();
            bot.prepareBotField();
        } else {
            playWithRobot = false;
        }

        generateNitrogen();
        changeImageResource = false;
    }

    private void invisibleCarPoint() {
        runOnUiThread(() -> {
            for (int i = 0; i < numberOfPlayer; i++) {
                carPoints[i].setVisibility(View.INVISIBLE);
            }
        });
    }

    private void carPositionDependOnSeekbar() {
        // move car depend on seekbar position
        if (seekBarThumbPos >= 300) {
            movingCarStep = movingStep;
        } else if (seekBarThumbPos >= 150) {
            movingCarStep = movingStep / 2;
        } else if (seekBarThumbPos >= 0) {
            movingCarStep = 0;
        }

        // if car receive spoil point
        if (seekBarThumbPos > seekBarWidth - 20) {
            movingCarStep = 0;
            carBoil = true;
        }

        // move car depend on each player turn
        for (int i = 0; i < PlayerActivity.playerList.size(); i++) {
            if (PlayerActivity.playerList.get(i).isPlayerTurn()) {
                PlayerActivity.playerList.get(i).setPlayerScore
                        (startMoving(playersCar[playerNumber], playersCarLayout[i],
                                PlayerActivity.playerList.get(i), playersScoreTxt[i], playersInnerLayout[i]));
                break;
            }
        }
    }


    private int startMoving(ImageView playerImg, ConstraintLayout playerImgLayout,
                             Player player, TextView playerScoreTxt, ConstraintLayout layout) {
        carOnCLickListener(playerImgLayout, layout);
        currentLayout = layout;

        int score = 0;
        // move car till end point
        if (carPosition < windowWidth - windowWidth / 15) {
            carPosition += movingCarStep;
            playerImgLayout.setX(carPosition);

            // when capsule behind the car make carCollision true
            if (carCollision) {
                carCollision = nitrogen.checkNitrogenCrossCar(playerImgLayout, playWithRobot);
            }
            else {
                getNitrogen = false;
                // if car receive target point generate capsule
                carCollision = nitrogen.generateCapsule(carPosition, movingStep, layout, carPoints[playerNumber], roadHeight);
                // robot eat nitrogen randomly
                if (playWithRobot) {
                    int num = Arrays.binarySearch(numberOfNitrogen, nitrogen.getNitrogenCount());
                    if (num >= 0) {
                        getNitrogen = true;
                    }
                }
            }

            // make car static when seekbar receive spoil point
            if (carBoil && counter == 0) {
                runOnUiThread(() -> {
                    playerImg.setImageResource(player.getPlayerImage());

                    // add smoke to page
                    smoke = new GifImageView(this);
                    smoke.setImageResource(R.drawable.smoke);
                    smoke.setId(View.generateViewId());
                    Log.d("rggr", "startMoving: " + screenWidth*0.09);
                    ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int)((int) screenWidth*0.09)
                            , (int)((int) screenWidth*0.09));
                    smoke.setLayoutParams(params);
                    playerImgLayout.addView(smoke);
                    // connect image constraint to layout
                    ConstraintSet set = new ConstraintSet();
                    set.clone(playerImgLayout);
                    set.connect(smoke.getId(), ConstraintSet.END, playerImgLayout.getId(), ConstraintSet.END, -8);
                    set.connect(smoke.getId(), ConstraintSet.TOP, playerImgLayout.getId(), ConstraintSet.TOP, 0);
                    set.applyTo(playerImgLayout);
                });
                counter++;
            } else if (counter == 1 && seekBarThumbPos <= 210) {
                runOnUiThread(() -> {
                    // remove smoke and return own car image
                    playerImgLayout.removeView(smoke);
                    playerImg.setImageResource(player.getPlayerGif());
                });
                counter = 0;
                carBoil = false;
            }

            // check if nitrogen progressbar clicked
            nitrogenProgressClicked(playerImg, playerImgLayout, player, playerScoreTxt);

            score = addPlayerScore(playerScoreTxt, player);
        }

        return score;
    }

    private void fastCarSpeed(ImageView playerImage, ConstraintLayout playerImgLayout, Player player, TextView playerTxtScore) {
        if (nitroClick) {
            // add to player score when it go fast
            // show speed back of car when click on nitrogen
            //???
            Thread speedUpCar = new Thread(() -> {
                movingCarStep = progressValue;
                while (nitroClick) {
                    for (fastSpeedNumber = 0; fastSpeedNumber < 3; fastSpeedNumber++) {
                        carPosition += movingCarStep;
                        runOnUiThread(() -> playerImgLayout.setX(carPosition));
                        // add to player score when it go fast
                        player.setPlayerScore(addPlayerScore(playerTxtScore, player));

                        // show speed back of car when click on nitrogen
                        if (showSpeed) {
                            showSpeed = false;
                            speedImage = new GifImageView(this);
                            runOnUiThread(() -> {
                                speedImage.setImageResource(R.drawable.speed);
                                speedImage.setId(View.generateViewId());
                                Log.d("rggr", "startMoving: " + screenWidth*0.09);
                                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams((int)((int) screenWidth*0.1)
                                        , (int)((int) screenWidth*0.1));
                                speedImage.setLayoutParams(params);
                                playerImgLayout.addView(speedImage);
                                // connect image constraint to layout
                                ConstraintSet set = new ConstraintSet();
                                set.clone(playerImgLayout);
                                set.connect(speedImage.getId(), ConstraintSet.END, playerImage.getId(), ConstraintSet.START, 0);
                                set.connect(speedImage.getId(), ConstraintSet.START, playerImgLayout.getId(), ConstraintSet.START, 0);
                                set.connect(speedImage.getId(), ConstraintSet.TOP, playerImgLayout.getId(), ConstraintSet.TOP, 0);
                                set.connect(speedImage.getId(), ConstraintSet.BOTTOM, playerImgLayout.getId(), ConstraintSet.BOTTOM, 0);
                                set.applyTo(playerImgLayout);
                            });
                        }
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (fastSpeedNumber == 2) {
                            robotNitrogenCounter = 0;
                            robotNitrogenFeed = nitrogen.robotNitroRandomNumber();
                            runOnUiThread(() -> playerImgLayout.removeView(speedImage));
                            break;
                        }
                    }
                    nitroClick = false;
                }
                runOnUiThread(() -> currentLayout.removeView(speedImage));
            });
            speedUpCar.start();
        }
    }

    private int addPlayerScore(TextView playerScoreTxt, Player player) {
        int playerScore = player.getPlayerScore();
        // add player score
        if (movingCarStep != 0) {
            playerScore += movingCarStep / 2;
        }

        int finalPlayerScore = playerScore;
        runOnUiThread(() -> playerScoreTxt.setText(String.valueOf(finalPlayerScore)));

        return playerScore;
    }

    private void scrollBackground(View scrollView) {
        if (scrollX < windowWidth) {
            // car moving sooner background
            // when car receive target point, scrolling background starting

//            scrollX = carPosition - windowWidth/25;

            scrollX = carPosition - screenHeight / 2;

            // move background
            scrollY = carPosition;
            scrollView.scrollTo(scrollX, scrollY);
        }
    }

    private void startVoiceListening() {
        // prepare recording sound
        SoundDetector soundDetector = new SoundDetector();
        recorder = soundDetector.startVoiceListening(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finishTheGame();
    }

    private void finishTheGame() {
        stopHandlers();
        if (recorder != null) {
            try {
                recorder.stop();
                recorder.reset();
                recorder.release();
                recorder = null;
            } catch (Exception e) {
                Log.d("2020", "onDestroy: " + e.getMessage());
            }
        }
    }

    private void stopHandlers() {
        mainHandler.removeCallbacksAndMessages(null);
        robotHandler.removeCallbacksAndMessages(null);
        executorService.shutdown();
    }

    @Override
    public void startTheGame(boolean isPlayerRobot, String messageTxt) {
        if (messageTxt.equals(getString(R.string.finishMsg))) {
            finish();
            // check all game list and make carGameDone true to not start this game again
            for (int i = 0; i < SelectGameActivity.gamesList.size(); i++) {
                if (SelectGameActivity.gamesList.get(i).getGameName().equals(getString(R.string.carGameTitle))) {
                    SelectGameActivity.gamesList.get(i).setGameDone(true);
                    break;
                }
            }
            // show result activity
            startActivity(new Intent(CarGameActivity.this, ResultActivity.class));
        } else {
            playWithRobot = isPlayerRobot;
            startGame = false;
            loose = false;
            if (isNext) {
                // reset the page
                runOnUiThread(() -> binding.fadeLayout.setVisibility(View.VISIBLE));
                definePlayerTurn(playerNumber);

                nitrogen = new Nitrogen(this, binding.mainLayout, screenWidth, screenHeight);
                nitrogen.generateRandomNumber();
                carCollision = false;
            }

            // show reverse number
            runOnUiThread(() -> {
                showNumberToStart(0, "3");
                showNumberToStart(1000, "2");
                showNumberToStart(2000, "1");

                if (playWithRobot) {
                    showNumberToStart(3000, getString(R.string.robot_play));
                } else {
                    showNumberToStart(3000, "start");
                }
                showNumberToStart(4000, "");
            });

            // loose boolean being active after 10 second
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    looseTimer++;
                    if (looseTimer > 14) {
                        loose = true;
                        timer.cancel();
                    }
                }
            }, 0, 1000);
        }
    }

    @Override
    public void restartFromNextPlayerDialog(DialogFragment dialogFragment) {
        // ???? if current player is not first player restart button work
        if (playerNumber > 0) {
            restartGame();
            dialogFragment.dismiss();
        }
    }

    @Override
    public void continueGame() {
        mainHandler.postDelayed(seekbarPosThread, threadDelay);
    }

    @Override
    public void goMainMenu() {
        finishTheGame();
        startActivity(new Intent(CarGameActivity.this, SelectGameActivity.class));
    }

    @Override
    public void restartGame() {
        looseTimer = 0;
        timer.cancel();
        loose = false;
        stopHandlers();

        resetNitrogenProgressBar();
        resetPlayersFiled();
        startGameAtFirst();
        invisibleCarPoint();
    }

    // make all players empty
    private void resetPlayersFiled() {
       runOnUiThread(() -> {
           for (int i = 0; i < PlayerActivity.playerList.size(); i++) {
               playersScoreTxt[i].setText("0");
               playersCarLayout[i].setX(carsFirstPosition);
               playersCar[i].setImageResource(PlayerActivity.playerList.get(i).getPlayerImage());
               playersScrollView[i].setScrollX(0);
               PlayerActivity.playerList.get(i).setPlayerScore(0);
           }
       });
    }

}

