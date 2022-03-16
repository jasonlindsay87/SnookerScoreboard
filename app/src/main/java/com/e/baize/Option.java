package com.e.baize;

import static com.e.baize.Database.OPTION_DESC_COLUMN;
import static com.e.baize.Database.OPTION_SWITCH_COLUMN;
import static com.e.baize.Database.OPTION_TABLE_NAME;
import static com.e.baize.Database.OPTION_VALUE_COLUMN;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class Option {
    private static Database dbHandler = new Database(SnookerScoreboard.getAppContext(), null, null, 1);

    public static void initGameSettings(){
        String query = "SELECT * FROM " + OPTION_TABLE_NAME + " WHERE " + OPTION_DESC_COLUMN + "= 'KeepScreenOn'";
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(OPTION_DESC_COLUMN, "KeepScreenOn");
            values.put(OPTION_SWITCH_COLUMN, 0);
            values.put(OPTION_VALUE_COLUMN, "");
            db.insert(OPTION_TABLE_NAME, null, values);
        }

        query = "SELECT * FROM " + OPTION_TABLE_NAME + " WHERE " + OPTION_DESC_COLUMN + "= 'DefaultPlayerOneName'";
        db = dbHandler.getWritableDatabase();
        cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(OPTION_DESC_COLUMN, "DefaultPlayerOneName");
            values.put(OPTION_SWITCH_COLUMN, 0);
            values.put(OPTION_VALUE_COLUMN, "Player One");
            db.insert(OPTION_TABLE_NAME, null, values);
        }

        query = "SELECT * FROM " + OPTION_TABLE_NAME + " WHERE " + OPTION_DESC_COLUMN + "= 'DefaultPlayerTwoName'";
        db = dbHandler.getWritableDatabase();
        cursor = db.rawQuery(query, null);

        if (!cursor.moveToFirst()) {
            ContentValues values = new ContentValues();
            values.put(OPTION_DESC_COLUMN, "DefaultPlayerTwoName");
            values.put(OPTION_SWITCH_COLUMN, 0);
            values.put(OPTION_VALUE_COLUMN, "Player Two");
            db.insert(OPTION_TABLE_NAME, null, values);
        }
        cursor.close();
    }

    public static Boolean getOptionSwitchState(String optionName){
        Boolean res = false;

        String query = "SELECT " + OPTION_SWITCH_COLUMN+ " FROM " + OPTION_TABLE_NAME + " WHERE " + OPTION_DESC_COLUMN + "='" + optionName  +"'";
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            res = cursor.getInt(cursor.getColumnIndex(OPTION_SWITCH_COLUMN)) == 1;
        }
        cursor.close();

        return res;
    }

    public static String getOptionValue(String optionName){
        String res = "";

        String query = "SELECT " + OPTION_VALUE_COLUMN + " FROM " + OPTION_TABLE_NAME + " WHERE " + OPTION_DESC_COLUMN + " ='" + optionName  +"'";
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            res = cursor.getString(cursor.getColumnIndex(OPTION_VALUE_COLUMN));
        }
        cursor.close();

        return res;
    }

    public static void setOptionSwitchState(Boolean on){
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        ContentValues value = new ContentValues();
        value.put(OPTION_SWITCH_COLUMN, on ? 1 : 0);

        db.update(OPTION_TABLE_NAME, value, OPTION_DESC_COLUMN + "= ?", new String[]{"KeepScreenOn"});

    }

    public static Boolean setDefaultPlayerName(int playerNo, String value){
        Boolean res = false;
        if (!value.isEmpty()) {
            ContentValues newVal = new ContentValues();
            newVal.put(OPTION_VALUE_COLUMN, value);
            String[] optionDesc = new String[]{playerNo == 1 ? SnookerScoreboard.getAppContext().getResources().getString(R.string.options_defaultP1_name)
                                                            : SnookerScoreboard.getAppContext().getResources().getString(R.string.options_defaultP2_name)};

            SQLiteDatabase db = dbHandler.getWritableDatabase();
            try {
                db.update(OPTION_TABLE_NAME, newVal, OPTION_DESC_COLUMN + " = ? ", optionDesc);
                res = true;
            } catch (Exception e) {
                Log.e("Database", e.getMessage(), e);
            }
            db.update(OPTION_TABLE_NAME, newVal, OPTION_DESC_COLUMN + " = ? ", optionDesc);
        }
        return res;
    }

    public static String getDefaultPlayerName(int playerNo){
        String res = "";
        String player ="";
        SQLiteDatabase db = dbHandler.getWritableDatabase();
        if(playerNo == 1) {
            player = SnookerScoreboard.getAppContext().getResources().getString(R.string.options_defaultP1_name);
        } else {
            player = SnookerScoreboard.getAppContext().getResources().getString(R.string.options_defaultP2_name);
        }

        String query = "SELECT " + OPTION_VALUE_COLUMN + " FROM " + OPTION_TABLE_NAME + " WHERE " + OPTION_DESC_COLUMN + " = '" + player +"'";
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()){
            res = cursor.getString(cursor.getColumnIndex(OPTION_VALUE_COLUMN));
        }

        cursor.close();

        return res;
    }
}
