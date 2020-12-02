package com.e.baize;

public class Table extends ScoreboardActivity {
    Player activePlayer;
    Player nextBreak;

    public Player getActivePlayer() {
        return activePlayer;
    }

    public Player getNextBreak() {
        return nextBreak;
    }

    public void setNextBreak(Player nextBreak) {
        this.nextBreak = nextBreak;
    }

    public void setActivePlayer(Player activePlayer){
        if (activePlayer != null) {
            this.activePlayer = activePlayer;
        }
    }





}
