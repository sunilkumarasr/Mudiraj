package com.mudiraj.mudirajfoundation.Models

import com.google.gson.annotations.SerializedName

data class ForgotModel(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("code") val code: Int
)
