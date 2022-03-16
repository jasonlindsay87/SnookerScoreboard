package com.e.baize;

import static com.e.baize.Database.FRAME_GAME_ID_COLUMN;
import static com.e.baize.Database.FRAME_NUMBER_COLUMN;
import static com.e.baize.Database.FRAME_TABLE_NAME;
import static com.e.baize.Database.FRAME_WINNER_PLAYER_ID_COLUMN;
import static com.e.baize.Database.GAME_ID_COLUMN;
import static com.e.baize.Database.GAME_PLAYER1_COLUMN;
import static com.e.baize.Database.GAME_PLAYER2_COLUMN;
import static com.e.baize.Database.GAME_TABLE_NAME;
import static com.e.baize.Database.PLAYER_ID_COLUMN;
import static com.e.baize.Database.PLAYER_NAME_COLUMN;
import static com.e.baize.Database.PLAYER_TABLE_NAME;
import static com.e.baize.Database.SHOT_FRAME_ID_COLUMN;
import static com.e.baize.Database.SHOT_ID_COLUMN;
import static com.e.baize.Database.SHOT_PLAYER_ID_COLUMN;
import static com.e.baize.Database.SHOT_SCORE_COLUMN;
import static com.e.baize.Database.SHOT_TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player extends AppCompatActivity implements Serializable {
    private static Database dbHandler = new Database(SnookerScoreboard.getAppContext(), null, null, 1);
    public int playerID;
    public String playerName;
    public int framesWonCount;
    public List<Double> ScoreHistory = new ArrayList<>();

    public Player() {
    }

    public Player(int playerID, String playerName) {
        this.playerID = playerID;
        this.playerName = playerName;
    }

    public String getPlayerName(){ return this.playerName;}

    public static String getPlayerName(int playerID) {
        String name = "";

        String query = "SELECT " + PLAYER_NAME_COLUMN +
                " FROM " + PLAYER_TABLE_NAME +
                " WHERE " + PLAYER_ID_COLUMN + " = '" + playerID + "'";

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            name = cursor.getString(cursor.getColumnIndex(PLAYER_NAME_COLUMN));
        }
        cursor.close();

        return name;
    }

    public void setScore(double score) {
        this.ScoreHistory.add(score);
    }

    public List<Double> getScoreHistory() {
        return this.ScoreHistory;
    }

    public int getFrameScore(Frame frame){
        double score = 0;

        String query = "SELECT SUM(" + SHOT_SCORE_COLUMN + ") " +
                " FROM " + SHOT_TABLE_NAME +
                " WHERE " + PLAYER_ID_COLUMN + " = '" + this.playerID + "'" +
                " AND " + SHOT_FRAME_ID_COLUMN + " = " + frame.frameID ;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            score = cursor.getInt(0);
        }
        cursor.close();

        return (int) score;
    }

    public int getFramesWonCount(Game game){
        int count = 0;

        String query = "SELECT Count(*) " +
                " FROM " + FRAME_TABLE_NAME +
                " WHERE " + FRAME_GAME_ID_COLUMN + " = '" + game.gameID + "'" +
                " AND " + FRAME_WINNER_PLAYER_ID_COLUMN + " = '" + this.playerID +"'" ;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()) {
            count = cursor.getInt(0);
        }
        cursor.close();

        return count;
    }

    public void undoLastShot(Frame frame) {
        int id = 0;

        String query = "SELECT MAX(" + SHOT_ID_COLUMN + ") FROM " + SHOT_TABLE_NAME +
                        " WHERE " + SHOT_FRAME_ID_COLUMN + " = " + frame.frameID +
                        " AND " + SHOT_PLAYER_ID_COLUMN + " = " + this.playerID;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            id = cursor.getInt(0);
        }
        cursor.close();

        db.delete(SHOT_TABLE_NAME, SHOT_ID_COLUMN + " = ?", new String[]{Integer.toString(id)});
    }

    public static long addPlayer(Player player) {
        ContentValues values = new ContentValues();
        values.put(PLAYER_NAME_COLUMN, player.getPlayerName());
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.insert(PLAYER_TABLE_NAME, null, values);
    }

    public static Player getPlayer(int playerID) {
        Player player = new Player();
        String query = "SELECT * " +
                " FROM " + PLAYER_TABLE_NAME +
                " WHERE " + PLAYER_ID_COLUMN + " = '" + playerID + "'";

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            player.playerID = cursor.getInt(cursor.getColumnIndex(PLAYER_ID_COLUMN));
            player.playerName = cursor.getString(cursor.getColumnIndex(PLAYER_NAME_COLUMN));
        }
        cursor.close();

        return player;
    }
}

