package com.e.baize;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class StatsActivity extends AppCompatActivity {
    public StatsItems mStatsItems;
    public TableLayout statsTable;
    public TableLayout frameTable;
    public TableLayout p1ScoreTable;
    public TableLayout p2ScoreTable;
    public Player mPlayerOne;
    public Player mPlayerTwo;
    public int iFramesPlayed = 0;
    public int p1TotalPoints = 0;
    public int p2TotalPoints = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stats);
        statsTable = (TableLayout) findViewById(R.id.tblStats);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mStatsItems = (StatsItems) getIntent().getSerializableExtra("mMatchStats");
            this.mPlayerOne = (Player) getIntent().getSerializableExtra("mPlayerOne");
            this.mPlayerTwo = (Player) getIntent().getSerializableExtra("mPlayerTwo");
        }
        addPlayerNames();
        addCurrentMatchScore();
        addTotalPointsRow();
        addTotalBallsPottedRow();
        addAveragePotRow();
        addPreviousFrames();
        addCurrentFrame();
        updateTotalPoints();
        updateAveragePots();
        updateTotalBalls();
    }


    public void addPlayerNames() {
        String sNameP1 = (String) mPlayerOne.Name;
        String sNameP2 = (String) mPlayerTwo.Name;
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
        statsTable.addView(rNames);
    }

    public void addCurrentMatchScore(){
        String sFrames = mStatsItems.getP1FramesWon(mPlayerOne) + "     -     " + mStatsItems.getP2FramesWon(mPlayerTwo);
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
        statsTable.addView(rFrames);
    }

    public void addPreviousFrames() {
        while ((iFramesPlayed < mStatsItems.p1Scores.size()) && (iFramesPlayed < mStatsItems.p2Scores.size())) {
            addFrameHeader(iFramesPlayed + 1);
            p1ScoreTable = new TableLayout(this);
            p2ScoreTable = new TableLayout(this);
            frameTable = new TableLayout(this);

            TableRow rTotals = new TableRow(this);
            TextView tvP1Total = new TextView(this);
            TextView tvP2Total = new TextView(this);

            for (int j = iFramesPlayed; j < iFramesPlayed + 1; j++) {
                int frameTotal = 0;
                for (int k = 0; k < mStatsItems.p1Scores.get(iFramesPlayed).size(); k++) {
                    TableRow rScores = new TableRow(this);
                    rScores.setGravity(Gravity.CENTER);
                    ImageView imScore = new ImageView(this);
                    imScore.setImageBitmap(getBallColour(Integer.parseInt(mStatsItems.p1Scores.get(iFramesPlayed).get(k))));
                    rScores.addView(imScore);
                    p1ScoreTable.addView(rScores);
                    frameTotal += Integer.parseInt(mStatsItems.p1Scores.get(iFramesPlayed).get(k));
                }
                String sTotal = "(" + Integer.toString(frameTotal) + ")";
                p1TotalPoints += frameTotal;
                tvP1Total.setText(sTotal);
                tvP1Total.setTextColor(getResources().getColor(R.color.colorWhite));
                tvP1Total.setTypeface(null, Typeface.BOLD);
                tvP1Total.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            for (int j = iFramesPlayed; j < iFramesPlayed + 1; j++) {
                int frameTotal = 0;
                for (int k = 0; k < mStatsItems.p2Scores.get(iFramesPlayed).size(); k++) {
                    TableRow rScores = new TableRow(this);
                    rScores.setGravity(Gravity.CENTER);
                    ImageView imScore = new ImageView(this);
                    imScore.setImageBitmap(getBallColour(Integer.parseInt(mStatsItems.p2Scores.get(iFramesPlayed).get(k))));
                    rScores.addView(imScore);
                    p2ScoreTable.addView(rScores);
                    frameTotal += Integer.parseInt(mStatsItems.p2Scores.get(iFramesPlayed).get(k));
                }

                String sTotal = "(" + Integer.toString(frameTotal) + ")";
                p2TotalPoints += frameTotal;
                tvP2Total.setText(sTotal);
                tvP2Total.setTextColor(getResources().getColor(R.color.colorWhite));
                tvP2Total.setTypeface(null, Typeface.BOLD);
                tvP2Total.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            if (Integer.parseInt(tvP1Total.getText().toString().replaceAll("\\D+","")) > Integer.parseInt(tvP2Total.getText().toString().replaceAll("\\D+",""))) {
                tvP1Total.setTextColor(getResources().getColor(R.color.colorYellow));
            }
            else{
                tvP2Total.setTextColor(getResources().getColor(R.color.colorYellow));
            }

            rTotals.addView(tvP1Total);
            rTotals.addView(tvP2Total);
            TableRow rScores = new TableRow(this);
            rScores.addView(p1ScoreTable);
            rScores.addView(p2ScoreTable);
            rScores.setGravity(Gravity.CENTER_HORIZONTAL);
            rTotals.setGravity(Gravity.CENTER_HORIZONTAL);
            statsTable.addView(rScores);
            statsTable.addView(rTotals);
            iFramesPlayed++;
        }
    }

    public void addCurrentFrame(){
        int p1Score = 0;
        int p2Score = 0;
        addFrameHeader(iFramesPlayed +1);
        p1ScoreTable = new TableLayout(this);
        p2ScoreTable = new TableLayout(this);
        frameTable = new TableLayout(this);
        TableRow rTotals = new TableRow(this);
        TextView tvP1Total = new TextView(this);
        TextView tvP2Total = new TextView(this);
        int p1FrameTotal = 0;
        int p2FrameTotal = 0;

        for(int i = 0; i < mPlayerOne.getScoreHistory().size(); i++) {
            TableRow rScores = new TableRow(this);
            rScores.setGravity(Gravity.CENTER);
            ImageView imScore = new ImageView(this);
            imScore.setImageBitmap(getBallColour(mPlayerOne.getScoreHistory().get(i)));
            rScores.addView(imScore);
            p1ScoreTable.addView(rScores);
            p1FrameTotal += mPlayerOne.getScoreHistory().get(i);
            String sTotal = "(" + Integer.toString(p1FrameTotal) + ")";
            tvP1Total.setText(sTotal);
            tvP1Total.setTextColor(getResources().getColor(R.color.colorWhite));
            tvP1Total.setTypeface(null, Typeface.BOLD);
            tvP1Total.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        for(int i = 0; i < mPlayerTwo.getScoreHistory().size(); i++) {
            TableRow rScores = new TableRow(this);
            rScores.setGravity(Gravity.CENTER_HORIZONTAL);
            ImageView imScore = new ImageView(this);
            imScore.setImageBitmap(getBallColour(mPlayerTwo.getScoreHistory().get(i)));
            rScores.addView(imScore);
            p2ScoreTable.addView(rScores);
            p2FrameTotal += mPlayerTwo.getScoreHistory().get(i);
            String sTotal = "(" + Integer.toString(p2FrameTotal) + ")";
            tvP2Total.setText(sTotal);
            tvP2Total.setTextColor(getResources().getColor(R.color.colorWhite));
            tvP2Total.setTypeface(null, Typeface.BOLD);
            tvP2Total.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        }

        p1TotalPoints += p1FrameTotal;
        p2TotalPoints += p2FrameTotal;
        TableRow rInPlay = new TableRow(this);
        rInPlay.setGravity(Gravity.CENTER);
        TextView tvInPlay = new TextView(this);
        tvInPlay.setText("Frame " + (iFramesPlayed + 1) + " in play...");
        tvInPlay.setTextColor(getResources().getColor(R.color.colorWhite));
        tvInPlay.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvInPlay.setTypeface(null, Typeface.ITALIC);
        rInPlay.addView(tvInPlay);
        rTotals.addView(tvP1Total);
        rTotals.addView(tvP2Total);
        TableRow rScores = new TableRow(this);
        rScores.addView(p1ScoreTable);
        rScores.addView(p2ScoreTable);
        rScores.setGravity(Gravity.CENTER_HORIZONTAL);
        rTotals.setGravity(Gravity.CENTER_HORIZONTAL);
        statsTable.addView(rScores);
        statsTable.addView(rTotals);
        statsTable.addView(rInPlay);
    }

    public void addFrameHeader(Integer iFrameNumber) {
        String sFrame = "Frame " + iFrameNumber;
        TextView tvFrame = new TextView(this);
        TableRow rFrame = new TableRow(this);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        tvFrame.setLayoutParams(rowLayout);
        tvFrame.setText(sFrame);
        tvFrame.setBackgroundColor(getResources().getColor(R.color.colorWhite));
        tvFrame.setTextColor(getResources().getColor(R.color.colorBlack));
        tvFrame.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvFrame.setTextSize(14);
        tvFrame.setTypeface(null, Typeface.ITALIC);
        tvFrame.setPadding(0, 5, 0, 5);
        rowLayout.span = 2;
        rFrame.setGravity(Gravity.CENTER);
        rFrame.addView(tvFrame);
        statsTable.addView(rFrame);
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
        statsTable.addView(rTotalPoints);
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
        statsTable.addView(rAveragePot);
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
        statsTable.addView(rTotalBalls);
    }

    public void updateTotalPoints() {
        String sTotalPoints = " - Total points scored - ";
        TextView totalPoints = statsTable.findViewById(R.id.statsTotalPointsTextView);
        totalPoints.setText(p1TotalPoints + sTotalPoints + p2TotalPoints);
}

    public void updateAveragePots() {
        int p1Average = 0;
        int p2Average = 0;
        int p1ScoreListSize = 0;
        int p2ScoreListSize = 0;

        for (int i = 0; i < mStatsItems.p1Scores.size(); i++){
            int FrameSize = mStatsItems.p1Scores.get(i).size();
            p1ScoreListSize += FrameSize;
        }
        for (int i = 0; i < mStatsItems.p2Scores.size(); i++){
            int FrameSize = mStatsItems.p2Scores.get(i).size();
            p2ScoreListSize += FrameSize;
        }

        p1Average = p1TotalPoints / ((mPlayerOne.ScoreHistory.size() + p1ScoreListSize) == 0 ? 1 : mPlayerOne.ScoreHistory.size() + p1ScoreListSize);
        p2Average = p2TotalPoints / ((mPlayerTwo.ScoreHistory.size() + p2ScoreListSize) == 0 ? 1 : mPlayerTwo.ScoreHistory.size() + p2ScoreListSize);

        String sAveragePot = p1Average + " - Average pot - " + p2Average;
        TextView tvAveragePot = statsTable.findViewById(R.id.statsAveragePotTextView);
        tvAveragePot.setText(sAveragePot);
    }

    public void updateTotalBalls() {
        int p1Total = 0;
        int p2Total = 0;
        int p1ScoreListSize = 0;
        int p2ScoreListSize = 0;

        for (int i = 0; i < mStatsItems.p1Scores.size(); i++){
            int FrameSize = mStatsItems.p1Scores.get(i).size();
            p1ScoreListSize += FrameSize;
        }
        for (int i = 0; i < mStatsItems.p2Scores.size(); i++){
            int FrameSize = mStatsItems.p2Scores.get(i).size();
            p2ScoreListSize += FrameSize;
        }
        p1Total = p1ScoreListSize + mPlayerOne.ScoreHistory.size();
        p2Total = p2ScoreListSize + mPlayerTwo.ScoreHistory.size();

        String sTotalBalls = p1Total + " - Total balls potted - " + p2Total;
        TextView tvTotalBalls = statsTable.findViewById(R.id.statsTotalBallsTextView);
        tvTotalBalls.setText(sTotalBalls);
    }

    public Bitmap getBallColour(int iScore) {
        int colourDrawable = 0;
        Bitmap colourBMP;
        if (iScore != 0) {
            if (iScore == 1) {
                colourDrawable = R.drawable.red_ball;
            }
            else if (iScore == 2) {
                colourDrawable = R.drawable.yellow_ball;
            }
            else if (iScore == 3) {
                colourDrawable = R.drawable.green_ball;
            }
            else if (iScore == 4) {
                colourDrawable = R.drawable.brown_ball;
            }
            else if (iScore == 5) {
                colourDrawable = R.drawable.blue_ball;
            }
            else if (iScore == 6) {
                colourDrawable = R.drawable.pink_ball;
            }
            else if (iScore == 7) {
                colourDrawable = R.drawable.black_ball;
            }
        }
        else {
            colourDrawable = R.drawable._foul;
        }
        colourBMP = BitmapFactory.decodeResource(getResources(),colourDrawable);
        colourBMP = Bitmap.createScaledBitmap(colourBMP, 60,60, true);
        return colourBMP;
    }

    public void bClose(View view) {
        finish();
    }
}

