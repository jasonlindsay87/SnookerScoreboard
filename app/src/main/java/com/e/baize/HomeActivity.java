package com.e.baize;
import androidx.annotation.RequiresApi;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.util.Linkify;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    EditText eP1Name;
    EditText eP2Name;
    Player playerOne;
    Player playerTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar homeBar = findViewById(R.id.homeToolbar);
        homeBar.setTitle("");
        setSupportActionBar(homeBar);


        eP1Name = findViewById(R.id.p1name);
        eP2Name = findViewById(R.id.p2name);

        eP1Name.setHint(Option.getDefaultPlayerName(1));
        eP2Name.setHint(Option.getDefaultPlayerName(2));

    }

    @Override
        public void onResume() {
        super.onResume();
        eP1Name.setHint(Option.getDefaultPlayerName(1));
        eP2Name.setHint(Option.getDefaultPlayerName(2));
    }

    public void StartGame(View view) {
        Animate.animateButton(view.findViewById(R.id.button));
        Intent startGameIntent = new Intent(HomeActivity.this, ScoreboardActivity.class);
        eP1Name = findViewById(R.id.p1name);
        eP2Name = findViewById(R.id.p2name);
        String sP1Name = "";
        String sP2Name = "";

        if (eP1Name.getText().toString().trim().length() < 1) {
            sP1Name = Option.getDefaultPlayerName(1);
        }
        else {
            sP1Name = eP1Name.getText().toString();
        }
        if (eP2Name.getText().toString().trim().length() < 1) {
            sP2Name = Option.getDefaultPlayerName(2);
        }
        else {
            sP2Name = eP2Name.getText().toString();
        }

        playerOne = new Player();
        playerTwo = new Player();

        playerOne.playerName = sP1Name;
        playerTwo.playerName = sP2Name;
        SavePlayers(playerOne, playerTwo);

        Game game = new Game(
          playerOne,
          playerTwo,
          new ArrayList<Frame>()
        );

        game.gameID = (int)Game.addGame(game);
        startGameIntent.putExtra("Game", game);

        startActivity(startGameIntent);
    }

    public void SavedGames(View view){
        Animate.animateButton(view.findViewById(R.id.savedGamesButton));
        Intent savedGamesIntent = new Intent(HomeActivity.this, SavedGamesActivity.class);
        startActivity(savedGamesIntent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.options:
            Intent optionsIntent = new Intent(HomeActivity.this, OptionsActivity.class);
            startActivity(optionsIntent);
            return(true);
        case R.id.saved_games:
            Intent savedGamesIntent = new Intent(HomeActivity.this, SavedGamesActivity.class);
            startActivity(savedGamesIntent);
            return(true);
        case R.id.exit:
            this.finishAffinity();
            return(true);
        case R.id.about:
           AlertDialog.Builder builder = new AlertDialog.Builder(this)
                .setTitle("About")
                .setMessage(Html.fromHtml("Snooker Scoreboard v5.1 <br><br>" +
                        "<a href='https://paypal.me/BaizeSoftware?country.x=GB&locale.x=en_GB'>Donate</a> <span> \u2615</span><br><br>" +
                        "Copyright \u00A9 2022 Baize Software"))
                .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {  }
                });

            AlertDialog dialog = builder.create();
            dialog.show();
            ((TextView) dialog.findViewById(android.R.id.message)).setMovementMethod(LinkMovementMethod.getInstance());
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

    public void SavePlayers(Player playerOne, Player playerTwo){
            this.playerOne.playerID = (int)Player.addPlayer(playerOne);
            this.playerTwo.playerID = (int)Player.addPlayer(playerTwo);
    }
}
