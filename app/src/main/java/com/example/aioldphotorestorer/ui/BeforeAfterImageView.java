package com.example.aioldphotorestorer.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.aioldphotorestorer.R;

public class BeforeAfterImageView extends FrameLayout {

    private ImageView beforeImage;
    private ImageView afterImage;

    public BeforeAfterImageView(Context context) {
        super(context);
        init();
    }

    public BeforeAfterImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BeforeAfterImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.view_before_after, this);
        beforeImage = findViewById(R.id.beforeImage);
        afterImage = findViewById(R.id.afterImageMasked);
        updateMask(0.5f);
    }

    public ImageView getBeforeImage() {
        return beforeImage;
    }

    public ImageView getAfterImage() {
        return afterImage;
    }

    public void updateMask(float progress) {
        post(() -> {
            int width = getWidth();
            afterImage.getLayoutParams().width = (int) (width * progress);
            afterImage.requestLayout();
        });
    }
}
