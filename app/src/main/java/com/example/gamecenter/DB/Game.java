package com.example.gamecenter.DB;

public class Game {
    private int ID;
    private String gameName;

    public Game() {
    }

    public Game(String gameName) {
        this.gameName = gameName;
    }

    public Game(int ID, String gameName) {
        this.ID = ID;
        this.gameName = gameName;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
