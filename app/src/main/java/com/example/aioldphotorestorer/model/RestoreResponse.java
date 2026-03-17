package com.example.aioldphotorestorer.model;

import com.google.gson.annotations.SerializedName;

public class RestoreResponse {
    @SerializedName("restoredImageUrl")
    private String restoredImageUrl;

    @SerializedName("requestId")
    private String requestId;

    public String getRestoredImageUrl() {
        return restoredImageUrl;
    }

    public String getRequestId() {
        return requestId;
    }
}
