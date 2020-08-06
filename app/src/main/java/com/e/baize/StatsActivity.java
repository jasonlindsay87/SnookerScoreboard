package com.e.baize;

import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
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
    public int iFrameCount;
    public int iFramesAdded = 0;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats_layout);
        statsTable = (TableLayout) findViewById(R.id.tblStats);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.mStatsItems = (StatsItems) getIntent().getSerializableExtra("mMatchStats");
            this.mPlayerOne = (Player) getIntent().getSerializableExtra("mPlayerOne");
            this.mPlayerTwo = (Player) getIntent().getSerializableExtra("mPlayerTwo");
            this.iFrameCount = extras.getInt("iFrameCount");
        }
        addNames();
        addMatchScores();
        addScores();
    }

    private void addScores() {
        while ((iFramesAdded < mStatsItems.p1Scores.size()) && (iFramesAdded < mStatsItems.p2Scores.size())) {
            addFrameHeader(iFramesAdded + 1);
            p1ScoreTable = new TableLayout(this);
            p2ScoreTable = new TableLayout(this);
            frameTable = new TableLayout(this);

            TableRow rTotals = new TableRow(this);
            TextView tvP1Total = new TextView(this);
            TextView tvP2Total = new TextView(this);

            for (int j = iFramesAdded; j < iFramesAdded + 1; j++) {
                int frameTotal = 0;
                for (int k = 0; k < mStatsItems.p1Scores.get(iFramesAdded).size(); k++) {
                    TableRow rScores = new TableRow(this);
                    rScores.setGravity(Gravity.CENTER);
                    String sScore = (String) mStatsItems.p1Scores.get(iFramesAdded).get(k);
                    TextView tvScores = new TextView(this);
                    tvScores.setTextColor(getResources().getColor(R.color.colorWhite));
                    tvScores.setText(sScore);
                    tvScores.setTextSize(18);
                    tvScores.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    rScores.addView(tvScores);
                    p1ScoreTable.addView(rScores);
                    frameTotal += Integer.parseInt(mStatsItems.p1Scores.get(iFramesAdded).get(k));
                }
                String sTotal = "(" + Integer.toString(frameTotal) + ")";
                tvP1Total.setText(sTotal);
                tvP1Total.setTextColor(getResources().getColor(R.color.colorYellow));
                tvP1Total.setTypeface(null, Typeface.BOLD);
                tvP1Total.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }

            for (int j = iFramesAdded; j < iFramesAdded + 1; j++) {
                int frameTotal = 0;
                for (int k = 0; k < mStatsItems.p2Scores.get(iFramesAdded).size(); k++) {
                    TableRow rScores = new TableRow(this);
                    rScores.setGravity(Gravity.CENTER);
                    String sScore = (String) mStatsItems.p2Scores.get(iFramesAdded).get(k);
                    TextView tvScores = new TextView(this);
                    tvScores.setTextColor(getResources().getColor(R.color.colorWhite));
                    tvScores.setText(sScore);
                    tvScores.setTextSize(18);
                    tvScores.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    rScores.addView(tvScores);
                    p2ScoreTable.addView(rScores);
                    frameTotal += Integer.parseInt(mStatsItems.p2Scores.get(iFramesAdded).get(k));
                }

                String sTotal = "(" + Integer.toString(frameTotal) + ")";
                tvP2Total.setText(sTotal);
                tvP2Total.setTextColor(getResources().getColor(R.color.colorYellow));
                tvP2Total.setTypeface(null, Typeface.BOLD);
                tvP2Total.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            }
            rTotals.addView(tvP1Total);
            rTotals.addView(tvP2Total);
            TableRow rScores = new TableRow(this);
            rScores.addView(p1ScoreTable);
            rScores.addView(p2ScoreTable);
            statsTable.addView(rScores);
            statsTable.addView(rTotals);
            iFramesAdded++;
        }

    }

    public void addNames() {
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
        rNames.addView(tvNameP1);
        rNames.addView(tvNameP2);
        statsTable.addView(rNames);
    }

    public void addFrameHeader(int iFrameNumber) {
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
        rFrame.addView(tvFrame);
        statsTable.addView(rFrame);
    }

    public void addMatchScores(){
        String sFrames = mStatsItems.getP1Frames(mPlayerOne) + "     -     " + mStatsItems.getP1Frames(mPlayerTwo);
        TextView tvFrames = new TextView(this);
        TableRow rFrames = new TableRow(this);
        TableRow.LayoutParams rowLayout = new TableRow.LayoutParams();
        tvFrames.setLayoutParams(rowLayout);
        tvFrames.setText(sFrames);
        tvFrames.setTextColor(getResources().getColor(R.color.colorWhite));
        tvFrames.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvFrames.setBackgroundColor(getResources().getColor(R.color.colorDark));
        tvFrames.setTextColor(getResources().getColor(R.color.colorWhite));
        tvFrames.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        tvFrames.setTextSize(15);
        tvFrames.setPadding(0, 5, 0, 5);
        tvFrames.setTypeface(null, Typeface.BOLD);
        rowLayout.span = 2;
        rFrames.addView(tvFrames);
        statsTable.addView(rFrames);
    }

    public void bClose(View view) {
        finish();
    }
}

