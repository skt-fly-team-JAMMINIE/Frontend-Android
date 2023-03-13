package com.example.jammin.utils_chat

import com.google.gson.annotations.SerializedName

data class UserChatResponse(
    @SerializedName("msg") val msg: String,
    @SerializedName("bot") val bot: String,
    @SerializedName("send_tts") val send_tts:String
)


data class UserChat2Response(
    @SerializedName("msg") val msg: String
)