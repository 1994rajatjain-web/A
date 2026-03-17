package com.example.aioldphotorestorer.billing;

import android.app.Activity;
import android.content.Context;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesUpdatedListener;

import java.util.List;

public class BillingManager implements PurchasesUpdatedListener {
    private static final String PREMIUM_PRODUCT_ID = "premium_unlimited_restores";
    private final BillingClient billingClient;
    private boolean premiumActive;

    public BillingManager(Context context) {
        billingClient = BillingClient.newBuilder(context)
                .enablePendingPurchases()
                .setListener(this)
                .build();
    }

    public void connect(Runnable onReady) {
        billingClient.startConnection(new BillingClientStateListener() {
            @Override
            public void onBillingSetupFinished(BillingResult billingResult) {
                if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                    onReady.run();
                }
            }

            @Override
            public void onBillingServiceDisconnected() {
            }
        });
    }

    public void launchPremiumPurchase(Activity activity, BillingFlowParams flowParams) {
        billingClient.launchBillingFlow(activity, flowParams);
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, List<Purchase> purchases) {
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK && purchases != null) {
            for (Purchase purchase : purchases) {
                if (purchase.getProducts().contains(PREMIUM_PRODUCT_ID)) {
                    premiumActive = true;
                    acknowledge(purchase);
                }
            }
        }
    }

    private void acknowledge(Purchase purchase) {
        if (purchase.isAcknowledged()) {
            return;
        }
        AcknowledgePurchaseParams params = AcknowledgePurchaseParams.newBuilder()
                .setPurchaseToken(purchase.getPurchaseToken())
                .build();
        billingClient.acknowledgePurchase(params, result -> {
        });
    }

    public boolean isPremiumActive() {
        return premiumActive;
    }

    public void close() {
        billingClient.endConnection();
    }
}
