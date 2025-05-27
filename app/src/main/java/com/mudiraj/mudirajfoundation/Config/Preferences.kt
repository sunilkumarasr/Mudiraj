package com.mudiraj.mudirajfoundation.Config

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName

object Preferences {

    private const val PREFERENCE_NAME = "Disability"

    const val LOGINCHECK = "LOGINCHECK"
    const val userId = "userId"
    const val state = "state"
    const val constituencies = "constituencies"
    const val name = "name"
    const val mobileNumber = "mobileNumber"
    const val address = "address"
    const val email= "email"
    const val languageCode = "languageCode"
    const val cartCount = "cartCount"
    const val addressPosition = "addressPosition"
    const val minAmount = "minAmount"
    const val cityName = "cityName"

    const val id = "id"
    const val role = "role"
    const val date_of_birth = "date_of_birth"
    const val care_of = "care_of"
    const val gender = "gender"
    const val password = "password"
    const val image = "image"
    const val business_name = "business_name"
    const val mandal = "mandal"
    const val ward = "ward"
    const val pin_code = "pin_code"
    const val current_occupation = "current_occupation"
    const val organization_company = "organization_company"
    const val designation = "designation"
    const val work_location = "work_location"
    const val permanent_address = "permanent_address"
    const val membership_type = "membership_type"
    const val amount = "amount"
    const val facebook_profile = "facebook_profile"
    const val facebook_followers = "facebook_followers"
    const val instagram_profile = "instagram_profile"
    const val instagram_followes = "instagram_followes"
    const val twitter_x_profile = "twitter_x_profile"
    const val twitter_x_follwers = "twitter_x_follwers"
    const val linkedin_profile = "linkedin_profile"
    const val youtube_channel = "youtube_channel"
    const val youtube_followers = "youtube_followers"
    const val whatsapp_number = "whatsapp_number"
    const val telegram_username_channel = "telegram_username_channel"
    const val skills_interests = "skills_interests"
    const val volunteer_experience = "volunteer_experience"
    const val affiliated_organizations = "affiliated_organizations"
    const val marital_status = "marital_status"
    const val blood_group = "blood_group"
    const val two_wheeler = "two_wheeler"
    const val four_wheeler = "four_wheeler"
    const val status = "status"


    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE)
    }

    fun saveFloatValue(context: Context, key: String, value: Float) {
        with(getSharedPreferences(context).edit()) {
            putFloat(key, value)
            apply()
        }
    }

    fun loadFloatValue(context: Context, key: String, defValue: Float): Float {
        return getSharedPreferences(context).getFloat(key, defValue)
    }

    fun saveStringValue(context: Context, key: String, value: String) {
        with(getSharedPreferences(context).edit()) {
            putString(key, value)
            apply()
        }
    }

    fun loadStringValue(context: Context, key: String, defValue: String): String? {
        return getSharedPreferences(context).getString(key, defValue)
    }

    fun saveLongValue(context: Context, key: String, value: Long) {
        with(getSharedPreferences(context).edit()) {
            putLong(key, value)
            apply()
        }
    }

    fun loadLongValue(context: Context, key: String, defValue: Long): Long {
        return getSharedPreferences(context).getLong(key, defValue)
    }

    fun saveIntegerValue(context: Context, key: String, value: Int) {
        with(getSharedPreferences(context).edit()) {
            putInt(key, value)
            apply()
        }
    }

    fun loadIntegerValue(context: Context, key: String, defValue: Int): Int {
        return getSharedPreferences(context).getInt(key, defValue)
    }

    fun saveBooleanValue(context: Context, key: String, value: Boolean) {
        with(getSharedPreferences(context).edit()) {
            putBoolean(key, value)
            apply()
        }
    }

    fun deleteSharedPreferences(context: Context) {
        with(getSharedPreferences(context).edit()) {
            clear()
            commit() // Note: `commit()` is synchronous; you may want to use `apply()` for async behavior
        }
    }

    fun saveContactAsynParser(context: Context, key: String, value: String) {
        with(getSharedPreferences(context).edit()) {
            val json = Gson().toJson(value)
            putString(key, json)
            apply()
        }
    }

    fun loadContactAsynParser(context: Context, key: String, defValue: String): String? {
        return getSharedPreferences(context).getString(key, defValue)
    }

}