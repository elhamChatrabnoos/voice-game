package com.android.prj.voicegame.public_classes.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {

    public Player(){
    }

    private int id;
    private String playerName;
    private int playerScore;
    private int playerTotalScore;
    private boolean playerTurn;
    private String playerColor;
    private int playerResultStyle;
    private int playerImage;
    private int playerGif;
    private boolean playerRobot;
    private int playerSecondGif;
    private int playerResultSticker;

    protected Player(Parcel in) {
        id = in.readInt();
        playerName = in.readString();
        playerScore = in.readInt();
        playerTotalScore = in.readInt();
        playerTurn = in.readByte() != 0;
        playerColor = in.readString();
        playerResultStyle = in.readInt();
        playerResultSticker = in.readInt();
        playerImage = in.readInt();
        playerGif = in.readInt();
        playerSecondGif = in.readInt();
        playerRobot = in.readByte() != 0;
    }

    public int getPlayerResultSticker() {
        return playerResultSticker;
    }

    public void setPlayerResultSticker(int playerResultSticker) {
        this.playerResultSticker = playerResultSticker;
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public int getPlayerSecondGif() {
        return playerSecondGif;
    }

    public void setPlayerSecondGif(int playerSecondGif) {
        this.playerSecondGif = playerSecondGif;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isPlayerRobot() {
        return playerRobot;
    }

    public void setPlayerRobot(boolean playerRobot) {
        this.playerRobot = playerRobot;
    }

    public int getPlayerImage() {
        return playerImage;
    }

    public void setPlayerImage(int playerImage) {
        this.playerImage = playerImage;
    }

    public int getPlayerGif() {
        return playerGif;
    }

    public void setPlayerGif(int playerGif) {
        this.playerGif = playerGif;
    }

    public int getPlayerTotalScore() {
        return playerTotalScore;
    }

    public void setPlayerTotalScore(int playerTotalScore) {
        this.playerTotalScore = playerTotalScore;
    }

    public int getPlayerResultStyle() {
        return playerResultStyle;
    }

    public void setPlayerResultStyle(int playerResultStyle) {
        this.playerResultStyle = playerResultStyle;
    }

    public String getPlayerColor() {
        return playerColor;
    }

    public void setPlayerColor(String playerColor) {
        this.playerColor = playerColor;
    }

    public boolean isPlayerTurn() {
        return playerTurn;
    }

    public void setPlayerTurn(boolean playerTurn) {
        this.playerTurn = playerTurn;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(playerName);
        parcel.writeInt(playerScore);
        parcel.writeInt(playerTotalScore);
        parcel.writeByte((byte) (playerTurn ? 1 : 0));
        parcel.writeString(playerColor);
        parcel.writeInt(playerResultStyle);
        parcel.writeInt(playerResultSticker);
        parcel.writeInt(playerImage);
        parcel.writeInt(playerGif);
        parcel.writeInt(playerSecondGif);
        parcel.writeByte((byte) (playerRobot ? 1 : 0));
    }
}
