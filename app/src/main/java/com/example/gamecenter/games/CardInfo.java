package com.example.gamecenter.games;

public class CardInfo {
    private int ID;
    private int pair_number;
    private int image;
    private boolean isComplete = false;

    public CardInfo() {
    }

    public CardInfo(int ID, int pair_number, int image) {
        this.ID = ID;
        this.pair_number = pair_number;
        this.image = image;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPair_number() {
        return pair_number;
    }

    public void setPair_number(int pair_number) {
        this.pair_number = pair_number;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public boolean isComplete() {
        return isComplete;
    }

    public void setComplete(boolean complete) {
        isComplete = complete;
    }

    @Override
    public String toString() {
        return "CardInfo{" +
                "ID=" + ID +
                ", pair_number=" + pair_number +
                ", image=" + image +
                '}';
    }
}
