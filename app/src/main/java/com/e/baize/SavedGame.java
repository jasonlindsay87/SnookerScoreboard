package com.e.baize;

public class SavedGame {
    public int GameID;
    public String PlayerOneName;
    public String PlayerTwoName;
    public int playerOneScore;
    public int playerTwoScore;
    public String CreatedDate;

    public SavedGame(int gameID, String playerOne, String playerTwo, int playerOneScore, int playerTwoScore, String createdDate) {
        this.GameID = gameID;
        this.PlayerOneName = playerOne;
        this.PlayerTwoName = playerTwo;
        this.playerOneScore = playerOneScore;
        this.playerTwoScore = playerTwoScore;
        this.CreatedDate = createdDate;
    }
}
