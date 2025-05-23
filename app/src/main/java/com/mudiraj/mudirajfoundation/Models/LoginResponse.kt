package com.mudiraj.mudirajfoundation.Models

import com.google.gson.annotations.SerializedName

data class LoginModel(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val response: LoginResponse,
)
data class LoginResponse(
    @SerializedName("id") val id: String,
    @SerializedName("role") val role: String,
    @SerializedName("user_id") val user_id: String,
    @SerializedName("full_name") val full_name: String,
    @SerializedName("state") val state: String,
    @SerializedName("constituencies") val constituencies: String,
    @SerializedName("business_name") val business_name: String,
    @SerializedName("email") val email: String,
    @SerializedName("full_address") val full_address: String,
    @SerializedName("password") val password: String,
    @SerializedName("password_hit") val password_hit: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("image") val image: String,
    @SerializedName("status") val status: String,
    @SerializedName("membership_type") val membership_type: String,
)