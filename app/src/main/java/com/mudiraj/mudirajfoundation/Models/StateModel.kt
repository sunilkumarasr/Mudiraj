package com.mudiraj.mudirajfoundation.Models

import com.google.gson.annotations.SerializedName

data class StateModel(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val response: ArrayList<StateListResponse>,
)

data class StateListResponse(
    @SerializedName("id") val id : String,
    @SerializedName("name") val name : String,
    @SerializedName("status") val status : String
)