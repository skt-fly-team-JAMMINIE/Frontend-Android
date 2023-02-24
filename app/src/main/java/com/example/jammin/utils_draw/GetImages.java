package com.example.jammin.utils_draw;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface GetImages {
    @POST("/images")
    Call<ImagesResponse> PostImages(@Body Images images);

    @Multipart
    @POST("/uploadfilehouse")
    Call<UploadImageResponse> UploadHouseImage (@Part MultipartBody.Part imagefile);

    @Multipart
    @POST("/uploadfiletree")
    Call<UploadImageResponse> UploadTreeImage (@Part MultipartBody.Part imagefile);

    @Multipart
    @POST("/uploadfileperson")
    Call<UploadImageResponse> UploadPersonImage (@Part MultipartBody.Part imagefile);

}
