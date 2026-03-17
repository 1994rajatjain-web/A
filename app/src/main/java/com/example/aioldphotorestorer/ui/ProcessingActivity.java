package com.example.aioldphotorestorer.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.aioldphotorestorer.ads.AdManager;
import com.example.aioldphotorestorer.databinding.ActivityProcessingBinding;
import com.example.aioldphotorestorer.model.RestoreResponse;
import com.example.aioldphotorestorer.repository.RestoreRepository;
import com.example.aioldphotorestorer.util.FileUtils;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProcessingActivity extends AppCompatActivity {

    private ActivityProcessingBinding binding;
    private final RestoreRepository restoreRepository = new RestoreRepository();
    private final AdManager adManager = new AdManager();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProcessingBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        adManager.loadRewardedAd(this);

        String imageUriString = getIntent().getStringExtra("image_uri");
        if (imageUriString == null) {
            finish();
            return;
        }

        adManager.showRewardedAd(this, () -> restoreImage(Uri.parse(imageUriString)));
    }

    private void restoreImage(Uri uri) {
        binding.processingStatus.setText("Uploading and restoring...");
        binding.progressIndicator.show();
        try {
            File imageFile = FileUtils.copyUriToFile(this, uri);
            RequestBody requestBody = RequestBody.create(imageFile, MediaType.parse("image/*"));
            MultipartBody.Part imagePart = MultipartBody.Part.createFormData("image", imageFile.getName(), requestBody);

            restoreRepository.restoreImage(imagePart).enqueue(new Callback<>() {
                @Override
                public void onResponse(Call<RestoreResponse> call, Response<RestoreResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        openResult(uri.toString(), response.body().getRestoredImageUrl());
                    } else {
                        binding.processingStatus.setText("Restore failed. Try again.");
                        binding.progressIndicator.hide();
                    }
                }

                @Override
                public void onFailure(Call<RestoreResponse> call, Throwable t) {
                    binding.processingStatus.setText("Network error: " + t.getMessage());
                    binding.progressIndicator.hide();
                }
            });
        } catch (IOException e) {
            binding.processingStatus.setText("Failed to read image");
            binding.progressIndicator.hide();
        }
    }

    private void openResult(String beforeUri, String restoredUrl) {
        Intent intent = new Intent(this, ResultActivity.class);
        intent.putExtra("before_uri", beforeUri);
        intent.putExtra("after_url", restoredUrl);
        startActivity(intent);
        finish();
    }
}
