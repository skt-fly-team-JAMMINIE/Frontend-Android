package com.example.jammin.utils_chat

import com.google.gson.annotations.SerializedName

data class DrawReportResponse(

    @SerializedName("userid") val userid : Int,
    @SerializedName("day") val day : String,
    @SerializedName("house_img") val house_img : String,
    @SerializedName("tree_img") val tree_img : String,
    @SerializedName("person_img") val person_img : String,
    @SerializedName("result") val result : String,
    @SerializedName("house_text") val house_text : String,
    @SerializedName("tree_text") val tree_text : String,
    @SerializedName("person_text") val person_text : String,
    @SerializedName("f_type1") val f_type1 : Int,
    @SerializedName("f_type2") val f_type2 : Int,
    @SerializedName("f_type3") val f_type3 : Int,

)
