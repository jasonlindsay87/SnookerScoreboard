package com.e.baize;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Player extends AppCompatActivity implements Serializable  {
    public String Name;
    public List<Float> ScoreHistory = new ArrayList<>();
    private int FrameCount;

    public void setScore(float score) {
        ScoreHistory.add(score);
    }

    public List<Float> getScoreHistory() {
        return ScoreHistory;
    }

    public int getScore() {
        int score = 0;
        for (Float i : ScoreHistory) {
            score += i;
            }
        return score;
    }

    public void undoScore(){
        if(ScoreHistory.size() > 0){
            ScoreHistory.remove(ScoreHistory.size() -1);
            }
        }

    public void clearScore(){
        ScoreHistory.clear();
    }

    public int getFrameCount(){
        return FrameCount;
    }

    public void frameWin(){
        FrameCount ++;
    }

    public List<String> getFrameScores() {
        List<String> scoreList = new ArrayList<>();
        for (int i = 0; i < ScoreHistory.size(); i++) {
            String sScore = (Float.toString(ScoreHistory.get(i)));
            scoreList.add(sScore);
        }
        return scoreList;
    }
}

