package com.mudiraj.mudirajfoundation.Models

import com.google.gson.annotations.SerializedName

data class BannersModel(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val response: ArrayList<BannersResponse>,
)

data class BannersResponse(
    @SerializedName("id") val id : String,
    @SerializedName("image") val image : String,
    @SerializedName("constituencies") val constituencies : String,
    @SerializedName("created_at") val created_at : String,
    @SerializedName("created_by") val created_by : String,
    @SerializedName("updated_at") val updated_at : String,
    @SerializedName("updated_by") val updated_by : String,
    @SerializedName("status") val status : String,
    @SerializedName("position_order") val position_order : String,
)