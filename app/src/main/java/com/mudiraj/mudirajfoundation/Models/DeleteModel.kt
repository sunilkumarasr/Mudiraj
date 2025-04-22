package com.mudiraj.mudirajfoundation.Models

import com.google.gson.annotations.SerializedName

data class DeleteModel(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
)
