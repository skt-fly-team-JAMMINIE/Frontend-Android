package com.example.jammin.utils_chat

import com.example.jammin.utils_chat.ChatReportResponse
import com.example.jammin.utils_chat.DrawReportResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import java.util.*

interface ReportInterface {

    @GET("/report/draw/{day}")
    fun getDrawReport(@Path("day") day: String): Call<DrawReportResponse>

    @GET("/report/chat/{day}")
    fun getChatReport(@Path("day") day:String): Call<ChatReportResponse>
}