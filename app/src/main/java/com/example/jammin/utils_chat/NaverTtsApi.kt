package com.example.jammin.utils_chat

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface NaverTtsApi {

//    @Headers(
//        "X-NCP-APIGW-API-KEY-ID: {ahce7hh5it}",
//        "X-NCP-APIGW-API-KEY: {Y4oL5wkWpTbaIDVVrgjCxHSFwT8XVPeeihINQEBo}"
//    )

    @POST("tts")
    @FormUrlEncoded
    fun textToSpeech(
        @Header("X-NCP-APIGW-API-KEY-ID") clientId:String,
        @Header("X-NCP-APIGW-API-KEY") clientSecret:String,
        @Field("speaker") speaker: String,
        @Field("speed") speed: Int,
        @Field("text") text: String
    ): Call<ResponseBody>
}

