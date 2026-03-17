package com.example.aioldphotorestorer.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.aioldphotorestorer.databinding.ActivityUploadPhotoBinding;
import com.example.aioldphotorestorer.util.FileUtils;

import java.io.File;
import java.io.IOException;

public class UploadPhotoActivity extends AppCompatActivity {

    private ActivityUploadPhotoBinding binding;
    private Uri cameraUri;

    private final ActivityResultLauncher<Intent> galleryPicker = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null && result.getData().getData() != null) {
                    Uri selected = result.getData().getData();
                    previewImage(selected);
                }
            }
    );

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == RESULT_OK && cameraUri != null) {
                    previewImage(cameraUri);
                }
            }
    );

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUploadPhotoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.buttonGallery.setOnClickListener(v -> openGallery());
        binding.buttonCamera.setOnClickListener(v -> openCamera());

        binding.buttonRestoreNow.setOnClickListener(v -> {
            String selectedPath = (String) binding.previewImage.getTag();
            if (selectedPath != null) {
                Intent intent = new Intent(this, ProcessingActivity.class);
                intent.putExtra("image_uri", selectedPath);
                startActivity(intent);
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryPicker.launch(intent);
    }

    private void openCamera() {
        try {
            File imageFile = FileUtils.createTempImageFile(this);
            cameraUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imageFile);
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
            cameraLauncher.launch(cameraIntent);
        } catch (IOException e) {
            binding.statusText.setText("Unable to open camera");
        }
    }

    private void previewImage(Uri uri) {
        Glide.with(this).load(uri).into(binding.previewImage);
        binding.previewImage.setTag(uri.toString());
        binding.statusText.setText("Ready for AI restore");
        binding.buttonRestoreNow.setEnabled(true);
    }
}
