package com.example.aioldphotorestorer.api;

import com.example.aioldphotorestorer.model.RestoreResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface RestoreApiService {
    @Multipart
    @POST("restore")
    Call<RestoreResponse> restorePhoto(
            @Part MultipartBody.Part image,
            @Part("colorize") RequestBody colorize,
            @Part("faceRestore") RequestBody faceRestore,
            @Part("scratchRemoval") RequestBody scratchRemoval,
            @Part("upscaleHd") RequestBody upscaleHd
    );
}
