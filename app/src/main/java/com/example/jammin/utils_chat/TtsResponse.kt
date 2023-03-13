package com.example.jammin.utils_chat

import com.google.gson.annotations.SerializedName

data class TtsResponse(
    @SerializedName("sentence") val sentence:String
)
