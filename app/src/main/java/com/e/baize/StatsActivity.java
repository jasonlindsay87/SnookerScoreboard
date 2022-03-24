package com.e.baize;

import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class StatsActivity extends AppCompatActivity {
    public ImageView shareButton;
    public StatsItems mStatsItems;
    public Game game;
    public TableLayout summaryTable;
    public TableLayout scoresTable;
    public TableLayout frameTable;
    public TableLayout p1ScoreTable;
    public TableLayout p2ScoreTable;
    public LinearLayout scoresLayout;
    public ScrollView scoresScroll;
    public LinearLayout container;
    public int iFramesPlayed = 0;

    public Player mPlayerOne;
    public List<Double> p1ScoreList = new ArrayList<Double>();
    public int p1TotalPoints = 0;
    public int p1NumberOfFoulsAwarded = 0;
    public double p1FoulPointsAwarded = 0;

    public Player mPlayerTwo;
    public List<Double> p2ScoreList = new ArrayList<Double>();
    public int p2TotalPoints = 0;
    public int p2NumberOfFoulsAwarded = 0;
    public double p2FoulPointsAwarded = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_stats);
        summaryTable = (TableLayout) findViewById(R.id.tblSummary);
        scoresTable = (TableLayout) findViewById(R.id.tblStats);
        scoresLayout = (LinearLayout) findViewById(R.id.linearScores);
        shareButton = (ImageView) findViewById(R.id.btnShare);
        scoresScroll = (ScrollView) findViewById(R.id.scrollScores);
        container = (LinearLayout) findViewById(R.id.container);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mStatsItems = (StatsItems) getIntent().getSerializableExtra("mMatchStats");
            this.mPlayerOne = (Player) getIntent().getSerializableExtra("mPlayerOne");
            this.mPlayerTwo = (Player) getIntent().getSerializableExtra("mPlayerTwo");
            this.game = (Game) getIntent().getSerializableExtra("Game");
        }
        assert game!= null;
        for (Frame frame : game.frames) {
            ArrayList<String> p1scores = new ArrayList<>();
            ArrayList<String> p2scores = new ArrayList<>();
            ArrayList<Shot> shots = new ArrayList<>();
            shots = Frame.getPlayerShotsInFrame(frame.frameID, game.playerOne.playerID);
            for (Shot shot : shots) {
                p1scores.add(Double.toString(shot.score));
            }
            shots = Frame.getPlayerShotsInFrame(frame.frameID, game.playerTwo.playerID);
            for (Shot shot : shots) {
                p2scores.add(Double.toString(shot.score));
            }
            mStatsItems.p1Scores.add(p1scores);
            mStatsItems.p2Scores.add(p2scores);

        }

        verifyStoragePermission(StatsActivity.this);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animate.animateButton(findViewById(R.id.shareLayout));
                takeScreenShot(getWindow().getDecorView());
            }
        });

        addPlayerNames();
        addCurrentMatchScore();
        addGameTimeRow();
        addTotalPointsRow();
        addTotalBallsPottedRow();
        addFoulsAwardedRow();
        addNumberOfEachBall();

        addFrames();
        updateTotalPoints();
        updateFoulPoints();
        updateNumberOfEachBall();
        updateTotalBallsPotted();
    }

    public void addPlayerNames() {
        String sNameP1 = (String) mPlayerOne.playerName;
        String sNameP2 = (String) mPlayerTwo.playerName;
        TextView tvNameP1 = new TextView(this);
        TextView tvNameP2 = new TextView(this);
        TableRow rNames = new TableRow(this);
        tvNameP1.setPadding(10, 0, 25, 0);
        tvNameP2.setPadding(25, 0, 10, 0);
        tvNameP1.setTextColor(getResources().getColor(R.color.colorBlack));
        tvNameP2.setTextColor(getResources().getColor(R.color.colorBlack));
        tvNameP1.setBackgroundColor(getResources().getColor(R.color.colorYellow));
        tvNameP2.setBackgroundColor(getResources().getColor(R.color.colorYellow));
        tvNameP1.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvNameP2.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvNameP1.setTextSize(20);
        tvNameP2.setTextSize(20);
        tvNameP1.setTypeface(null, Typeface.BOLD);
        tvNameP2.setTypeface(null, Typeface.BOLD);
        tvNameP1.setText(sNameP1);
        tvNameP2.setText(sNameP2);
        tvNameP1.setWidth(330);
        tvNameP2.setWidth(330);
        tvNameP1.setGravity(Gravity.CENTER);
        tvNameP2.setGravity(Gravity.CENTER);
        rNames.addView(tvNameP1);
        rNames.addView(tvNameP2);
        rNames.setGravity(Gravity.CENTER);
        summaryTable.addView(rNames);
    }

    public void addCurrentMatchScore() {
        String sFrames = mPlayerOne.getFramesWonCount(game) + "     -     " + mPlayerTwo.getFramesWonCount(game);
        TextView tvFrames = new TextView(this);
        TableRow rFrames = new TableRow(this);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        tvFrames.setLayoutParams(rowLayout);
        tvFrames.setText(sFrames);
        tvFrames.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvFrames.setBackgroundColor(getResources().getColor(R.color.colorDark));
        tvFrames.setTextColor(getResources().getColor(R.color.colorWhite));
        tvFrames.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvFrames.setTextSize(19);
        tvFrames.setPadding(0, 5, 0, 5);
        tvFrames.setTypeface(null, Typeface.BOLD);
        rowLayout.span = 2;
        rFrames.addView(tvFrames);
        rFrames.setGravity(Gravity.CENTER);
        summaryTable.addView(rFrames);
    }

    public void addFrames() {
        p1ScoreTable = new TableLayout(this);
        p2ScoreTable = new TableLayout(this);
        while ((iFramesPlayed < mStatsItems.p1Scores.size()) && (iFramesPlayed < mStatsItems.p2Scores.size())) {
            addFrameHeader(iFramesPlayed + 1);

            LinearLayout frameLayout = new LinearLayout(this);
            frameLayout.setOrientation(LinearLayout.HORIZONTAL);


            FlexboxLayout.LayoutParams fbLayout = new FlexboxLayout.LayoutParams(500, FlexboxLayout.LayoutParams.WRAP_CONTENT);
            fbLayout.setFlexGrow(1f);

            FlexboxLayout p1ScoresLayout = new FlexboxLayout(this);
            p1ScoresLayout.setLayoutParams(fbLayout);
            p1ScoresLayout.setFlexDirection(FlexDirection.ROW);
            p1ScoresLayout.setFlexWrap(FlexWrap.WRAP);

            FlexboxLayout p2ScoresLayout = new FlexboxLayout(this);
            p2ScoresLayout.setLayoutParams(fbLayout);
            p2ScoresLayout.setFlexDirection(FlexDirection.ROW);
            p2ScoresLayout.setFlexWrap(FlexWrap.WRAP);

            TextView tvP1Total = new TextView(this);
            tvP1Total.setPadding(0,10,50,0);
            TextView tvP2Total = new TextView(this);
            tvP2Total.setPadding(50,10,0,0);

            for (int j = iFramesPlayed; j < iFramesPlayed + 1; j++) {
                double frameTotal = 0;

                for (int k = 0; k < mStatsItems.p1Scores.get(iFramesPlayed).size(); k++) {
                    double score = Double.parseDouble(mStatsItems.p1Scores.get(iFramesPlayed).get(k));

                    ImageView imScore = new ImageView(this);
                    imScore.setPadding(0,10,7,0);
                    imScore.setImageBitmap(getBallColour(score));
                    p1ScoresLayout.addView(imScore);
                    frameTotal += score;

                    if (score % 1 == 0) p1ScoreList.add(score);
                    if (score % 1 != 0) {
                        p1NumberOfFoulsAwarded++;
                        p1FoulPointsAwarded += score;
                    }
                }

                String sTotal = "(" + Math.round(frameTotal) + ")";
                p1TotalPoints += frameTotal;
                tvP1Total.setText(sTotal);
                tvP1Total.setTextColor(getResources().getColor(R.color.colorWhite));
                tvP1Total.setTypeface(null, Typeface.BOLD);
                tvP1Total.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvP1Total.setGravity(Gravity.CENTER);
            }
            frameLayout.addView(p1ScoresLayout);
            for (int j = iFramesPlayed; j < iFramesPlayed + 1; j++) {
                double frameTotal = 0;
                for (int k = 0; k < mStatsItems.p2Scores.get(iFramesPlayed).size(); k++) {
                    double score = Double.parseDouble(mStatsItems.p2Scores.get(iFramesPlayed).get(k));

                    ImageView imScore = new ImageView(this);
                    imScore.setPadding(0, 10, 7, 0);
                    imScore.setImageBitmap(getBallColour(score));
                    p2ScoresLayout.addView(imScore);
                    frameTotal += score;

                    if (score % 1 == 0) p2ScoreList.add(score);
                    if (score % 1 != 0) {
                        p2NumberOfFoulsAwarded++;
                        p2FoulPointsAwarded += score;
                    }
                }

                String sTotal = "(" + Math.round(frameTotal) + ")";
                p2TotalPoints += frameTotal;
                tvP2Total.setText(sTotal);
                tvP2Total.setTextColor(getResources().getColor(R.color.colorWhite));
                tvP2Total.setTypeface(null, Typeface.BOLD);
                tvP2Total.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                tvP2Total.setGravity(Gravity.CENTER);
            }
            frameLayout.addView(p2ScoresLayout);

            if (Double.parseDouble(tvP1Total.getText().toString().replaceAll("\\D+", "")) > Double.parseDouble(tvP2Total.getText().toString().replaceAll("\\D+", ""))) {
                tvP1Total.setTextColor(getResources().getColor(R.color.colorYellow));
            } else if (Double.parseDouble(tvP2Total.getText().toString().replaceAll("\\D+", "")) > Double.parseDouble(tvP1Total.getText().toString().replaceAll("\\D+", ""))) {
                tvP2Total.setTextColor(getResources().getColor(R.color.colorYellow));
            }

            scoresLayout.addView(frameLayout);
            LinearLayout frameScoreContainerLayout = new LinearLayout(this);
            frameScoreContainerLayout.setGravity(Gravity.CENTER);
            frameScoreContainerLayout.setPadding(0,5,0,20);

            frameScoreContainerLayout.addView(tvP1Total);
            frameScoreContainerLayout.addView(tvP2Total);
            scoresLayout.addView(frameScoreContainerLayout);
            iFramesPlayed++;
        }

        TextView tvInPlay = new TextView(this);
        tvInPlay.setPadding(2,2,2,2);
        tvInPlay.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvInPlay.setText("Frame "+ iFramesPlayed + " in play...");
        tvInPlay.setTypeface(null, Typeface.BOLD);
        tvInPlay.setTextColor(getResources().getColor(R.color.colorWhite));
        scoresLayout.addView(tvInPlay);
    }

    public void addFrameHeader(Integer iFrameNumber) {
        Frame frame = Frame.getFrameFromNumber(game.gameID, iFrameNumber);
        String sFrame = "Frame " + iFrameNumber + "\n " + frame.frameTime;
        TextView tvFrame = new TextView(this);
        tvFrame.setText(sFrame);
        tvFrame.setBackgroundColor(getResources().getColor(R.color.colorWhiteFade));
        tvFrame.setTextColor(getResources().getColor(R.color.colorBlack));
        tvFrame.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvFrame.setTextSize(14);
        tvFrame.setTypeface(null, Typeface.BOLD_ITALIC);
        tvFrame.setPadding(0, 5, 0, 5);
        scoresLayout.addView(tvFrame);

    }

    public void addGameTimeRow() {
        String sGameTime = "Match time - " + Game.gameTimer(game.gameID);
        TextView tvStats = new TextView(this);
        tvStats.setId(R.id.statsGameTimeTextView);
        TableRow rGameTime = new TableRow(this);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        tvStats.setLayoutParams(rowLayout);
        tvStats.setText(sGameTime);
        tvStats.setTextColor(getResources().getColor(R.color.colorWhite));
        tvStats.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvStats.setTextSize(14);
        tvStats.setPadding(0, 5, 0, 5);
        rowLayout.span = 2;
        rGameTime.setGravity(Gravity.CENTER);
        rGameTime.addView(tvStats);
        summaryTable.addView(rGameTime);
    }

    public void addTotalPointsRow() {
        String sTotalPoints = " - Total points scored - ";
        TextView tvStats = new TextView(this);
        tvStats.setId(R.id.statsTotalPointsTextView);
        TableRow rTotalPoints = new TableRow(this);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        tvStats.setLayoutParams(rowLayout);
        tvStats.setText(sTotalPoints);
        tvStats.setTextColor(getResources().getColor(R.color.colorWhite));
        tvStats.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvStats.setTextSize(14);
        tvStats.setPadding(0, 5, 0, 5);
        rowLayout.span = 2;
        rTotalPoints.setGravity(Gravity.CENTER);
        rTotalPoints.addView(tvStats);
        summaryTable.addView(rTotalPoints);
    }

    public void addFoulsAwardedRow() {
        String sFoulPoints = " - Foul points awarded - ";
        TextView tvStats = new TextView(this);
        tvStats.setId(R.id.statsFoulPointsTextView);
        TableRow rFoulPoints = new TableRow(this);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        tvStats.setLayoutParams(rowLayout);
        tvStats.setText(sFoulPoints);
        tvStats.setTextColor(getResources().getColor(R.color.colorWhite));
        tvStats.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvStats.setTextSize(14);
        tvStats.setPadding(0, 5, 0, 5);
        rowLayout.span = 2;
        rFoulPoints.setGravity(Gravity.CENTER);
        rFoulPoints.addView(tvStats);
        summaryTable.addView(rFoulPoints);
    }

    public void addNumberOfEachBall() {
        TableRow rNumberOfEachBall = new TableRow(this);
        rNumberOfEachBall.setId(R.id.statsNumberOfEachBallRow);
        rNumberOfEachBall.setGravity(Gravity.CENTER);
        summaryTable.addView(rNumberOfEachBall);
    }

    public void updateNumberOfEachBall() {
        LinearLayout p1Balls = new LinearLayout(this);
        LinearLayout p2Balls = new LinearLayout(this);
        p1Balls.setPadding(0,0,0,15);
        p2Balls.setPadding(0,0,0,15);

        TextView tvRedBall1 = new TextView(this);
        TextView tvYellowBall1 = new TextView(this);
        TextView tvGreenBall1 = new TextView(this);
        TextView tvBrownBall1 = new TextView(this);
        TextView tvBlueBall1  = new TextView(this);
        TextView tvPinkBall1  = new TextView(this);
        TextView tvBlackBall1  = new TextView(this);

        TextView tvRedBall2 = new TextView(this);
        TextView tvYellowBall2 = new TextView(this);
        TextView tvGreenBall2 = new TextView(this);
        TextView tvBrownBall2 = new TextView(this);
        TextView tvBlueBall2  = new TextView(this);
        TextView tvPinkBall2  = new TextView(this);
        TextView tvBlackBall2  = new TextView(this);

        int countReds1 = 0;
        int countYellows1 = 0;
        int countGreens1 = 0;
        int countBrowns1 = 0;
        int countBlues1 = 0;
        int countPinks1 = 0;
        int countBlacks1 = 0;
        int countReds2 = 0;
        int countYellows2 = 0;
        int countGreens2 = 0;
        int countBrowns2 = 0;
        int countBlues2 = 0;
        int countPinks2 = 0;
        int countBlacks2 = 0;

         for(int i = 0; i < p1ScoreList.size(); i++) {
            if (Math.round(p1ScoreList.get(i)) == 1 ) countReds1 ++;
            if (Math.round(p1ScoreList.get(i)) == 2 ) countYellows1 ++;
            if (Math.round(p1ScoreList.get(i)) == 3 ) countGreens1 ++;
            if (Math.round(p1ScoreList.get(i)) == 4 ) countBrowns1 ++;
            if (Math.round(p1ScoreList.get(i)) == 5 ) countBlues1 ++;
            if (Math.round(p1ScoreList.get(i)) == 6 ) countPinks1 ++;
            if (Math.round(p1ScoreList.get(i)) == 7 ) countBlacks1 ++;
         }

        for(int i = 0; i < p2ScoreList.size(); i++) {
            if (Math.round(p2ScoreList.get(i)) == 1 ) countReds2 ++;
            if (Math.round(p2ScoreList.get(i)) == 2 ) countYellows2 ++;
            if (Math.round(p2ScoreList.get(i)) == 3 ) countGreens2 ++;
            if (Math.round(p2ScoreList.get(i)) == 4 ) countBrowns2 ++;
            if (Math.round(p2ScoreList.get(i)) == 5 ) countBlues2 ++;
            if (Math.round(p2ScoreList.get(i)) == 6 ) countPinks2 ++;
            if (Math.round(p2ScoreList.get(i)) == 7 ) countBlacks2 ++;
        }


        tvRedBall1.setBackground(getSmallBallColour(1));
        tvRedBall1.setTextColor(getResources().getColor(R.color.colorWhite));
        tvRedBall1.setGravity(Gravity.CENTER);
        tvRedBall1.setText(Integer.toString(countReds1));
        p1Balls.addView(tvRedBall1);

        tvYellowBall1.setBackground(getSmallBallColour(2));
        tvYellowBall1.setTextColor(getResources().getColor(R.color.colorWhite));
        tvYellowBall1.setGravity(Gravity.CENTER);
        tvYellowBall1.setText(Integer.toString(countYellows1));
        p1Balls.addView(tvYellowBall1);

        tvGreenBall1.setBackground(getSmallBallColour(3));
        tvGreenBall1.setTextColor(getResources().getColor(R.color.colorWhite));
        tvGreenBall1.setGravity(Gravity.CENTER);
        tvGreenBall1.setText(Integer.toString(countGreens1));
        p1Balls.addView(tvGreenBall1);

        tvBrownBall1.setBackground(getSmallBallColour(4));
        tvBrownBall1.setTextColor(getResources().getColor(R.color.colorWhite));
        tvBrownBall1.setGravity(Gravity.CENTER);
        tvBrownBall1.setText(Integer.toString(countBrowns1));
        p1Balls.addView(tvBrownBall1);

        tvBlueBall1.setBackground(getSmallBallColour(5));
        tvBlueBall1.setTextColor(getResources().getColor(R.color.colorWhite));
        tvBlueBall1.setGravity(Gravity.CENTER);
        tvBlueBall1.setText(Integer.toString(countBlues1));
        p1Balls.addView(tvBlueBall1);

        tvPinkBall1.setBackground(getSmallBallColour(6));
        tvPinkBall1.setTextColor(getResources().getColor(R.color.colorWhite));
        tvPinkBall1.setGravity(Gravity.CENTER);
        tvPinkBall1.setText(Integer.toString(countPinks1));
        p1Balls.addView(tvPinkBall1);

        tvBlackBall1.setBackground(getSmallBallColour(7));
        tvBlackBall1.setTextColor(getResources().getColor(R.color.colorWhite));
        tvBlackBall1.setGravity(Gravity.CENTER);
        tvBlackBall1.setText(Integer.toString(countBlacks1));
        p1Balls.addView(tvBlackBall1);

        TextView tvSpacer = new TextView(this);
        tvSpacer.setText("  -  ");
        tvSpacer.setTextColor(getResources().getColor(R.color.colorWhite));
        p1Balls.addView(tvSpacer);

        tvRedBall2.setBackground(getSmallBallColour(1));
        tvRedBall2.setTextColor(getResources().getColor(R.color.colorWhite));
        tvRedBall2.setGravity(Gravity.CENTER);
        tvRedBall2.setText(Integer.toString(countReds2));
        p2Balls.addView(tvRedBall2);

        tvYellowBall2.setBackground(getSmallBallColour(2));
        tvYellowBall2.setTextColor(getResources().getColor(R.color.colorWhite));
        tvYellowBall2.setGravity(Gravity.CENTER);
        tvYellowBall2.setText(Integer.toString(countYellows2));
        p2Balls.addView(tvYellowBall2);

        tvGreenBall2.setBackground(getSmallBallColour(3));
        tvGreenBall2.setTextColor(getResources().getColor(R.color.colorWhite));
        tvGreenBall2.setGravity(Gravity.CENTER);
        tvGreenBall2.setText(Integer.toString(countGreens2));
        p2Balls.addView(tvGreenBall2);

        tvBrownBall2.setBackground(getSmallBallColour(4));
        tvBrownBall2.setTextColor(getResources().getColor(R.color.colorWhite));
        tvBrownBall2.setGravity(Gravity.CENTER);
        tvBrownBall2.setText(Integer.toString(countBrowns2));
        p2Balls.addView(tvBrownBall2);

        tvBlueBall2.setBackground(getSmallBallColour(5));
        tvBlueBall2.setTextColor(getResources().getColor(R.color.colorWhite));
        tvBlueBall2.setGravity(Gravity.CENTER);
        tvBlueBall2.setText(Integer.toString(countBlues2));
        p2Balls.addView(tvBlueBall2);

        tvPinkBall2.setBackground(getSmallBallColour(6));
        tvPinkBall2.setTextColor(getResources().getColor(R.color.colorWhite));
        tvPinkBall2.setGravity(Gravity.CENTER);
        tvPinkBall2.setText(Integer.toString(countPinks2));
        p2Balls.addView(tvPinkBall2);

        tvBlackBall2.setBackground(getSmallBallColour(7));
        tvBlackBall2.setTextColor(getResources().getColor(R.color.colorWhite));
        tvBlackBall2.setGravity(Gravity.CENTER);
        tvBlackBall2.setText(Integer.toString(countBlacks2));
        p2Balls.addView(tvBlackBall2);

        TableRow rNumberOfEachBall = summaryTable.findViewById(R.id.statsNumberOfEachBallRow);
        rNumberOfEachBall.addView(p1Balls);
        rNumberOfEachBall.addView(p2Balls);
    }

    public void addAveragePotRow() {
        String sTotalPoints = " - Average pot - ";
        TextView tvStats = new TextView(this);
        tvStats.setId(R.id.statsAveragePotTextView);
        TableRow rAveragePot = new TableRow(this);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        tvStats.setLayoutParams(rowLayout);
        tvStats.setText(sTotalPoints);
        tvStats.setTextColor(getResources().getColor(R.color.colorWhite));
        tvStats.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvStats.setTextSize(14);
        tvStats.setPadding(0, 5, 0, 5);
        rowLayout.span = 2;
        rAveragePot.setGravity(Gravity.CENTER);
        rAveragePot.addView(tvStats);
        scoresTable.addView(rAveragePot);
    }

    public void addTotalBallsPottedRow() {
        String sTotalBalls = " - Total balls potted - ";
        TextView tvStats = new TextView(this);
        tvStats.setId(R.id.statsTotalBallsTextView);
        TableRow rTotalBalls = new TableRow(this);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        tvStats.setLayoutParams(rowLayout);
        tvStats.setText(sTotalBalls);
        tvStats.setTextColor(getResources().getColor(R.color.colorWhite));
        tvStats.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvStats.setTextSize(14);
        tvStats.setPadding(0, 5, 0, 5);
        rowLayout.span = 2;
        rTotalBalls.setGravity(Gravity.CENTER);
        rTotalBalls.addView(tvStats);
        summaryTable.addView(rTotalBalls);
    }

    public void updateTotalPoints() {
        String sTotalPoints = p1TotalPoints + " - Total points scored - " + p2TotalPoints;
        TextView tvTotalPoints = summaryTable.findViewById(R.id.statsTotalPointsTextView);
        tvTotalPoints.setText(sTotalPoints);
    }

    public void updateFoulPoints() {
        String sFoulPoints = Math.round(p1FoulPointsAwarded) + " - Foul points awarded - " + Math.round(p2FoulPointsAwarded);
        TextView tvFoulPoints = summaryTable.findViewById(R.id.statsFoulPointsTextView);
        tvFoulPoints.setText(sFoulPoints);
    }

    public void updateAveragePots() {
        int p1Average = 0;
        int p2Average = 0;
        int p1ScoreListSize = 0;
        int p2ScoreListSize = 0;

        for (int i = 0; i < mStatsItems.p1Scores.size(); i++) {
            int FrameSize = mStatsItems.p1Scores.get(i).size();
            p1ScoreListSize += FrameSize;
        }
        for (int i = 0; i < mStatsItems.p2Scores.size(); i++) {
            int FrameSize = mStatsItems.p2Scores.get(i).size();
            p2ScoreListSize += FrameSize;
        }

        p1Average = p1TotalPoints / ((mPlayerOne.ScoreHistory.size() + p1ScoreListSize) == 0 ? 1 : mPlayerOne.ScoreHistory.size() + p1ScoreListSize);
        p2Average = p2TotalPoints / ((mPlayerTwo.ScoreHistory.size() + p2ScoreListSize) == 0 ? 1 : mPlayerTwo.ScoreHistory.size() + p2ScoreListSize);

        String sAveragePot = p1Average + " - Average pot - " + p2Average;
        TextView tvAveragePot = scoresTable.findViewById(R.id.statsAveragePotTextView);
        tvAveragePot.setText(sAveragePot);
    }

    public void updateTotalBallsPotted() {
        int p1TotalPots = 0;
        int p2TotalPots = 0;
        int p1PreviousFramePots = 0;
        int p2PreviousFramePots = 0;

        for (int i = 0; i < mStatsItems.p1Scores.size(); i++) {
            int CurrentFrameSize = mStatsItems.p1Scores.get(i).size();
            p1PreviousFramePots += CurrentFrameSize;
        }
        for (int i = 0; i < mStatsItems.p2Scores.size(); i++) {
            int CurrentFrameSize = mStatsItems.p2Scores.get(i).size();
            p2PreviousFramePots += CurrentFrameSize;
        }
        p1TotalPots = (p1PreviousFramePots + mPlayerOne.ScoreHistory.size()) - p1NumberOfFoulsAwarded;
        p2TotalPots = (p2PreviousFramePots + mPlayerTwo.ScoreHistory.size()) - p2NumberOfFoulsAwarded;

        String sTotalBalls = p1ScoreList.size() + " - Total balls potted - " + p2ScoreList.size();
        TextView tvTotalBalls = summaryTable.findViewById(R.id.statsTotalBallsTextView);

        tvTotalBalls.setText(sTotalBalls);
    }

    public Bitmap getBallColour(double score) {
        int colourDrawable = 0;
        Bitmap colourBMP;
        if (score % 1 == 0) {
            if (score == 1) {
                colourDrawable = R.drawable.red_ball;
            } else if (score == 2) {
                colourDrawable = R.drawable.yellow_ball;
            } else if (score == 3) {
                colourDrawable = R.drawable.green_ball;
            } else if (score == 4) {
                colourDrawable = R.drawable.brown_ball;
            } else if (score == 5) {
                colourDrawable = R.drawable.blue_ball;
            } else if (score == 6) {
                colourDrawable = R.drawable.pink_ball;
            } else if (score == 7) {
                colourDrawable = R.drawable.black_ball;
            }

        } else {
            if (Math.round(score * 10000) == 40001) {
                colourDrawable = R.drawable._foul4;
            } else if (Math.round(score * 10000) == 50001) {
                colourDrawable = R.drawable._foul5;
            } else if (Math.round(score * 10000) == 60001) {
                colourDrawable = R.drawable._foul6;
            } else if (Math.round(score * 10000) == 70001) {
                colourDrawable = R.drawable._foul7;
            } else {
                colourDrawable = R.drawable._foul;
            }

        }
        colourBMP = BitmapFactory.decodeResource(getResources(), colourDrawable);
        colourBMP = Bitmap.createScaledBitmap(colourBMP, 60, 60, true);
        return colourBMP;
    }

    public Drawable getSmallBallColour(double score) {
        int colourDrawable = 0;
        Bitmap colourBMP;
        if (score % 1 == 0) {
            if (score == 1) {
                colourDrawable = R.drawable.red_ball;
            } else if (score == 2) {
                colourDrawable = R.drawable.yellow_ball;
            } else if (score == 3) {
                colourDrawable = R.drawable.green_ball;
            } else if (score == 4) {
                colourDrawable = R.drawable.brown_ball;
            } else if (score == 5) {
                colourDrawable = R.drawable.blue_ball;
            } else if (score == 6) {
                colourDrawable = R.drawable.pink_ball;
            } else if (score == 7) {
                colourDrawable = R.drawable.black_ball;
            }

        } else {
            if (Math.round(score * 10000) == 40001) {
                colourDrawable = R.drawable._foul4;
            } else if (Math.round(score * 10000) == 50001) {
                colourDrawable = R.drawable._foul5;
            } else if (Math.round(score * 10000) == 60001) {
                colourDrawable = R.drawable._foul6;
            } else if (Math.round(score * 10000) == 70001) {
                colourDrawable = R.drawable._foul7;
            } else {
                colourDrawable = R.drawable._foul;
            }

        }
        colourBMP = BitmapFactory.decodeResource(getResources(), colourDrawable);
        colourBMP = Bitmap.createScaledBitmap(colourBMP, 60, 60, true);

        Drawable d = new BitmapDrawable(getResources(), colourBMP);
        return d;
    }

    public void bClose(View view) {
        Animate.animateButton(view.findViewById(R.id.btnBack));
        finish();
    }

    private void takeScreenShot(View view) {
        Date date = new Date();
        CharSequence format = DateFormat.format("MM-dd-yyyy_hh:mm:ss", date);

        try {
            File mainDir = new File(
                    this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "Screenshots");
            if (!mainDir.exists()) {
                boolean mkdir = mainDir.mkdir();
            }

            String path = mainDir + "/" + "SnookerScoreboard" + "-" + format + ".jpeg";
            view.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
            //Bitmap bitmap = getBitmapFromView(container, container.getChildAt(0).getHeight(), container.getChildAt(0).getWidth());

            view.setDrawingCacheEnabled(false);

            File imageFile = new File(path);
            FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();

            shareScreenShot(imageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private Bitmap getBitmapFromView(View view, int height, int width) {
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Drawable bgDrawable = view.getBackground();
        if (bgDrawable != null)
            bgDrawable.draw(canvas);
        else
            canvas.drawColor(Color.WHITE);
        view.draw(canvas);
        return bitmap;
    }

    private void shareScreenShot(File imageFile) {
        Uri uri = FileProvider.getUriForFile(
            Objects.requireNonNull(getApplicationContext()),
            BuildConfig.APPLICATION_ID + ".provider",
            imageFile
        );

        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.setType("image/*");
        intent.putExtra(android.content.Intent.EXTRA_TEXT, "Snooker Scoreboard Screenshot");
        intent.putExtra(Intent.EXTRA_STREAM, uri);

        try {
            this.startActivity(Intent.createChooser(intent, "Share With"));
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, "No App Available to share this file type.", Toast.LENGTH_SHORT).show();
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static final String[] PERMISSION_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };

    public static void verifyStoragePermission(Activity activity) {
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSION_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }

}

