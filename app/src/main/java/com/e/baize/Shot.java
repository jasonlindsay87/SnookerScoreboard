package com.e.baize;

import static com.e.baize.Database.SHOT_FRAME_ID_COLUMN;
import static com.e.baize.Database.SHOT_ID_COLUMN;
import static com.e.baize.Database.SHOT_PLAYER_ID_COLUMN;
import static com.e.baize.Database.SHOT_SCORE_COLUMN;
import static com.e.baize.Database.SHOT_TABLE_NAME;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import java.io.Serializable;

public class Shot implements Serializable {
    private static Database dbHandler = new Database(SnookerScoreboard.getAppContext(), null, null, 1);
    public int shotID;
    public int frameID;
    public int playerID;
    public double score;


    public Shot() {}
    public Shot(int frameID, int playerID, double score) {
        this.frameID = frameID;
        this.playerID = playerID;
        this.score = score;
    }

    public static long addShot(Shot shot) {

        ContentValues values = new ContentValues();
        values.put(SHOT_FRAME_ID_COLUMN, shot.frameID);
        values.put(SHOT_PLAYER_ID_COLUMN, shot.playerID);
        values.put(SHOT_SCORE_COLUMN, shot.score);

        SQLiteDatabase db = dbHandler.getWritableDatabase();
        return db.insert(SHOT_TABLE_NAME, null, values);
    }

}
