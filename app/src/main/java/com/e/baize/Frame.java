package com.e.baize;

import static com.e.baize.Database.FRAME_GAME_ID_COLUMN;
import static com.e.baize.Database.FRAME_ID_COLUMN;
import static com.e.baize.Database.FRAME_NUMBER_COLUMN;
import static com.e.baize.Database.FRAME_P1SCORE_COLUMN;
import static com.e.baize.Database.FRAME_P2SCORE_COLUMN;
import static com.e.baize.Database.FRAME_TABLE_NAME;
import static com.e.baize.Database.FRAME_TIMER_COLUMN;
import static com.e.baize.Database.FRAME_WINNER_PLAYER_ID_COLUMN;
import static com.e.baize.Database.GAME_ID_COLUMN;
import static com.e.baize.Database.GAME_TABLE_NAME;
import static com.e.baize.Database.GAME_TIMER_COLUMN;
import static com.e.baize.Database.SHOT_FRAME_ID_COLUMN;
import static com.e.baize.Database.SHOT_ID_COLUMN;
import static com.e.baize.Database.SHOT_PLAYER_ID_COLUMN;
import static com.e.baize.Database.SHOT_SCORE_COLUMN;
import static com.e.baize.Database.SHOT_TABLE_NAME;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.lang.UProperty;

import java.io.Serializable;
import java.util.ArrayList;

public class Frame implements Serializable {
    public int frameID;
    public int gameID;
    public int frameNumber;
    public int p1Score;
    public int p2Score;
    public ArrayList<Shot> shots;
    public String frameTime;
    public int frameWinnerPlayerID;

    private static Database dbHandler = new Database(SnookerScoreboard.getAppContext(), null, null, 1);

    public Frame() {}

    public Frame(int gameID, int frameNumber, ArrayList<Shot> shots) {
        this.gameID = gameID;
        this.frameNumber = frameNumber;
        this.shots = shots;
    }

