package com.example.jammin.utils_draw;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface CallModel {
    @GET("/model/{user_id}")
    Call<ModelResponse> CallModel (@Path("user_id") Integer user_id);

    @GET("/report/keyword/{user_id}")
    Call<KeywordResponse> CallKeyword (@Path("user_id") Integer user_id);
}
