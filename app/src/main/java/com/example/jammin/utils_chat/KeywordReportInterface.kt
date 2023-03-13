package com.example.jammin.utils_chat

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface KeywordReportInterface {

    @GET("/report/dic/{keyword}")
    fun getKeywordReport(@Path("keyword") keyword: Int): Call<KeywordReportResponse>

}