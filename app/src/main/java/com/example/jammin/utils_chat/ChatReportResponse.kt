package com.example.jammin.utils_chat

import com.google.gson.annotations.SerializedName


data class ChatReportResponse(

    @SerializedName("userid") val userid : Int,
    @SerializedName("day") val day : String,
    @SerializedName("keyword") val keyword : List<String>,
    //@SerializedName("keyword") val keyword : String,
    @SerializedName("emotion") val emotion : Int,
    @SerializedName("text") val text : String

)
