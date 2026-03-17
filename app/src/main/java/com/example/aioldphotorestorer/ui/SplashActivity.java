package com.example.aioldphotorestorer.ui;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.AccelerateDecelerateInterpolator;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aioldphotorestorer.databinding.ActivitySplashBinding;

public class SplashActivity extends AppCompatActivity {

    private ActivitySplashBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySplashBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ObjectAnimator animator = ObjectAnimator.ofFloat(binding.logoImage, "alpha", 0.2f, 1f);
        animator.setDuration(1200);
        animator.setInterpolator(new AccelerateDecelerateInterpolator());
        animator.start();

        binding.getRoot().postDelayed(() -> {
            startActivity(new Intent(this, HomeActivity.class));
            finish();
        }, 1700);
    }
}
