package com.android.prj.voicegame.model;

import android.app.Activity;

public class Game {
    private String gameName;
    private boolean isGameDone;

    public Game(String gameName, boolean isGameDone) {
        this.gameName = gameName;
        this.isGameDone = isGameDone;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }

    public boolean isGameDone() {
        return isGameDone;
    }

    public void setGameDone(boolean gameDone) {
        isGameDone = gameDone;
    }
}
