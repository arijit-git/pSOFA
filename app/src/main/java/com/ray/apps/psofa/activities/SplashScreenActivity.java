package com.ray.apps.psofa.activities;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.firebase.database.FirebaseDatabase;
import com.ray.apps.psofa.R;

public class SplashScreenActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        final ImageView imageView = (ImageView) findViewById(R.id.imageView);
        final Animation animation_1 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.rotate);
        final Animation animation_3 = AnimationUtils.loadAnimation(getBaseContext(),R.anim.abc_fade_out);



        animation_1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                FirebaseDatabase.getInstance().setPersistenceEnabled(true);

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                imageView.startAnimation(animation_3);
                finish();

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });



        Handler handler = new Handler();
        imageView.startAnimation(animation_1);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent i = new Intent(getBaseContext(),SignInActivity.class);
                startActivity(i);
                finish();

            }
        },2000);


    }
}
