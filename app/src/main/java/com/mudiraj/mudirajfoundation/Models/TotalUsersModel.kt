package com.mudiraj.mudirajfoundation.Models

import com.google.gson.annotations.SerializedName

data class TotalUsersModel(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val response: TotalUsersResponse,
)

data class TotalUsersResponse(
    @SerializedName("id") val id: String,
    @SerializedName("count") val count: String,
    @SerializedName("created_at") val created_at: String,
    @SerializedName("updated_at") val updated_at: String,
)