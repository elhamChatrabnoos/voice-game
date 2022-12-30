package com.android.prj.voicegame.fish_game.models;

import android.os.Parcel;
import android.os.Parcelable;

import com.android.prj.voicegame.public_classes.model.Player;

public class FishPlayer extends Player implements Parcelable {
    private int confusedGif;

    protected FishPlayer(Parcel in) {
        confusedGif = in.readInt();
    }

    public static final Creator<FishPlayer> CREATOR = new Creator<FishPlayer>() {
        @Override
        public FishPlayer createFromParcel(Parcel in) {
            return new FishPlayer(in);
        }

        @Override
        public FishPlayer[] newArray(int size) {
            return new FishPlayer[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(confusedGif);
    }
}
