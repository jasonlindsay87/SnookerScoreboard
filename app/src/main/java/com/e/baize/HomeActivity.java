package com.e.baize;
import androidx.annotation.RequiresApi;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.AppCompatActivity;

public class HomeActivity extends AppCompatActivity {
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        Toolbar homeBar = (Toolbar) findViewById(R.id.homeToolbar);
        setSupportActionBar(homeBar);
    }

    public void sendMessage(View view) {
        Intent Intent = new Intent(HomeActivity.this, ScoreboardActivity.class);
        EditText eP1Name = findViewById(R.id.p1name);
        EditText eP2Name = findViewById(R.id.p2name);
        String sP1Name = "";
        String sP2Name = "";

        if (eP1Name.getText().toString().trim().length() < 1) {
            sP1Name = "Player One";
        }
        else {
            sP1Name = eP1Name.getText().toString();
        }
        if (eP2Name.getText().toString().trim().length() < 1) {
            sP2Name = "Player Two";
        }
        else {
            sP2Name = eP2Name.getText().toString();
        }

        Intent.putExtra("sP1name", sP1Name);
        Intent.putExtra("sP2name", sP2Name);
        startActivity(Intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);

        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) { switch(item.getItemId()) {
        case R.id.add:

            return(true);
        case R.id.about:
            //add the function to perform here
            return(true);
        case R.id.exit:
            this.finishAffinity();
            return(true);
    }
        return(super.onOptionsItemSelected(item));
    }

}
