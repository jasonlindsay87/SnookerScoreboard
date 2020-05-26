package com.e.baize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardActivity extends AppCompatActivity {
    int score = 0;
    List<Integer> scoreHist = new ArrayList();
    TextView tP1Name = findViewById(R.id.p1name);
    TextView tP2Name = findViewById(R.id.p2name);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        Toolbar scoreboardBar = (Toolbar) findViewById(R.id.scoreboardToolbar);
        setSupportActionBar(scoreboardBar);

        Bundle extras = getIntent().getExtras();
        String sP1Name = extras.getString("sP1name");
        String sP2Name = extras.getString("sP2name");

        tP1Name.setText(sP1Name);
        tP2Name.setText(sP2Name);
    }

    public void redPotted(View v){
        scoreHist.add(1);
        updateScore(null);
    }
    public void yellowPotted(View v){
        scoreHist.add(2);
        updateScore(null);
    }
    public void greenPotted(View v){
        scoreHist.add(3);
        updateScore(null);
    }
    public void brownPotted(View v){
        scoreHist.add(4);
        updateScore(null);
    }
    public void bluePotted(View v){
        scoreHist.add(5);
        updateScore(null);
    }
    public void pinkPotted(View v){
        scoreHist.add(6);
        updateScore(null);
    }
    public void blackPotted(View v){
        scoreHist.add(7);
        updateScore(null);
    }

    public void updateScore(View v){
        int iTotalScore = 0;
        for (int i : scoreHist){
            iTotalScore += i;
        }

        TextView tTotalScore = findViewById(R.id.p1Score);
        tTotalScore.setText(Integer.toString(iTotalScore));
    }

    public void undoLastBall(View v) {
        if (scoreHist.size() > 0) {
            scoreHist.remove(scoreHist.size() - 1);
        }
        updateScore(null);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scoreboard_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.add:
            return(true);
        case R.id.about:
            //add the function to perform here
            return(true);
        case R.id.exit:
            this.finishAffinity();
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

}
