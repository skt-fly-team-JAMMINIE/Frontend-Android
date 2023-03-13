package com.example.jammin.utils_draw;


import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface GetVoice {
    @GET("/htp/{type}")
    Call<VoiceResponse> TTSVoice (@Path("type") String type);
}
