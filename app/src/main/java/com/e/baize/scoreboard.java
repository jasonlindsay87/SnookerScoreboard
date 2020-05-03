package com.e.baize;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;


public class scoreboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        String mP1name = intent.getStringExtra(HomeActivity.PLAYER_ONE_NAME);
        String mP2name = intent.getStringExtra(HomeActivity.PLAYER_TWO_NAME);

        // Capture the layout's TextView and set the string as its text
        TextView tP1Name = findViewById(R.id.p1name);
        tP1Name.setText(mP1name);

        TextView tP2Name = findViewById(R.id.p2name);
        tP2Name.setText(mP2name);
    }
}
