package com.example.socialpuig;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.airbnb.lottie.LottieAnimationView;

import java.util.Timer;
import java.util.TimerTask;

public class SplashActivity extends AppCompatActivity {

    ImageView appLogoImage;
    //LottieAnimationView lottie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        };
        Timer tiempo = new Timer();
        tiempo.schedule(tarea, 2000);

        //animaciones

        appLogoImage = findViewById(R.id.imageViewsplash);
        //lottie = findViewById(R.id.lottie);


        Animation animationUp = AnimationUtils.loadAnimation(this, R.anim.anim_up);
        Animation animationDown = AnimationUtils.loadAnimation(this, R.anim.anim_down);

        appLogoImage.setAnimation(animationUp);

        final LottieAnimationView lottieAnimationView = findViewById(R.id.lottie);
        lottieAnimationView.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                // No necesitas hacer nada aquí
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                // Aquí puedes iniciar la segunda animación
                segundaAnimacion();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
                // No necesitas hacer nada aquí
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
                // No necesitas hacer nada aquí
            }
        });


    }
    private void segundaAnimacion() {
        // Código para iniciar la segunda animación
        // Por ejemplo:
        LottieAnimationView segundaLottieAnimationView = findViewById(R.id.lottie);
        segundaLottieAnimationView.playAnimation();
    }
}