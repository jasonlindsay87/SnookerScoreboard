package com.e.baize;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ScoreboardActivity extends AppCompatActivity {
    final Player mPlayerOne = new Player();
    final Player mPlayerTwo = new Player();
    public int iFrameCount;
    public Table mTable;
    public StatsItems mMatchStats;
    private ArrayList<FoulItems> mFoulList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);
        Bundle extras = getIntent().getExtras();
        assert extras != null;
        if (extras.getString("sP1name") != null) {
            mPlayerOne.Name = extras.getString("sP1name");
        }
        if (extras.getString("sP2name") != null) {
            mPlayerTwo.Name = extras.getString("sP2name");
        }
        final TextView tP1Name = findViewById(R.id.p1name);
        final TextView tP2Name = findViewById(R.id.p2name);
        final ImageView imRed = findViewById(R.id.redBall);
        final ImageView imYellow = findViewById(R.id.yellowBall);
        final ImageView imGreen = findViewById(R.id.greenBall);
        final ImageView imBrown = findViewById(R.id.brownBall);
        final ImageView imBlue = findViewById(R.id.blueBall);
        final ImageView imPink = findViewById(R.id.pinkBall);
        final ImageView imBlack = findViewById(R.id.blackBall);

        mMatchStats = new StatsItems();

        Toolbar scoreboardBar = findViewById(R.id.scoreboardToolbar);
        setSupportActionBar(scoreboardBar);

        tP1Name.setText(mPlayerOne.Name);
        tP2Name.setText(mPlayerTwo.Name);
        initFoulList();
        iFrameCount = 1;

        mTable = new Table();
        mTable.setNextBreak(mPlayerTwo);
        setPlayerOneActive();

        final Spinner spinnerFouls = findViewById(R.id.spinFoul);
        FoulAdapter mFoulAdapter = new FoulAdapter(this, mFoulList);
        spinnerFouls.setAdapter(mFoulAdapter);
        spinnerFouls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FoulItems clickedItem = (FoulItems) parent.getItemAtPosition(position);
                float foulPoints = clickedItem.getFoulPoints();
                if (foulPoints != 0) {
                    if (mTable.getActivePlayer() == mPlayerOne) {
                        mPlayerTwo.setScore(foulPoints);
                        foulPoints = Math.round(foulPoints);
                        updateP2Score(null);
                        displayPopup(String.format("%.0f",foulPoints) +" foul points awarded to\n" + mPlayerTwo.Name);
                        //Toast.makeText(getApplicationContext(), foulPoints + " foul points awarded to " + mPlayerTwo.Name, Toast.LENGTH_SHORT).show();
                    }
                    if (mTable.getActivePlayer() == mPlayerTwo) {
                        mPlayerOne.setScore(foulPoints);
                        foulPoints = Math.round(foulPoints);
                        updateP1Score(null);
                        displayPopup(String.format("%.0f",foulPoints) +" foul points awarded to\n " + mPlayerOne.Name);
                        //Toast.makeText(getApplicationContext(), foulPoints + " foul points awarded to " + mPlayerOne.Name, Toast.LENGTH_SHORT).show();
                    }
                }
                spinnerFouls.setSelection(0);

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        tP1Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayerOneActive();
            }
        });
        tP2Name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setPlayerTwoActive();
            }
        });
        imRed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBall(v);
                ballPotted(1);
            }
        });
        imYellow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBall(v);
                ballPotted(2);
            }
        });
        imGreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBall(v);
                ballPotted(3);
            }
        });
        imBrown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBall(v);
                ballPotted(4);
            }
        });
        imBlue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBall(v);
                ballPotted(5);
            }
        });
        imPink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBall(v);
                ballPotted(6);
            }
        });
        imBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateBall(v);
                ballPotted(7);
            }
        });
    }

    public void animateBall(View v) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.ball_anim);
        anim.reset();
        v.clearAnimation();
        v.startAnimation(anim);
    }

    public void setPlayerOneActive() {
        final TextView tP1Name = findViewById(R.id.p1name);
        final TextView tP2Name = findViewById(R.id.p2name);
        mTable.setActivePlayer(mPlayerOne);
        tP1Name.setBackground(getResources().getDrawable(R.drawable.p1selected));
        tP1Name.setTextColor(getResources().getColor(R.color.colorBlack));
        tP2Name.setBackgroundColor(0x00000000);
        tP2Name.setTextColor(getResources().getColor(R.color.colorDark));
    }

    public void setPlayerTwoActive() {
        final TextView tP1Name = findViewById(R.id.p1name);
        final TextView tP2Name = findViewById(R.id.p2name);
        mTable.setActivePlayer(mPlayerTwo);
        tP2Name.setBackground(getResources().getDrawable(R.drawable.p2selected));
        tP2Name.setTextColor(getResources().getColor(R.color.colorBlack));
        tP1Name.setBackgroundColor(0x00000000);
        tP1Name.setTextColor(getResources().getColor(R.color.colorDark));
    }

    private void initFoulList() {
        mFoulList = new ArrayList<>();
        mFoulList.add(new FoulItems(0, R.drawable._foul));
        mFoulList.add(new FoulItems((float) 4.0001, R.drawable._foul4));
        mFoulList.add(new FoulItems((float)5.0001, R.drawable._foul5));
        mFoulList.add(new FoulItems((float)6.0001, R.drawable._foul6));
        mFoulList.add(new FoulItems((float)7.0001, R.drawable._foul7));
    }

    public void ballPotted(int score) {
        if (mTable.getActivePlayer() == mPlayerOne) {
            mPlayerOne.setScore(score);
            updateP1Score(null);
        } else if (mTable.getActivePlayer() == mPlayerTwo) {
            mPlayerTwo.setScore(score);
            updateP2Score(null);
        }
    }

    public void updateP1Score(View v) {
        TextView tTotalP1Score = findViewById(R.id.p1Score);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.score_anim);
        anim.reset();
        tTotalP1Score.setText(Integer.toString(mPlayerOne.getScore()));
        tTotalP1Score.clearAnimation();
        tTotalP1Score.startAnimation(anim);
    }

    public void updateP2Score(View v) {
        TextView tTotalP2Score = findViewById(R.id.p2Score);
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.score_anim);
        anim.reset();
        tTotalP2Score.setText(Integer.toString(mPlayerTwo.getScore()));
        tTotalP2Score.clearAnimation();
        tTotalP2Score.startAnimation(anim);
    }

    public void undoP1(View v) {
        mPlayerOne.undoScore();
        mPlayerOne.getScore();
        updateP1Score(null);
    }

    public void undoP2(View v) {
        mPlayerTwo.undoScore();
        mPlayerTwo.getScore();
        updateP2Score(null);
    }

    public void updateFrames() {
        String frameWinner = "";
        TextView tP1Frames = findViewById(R.id.p1Frames);
        TextView tP2Frames = findViewById(R.id.p2Frames);
        if (mPlayerOne.getScore() > mPlayerTwo.getScore()) {
            mPlayerOne.frameWin();
            frameWinner = mPlayerOne.Name;
        } else {
            mPlayerTwo.frameWin();
            frameWinner = mPlayerTwo.Name;
        }

        if (mTable.getNextBreak() == mPlayerOne) {
            setPlayerOneActive();
            mTable.setNextBreak(mPlayerTwo);
        } else if (mTable.getNextBreak() == mPlayerTwo) {
            setPlayerTwoActive();
            mTable.setNextBreak(mPlayerOne);
        }
        mMatchStats.saveFrames(mPlayerOne, mPlayerTwo);
        mPlayerOne.clearScore();
        mPlayerTwo.clearScore();
        updateP1Score(null);
        updateP2Score(null);
        iFrameCount++;
        displayPopup(frameWinner + " wins the frame!\n\n- Frame " + iFrameCount + " -\n" + mTable.getActivePlayer().Name+ " to break");
        //Toast.makeText(getApplicationContext(), "Frame " + iFrameCount + ". " + mTable.getActivePlayer().Name + " to break", Toast.LENGTH_LONG).show();
        tP1Frames.setText(Integer.toString(mPlayerOne.getFrameCount()));
        tP2Frames.setText(Integer.toString(mPlayerTwo.getFrameCount()));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scoreboard_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.endFrame:
                newFrame(null);
                break;
            case R.id.rules:
                showRules();
                break;
            case R.id.stats:
                showStats();
                break;
            case R.id.endMatch:
                endMatch();
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
    public void newFrame(View view) {
        if (mPlayerOne.getScore() != mPlayerTwo.getScore()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
            builder.setMessage("Start a new frame?")
                    .setTitle("End Frame")
                    .setCancelable(false)
                    .setPositiveButton
                            (
                                    "Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            updateFrames();
                                        }
                                    }
                            )
                    .setNegativeButton
                            ("No", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.dismiss();
                                        }
                                    }
                            );
            AlertDialog dialog = builder.create();
            dialog.show();
        } else {
            Vibrator vibe = (Vibrator) ScoreboardActivity.this.getSystemService(Context.VIBRATOR_SERVICE);
            vibe.vibrate(200);
            Toast.makeText(getApplicationContext(), "Scores are level! Re-spot the black?", Toast.LENGTH_LONG).show();
        }
    }

    protected void showRules() {
        Intent rulesIntent = new Intent(ScoreboardActivity.this, RulesActivity.class);
        startActivity(rulesIntent);
    }

    @Override
    public void onBackPressed() {
        endMatch();
    }

    public void endMatch() {
        AlertDialog.Builder altBuilder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        altBuilder.setMessage("Are you sure you want to go back? Current match data will be lost.")
                .setTitle("End Match")
                .setCancelable(false)
                .setPositiveButton
                        (
                                "Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent backIntent = new Intent(ScoreboardActivity.this, HomeActivity.class);
                                        startActivity(backIntent);
                                    }
                                }
                        )
                .setNegativeButton
                        ("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                }
                        );
        AlertDialog dialog = altBuilder.create();
        dialog.show();
    }

    public void showStats() {
        Intent statsIntent = new Intent(ScoreboardActivity.this, StatsActivity.class);
        statsIntent.putExtra("mMatchStats", mMatchStats);
        statsIntent.putExtra("mPlayerOne", mPlayerOne);
        statsIntent.putExtra("mPlayerTwo", mPlayerTwo);
        startActivity(statsIntent);
    }

    public void displayPopup(String message) {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext()
                .getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);
        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tvpopUP = (TextView) popupView.findViewById(R.id.popupMessage);
        tvpopUP.setText(message);
        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        new Handler().postDelayed(new Runnable(){
            public void run() {
                popupWindow.dismiss();
            }
        }, 5000);
    }
}