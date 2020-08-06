package com.e.baize;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class StatsItems extends ScoreboardActivity implements Serializable {
    public List<List<String>> p1Scores = new ArrayList<List<String>>();
    public List<List<String>> p2Scores = new ArrayList<List<String>>();

    public void saveFrames(Player playerOne, Player playerTwo) {
        saveP1Frame(playerOne);
        saveP2Frame(playerTwo);
    }

    public void saveP1Frame(Player player) {
        ArrayList<String> scoreList = new ArrayList<String>();
        for (int i = 0; i < player.getFrameScores().size(); i++) {
            String sScore = player.getFrameScores().get(i);
            scoreList.add(sScore);
        }
        p1Scores.add(scoreList);
    }

    public void saveP2Frame(Player player) {
        ArrayList<String> scoreList = new ArrayList<String>();
        for (int i = 0; i < player.getFrameScores().size(); i++) {
            String sScore = player.getFrameScores().get(i);
            scoreList.add(sScore);
        }
        p2Scores.add(scoreList);
    }

    public String getP1Frames(Player player){
        String sFrameCount = Integer.toString(player.getFrameCount());
        return sFrameCount;
    }

    public String getP2Frames(Player player){
        String sFrameCount = Integer.toString(player.getFrameCount());
        return sFrameCount;
    }
}

