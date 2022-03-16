package com.e.baize;

import static com.e.baize.Database.FRAME_GAME_ID_COLUMN;
import static com.e.baize.Database.FRAME_ID_COLUMN;
import static com.e.baize.Database.FRAME_NUMBER_COLUMN;
import static com.e.baize.Database.FRAME_P1SCORE_COLUMN;
import static com.e.baize.Database.FRAME_P2SCORE_COLUMN;
import static com.e.baize.Database.FRAME_TABLE_NAME;
import static com.e.baize.Database.FRAME_WINNER_PLAYER_ID_COLUMN;
import static com.e.baize.Database.GAME_CREATED_DATE_COLUMN;
import static com.e.baize.Database.GAME_ID_COLUMN;
import static com.e.baize.Database.GAME_PLAYER1_COLUMN;
import static com.e.baize.Database.GAME_PLAYER2_COLUMN;
import static com.e.baize.Database.GAME_TABLE_NAME;
import static com.e.baize.Database.GAME_TIMER_COLUMN;
import static com.e.baize.Database.PLAYER_ID_COLUMN;
import static com.e.baize.Database.PLAYER_TABLE_NAME;
import static com.e.baize.Database.SHOT_FRAME_ID_COLUMN;
import static com.e.baize.Database.SHOT_ID_COLUMN;
import static com.e.baize.Database.SHOT_PLAYER_ID_COLUMN;
import static com.e.baize.Database.SHOT_SCORE_COLUMN;
import static com.e.baize.Database.SHOT_TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Serializable;
import java.util.ArrayList;

public class Game extends ScoreboardActivity implements Serializable {
    private static Database dbHandler = new Database(SnookerScoreboard.getAppContext(), null, null, 1);
    public Integer gameID;
    public Player playerOne;
    public Player playerTwo;
    public ArrayList<Frame> frames;
    public Player nextBreak;
    public Player activePlayer;

    public Game() {
        playerOne = new Player();
        playerTwo = new Player();
        frames = new ArrayList<>();
    }

    public Game(Player playerOne, Player playerTwo, ArrayList<Frame> frames) {
        this.playerOne = playerOne;
        this.playerTwo = playerTwo;
        this.frames = frames;
    }

    public Player getActivePlayer() {
        return activePlayer;
    }

    public void setActivePlayer(Player activePlayer){
        if (activePlayer != null) {
            this.activePlayer = activePlayer;
        }
    }

    public static long addGame(Game game) {
        ContentValues values = new ContentValues();
        values.put(GAME_PLAYER1_COLUMN, game.playerOne.playerID);
        values.put(GAME_PLAYER2_COLUMN, game.playerTwo.playerID);
        values.put(GAME_CREATED_DATE_COLUMN, android.text.format.DateFormat.format("dd-MM-yyyy HH:mm", new java.util.Date()).toString());
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.insert(GAME_TABLE_NAME, null, values);
    }

    public static Game getGame(int gameID) {
        Game game = new Game();

        String query = "SELECT * " +
                " FROM " + GAME_TABLE_NAME +
                " Where " + GAME_ID_COLUMN + " = '" + gameID + "'";

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            game.gameID = cursor.getInt(cursor.getColumnIndex(GAME_ID_COLUMN));
            game.playerOne.playerID = cursor.getInt(cursor.getColumnIndex(GAME_PLAYER1_COLUMN));
            game.playerTwo.playerID = cursor.getInt(cursor.getColumnIndex(GAME_PLAYER2_COLUMN));
            game.frames = new ArrayList<Frame>();
        }


        query = "SELECT * " +
                " FROM " + FRAME_TABLE_NAME +
                " Where " + FRAME_GAME_ID_COLUMN + " = '" + gameID + "'";

