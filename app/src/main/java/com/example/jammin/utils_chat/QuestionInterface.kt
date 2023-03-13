package com.example.jammin.utils_chat

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface QuestionInterface {

    // 임시 테스트용 메소드 >> 삭제 예정
    @GET("/question/normal1")
    fun getTestInput(): Call<QuestionResponse>

    @GET("/question/{type}")
    fun getNormalQuestion(@Path("type") type: String):Call<QuestionResponse>

    @GET("/question/keyword/{keyword}")
    fun getKeywordQuestion(@Path("keyword") type: Int):Call<QuestionResponse>


    // ======= 02-26 ==========
    // keyword question 2
    @GET("/question2/keyword")
    fun getKeyword2Question():Call<KeywordQResponse>

}