package com.e.baize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class ScoreboardActivity extends AppCompatActivity {
    private Database dbHandler = new Database(this, null, null, 1);

    Game game;
    Timer gameTimer;
    Timer frameTimer;
    Player playerOne;
    Player playerTwo;
    Frame frame;
    StatsItems mMatchStats;
    private ArrayList<FoulItems> mFoulList;
    TextView tP1Name;
    TextView tP2Name;
    TextView tP1Frames;
    TextView tP2Frames;
    TextView tGameTime;
    TextView tFrameTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        Toolbar scoreboardBar = findViewById(R.id.scoreboardToolbar);
        scoreboardBar.setTitle("");
        setSupportActionBar(scoreboardBar);

        tP1Frames = findViewById(R.id.p1Frames);
        tP2Frames = findViewById(R.id.p2Frames);
        tP1Name = findViewById(R.id.p1name);
        tP2Name = findViewById(R.id.p2name);
        tGameTime = findViewById(R.id.gameTime);
        tFrameTime = findViewById(R.id.frameTime);
        final LinearLayout p1Score = findViewById(R.id.p1ScoreContainer);
        final LinearLayout p2Score = findViewById(R.id.p2ScoreContainer);
        final ImageView imRed = findViewById(R.id.redBall);
        final ImageView imYellow = findViewById(R.id.yellowBall);
        final ImageView imGreen = findViewById(R.id.greenBall);
        final ImageView imBrown = findViewById(R.id.brownBall);
        final ImageView imBlue = findViewById(R.id.blueBall);
        final ImageView imPink = findViewById(R.id.pinkBall);
        final ImageView imBlack = findViewById(R.id.blackBall);
        final Spinner spinnerFouls = findViewById(R.id.spinFoul);

        mMatchStats = new StatsItems();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            game = (Game) getIntent().getSerializableExtra("Game");
        }

        assert game != null;
        playerOne = game.playerOne;
        playerTwo = game.playerTwo;

        playerOne.playerName = Player.getPlayerName(playerOne.playerID);
        playerTwo.playerName = Player.getPlayerName(playerTwo.playerID);

        tP1Name.setText(playerOne.playerName);
        tP2Name.setText(playerTwo.playerName);

        frame = new Frame(
                game.gameID,
                1,
                new ArrayList<>()
        );

        if (game.frames.size() > 0) {
            game.nextBreak = playerTwo;
            if (game.frames.size() % 2 == 0) { game.nextBreak = playerOne; }
            frame = game.frames.get(game.frames.size()-1);
            tP1Frames.setText(Integer.toString(Frame.getPlayerFrameWinCount(game.gameID, playerOne.playerID)));
            tP2Frames.setText(Integer.toString(Frame.getPlayerFrameWinCount(game.gameID, playerTwo.playerID)));

            for (Shot shot : Frame.getCurrentFrame(game.gameID).shots) {
                if (shot.playerID == playerOne.playerID) {
                    animateP1Score();
                } else {
                    animateP2Score();
                }
            }
        } else{
            frame.frameID = (int) Frame.addFrame(frame);
            game.frames.add(frame);
            game.nextBreak = playerTwo;
        }

        initFoulList();
        FoulAdapter mFoulAdapter = new FoulAdapter(this, mFoulList);
        spinnerFouls.setAdapter(mFoulAdapter);
        spinnerFouls.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                FoulItems clickedItem = (FoulItems) parent.getItemAtPosition(position);
                float foulPoints = clickedItem.getFoulPoints();
                if (foulPoints != 0) {
                        Shot shot = new Shot();
                        shot.frameID = frame.frameID;
                        shot.score = foulPoints;
                        shot.playerID = game.activePlayer == playerOne ? playerTwo.playerID : playerOne.playerID;
                        Shot.addShot(shot);
                        frame.shots.add(shot);

                        if(game.activePlayer == playerOne){
                            animateP2Score();
                            displayPopup(String.format("%.0f", foulPoints) + " foul points awarded to\n" + playerTwo.playerName);
                        } else {
                            animateP1Score();
                            displayPopup(String.format("%.0f", foulPoints) + " foul points awarded to\n " + playerOne.playerName);
                        }
                }
                spinnerFouls.setSelection(0);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        p1Score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlayerOneActive();
            }
        });
        p2Score.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setPlayerTwoActive();
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

        setPlayerOneActive();
        keepScreenOn();
    }

    @Override
    public void onResume() {
        super.onResume();
        keepScreenOn();
        startTimers();
    }

    @Override
    public void onPause() {
        super.onPause();
        gameTimer.cancel();
        frameTimer.cancel();
    }

    private void keepScreenOn() {
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (Option.getOptionSwitchState("KeepScreenOn")) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public void startTimers(){
        gameTimer = new Timer();
        gameTimer.schedule(new TimerTask() {
                @Override
                public void run () {
                runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        tGameTime.setText(Game.gameTimer(game.gameID));
                    }
                });
                }
            },0,1000);

        frameTimer = new Timer();
        frameTimer.schedule(new TimerTask() {
            @Override
            public void run () {
                runOnUiThread(new TimerTask() {
                    @Override
                    public void run() {
                        tFrameTime.setText(Frame.frameTimer((frame.frameID)));
                    }
                });
            }
        },0,1000);
    }

    public void stopTimers(){
        gameTimer.cancel();
        frameTimer.cancel();
        finish();
    }

    private void ballPotted(double score) {
        Shot shot = new Shot();
        shot.frameID = frame.frameID;
        shot.score = score;
        shot.playerID = game.activePlayer.playerID;

        Shot.addShot(shot);
        frame.shots.add(shot);

        if (game.activePlayer == playerOne) {
            animateP1Score();
        } else if (game.activePlayer == playerTwo) {
            animateP2Score();
        }
    }

    private void animateP1Score() {
        TextView tTotalP1Score = findViewById(R.id.p1Score);
        tTotalP1Score.setTextColor(getResources().getColor(R.color.colorYellow));
        new CountDownTimer(1000, 1000) {
            public void onFinish() {
                tTotalP1Score.setTextColor(getResources().getColor(R.color.colorWhite));
            }
            public void onTick(long millisUntilFinished) {
            }
        }.start();

        Animation anim = AnimationUtils.loadAnimation(this, R.anim.score_anim);
        anim.reset();
        tTotalP1Score.setText(Integer.toString(playerOne.getFrameScore(frame)));
        tTotalP1Score.clearAnimation();
        tTotalP1Score.startAnimation(anim);
    }

    private void animateP2Score() {
        TextView tTotalP2Score = findViewById(R.id.p2Score);
        tTotalP2Score.setTextColor(getResources().getColor(R.color.colorYellow));
        new CountDownTimer(1000, 1000) {
            public void onFinish() {
                tTotalP2Score.setTextColor(getResources().getColor(R.color.colorWhite));
            }
            public void onTick(long millisUntilFinished) {
            }
        }.start();
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.score_anim);
        anim.reset();
        tTotalP2Score.setText(Integer.toString(playerTwo.getFrameScore(frame)));
        tTotalP2Score.clearAnimation();
        tTotalP2Score.startAnimation(anim);
    }

    private void animateBall(View v) {
        Animation anim = AnimationUtils.loadAnimation(this, R.anim.ball_anim);
        anim.reset();
        v.clearAnimation();
        v.startAnimation(anim);
    }

    private void setPlayerOneActive() {
        game.setActivePlayer(playerOne);
        tP1Name.setBackground(getResources().getDrawable(R.drawable.p1selected));
        tP1Name.setTextColor(getResources().getColor(R.color.colorBlack));
        tP2Name.setBackgroundColor(0x00000000);
        tP2Name.setTextColor(getResources().getColor(R.color.colorDark));
    }

    private void setPlayerTwoActive() {
        game.setActivePlayer(playerTwo);
        tP2Name.setBackground(getResources().getDrawable(R.drawable.p2selected));
        tP2Name.setTextColor(getResources().getColor(R.color.colorBlack));
        tP1Name.setBackgroundColor(0x00000000);
        tP1Name.setTextColor(getResources().getColor(R.color.colorDark));
    }

    private void initFoulList() {
        mFoulList = new ArrayList<>();
        mFoulList.add(new FoulItems(0, R.drawable._foul));
        mFoulList.add(new FoulItems((float) 4.0001, R.drawable._foul4));
        mFoulList.add(new FoulItems((float) 5.0001, R.drawable._foul5));
        mFoulList.add(new FoulItems((float) 6.0001, R.drawable._foul6));
        mFoulList.add(new FoulItems((float) 7.0001, R.drawable._foul7));
    }

    public void undoP1(View view) {
        playerOne.undoLastShot(frame);
        animateP1Score();
    }

    public void undoP2(View view) {
        playerTwo.undoLastShot(frame);
        animateP2Score();
    }

    private void newFrame() {
        String frameWinner = "";

        if (playerOne.getFrameScore(frame) > playerTwo.getFrameScore(frame)) {
            frameWinner = playerOne.playerName;
            frame.frameWinnerPlayerID = playerOne.playerID;
        } else {
            frameWinner = playerTwo.playerName;
            frame.frameWinnerPlayerID = playerTwo.playerID;
        }

        if (game.nextBreak == playerOne) {
            setPlayerOneActive();
            game.nextBreak = playerTwo;
        } else if (game.nextBreak == playerTwo) {
            setPlayerTwoActive();
            game.nextBreak = playerOne;
        }

        Frame.updateFrame(frame);

        displayPopup(frameWinner + " wins the frame!\n\n- Frame " + (frame.frameNumber + 1) + " -\n" + game.activePlayer.playerName + " to break");
        tP1Frames.setText(Integer.toString(Frame.getPlayerFrameWinCount(game.gameID, playerOne.playerID)));
        tP2Frames.setText(Integer.toString(Frame.getPlayerFrameWinCount(game.gameID, playerTwo.playerID)));

        frame = new Frame(
                game.gameID,
                game.frames.size() + 1,
                new ArrayList<Shot>());
        frame.frameID = (int) Frame.addFrame(frame);
        game.frames.add(frame);

        animateP1Score();
        animateP2Score();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.scoreboard_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.endFrame:
                onNewFramePressed(null);
                break;
            case R.id.rules:
                Intent rulesIntent = new Intent(ScoreboardActivity.this, RulesActivity.class);
                startActivity(rulesIntent);
                break;
            case R.id.stats:
                onShowStatsPressed();
                break;
            case R.id.options:
                Intent optionsIntent = new Intent(ScoreboardActivity.this, OptionsActivity.class);
                startActivity(optionsIntent);
                break;
            case R.id.endMatch:
                onEndMatchPressed();
                break;
            case R.id.load:
                Intent loadIntent = new Intent(ScoreboardActivity.this, SavedGamesActivity.class);
                startActivity(loadIntent);
                break;
        }
        return (super.onOptionsItemSelected(item));
    }

    private void onNewFramePressed(View view) {
        if (playerOne.getFrameScore(frame) != playerTwo.getFrameScore(frame)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Start a new frame?")
                    .setTitle("End Frame")
                    .setCancelable(false)
                    .setPositiveButton
                            (
                                    "Yes", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            newFrame();
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

    @Override
    public void onBackPressed() {
        stopTimers();
    }

    private void onEndMatchPressed() {
        stopTimers();
    }

    private void onShowStatsPressed() {
        Intent statsIntent = new Intent(ScoreboardActivity.this, StatsActivity.class);
        statsIntent.putExtra("mMatchStats", mMatchStats);
        statsIntent.putExtra("mPlayerOne", playerOne);
        statsIntent.putExtra("mPlayerTwo", playerTwo);
        statsIntent.putExtra("Game", game);
        startActivity(statsIntent);
    }

    private void displayPopup(String message) {

        LayoutInflater layoutInflater = (LayoutInflater) getBaseContext().getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = layoutInflater.inflate(R.layout.popup, null);

        final PopupWindow popupWindow = new PopupWindow(popupView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        TextView tvpopUP = (TextView) popupView.findViewById(R.id.popupMessage);
        tvpopUP.setText(message);

        new Handler().postDelayed(new Runnable() {

            public void run() {
                popupWindow.showAtLocation(getWindow().getDecorView(), Gravity.CENTER, 0, 0);
            }

        }, 500L);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                popupWindow.dismiss();
            }
        }, 3500);
    }


}