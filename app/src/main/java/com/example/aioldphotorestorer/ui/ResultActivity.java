package com.example.aioldphotorestorer.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.aioldphotorestorer.databinding.ActivityResultBinding;
import com.example.aioldphotorestorer.util.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ResultActivity extends AppCompatActivity {

    private ActivityResultBinding binding;
    private String afterUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String beforeUri = getIntent().getStringExtra("before_uri");
        afterUrl = getIntent().getStringExtra("after_url");

        Glide.with(this).load(beforeUri).into(binding.beforeAfterView.getBeforeImage());
        Glide.with(this).load(afterUrl).into(binding.beforeAfterView.getAfterImage());
        binding.compareSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                binding.beforeAfterView.updateMask(progress / 100f);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });

        binding.buttonDownload.setOnClickListener(v -> downloadRestoredImage());
        binding.buttonShare.setOnClickListener(v -> shareRestoredImage());
    }

    private void downloadRestoredImage() {
        Glide.with(this).asBitmap().load(afterUrl).into(new BitmapTarget(false));
    }

    private void shareRestoredImage() {
        Glide.with(this).asBitmap().load(afterUrl).into(new BitmapTarget(true));
    }

    private class BitmapTarget extends CustomTarget<Bitmap> {
        private final boolean share;

        private BitmapTarget(boolean share) {
            this.share = share;
        }

        @Override
        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
            saveBitmap(resource, share);
        }

        @Override
        public void onLoadCleared(@Nullable Drawable placeholder) {
        }
    }

    private void saveBitmap(Bitmap bitmap, boolean share) {
        File file = FileUtils.getDownloadTarget(this);
        try (FileOutputStream out = new FileOutputStream(file)) {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 95, out);
            out.flush();
            binding.resultStatus.setText("Saved: " + file.getName());
            if (share) {
                Uri contentUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", file);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri);
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Restored with AI Old Photo Restorer & Colorizer");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                startActivity(Intent.createChooser(shareIntent, "Share restored photo"));
            }
        } catch (IOException e) {
            binding.resultStatus.setText("Save failed: " + e.getMessage());
        }
    }
}
