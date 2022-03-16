package com.e.baize;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Vibrator;
import android.view.View;

import java.util.ArrayList;


public class SavedGamesActivity extends AppCompatActivity implements GameRecyclerViewAdapter.ItemClickListener{
    GameRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_saved_games);

        // set up the RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new GameRecyclerViewAdapter(this, getGames());
        adapter.setClickListener(this);
        recyclerView.setAdapter(adapter);


        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), layoutManager.getOrientation());
        //recyclerView.addItemDecoration(dividerItemDecoration);
    }

    @Override
    public void onSelect(View view, int position) {
        Game game = Game.getGame(adapter.getItem(position));

        Intent gameIntent = new Intent(SavedGamesActivity.this, ScoreboardActivity.class);
        gameIntent.putExtra("Game", game);
        startActivity(gameIntent);
    }

    public void onDelete(View view, final int position, SavedGame game) {
        Vibrator vibe = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibe.vibrate(100);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this, android.R.style.Theme_DeviceDefault_Dialog_Alert);
        String message = " \n \n" + game.CreatedDate + "\n" + game.PlayerOneName +" ["+ game.playerOneScore +"]   vs.   ["+ game.playerTwoScore + "] " + game.PlayerTwoName;
        alertBuilder.setMessage("Are you sure you want to delete the following game?" + message)
                .setTitle("Delete Game")
                .setCancelable(true)
                .setPositiveButton
                        (
                            "Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                adapter.deleteItem(position);
                                }
                            }
                        )
                .setNegativeButton
                        (
                            "No", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                }
                            }
                        );
        AlertDialog dialog = alertBuilder.create();
        dialog.show();
    }

    public void onLongClick(View view, int position) {

    }

    public ArrayList<SavedGame> getGames(){
        ArrayList<SavedGame> gameList = Game.loadGames();
        gameList.sort((SavedGame g1, SavedGame g2) ->(Integer.compare(g2.GameID, g1.GameID)));
        return gameList;
    }

    public void bClose(View view){
        Animate.animateButton(view.findViewById(R.id.btnBack));
        finish();
    }


}
