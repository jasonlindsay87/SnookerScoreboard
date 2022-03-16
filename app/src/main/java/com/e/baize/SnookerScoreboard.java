package com.e.baize;

import android.app.Application;
import android.content.Context;

public class SnookerScoreboard extends Application {
    private static Context context;

    public void onCreate() {
        super.onCreate();
        SnookerScoreboard.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return SnookerScoreboard.context;
    }
}
