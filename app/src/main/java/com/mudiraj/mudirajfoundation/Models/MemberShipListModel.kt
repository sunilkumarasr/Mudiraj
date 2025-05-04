package com.mudiraj.mudirajfoundation.Models

import com.google.gson.annotations.SerializedName

data class MemberShipListModel(
    @SerializedName("status") val status: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("data") val response: ArrayList<MemberShipListResponse>,
)

data class MemberShipListResponse(
    @SerializedName("membership_id") val membershipId : String,
    @SerializedName("name") val name : String,
    @SerializedName("membership_list") val membershipList: ArrayList<MemberShipList>,
)

data class MemberShipList(
    @SerializedName("user_id") val userId : String,
    @SerializedName("role") val role : String,
    @SerializedName("full_name") val fullName : String,
    @SerializedName("email") val email : String,
    @SerializedName("phone") val phone : String,
    @SerializedName("image") val image : String,
    @SerializedName("business_name") val businessName : String,
    @SerializedName("full_address") val fullAddress : String,
    @SerializedName("state") val state : String,
    @SerializedName("constituencies") val constituencies : String,
    @SerializedName("membership_type") val membershipType : String,
    @SerializedName("amount") val amount : String,
)