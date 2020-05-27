package com.e.baize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Context;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardActivity extends AppCompatActivity {
    int selectedPlayer = 0;
    List<Integer> p1ScoreHist = new ArrayList();
    List<Integer> p2ScoreHist = new ArrayList();

    @Override
 protected void onCreate(Bundle savedInstanceState) {
     super.onCreate(savedInstanceState);
     setContentView(R.layout.activity_scoreboard);
     final TextView tP1Name = findViewById(R.id.p1name);
     final TextView tP2Name = findViewById(R.id.p2name);
     final ImageView imRed = findViewById(R.id.redBall);
     final ImageView imFoul = findViewById(R.id.foulBall);
     final ImageView imYellow = findViewById(R.id.yellowBall);
     final ImageView imGreen = findViewById(R.id.greenBall);
     final ImageView imBrown = findViewById(R.id.brownBall);
     final ImageView imBlue = findViewById(R.id.blueBall);
     final ImageView imPink = findViewById(R.id.pinkBall);
     final ImageView imBlack = findViewById(R.id.blackBall);
     Toolbar scoreboardBar = findViewById(R.id.scoreboardToolbar);
     setSupportActionBar(scoreboardBar);
     Bundle extras = getIntent().getExtras();
     String sP1Name = extras.getString("sP1name");
     String sP2Name = extras.getString("sP2name");

     tP1Name.setText(sP1Name);
     tP2Name.setText(sP2Name);

     tP1Name.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             selectedPlayer = 1;
             tP1Name.setBackgroundColor(getResources().getColor(R.color.colorWhite));
             tP1Name.setTextColor(getResources().getColor(R.color.colorBlack));
             tP2Name.setBackgroundColor(0x00000000);
             tP2Name.setTextColor(getResources().getColor(R.color.colorWhite));
         }
     });

     tP2Name.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             selectedPlayer = 2;
             tP2Name.setBackgroundColor(getResources().getColor(R.color.colorWhite));
             tP2Name.setTextColor(getResources().getColor(R.color.colorBlack));
             tP1Name.setBackgroundColor(0x00000000);
             tP1Name.setTextColor(getResources().getColor(R.color.colorWhite));
         }
     });

     imFoul.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ballPotted(selectedPlayer, 0);
            }
        });
     imRed.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             ballPotted(selectedPlayer, 1);
         }
     });
     imYellow.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             ballPotted(selectedPlayer, 2);
         }
     });
     imGreen.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             ballPotted(selectedPlayer, 3);
         }
     });
     imBrown.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             ballPotted(selectedPlayer, 4);
         }
     });
     imBlue.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             ballPotted(selectedPlayer, 5);
         }
     });
     imPink.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             ballPotted(selectedPlayer, 6);
         }
     });
     imBlack.setOnClickListener(new View.OnClickListener() {
         @Override
         public void onClick(View v) {
             ballPotted(selectedPlayer, 7);
         }
     });
 }
    public void noPlayerSelected (View v) {
        Vibrator vibe = (Vibrator) ScoreboardActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(200);
        Toast.makeText(getApplicationContext(),"Select a player to update the score.", Toast.LENGTH_SHORT).show();
    }

    public void ballPotted(int player, int score){
        if (selectedPlayer == 0) {
            noPlayerSelected (null);
        }
        else if (selectedPlayer == 1) {
            p1ScoreHist.add(score);
            updateP1Score(null);
        }
        else if (selectedPlayer == 2) {
            p2ScoreHist.add(score);
            updateP2Score(null);
        }
    }

    public void updateP1Score(View v){
        int iP1TotalScore = 0;
        for (int i : p1ScoreHist){
            iP1TotalScore += i;
        }
        TextView tTotalP1Score = findViewById(R.id.p1Score);
        tTotalP1Score.setText(Integer.toString(iP1TotalScore));
    }
    public void updateP2Score(View v){
        int iP2TotalScore = 0;
        for (int i : p2ScoreHist){
            iP2TotalScore += i;
        }
        TextView tTotalP1Score = findViewById(R.id.p2Score);
        tTotalP1Score.setText(Integer.toString(iP2TotalScore));
    }

    public void undoP1(View v) {
        if (p1ScoreHist.size() > 0) {
            p1ScoreHist.remove(p1ScoreHist.size() - 1);
        }
        updateP1Score(null);
    }

    public void undoP2(View v) {
        if (p2ScoreHist.size() > 0) {
            p2ScoreHist.remove(p2ScoreHist.size() - 1);
        }
        updateP2Score(null);
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

        case R.id.exit:
            this.finishAffinity();
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }
}
