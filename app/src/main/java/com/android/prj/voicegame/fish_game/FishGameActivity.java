package com.android.prj.voicegame.fish_game;

import static com.android.prj.voicegame.public_classes.ScreenRelative.screenHeight;
import static java.lang.Thread.sleep;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintSet;

import com.android.prj.voicegame.public_classes.PlaySound;
import com.android.prj.voicegame.public_classes.activities.SelectGameActivity;
import com.android.prj.voicegame.R;
import com.android.prj.voicegame.public_classes.activities.PlayerSelectionActivity;
import com.android.prj.voicegame.public_classes.activities.ResultActivity;
import com.android.prj.voicegame.public_classes.dialogs.NextPlayerDialog;
import com.android.prj.voicegame.public_classes.dialogs.PauseDialog;
import com.android.prj.voicegame.fish_game.models.BarriersAndOptions;
import com.android.prj.voicegame.fish_game.models.Fish;
import com.android.prj.voicegame.fish_game.models.FishBot;
import com.android.prj.voicegame.fish_game.models.HammerFish;
import com.android.prj.voicegame.fish_game.models.Hook;
import com.android.prj.voicegame.public_classes.model.Player;
import com.android.prj.voicegame.fish_game.models.Rock;
import com.android.prj.voicegame.fish_game.models.Shark;
import com.android.prj.voicegame.fish_game.models.SwordFish;
import com.android.prj.voicegame.public_classes.PublicSetting;
import com.android.prj.voicegame.public_classes.ScreenRelative;
import com.android.prj.voicegame.public_classes.SoundDetector;

