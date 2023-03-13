package com.example.jammin.utils_chat

import com.google.gson.annotations.SerializedName

data class KeywordReportResponse(
    @SerializedName("keyword") val keyword : Int,
    @SerializedName("sentence") val sentence : String
)
