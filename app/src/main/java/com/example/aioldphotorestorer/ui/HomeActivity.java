package com.example.aioldphotorestorer.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.aioldphotorestorer.adapter.FeatureAdapter;
import com.example.aioldphotorestorer.billing.BillingManager;
import com.example.aioldphotorestorer.databinding.ActivityHomeBinding;

import java.util.Arrays;

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;
    private BillingManager billingManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        billingManager = new BillingManager(this);
        billingManager.connect(() -> binding.premiumBadge.setText("Premium available"));

        binding.recyclerFeatures.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerFeatures.setAdapter(new FeatureAdapter(Arrays.asList(
                "AI Colorization",
                "Face Restoration",
                "Scratch Removal",
                "HD Upscaling"
        )));

        binding.buttonStart.setOnClickListener(v -> startActivity(new Intent(this, UploadPhotoActivity.class)));
        binding.buttonUpgrade.setOnClickListener(v -> binding.premiumBadge.setText("Connect Play Console product for billing"));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (billingManager != null) {
            billingManager.close();
        }
    }
}
