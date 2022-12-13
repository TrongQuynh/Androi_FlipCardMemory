package com.example.gamecenter.games;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

public class ModeGame implements Parcelable {
    private String modeName; // Simple, Limited Tries
    private String sizeBoard; // 3x2,4x4
    private String themeName; // number, animal, word

    public ModeGame() {
    }

    public ModeGame(String modeName,String sizeBoard, String theme) {
        this.modeName = modeName;
        this.sizeBoard = sizeBoard;
        this.themeName = theme;
    }


    protected ModeGame(Parcel in) {
        modeName = in.readString();
        sizeBoard = in.readString();
        themeName = in.readString();
    }

    public static final Creator<ModeGame> CREATOR = new Creator<ModeGame>() {
        @Override
        public ModeGame createFromParcel(Parcel in) {
            return new ModeGame(in);
        }

        @Override
        public ModeGame[] newArray(int size) {
            return new ModeGame[size];
        }
    };

    public String getSizeBoard() {
        return sizeBoard;
    }

    public void setSizeBoard(String sizeBoard) {
        this.sizeBoard = sizeBoard;
    }


    public String getThemeName() {
        return themeName;
    }

    public void setThemeName(String themeName) {
        this.themeName = themeName;
    }

    public String getModeName() {
        return modeName;
    }

    public void setModeName(String modeName) {
        this.modeName = modeName;
    }

    //    Custom get - set
    public int getAmountRow(){
        Log.d("DEBUG", "getAmountRow: " +this.getSizeBoard().replaceAll("size_",""));
        int row = Integer.parseInt(this.getSizeBoard().replaceAll("size_","").split("x")[0]);
        return row;
    }

    public int getAmountColumn(){
        int column = Integer.parseInt(this.getSizeBoard().replaceAll("size_","").split("x")[1]);
        return column;
    }

    public int getSizeCard(){

        if(this.getSizeBoard().contains("3x2")){
            return 420;
        }
        else if(this.sizeBoard.contains("4x3")){
            return 330;
        }else if(this.sizeBoard.contains("4x4") || this.sizeBoard.contains("5x4")){
            return 250;
        }
        else if(this.sizeBoard.contains("6x5") || this.sizeBoard.contains("8x5")){
            return 200;
        }
        return 10;
    }

    @Override
    public String toString() {
        return "ModeGame{" +
                "modeName='" + sizeBoard + '\'' +
                ", theme='" + themeName + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(modeName);
        parcel.writeString(sizeBoard);
        parcel.writeString(themeName);
    }
}
