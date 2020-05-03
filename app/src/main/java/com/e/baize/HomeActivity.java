package com.e.baize;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {
    public static final String PLAYER_ONE_NAME = "com.e.baize.MESSAGE";
    public static final String PLAYER_TWO_NAME = "com.e.baize.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, scoreboard.class);
        EditText p1name = findViewById(R.id.p1name);
        EditText p2name = findViewById(R.id.p2name);
        String mP1name = p1name.getText().toString();
        String mP2name = p2name.getText().toString();
        intent.putExtra(PLAYER_ONE_NAME, mP1name);
        intent.putExtra(PLAYER_TWO_NAME, mP2name);
        startActivity(intent);
    }
}