import java.util.Arrays;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class FishGameActivity extends AppCompatActivity implements
        NextPlayerDialog.Finish, PauseDialog.Actions {

    private static final String TAG = "2121";
    public static final String HOOKER_CUTTER = "hooker cutter";
    public static final String ROCK_DESTROYER = "Rock destroyer";
    public static final String MESSAGE = "message";
    private com.android.prj.voicegame.databinding.ActivityFishBinding binding;
    private MediaRecorder recorder;
    private Handler fishMovingHandler;
    private Handler sharkMovingHandler;
    private long fishHandlerDelay;
    private long sharkHandlerDelay;
    private int soundVolume;
    private Fish fish;
    private float soundSensitive;
    private SeekBarMoving seekBarMoving;
    private int seekBarThumbPos;
    private int minSound;
    private Shark shark;
    private Thread fishThread;
    private Thread sharkThread;
    private int fishPosition;
    private BarriersAndOptions barriers;
    private boolean loopVar;
    private boolean checkCollision;
    private boolean[] rocksCollision;
    private List<Rock> rockList;
    private List<SwordFish> swordFishList;
    private List<Hook> hookList;
    private List<HammerFish> hammerFishList;
    private boolean fishChanged;
    private Timer timer;
    private boolean playWithRobot;
    private boolean startGame ;
    private boolean loose;
    private boolean isNext;
    private int playerNumber;
    private long looseTimer;
    private double numberOfPlayer;
    public static boolean enablePauseButton;
    private boolean tempBoolean;
    private boolean gameFinished;
    private float fishFirstX;
    private float sharkFirstX;
    private float fishFirstY;
    private float sharkFistY;
    private ExecutorService executorServices;
    private int playerScore;
    private int index;
    private FishBot bot;
    private float sound;
    private boolean robotPlaying;
    private float robotSound;
    private Handler robotHandler;
    private boolean delayRunning;
    private ProgressBar durationProgress;
    private boolean showProgress;
    private int maxProgressValue;
    private boolean showProgressingLoop;
    private boolean makeSound = true;
    private PauseDialog pauseDialog;
    private ImageView[] playerScoreImage;
    private LinearLayout[] playerScoreLayout;
    private TextView[] playersScoreTxt;
    private int loosePoint;
    private boolean confusedFish;
    private MediaPlayer backgroundSound;
    private MediaPlayer confusedSound;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = com.android.prj.voicegame.databinding.ActivityFishBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        getAndSetPlayerInfo();
        numberOfPlayer = PlayerSelectionActivity.numberOfPlayer;

        //cant manipulate scrollview
        binding.gameScrollView.setOnTouchListener((view, motionEvent) -> true);

        ScreenRelative.getScreenSize(this);
        defineField();

        // if robot game is start with human or game is without robot
        if (!playWithRobot) {
            runOnUiThread(() -> showDialogFinish(PlayerSelectionActivity.playerList.get(playerNumber).isPlayerRobot(), PlayerSelectionActivity.playerList.get(playerNumber).getPlayerName()));
        }
        else {
            startTheGame(PlayerSelectionActivity.playerList.get(playerNumber).isPlayerRobot(), PlayerSelectionActivity.playerList.get(playerNumber).getPlayerName());
        }

        clickItem();
        ScreenRelative screenRelative = new ScreenRelative();

        // change fish size depend on screen size
        screenRelative.makeViewResponsive((int)((int)ScreenRelative.screenWidth*0.11),
                (int)((int)ScreenRelative.screenWidth*0.11), binding.fishImage);

        // change shark size depend on screen size
        screenRelative.makeViewResponsive((int)((int)ScreenRelative.screenWidth*0.35),
                (int)((int) screenHeight*0.35), binding.sharkImage);

        // take the shark out of page
        binding.sharkImage.setX(-1300);
        binding.sharkImage.setY(screenHeight/2);
        soundSensitive = PlayerSelectionActivity.soundSensitive;

        PublicSetting.setAppLanguage(getApplicationContext().getResources());
        PublicSetting.hideBars(this);
    }

    @Override
    public void onBackPressed() {

    }

    private void clickItem() {
        binding.pauseBtn.setOnClickListener(view -> {
            PlaySound.playSound(this, R.raw.click_sound, false);
            if (enablePauseButton && startGame) {
                stopHandlers();
                pauseDialog = new PauseDialog(this, this);
            }
        });
    }

    private void getAndSetPlayerInfo() {
        // set image for each player depend on her color
        for (int i = 0; i < PlayerSelectionActivity.playerList.size(); i++) {
            PlayerSelectionActivity.playerList.get(i).setPlayerScore(0);
            String playerColor = PlayerSelectionActivity.playerList.get(i).getPlayerColor();
            if (playerColor.equals(getString(R.string.blue_color))) {
                PlayerSelectionActivity.playerList.get(i).setPlayerGif(R.drawable.fish_gif_blue);
                PlayerSelectionActivity.playerList.get(i).setPlayerSecondGif(R.drawable.confused_blue_fish);
            } else if (playerColor.equals(getString(R.string.green_color))) {
                PlayerSelectionActivity.playerList.get(i).setPlayerGif(R.drawable.fish_gif_gereen);
                PlayerSelectionActivity.playerList.get(i).setPlayerSecondGif(R.drawable.confused_green_fish);
            } else if (playerColor.equals(getString(R.string.red_color))) {
                PlayerSelectionActivity.playerList.get(i).setPlayerGif(R.drawable.fish_gif_red);
                PlayerSelectionActivity.playerList.get(i).setPlayerSecondGif(R.drawable.confused_red_fish);
            } else if (playerColor.equals(getString(R.string.yellow_color))) {
                PlayerSelectionActivity.playerList.get(i).setPlayerGif(R.drawable.fish_gif_yellow);
                PlayerSelectionActivity.playerList.get(i).setPlayerSecondGif(R.drawable.confused_yellow_fish);
            }
        }

        // put views in arrays to use before
        playerScoreImage = new ImageView[]{binding.imgScore1, binding.imgScore2, binding.imgScore3, binding.imgScore4};
        playerScoreLayout = new LinearLayout[]{binding.player1ScoreLayout, binding.player2ScoreLayout, binding.player3ScoreLayout, binding.player4ScoreLayout};
        playersScoreTxt = new TextView[]{binding.player1Score, binding.player2Score, binding.player3Score, binding.player4Score};

        readyPlayerBoard();
    }

    private void readyPlayerBoard() {
        // save image of player and their score to a array
        for (int i = 0; i < PlayerSelectionActivity.playerList.size(); i++) {
            playerScoreImage[i].setImageResource(PlayerSelectionActivity.playerList.get(i).getPlayerGif());
        }

        // remove additional layout
        for (int i = PlayerSelectionActivity.playerList.size(); i < 4; i++) {
            binding.scoresLayout.removeView(playerScoreLayout[i]);
        }
    }

    private void defineField() {
        fishMovingHandler = new Handler();
        sharkMovingHandler = new Handler();
        enablePauseButton = true;
        robotHandler = new Handler();
        robotPlaying = false;
        fishHandlerDelay = 50;
        sharkHandlerDelay = 50;
        checkCollision = true;
        soundVolume = 0;
        fishChanged = false;
        int backgroundWidth = 16000;
        gameFinished = false;
        delayRunning = false;
        showProgress = true;
        maxProgressValue = 6;

        addProgressBar();

        binding.fishImage.setImageResource(PlayerSelectionActivity.playerList.get(playerNumber).getPlayerGif());
        // check first player that is robot or not for starting the game
        if (PlayerSelectionActivity.playerList.get(playerNumber).isPlayerRobot()) {
            playWithRobot = true;
            tempBoolean = true;
            bot = new FishBot();
            bot.prepareBotField();
        }
        else {
            playWithRobot = false;
        }

        // object to ge sound and return it depend on sensitive
        SoundDetector soundDetector = new SoundDetector();
        recorder = soundDetector.startVoiceListening(this);

        // make fish and shark object to use later
        fish = new Fish(backgroundWidth, ScreenRelative.screenWidth,
                screenHeight, binding.fishImage, binding.fishLayout);

        shark = new Shark(backgroundWidth, screenHeight,
                binding.gameLayout.getMinHeight(), binding.scoresLayout);

        // make barriers on page
        barriers = new BarriersAndOptions(this, binding.gameLayout, screenHeight, ScreenRelative.screenWidth);
        barriers.generateObjects();

        seekBarMoving = new SeekBarMoving();
        seekBarMoving.setSeekbar(binding.seekBar);

        // define list to use barriers list that generate before
        rockList = barriers.getRockList();
        swordFishList = barriers.getSwordFishList();
        swordFishList = barriers.getSwordFishList();
        hookList = barriers.getHookList();
        hammerFishList = barriers.getHammerFishList();

        ////// ???
        for (int i = 0; i < hookList.size(); i++) {
            Log.d("MyHooksX", "hooks x: " + hookList.get(i).getImageView().getX());
        }

        // start fish and shark thread
        startFishMoving(PlayerSelectionActivity.playerList.get(playerNumber));
        startSharkMoving(binding.sharkImage);

        rocksCollision = new boolean[rockList.size()];
        Arrays.fill(rocksCollision, false);
    }

    private void startFishMoving(Player player) {
        fishThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // get values until they are 0
                if (fishFirstX == 0) {
                    fishFirstX = binding.fishLayout.getX();
                }
                if (sharkFirstX == 0) {
                    sharkFirstX = binding.sharkImage.getX();
                }
                if (fishFirstY == 0) {
                    fishFirstY = binding.fishLayout.getY();
                }
                if (sharkFistY == 0) {
                    sharkFistY = binding.sharkImage.getY();
                }
                if (loosePoint == 0){
                    loosePoint = binding.gameScrollView.getWidth() * 7;
                }

                // define witch time start game
                fishMovingHandler.postDelayed(this, fishHandlerDelay);
                if (recorder != null) {
                    // get sound from output
                    soundVolume = recorder.getMaxAmplitude();
                    minSound = 300;
                    if (!SeekBarMoving.gameFinished) {
                        if (playWithRobot) {
                            defineRobotActions();
                            // when bot receive target x of page loose game
                            if (binding.fishLayout.getX() >= loosePoint) {
                                SeekBarMoving.gameFinished = true;
                            }
                        }
                        else {
                            getSound(soundVolume, soundSensitive, false);
                            seekBarThumbPos = seekBarMoving.moveSpeedPointer(binding.seekBar, sound, minSound);
                        }

                        // if fish doesn't hit any rock move fish
                        if (!delayRunning) {
                            fishPosition = fish.determineFishPosition(binding.fishLayout, seekBarThumbPos);
                            fish.scrollBackground(binding.gameScrollView);
                        }
                        // if seekbar is above of 50 number and don t hit rock increase player score
                        if (seekBarThumbPos > 50 && !delayRunning) {
                            setPlayerScore(player);
                        }
                        // when seekbar thumb go above 100 disable pause button
                        if (seekBarThumbPos >= 100) {
                            runOnUiThread(() -> binding.pauseBtn.setImageResource(R.drawable.disable_pause_icon));
                            FishGameActivity.enablePauseButton = false;
                        }
                    }
                    // when player lost shark receive it self to fish
                    else if (!gameFinished) {
                        shark.changePosition(binding.sharkImage, fishPosition);
                        delayRunning = false;
                        gameFinished = true;
                    }
                    if (checkCollision) {
                        checkFishCollision(player);
                    }
                }
            }
        });
    }

    private void defineRobotActions() {
        int distance = (int) (binding.fishLayout.getWidth() * 1.5);
        getSound(soundVolume, soundSensitive, true);
        seekBarThumbPos = seekBarMoving.moveSpeedPointer(binding.seekBar, sound, minSound);

        // if fish is close to hooks
        for (int j = 0; j < hookList.size(); j++) {
            ImageView hook = hookList.get(j).getImageView();
            if ((binding.fishLayout.getX() >= hook.getX() - distance)
                    && (binding.fishLayout.getX() <= hook.getX() + distance)) {
                makeSound = false;
                seekBarThumbPos = seekBarMoving.moveSpeedPointer(binding.seekBar, 2000, minSound);
                sound = 2000;
            }
        }

        // check if fish is close to rocks
        for (int j = 0; j < rockList.size(); j++) {
            ImageView rock = rockList.get(j).getImageView();
            if ((binding.fishLayout.getX() >= rock.getX() - distance)
                    && (binding.fishLayout.getX() <= rock.getX() + distance)) {
                makeSound = false;
                seekBarThumbPos = seekBarMoving.moveSpeedPointer(binding.seekBar, 7000, minSound);
                sound = 7000;
            }
        }
    }

    private void getSound(float soundVolume, float sliderValue, boolean robotPlaying) {
        sound = 1;
        if (soundVolume > 10 && !robotPlaying) {
            sound = (soundVolume / ((sliderValue + 1) * 0.3f));
        }
        // when robot is playing
        else if (robotPlaying) {
            if (tempBoolean) {
                getRobotSound();
            }
            sound = robotSound;
        }
    }

    // generate sound for bot every 500 ms
    public void getRobotSound() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                robotPlaying = true;
                tempBoolean = false;
                robotSound = bot.generateSound();
                robotHandler.postDelayed(this, 500);
            }
        });
        thread.start();
    }


    private void setPlayerScore(Player player) {
        playerScore += 5;
        player.setPlayerScore(playerScore);
        runOnUiThread(() -> {
            playersScoreTxt[playerNumber].setText(String.valueOf(playerScore));
        });
    }

    private void finishTheGame() {
        playerNumber ++;
        // when last player play finish the game with show dialog
        if (playerNumber == numberOfPlayer) {
            // show finish dialog and make game false to do not come again to this game
            runOnUiThread(() -> showDialogFinish(false, getString(R.string.finishMsg)));
        } else {
            runOnUiThread(() -> resetTheGame());
            // when next player turn show suitable dialog depend on robot or human
            isNext = true;
            if (!playWithRobot) {
                runOnUiThread(() -> showDialogFinish(PlayerSelectionActivity.playerList.get(playerNumber).isPlayerRobot(), PlayerSelectionActivity.playerList.get(playerNumber).getPlayerName()));
            } else {
                startTheGame(PlayerSelectionActivity.playerList.get(playerNumber).isPlayerRobot(), PlayerSelectionActivity.playerList.get(playerNumber).getPlayerName());
            }
        }
    }

    private void checkFishCollision(Player player) {
        checkCollisionToHooks();

        checkCollisionToRocks(player);

        checkCollisionToHammerFishes(player);

        checkCollisionToSwordFish(player);

        checkCollisionToShark();
    }

    private void checkCollisionToShark() {
        // when shark receive fish
        if (barriers.checkCollision(binding.sharkImage, binding.fishLayout, 100, 80, 50, 80)) {
            runOnUiThread(() -> binding.fishLayout.setVisibility(View.INVISIBLE));
            stopHandlers();
            finishTheGame();
        }
    }

    private void checkCollisionToSwordFish(Player player) {
        // check collision to swordfish
        if (!Fish.hookCutter && !fishChanged) {
            runOnUiThread(() -> {
                for (int i = 0; i < swordFishList.size(); i++) {
                    if (barriers.checkCollision(binding.fishLayout, swordFishList.get(i).getImageView(), 0, 0, 0, 0)) {
                        fish.changeToSwordFish();
                        binding.gameLayout.removeView(swordFishList.get(i).getImageView());
                        keepFishStateForTime(HOOKER_CUTTER, player);
                        break;
                    }
                }
            });
        }
    }

    private void checkCollisionToHammerFishes(Player player) {
        // check collision to hammer fish images
        if (!Fish.rockDestroyer && !fishChanged) {
            runOnUiThread(() -> {
                for (int i = 0; i < hammerFishList.size(); i++) {
                    if (barriers.checkCollision(binding.fishLayout, hammerFishList.get(i).getImageView(), 0, 0, 0, 0)) {
                        fish.changeToHammer();
                        keepFishStateForTime(ROCK_DESTROYER, player);
                        binding.gameLayout.removeView(hammerFishList.get(i).getImageView());
                        break;
                    }
                }
            });
        }
    }

    private void checkCollisionToRocks(Player player) {
        // check collision to rocks images
        for (int i = 0; i < rockList.size(); i++) {
            if (barriers.checkCollision(binding.fishLayout, rockList.get(i).getImageView(), 20, 0, 150 ,0)) {
                if (!Fish.rockDestroyer && !rocksCollision[i]) {
                    checkCollision = false;
                    rocksCollision[i] = true;
                    confusedFish = true;
                    fish.stopFish();
                    // change fish image to confused image and a little backward
                    runOnUiThread(() -> {
                        confusedSound = MediaPlayer.create(this, R.raw.confused_sound);
                        confusedSound.start();
                        binding.fishImage.setImageResource(player.getPlayerSecondGif());
                        makeDelay(player);
                    });
                    break;
                }
                // if fish was hammer destroyer and hit the rock rocks destroy
                else if (Fish.rockDestroyer) {
                    rocksCollision[i] = true;
                    fish.destroyTheRock(rockList.get(i).getImageView());
                    break;
                }
            }
        }
    }

    private void checkCollisionToHooks() {
        // collision to hook image
        for (int i = 0; i < hookList.size(); i++) {
            if (barriers.checkCollision(binding.fishLayout, hookList.get(i).getImageView(), 50, 0, 0, 50)) {
                // if fish don t have collision with hookerCutter before
                if (!Fish.hookCutter) {
                    fish.goUp(hookList.get(i).getImageView());
                    stopHandlers();
                    Thread thread = new Thread(() -> {
                        for (int j = 0; j < 2; j++) {
                            try {
                                sleep(500);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (j == 1) {
                                runOnUiThread(() -> {
                                    finishTheGame();
                                    binding.gameScrollView.scrollTo(0, 0);
                                });
                                break;
                            }
                        }
                    });
                    thread.start();
                } else {
                    fish.cutTheHooks(hookList.get(i).getImageView());
                }
            }
        }
    }

    private void keepFishStateForTime(String fishState, Player player) {
        showProgressingLoop = true;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                fishChanged = true;
                loopVar = true;
                showProgress = true;
                while (loopVar) {
                    for (index = 120; index > 0; index--) {
                        // add progressbar one time and change its position another times
                        if (showProgress) {
                            showProgress = false;
                            runOnUiThread(() -> {
                                durationProgress.setVisibility(View.VISIBLE);
                                durationProgress.setProgress(maxProgressValue);
                            });
                            startShowProgressing();
                        }

                        // ?? if receive 1 get out from loop
                        if (index == 1 || confusedFish) {
                            loopVar = false;
                            showProgressingLoop = false;
                            // remove progressbar from page
                            runOnUiThread(() -> {
                                runOnUiThread(() -> durationProgress.setVisibility(View.INVISIBLE));
                                maxProgressValue = 6;
                            });
                            fishChanged = false;
                            // if fish was swordfish
                            if (fishState.equals(HOOKER_CUTTER)) {
                                Fish.hookCutter = false;
                            }
                            // if fish was hammer
                            if (fishState.equals(ROCK_DESTROYER)) {
                                Fish.rockDestroyer = false;
                            }
                            runOnUiThread(() -> binding.fishImage.setImageResource(player.getPlayerGif()));
                            break;
                        }
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            private void startShowProgressing() {
                Thread progressingThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (showProgressingLoop) {
                            runOnUiThread(() -> {
                                // reduce progressbar value
                                durationProgress.setProgress(maxProgressValue);
                                maxProgressValue--;
                            });
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                progressingThread.start();
            }
        });
        executorServices.execute(thread);
    }

    private void stopHandlers() {
        if (!enablePauseButton){
            PlaySound.playSound(this, R.raw.game_over, false);
        }
        fishMovingHandler.removeCallbacksAndMessages(null);
        sharkMovingHandler.removeCallbacksAndMessages(null);
        if (robotPlaying) {
            robotHandler.removeCallbacksAndMessages(null);
            backgroundSound.stop();
            backgroundSound.release();
        }
    }

    private void makeDelay(Player player) {
        // save value of fish step for return it later
        delayRunning = true;
        Thread thread = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                if (i == 2) {
                    confusedSound.stop();
                    confusedSound.release();
                    confusedFish = false;
                    fishHandlerDelay = 50;
                    delayRunning = false;
                    FishGameActivity.this.runOnUiThread(() -> {
                        // change fish image to sword fish or to confused fish
                        if (Fish.hookCutter) {
                            binding.fishImage.setImageResource(R.drawable.sword_fish);
                        } else {
                            binding.fishImage.setImageResource(player.getPlayerGif());
                        }
                    });
                    checkCollision = true;
                    break;
                }
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
        executorServices.execute(thread);
    }

    private void addProgressBar() {
        // add progress bar above of fish imageView
        durationProgress = new ProgressBar(this, null, android.R.attr.progressBarStyleHorizontal);
        durationProgress.setMax(maxProgressValue);
        durationProgress.setProgressTintList(ColorStateList.valueOf(Color.RED));

        // for attach and add view to fish image
        ConstraintSet set = new ConstraintSet();
        set.clone(binding.fishLayout);
        set.connect(durationProgress.getId(), ConstraintSet.TOP, binding.fishLayout.getId(),
                ConstraintSet.TOP, 60);

        // set size for progressbar
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(getResources().getDimensionPixelSize(R.dimen.fish_image_size),
                getResources().getDimensionPixelSize(R.dimen.fish_image_size) / 2);

        durationProgress.setLayoutParams(params);
        durationProgress.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        runOnUiThread(() -> binding.fishLayout.addView(durationProgress));
        durationProgress.setVisibility(View.INVISIBLE);
    }

    // a thread for shark moving
    private void startSharkMoving(ImageView sharkImage) {
        sharkThread = new Thread(new Runnable() {
            @Override
            public void run() {
                // define witch time start game
                sharkMovingHandler.postDelayed(this, sharkHandlerDelay);
                shark.determineSharkPosition(sharkImage, seekBarThumbPos,
                        fishPosition, binding.gameScrollView.getHeight());
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void definePlayerTurn(int playerNumber) {
        // to define which player should started
        for (int i = 0; i < PlayerSelectionActivity.playerList.size(); i++) {
            PlayerSelectionActivity.playerList.get(i).setPlayerTurn(false);
            if (i == playerNumber) {
                PlayerSelectionActivity.playerList.get(i).setPlayerTurn(true);
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

                    // show reverse number count
                    if (targetTime == 0) {
                        startFishMoving(PlayerSelectionActivity.playerList.get(playerNumber));
                    }

                    // after 4 second game started
                    if (targetTime == 4000) {
                        binding.reverseCount.setVisibility(View.INVISIBLE);
                        binding.fadeLayout.setVisibility(View.INVISIBLE);
                        startGame = true;
                        executorServices = Executors.newFixedThreadPool(4);
                        executorServices.execute(fishThread);
                        executorServices.execute(sharkThread);
                    }
                });
            }
        }, targetTime);
    }

    private void resetTheGame() {
        index = 0;
        confusedFish = false;
        // if player loose when progressbar is showing remove it for ready next player
        binding.fishLayout.removeView(durationProgress);
        binding.pauseBtn.setImageResource(R.drawable.pause_icon);
        showProgressingLoop = false;

        Fish.movingStep = 20;
        loopVar = false;
        looseTimer = 0;
        timer.cancel();
        playerScore = 0;
        binding.seekBar.setProgress(0);
        binding.gameScrollView.scrollTo(0, 0);

        runOnUiThread(this::removeViewsFromPage);
        executorServices.shutdown();

        defineField();

        SeekBarMoving.gameFinished = false;
        SeekBarMoving.startGame = false;
        Fish.hookCutter = false;
        Fish.rockDestroyer = false;
        makeSound = true;

        // return fish and shark to their first position
        binding.fishLayout.setX(fishFirstX);
        binding.fishLayout.setY(fishFirstY);

        binding.sharkImage.setX(sharkFirstX);
        binding.sharkImage.setY(sharkFistY);

        binding.fishLayout.setVisibility(View.VISIBLE);
    }

    private void removeViewsFromPage() {
        //remove all views from pages to add new one
        for (int i = 0; i < rockList.size(); i++) {
            binding.gameLayout.removeView(rockList.get(i).getImageView());
        }

        for (int i = 0; i < hookList.size(); i++) {
            binding.gameLayout.removeView(hookList.get(i).getImageView());
        }

        for (int i = 0; i < swordFishList.size(); i++) {
            binding.gameLayout.removeView(swordFishList.get(i).getImageView());
        }

        for (int i = 0; i < hammerFishList.size(); i++) {
            binding.gameLayout.removeView(hammerFishList.get(i).getImageView());
        }
    }

    private void showDialogFinish(boolean isPlayerRobot, String message) {
        new NextPlayerDialog(this, this, message, isPlayerRobot);
    }

    @Override
    public void startTheGame(boolean isPlayerRobot, String messageTxt) {
        if (messageTxt.equals(getString(R.string.finishMsg))) {
            // check all game list and make fishGameDone true to not start this game again
            for (int i = 0; i < SelectGameActivity.gamesList.size(); i++) {
                if (SelectGameActivity.gamesList.get(i).getGameName().equals(getString(R.string.fishGameTitle))) {
                    SelectGameActivity.gamesList.get(i).setGameDone(true);
                    break;
                }
            }
            // show result activity
            finish();
            startActivity(new Intent(FishGameActivity.this, ResultActivity.class));
        } else {
            if (isPlayerRobot) {
                playWithRobot = true;
            } else {
                playWithRobot = false;
            }
            startGame = false;
            loose = false;
            if (isNext) {
                // reset the page
                isNext = false;
                runOnUiThread(() -> {
                    binding.fadeLayout.setVisibility(View.VISIBLE);
                });
                definePlayerTurn(playerNumber);
            }
            // show reverse number
            runOnUiThread(() -> {
                if (playWithRobot && !startGame) {
                    startBackgroundSound();
                }
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
                    looseTimer ++;
                    if (looseTimer > 10) {
                        loose = true;
                        timer.cancel();
                        Shark.followFish = true;
                    }
                }
            }, 0, 1000);
        }
    }

    private void startBackgroundSound() {
        backgroundSound = MediaPlayer.create(this, R.raw.fish_game_background);
        backgroundSound.setLooping(true);
        backgroundSound.start();
    }


    @Override
    public void restartFromNextPlayerDialog(Dialog dialog) {
        // if current player is not first player
        if (playerNumber > 0) {
            restartGame();
            dialog.dismiss();
        }
    }

    @Override
    public void goMenuFromNextPlayerDialog() {
        finish();
        startActivity(new Intent(FishGameActivity.this, SelectGameActivity.class));
    }

    private void resetPlayersFiled() {
        // reset all players score top of the board
        for (int i = 0; i < PlayerSelectionActivity.playerList.size(); i++) {
            playersScoreTxt[i].setText("0");
            PlayerSelectionActivity.playerList.get(i).setPlayerScore(0);
        }
    }

    @Override
    public void continueGame() {
        fishMovingHandler.postDelayed(fishThread, fishHandlerDelay);
        sharkMovingHandler.postDelayed(sharkThread, fishHandlerDelay);
        if (playWithRobot){
            startBackgroundSound();
        }
    }

    @Override
    public void goMainMenu() {
        stopHandlers();
        finish();
        startActivity(new Intent(FishGameActivity.this, SelectGameActivity.class));
    }

    @Override
    public void restartGame() {
        playerNumber = 0;
        isNext = true;

        runOnUiThread(this::resetTheGame);

        // empty all player score
        for (int i = 0; i < PlayerSelectionActivity.playerList.size(); i++) {
            PlayerSelectionActivity.playerList.get(i).setPlayerScore(i);
        }

        // if robot game is start with human or game is without robot
        if (!playWithRobot) {
            runOnUiThread(() -> showDialogFinish(PlayerSelectionActivity.playerList.get(playerNumber).isPlayerRobot(), PlayerSelectionActivity.playerList.get(playerNumber).getPlayerName()));
        }
        else {
            startTheGame(PlayerSelectionActivity.playerList.get(playerNumber).isPlayerRobot(), PlayerSelectionActivity.playerList.get(playerNumber).getPlayerName());
        }
        resetPlayersFiled();
    }

}