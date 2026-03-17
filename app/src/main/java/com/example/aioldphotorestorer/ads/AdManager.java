package com.example.aioldphotorestorer.ads;

import android.app.Activity;
import android.content.Context;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.LoadAdError;
import com.google.android.gms.ads.rewarded.RewardItem;
import com.google.android.gms.ads.rewarded.RewardedAd;
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback;

public class AdManager {
    private RewardedAd rewardedAd;
    private static final String TEST_REWARDED_UNIT = "ca-app-pub-3940256099942544/5224354917";

    public void loadRewardedAd(Context context) {
        AdRequest request = new AdRequest.Builder().build();
        RewardedAd.load(context, TEST_REWARDED_UNIT, request, new RewardedAdLoadCallback() {
            @Override
            public void onAdLoaded(RewardedAd ad) {
                rewardedAd = ad;
            }

            @Override
            public void onAdFailedToLoad(LoadAdError loadAdError) {
                rewardedAd = null;
            }
        });
    }

    public void showRewardedAd(Activity activity, Runnable onUnlocked) {
        if (rewardedAd == null) {
            onUnlocked.run();
            return;
        }
        rewardedAd.show(activity, rewardItem -> {
            if (isRewardGranted(rewardItem)) {
                onUnlocked.run();
            }
        });
    }

    private boolean isRewardGranted(RewardItem rewardItem) {
        return rewardItem != null && rewardItem.getAmount() > 0;
    }
}
