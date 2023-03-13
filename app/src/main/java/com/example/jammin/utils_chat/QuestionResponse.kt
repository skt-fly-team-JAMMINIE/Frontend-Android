package com.example.jammin.utils_chat

import com.google.gson.annotations.SerializedName

data class QuestionResponse(
    @SerializedName("sentence") val sentence: String,
    @SerializedName("send_tts") val send_tts: String

)

// 02-26
// ============ keyword question ================
data class KeywordQResponse(
    @SerializedName("sentence1") val sentence1: String,
    @SerializedName("sentence2") val sentence2: String,
    @SerializedName("send_tts1") val send_tts1: String,
    @SerializedName("send_tts2") val send_tts2: String
)