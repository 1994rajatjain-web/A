package com.example.aioldphotorestorer.repository;

import com.example.aioldphotorestorer.api.ApiClient;
import com.example.aioldphotorestorer.model.RestoreResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;

public class RestoreRepository {

    public Call<RestoreResponse> restoreImage(MultipartBody.Part imagePart) {
        RequestBody boolTrue = RequestBody.create("true", MediaType.parse("text/plain"));
        return ApiClient.getApiService().restorePhoto(imagePart, boolTrue, boolTrue, boolTrue, boolTrue);
    }
}
