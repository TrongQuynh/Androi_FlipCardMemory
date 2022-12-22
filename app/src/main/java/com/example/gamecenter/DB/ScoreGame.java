package com.example.gamecenter.DB;

public class ScoreGame {
    private int ID;
    private int gameID;
    private String gameMode;
    private int time;
    private int tries;

    public ScoreGame() {
    }

    public ScoreGame(int gameID, String gameMode, int time, int tries) {
        this.gameID = gameID;
        this.gameMode = gameMode;
        this.time = time;
        this.tries = tries;
    }

    public int getGameID() {
        return gameID;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void setGameID(int gameID) {
        this.gameID = gameID;
    }

    public String getGameMode() {
        return gameMode;
    }

    public void setGameMode(String gameMode) {
        this.gameMode = gameMode;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getTries() {
        return tries;
    }

    public void setTries(int tries) {
        this.tries = tries;
    }
}
