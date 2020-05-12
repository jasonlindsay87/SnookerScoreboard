package com.e.baize;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }

    public void sendMessage(View view) {
        Intent Intent = new Intent(HomeActivity.this, ScoreboardActivity.class);
        EditText eP1Name = findViewById(R.id.p1name);
        EditText eP2Name = findViewById(R.id.p2name);
        String sP1Name = "";
        String sP2Name = "";

        if (eP1Name.getText().toString().trim().length() < 1) {
            sP1Name = "Player One";
        }
        else {
            sP1Name = eP1Name.getText().toString();
        }
        if (eP2Name.getText().toString().trim().length() < 1) {
            sP2Name = "Player Two";
        }
        else {
            sP2Name = eP2Name.getText().toString();
        }

        Intent.putExtra("sP1name", sP1Name);
        Intent.putExtra("sP2name", sP2Name);
        startActivity(Intent);
    }
}
