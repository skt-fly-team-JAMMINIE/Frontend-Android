package com.example.jammin.utils_chat

import com.google.gson.annotations.SerializedName

data class TTS(
    @SerializedName(value = "sentence")val sentence: String
)
