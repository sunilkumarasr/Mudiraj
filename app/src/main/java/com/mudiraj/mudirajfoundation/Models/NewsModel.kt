package com.mudiraj.mudirajfoundation.Models

import com.google.gson.annotations.SerializedName

data class NewsModel(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val response: ArrayList<NewsResponse>,
)


data class NewsResponse(
    @SerializedName("id") val id : String,
    @SerializedName("title") val title : String,
    @SerializedName("short_description") val short_description : String,
    @SerializedName("description") val description : String,
    @SerializedName("image") val image : String,
    @SerializedName("slug") val slug : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("created_by") val created_by : String,
    @SerializedName("status") val status : String,
    @SerializedName("constituencies") val constituencies : String,
)