package org.example;

public class PlayerSelection {
    private int player1Type = 1;
    private int player2Type = 2;

    public void setPlayer1Type(int type) {
        this.player1Type = type;
    }

    public void setPlayer2Type(int type) {
        this.player2Type = type;
    }

    public int getPlayer1Type() {
        return this.player1Type;
    }

    public int getPlayer2Type() {
        return this.player2Type;
    }
}