    public ArrayList<Shot> getShots(){
        ArrayList<Shot> res = new ArrayList<>();

        String query = "SELECT * " +
                " FROM " + SHOT_TABLE_NAME +
                " WHERE " + SHOT_FRAME_ID_COLUMN + " = '" + this.frameID + "' ORDER BY " + SHOT_ID_COLUMN;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            do{
                Shot shot = new Shot();
                shot.shotID = cursor.getInt(cursor.getColumnIndex(SHOT_ID_COLUMN));
                shot.frameID = cursor.getInt(cursor.getColumnIndex(SHOT_FRAME_ID_COLUMN));
                shot.playerID = cursor.getInt(cursor.getColumnIndex(SHOT_PLAYER_ID_COLUMN));
                shot.score = cursor.getInt(cursor.getColumnIndex(SHOT_SCORE_COLUMN));

                res.add(shot);

            } while (cursor.moveToNext());
        }
        cursor.close();
        return res;

    }

    public static Frame getFrameFromNumber(int gameID, int frameNumber) {

        Frame frame = new Frame();
        String query = "SELECT * " +
                " FROM " + FRAME_TABLE_NAME +
                " Where " + FRAME_GAME_ID_COLUMN + " = '" + gameID + "'" +
                " AND "+ FRAME_NUMBER_COLUMN  + " = " + frameNumber ;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            frame.frameID = cursor.getInt(cursor.getColumnIndex(FRAME_ID_COLUMN));
            frame.gameID = cursor.getInt(cursor.getColumnIndex(FRAME_GAME_ID_COLUMN));
            frame.frameNumber = cursor.getInt(cursor.getColumnIndex(FRAME_NUMBER_COLUMN));
            frame.p1Score = cursor.getInt(cursor.getColumnIndex(FRAME_P1SCORE_COLUMN));
            frame.p2Score = cursor.getInt(cursor.getColumnIndex(FRAME_P2SCORE_COLUMN));
            frame.shots = new ArrayList<Shot>();
            frame.frameTime = frameTimer(cursor.getInt(cursor.getColumnIndex(FRAME_ID_COLUMN)));

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
        }
        cursor.close();

        return frame;
    }

    public static long addFrame(Frame frame) {
        ContentValues values = new ContentValues();
        values.put(FRAME_GAME_ID_COLUMN, frame.gameID);
        values.put(FRAME_NUMBER_COLUMN, frame.frameNumber);
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.insert(FRAME_TABLE_NAME, null, values);
    }

    public static void updateFrame(Frame frame) {
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues args = new ContentValues();
        args.put(FRAME_WINNER_PLAYER_ID_COLUMN, frame.frameWinnerPlayerID);
        db.update(FRAME_TABLE_NAME, args, FRAME_ID_COLUMN + " = ?", new String[]{Integer.toString(frame.frameID)});
    }

    public static Frame getCurrentFrame(int gameID){
        Frame frame = new Frame();

        String query = "SELECT * " +
                " FROM " + FRAME_TABLE_NAME +
                " Where " + FRAME_GAME_ID_COLUMN + " = '" + gameID + "'" +
                " AND "+ FRAME_NUMBER_COLUMN  +
                " = (SELECT MAX(" + FRAME_NUMBER_COLUMN + ") FROM " + FRAME_TABLE_NAME + " WHERE " + FRAME_GAME_ID_COLUMN + " = '" + gameID + "')";

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()){
            frame.frameID = cursor.getInt(cursor.getColumnIndex(FRAME_ID_COLUMN));
            frame.gameID = cursor.getInt(cursor.getColumnIndex(FRAME_GAME_ID_COLUMN));
            frame.frameNumber = cursor.getInt(cursor.getColumnIndex(FRAME_NUMBER_COLUMN));
            frame.p1Score = cursor.getInt(cursor.getColumnIndex(FRAME_P1SCORE_COLUMN));
            frame.p2Score = cursor.getInt(cursor.getColumnIndex(FRAME_P2SCORE_COLUMN));
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
        }
        cursor.close();

        return frame;
    }

    public static ArrayList<Shot> getPlayerShotsInFrame(int frameID, int playerID){
        ArrayList<Shot> res = new ArrayList<>();

        String query = "SELECT *" +
                " FROM " + SHOT_TABLE_NAME +
                " Where " + SHOT_FRAME_ID_COLUMN + " = '" + frameID + "'" +
                " AND " + SHOT_PLAYER_ID_COLUMN + " = '" +  playerID + "'" +
                " ORDER BY " + SHOT_ID_COLUMN;

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            do{
                Shot shot = new Shot();
                shot.shotID = cursor.getInt(cursor.getColumnIndex(SHOT_ID_COLUMN));
                shot.score = cursor.getDouble(cursor.getColumnIndex(SHOT_SCORE_COLUMN));
                res.add(shot);
            } while (cursor.moveToNext());
        }
        cursor.close();

        return res;
    }

    public static int getPlayerFrameWinCount(int gameID, int playerID){
        int res = 0;

        String query = "SELECT COUNT() " +
                " FROM " + FRAME_TABLE_NAME +
                " Where " + FRAME_GAME_ID_COLUMN + " = '" + gameID + "'" +
                " AND " + FRAME_WINNER_PLAYER_ID_COLUMN + " = '" +  playerID + "'";

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if(cursor.moveToFirst()){
            res = cursor.getInt(0);
        }

        cursor.close();

        return res;
    }

    public static String frameTimer(int frameID){
        Integer time = 0;

        String query = "SELECT " + FRAME_TIMER_COLUMN +
                " FROM " + FRAME_TABLE_NAME +
                " WHERE " + FRAME_ID_COLUMN + " = '" + frameID + "'";

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            time = cursor.getInt((cursor.getColumnIndex(FRAME_TIMER_COLUMN)));
        }

        cursor.close();

        if (time == null) { time = 0; }

        time = time + 1;

        ContentValues value = new ContentValues();
        value.put(FRAME_TIMER_COLUMN, time);
        db.update(FRAME_TABLE_NAME, value, FRAME_ID_COLUMN + " = ?", new String[]{ Integer.toString(frameID)});

        //int secs = gameSecs % 60;
        String mins = Integer.toString((time / 60) % 60);
        if (mins.length() == 1) mins = "0" + mins;
        String hrs = Integer.toString((time / 60) / 60);
        if (hrs.length() == 1) hrs = "0" + hrs;
        String frameTime = hrs + ":" + mins;
        return frameTime;
    }
}
