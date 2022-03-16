package com.e.baize;


import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

public class Database extends SQLiteOpenHelper {
    public static final String GAME_TABLE_NAME = "Games";
    public static final String GAME_ID_COLUMN = "GameID";
    public static final String GAME_PLAYER1_COLUMN = "Player1ID";
    public static final String GAME_PLAYER2_COLUMN = "Player2ID";
    public static final String GAME_PLAYER_WINNER_COLUMN = "PlayerWinnerID";
    public static final String GAME_CREATED_DATE_COLUMN = "CreatedDate";
    public static final String GAME_TIMER_COLUMN = "GameTime";

    public static final String PLAYER_TABLE_NAME = "Players";
    public static final String PLAYER_ID_COLUMN = "PlayerID";
    public static final String PLAYER_NAME_COLUMN = "PlayerName";

    public static final String FRAME_TABLE_NAME = "Frames";
    public static final String FRAME_ID_COLUMN = "FrameID";
    public static final String FRAME_GAME_ID_COLUMN = "GameID";
    public static final String FRAME_NUMBER_COLUMN = "FrameNumber";
    public static final String FRAME_WINNER_PLAYER_ID_COLUMN = "FrameWinnerPlayerID";
    public static final String FRAME_P1SCORE_COLUMN = "PlScore";
    public static final String FRAME_P2SCORE_COLUMN = "P2Score";
    public static final String FRAME_TIMER_COLUMN = "FrameTime";

    public static final String SHOT_TABLE_NAME = "Shots";
    public static final String SHOT_ID_COLUMN = "ShotID";
    public static final String SHOT_FRAME_ID_COLUMN = "FrameID";
    public static final String SHOT_PLAYER_ID_COLUMN = "PlayerID";
    public static final String SHOT_SCORE_COLUMN = "Score";

    public static final String OPTION_TABLE_NAME = "Options";
    public static final String OPTION_ID_COLUMN = "OptionID";
    public static final String OPTION_DESC_COLUMN = "Description";
    public static final String OPTION_SWITCH_COLUMN = "Switch";
    public static final String OPTION_VALUE_COLUMN = "Value";

    //database info
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "SnookerScoreboard.db";


    Context context;

    //initialize the database

    public Database(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        this.context = context;
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + PLAYER_TABLE_NAME + "(" + PLAYER_ID_COLUMN + " INTEGER PRIMARY KEY AUTOINCREMENT," + PLAYER_NAME_COLUMN + " TEXT )";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + GAME_TABLE_NAME + "(" + GAME_ID_COLUMN +" INTEGER PRIMARY KEY AUTOINCREMENT, " + GAME_PLAYER1_COLUMN + " INTEGER, " + GAME_PLAYER2_COLUMN + " INTEGER, " + GAME_PLAYER_WINNER_COLUMN + " INTEGER, " + GAME_CREATED_DATE_COLUMN + " DATE,"  + GAME_TIMER_COLUMN + " INTEGER )";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + FRAME_TABLE_NAME + "(" + FRAME_ID_COLUMN +" INTEGER PRIMARY KEY AUTOINCREMENT, " + FRAME_GAME_ID_COLUMN + " INTEGER, " + FRAME_NUMBER_COLUMN + " INTEGER, "  + FRAME_P1SCORE_COLUMN + " INTEGER, " + FRAME_P2SCORE_COLUMN + " INTEGER, " + FRAME_WINNER_PLAYER_ID_COLUMN + " INTEGER,"  + FRAME_TIMER_COLUMN +  " INTEGER )";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + SHOT_TABLE_NAME + "(" + SHOT_ID_COLUMN +" INTEGER PRIMARY KEY AUTOINCREMENT, " + SHOT_FRAME_ID_COLUMN + " INTEGER, " + SHOT_PLAYER_ID_COLUMN + " INTEGER, "  + SHOT_SCORE_COLUMN + " REAL )";
        db.execSQL(CREATE_TABLE);

        CREATE_TABLE = "CREATE TABLE " + OPTION_TABLE_NAME + "(" + OPTION_ID_COLUMN +" INTEGER PRIMARY KEY AUTOINCREMENT, " + OPTION_DESC_COLUMN + " TEXT, " + OPTION_SWITCH_COLUMN + " INTEGER, "  + OPTION_VALUE_COLUMN + " TEXT )";
        db.execSQL(CREATE_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {}


}
