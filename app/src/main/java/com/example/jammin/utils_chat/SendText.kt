package com.example.jammin.utils_chat

import com.google.gson.annotations.SerializedName

data class SendText(
    @SerializedName(value = "userid")val userid: Int,
    @SerializedName(value = "text")val text: String
)
