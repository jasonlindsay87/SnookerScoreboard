package com.e.baize;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

public class Animate {

    public static void animateButton(View view){
        Animation anim = AnimationUtils.loadAnimation(SnookerScoreboard.getAppContext(), R.anim.ball_anim);
        anim.reset();
        view.clearAnimation();
        view.startAnimation(anim);
    }
}
