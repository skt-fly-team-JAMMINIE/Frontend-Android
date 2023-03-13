package com.example.jammin.utils_chat


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


// const val BASE_URL = "http://10.0.2.2:8000"

const val BASE_URL = "http://34.64.152.40:8000"


 //timeout setting
var okHttpClient = OkHttpClient().newBuilder()
    .connectTimeout(2000000, TimeUnit.SECONDS)
    .readTimeout(2000000, TimeUnit.SECONDS)
    .writeTimeout(2000000, TimeUnit.SECONDS)
    .build()


fun getRetrofit():Retrofit{
    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create()).build()

    return retrofit
}