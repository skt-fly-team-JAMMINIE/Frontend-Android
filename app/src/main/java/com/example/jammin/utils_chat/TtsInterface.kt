package com.example.jammin.utils_chat

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface TtsInterface {

    @POST("/tts")
    fun getTTS(@Body sentence: String): Call<TtsResponse>

}