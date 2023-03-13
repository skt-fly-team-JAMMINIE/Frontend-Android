package com.example.jammin.utils_chat

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserChatInterface {


    @POST("/report/chat/model")
    fun inputChatToModel(@Body sendText: SendText): Call<UserChatResponse>

    @POST("/report/chat")
    fun inputChat(@Body sendText: SendText): Call<UserChat2Response>


    ////////// 02-05 추가 >> 감정분석 모델 ////////////
    @GET("/analysis")
    fun getAnalysis():Call<ResponseBody>
}