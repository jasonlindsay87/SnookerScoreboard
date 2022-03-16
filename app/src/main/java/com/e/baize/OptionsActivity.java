package com.e.baize;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import android.graphics.Path;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

public class OptionsActivity extends AppCompatActivity {
    public EditText p1Name;
    public EditText p2Name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_options);
        SwitchCompat screenOnSwitch = findViewById(R.id.screenOnSwitch);
        screenOnSwitch.setChecked(Option.getOptionSwitchState("KeepScreenOn"));
        screenOnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean checked) {
                if (checked) {
                    Option.setOptionSwitchState(true);
                } else {
                    Option.setOptionSwitchState(false);
                }
            }
        });

        p1Name = (EditText) findViewById(R.id.p1Name);
        p1Name.setHint(Option.getOptionValue(getResources().getString(R.string.options_defaultP1_name)));
        p2Name = (EditText) findViewById(R.id.p2Name);
        p2Name.setHint(Option.getOptionValue(getResources().getString(R.string.options_defaultP2_name)));
    }

    public void bUpdateP1(View view){
        Animate.animateButton(view.findViewById(R.id.bSave1));
        Option.setDefaultPlayerName( 1, p1Name.getText().toString());
        Toast.makeText(this, "Default Player One name updated.", Toast.LENGTH_SHORT).show();
    }
    public void bUpdateP2(View view){
        Animate.animateButton(view.findViewById(R.id.bSave2));
        Option.setDefaultPlayerName( 2, p2Name.getText().toString());
        Toast.makeText(this, "Default Player Two name updated.", Toast.LENGTH_SHORT).show();
    }

    public void bClose(View view){
        Animate.animateButton(view.findViewById(R.id.btnBack));
        finish();
    }

}