        cursor = db.rawQuery(query, null);
        if (cursor.moveToFirst()){
            do {
                Frame frame = new Frame();
                frame.frameID = cursor.getInt(cursor.getColumnIndex(FRAME_ID_COLUMN));
                frame.gameID = cursor.getInt(cursor.getColumnIndex(FRAME_GAME_ID_COLUMN));
                frame.frameNumber = cursor.getInt(cursor.getColumnIndex(FRAME_NUMBER_COLUMN));
                frame.p1Score = cursor.getInt(cursor.getColumnIndex(FRAME_P1SCORE_COLUMN));
                frame.p2Score = cursor.getInt(cursor.getColumnIndex(FRAME_P2SCORE_COLUMN));
                frame.frameWinnerPlayerID = cursor.getInt(cursor.getColumnIndex(FRAME_WINNER_PLAYER_ID_COLUMN));
                frame.shots = new ArrayList<Shot>();


                query = "SELECT * " +
                        " FROM " + SHOT_TABLE_NAME +
                        " Where " + SHOT_FRAME_ID_COLUMN + " = '" + frame.frameID + "'";
                Cursor shotCursor = db.rawQuery(query, null);
                if (shotCursor.moveToFirst()){
                    do {
                        Shot shot = new Shot();
                        shot.shotID = shotCursor.getInt(shotCursor.getColumnIndex(SHOT_ID_COLUMN));
                        shot.frameID = shotCursor.getInt(shotCursor.getColumnIndex(SHOT_FRAME_ID_COLUMN));
                        shot.playerID = shotCursor.getInt(shotCursor.getColumnIndex(SHOT_PLAYER_ID_COLUMN));
                        shot.score = shotCursor.getDouble(shotCursor.getColumnIndex(SHOT_SCORE_COLUMN));

                        frame.shots.add(shot);
                    } while(shotCursor.moveToNext());
                }
                shotCursor.close();

                game.frames.add(frame);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return game;
    }

    public static void deleteGame(int gameID){
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        db.delete(SHOT_TABLE_NAME, SHOT_FRAME_ID_COLUMN + " IN (SELECT " + FRAME_ID_COLUMN + " FROM "+ FRAME_TABLE_NAME + " WHERE " + FRAME_GAME_ID_COLUMN + " = ?)", new String[] { String.valueOf(gameID) });
        db.delete(PLAYER_TABLE_NAME, PLAYER_ID_COLUMN + " IN (SELECT " + GAME_PLAYER1_COLUMN + " FROM "+ GAME_TABLE_NAME + " WHERE " + GAME_ID_COLUMN + "= ?)", new String[] { String.valueOf(gameID) });
        db.delete(PLAYER_TABLE_NAME, PLAYER_ID_COLUMN + " IN (SELECT " + GAME_PLAYER2_COLUMN + " FROM "+ GAME_TABLE_NAME + " WHERE " + GAME_ID_COLUMN + "= ?)", new String[] { String.valueOf(gameID) });
        db.delete(GAME_TABLE_NAME, GAME_ID_COLUMN + " = ?", new String[] { String.valueOf(gameID) });
        db.delete(FRAME_TABLE_NAME, FRAME_GAME_ID_COLUMN + " = ?", new String[] { String.valueOf(gameID) });
    }

    public static ArrayList<SavedGame> loadGames(){
        ArrayList<SavedGame> gameList = new ArrayList<>();

        String query = "SELECT g.GameID, p1.PlayerName, p2.PlayerName, g.CreatedDate, " +
                " (SELECT COUNT(*) FROM " + FRAME_TABLE_NAME  + " WHERE " + FRAME_WINNER_PLAYER_ID_COLUMN + " = g.player1ID) as pOneScore," +
                " (SELECT COUNT(*) FROM " + FRAME_TABLE_NAME  + " WHERE " + FRAME_WINNER_PLAYER_ID_COLUMN + " = g.player2ID) as pTwoScore" +
                " FROM " + GAME_TABLE_NAME + " g" +
                " JOIN " + PLAYER_TABLE_NAME + " p1 ON g.Player1ID = p1.PlayerID" +
                " JOIN " + PLAYER_TABLE_NAME + " p2 ON g.Player2ID = p2.PlayerID";
        SQLiteDatabase db = dbHandler.getWritableDatabase();

        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            SavedGame savedGame = new SavedGame(cursor.getInt(cursor.getColumnIndex(GAME_ID_COLUMN)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(cursor.getColumnIndex("pOneScore")),
                    cursor.getInt(cursor.getColumnIndex("pTwoScore")),
                    cursor.getString(cursor.getColumnIndex(GAME_CREATED_DATE_COLUMN)));
            gameList.add(savedGame);
        }
        cursor.close();
        return gameList;
    }

    public static String gameTimer(int gameID){
        Integer time = 0;

        String query = "SELECT " + GAME_TIMER_COLUMN +
                " FROM " + GAME_TABLE_NAME +
                " WHERE " + GAME_ID_COLUMN + " = '" + gameID + "'";

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            time = cursor.getInt((cursor.getColumnIndex(GAME_TIMER_COLUMN)));
        }

        cursor.close();

        if (time == null) { time = 0; }

        time = time + 1;

        ContentValues value = new ContentValues();
        value.put(GAME_TIMER_COLUMN, time);
        db.update(GAME_TABLE_NAME, value, GAME_ID_COLUMN + " = ?", new String[]{ Integer.toString(gameID)});

        //int secs = gameSecs % 60;
        String mins = Integer.toString((time / 60) % 60);
        if (mins.length() == 1) mins = "0" + mins;
        String hrs = Integer.toString((time / 60) / 60);
        if (hrs.length() == 1) hrs = "0" + hrs;
        String gameTime = hrs + ":" + mins;

        return gameTime;
    }
}
