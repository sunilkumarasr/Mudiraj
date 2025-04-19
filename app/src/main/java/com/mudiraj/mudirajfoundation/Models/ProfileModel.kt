package com.mudiraj.mudirajfoundation.Models

import com.google.gson.annotations.SerializedName

data class ProfileModel(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String
)
