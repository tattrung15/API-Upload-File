package com.hithaui.services;

import com.hithaui.models.FileUpload;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ImageService {

    @POST("/images")
    @Multipart
    Call<FileUpload> uploadImage(@Part MultipartBody.Part image);
}
