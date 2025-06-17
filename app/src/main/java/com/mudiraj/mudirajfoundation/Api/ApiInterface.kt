package com.mudiraj.mudirajfoundation.Api

import com.mudiraj.mudirajfoundation.Models.AboutUsModel
import com.mudiraj.mudirajfoundation.Models.BannersModel
import com.mudiraj.mudirajfoundation.Models.ContactUsModel
import com.mudiraj.mudirajfoundation.Models.DeleteModel
import com.mudiraj.mudirajfoundation.Models.FavouriteModel
import com.mudiraj.mudirajfoundation.Models.ForgotModel
import com.mudiraj.mudirajfoundation.Models.GalleryListModel
import com.mudiraj.mudirajfoundation.Models.LoginModel
import com.mudiraj.mudirajfoundation.Models.MemberShipListModel
import com.mudiraj.mudirajfoundation.Models.NewsModel
import com.mudiraj.mudirajfoundation.Models.PrivacyPolicyModel
import com.mudiraj.mudirajfoundation.Models.ProfileModel
import com.mudiraj.mudirajfoundation.Models.RegisterModel
import com.mudiraj.mudirajfoundation.Models.StateModel
import com.mudiraj.mudirajfoundation.Models.TermsAndConditionsModel
import com.mudiraj.mudirajfoundation.Models.TotalUsersModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.POST
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ApiInterface {

    @FormUrlEncoded
    @POST("registration")
    fun registerApi(
        @Field("api_key") apiKey: String,
        @Field("email") emailId: String,
        @Field("password") password: String,
        @Field("phone") mobileNumber: String,
        @Field("full_name") fullName: String,
        @Field("date_of_birth") date_of_birth: String,
        @Field("care_of") care_of: String,
        @Field("gender") gender: String,
        @Field("full_address") address: String,
        @Field("business_name") business_name: String,
        @Field("constituencies") constituencies: String,
        @Field("state") state: String,
        @Field("mandal") mandal: String,
        @Field("ward") ward: String,
        @Field("pin_code") pin_code: String,
        @Field("current_occupation") current_occupation: String,
        @Field("organization_company") organization_company: String,
        @Field("designation") designation: String,
        @Field("work_location") work_location: String,
        @Field("permanent_address") permanent_address: String,
        @Field("facebook_profile") facebook_profile: String,
        @Field("facebook_followers") facebook_followers: String,
        @Field("instagram_profile") instagram_profile: String,
        @Field("instagram_followes") instagram_followes: String,
        @Field("twitter_x_profile") twitter_x_profile: String,
        @Field("twitter_x_follwers") twitter_x_follwers: String,
        @Field("linkedin_profile") linkedin_profile: String,
        @Field("youtube_channel") youtube_channel: String,
        @Field("youtube_followers") youtube_followers: String,
        @Field("whatsapp_number") whatsapp_number: String,
        @Field("telegram_username_channel") telegram_username_channel: String,
        @Field("skills_interests") skills_interests: String,
        @Field("volunteer_experience") volunteer_experience: String,
        @Field("affiliated_organizations") affiliated_organizations: String,
        @Field("marital_status") marital_status: String,
        @Field("blood_group") blood_group: String,
        @Field("two_wheeler") two_wheeler: String,
        @Field("four_wheeler") four_wheeler: String,
    ): Call<RegisterModel>

    @FormUrlEncoded
    @POST("state_list")
    fun StateListApi(
        @Field("api_key") apiKey: String
    ): Call<StateModel>

    @FormUrlEncoded
    @POST("constituencies_list")
    fun ConstituencyListApi(
        @Field("api_key") apiKey: String,
        @Field("state_id") state_id: String
    ): Call<StateModel>

    @FormUrlEncoded
    @POST("login")
    fun loginApi(
        @Field("api_key") apiKey: String,
        @Field("phone") phone: String,
        @Field("password") password: String
    ): Call<LoginModel>

    @FormUrlEncoded
    @POST("user_forget_password")
    fun forgotApi(
        @Field("api_key") apiKey: String,
        @Field("email_id") username: String
    ): Call<ForgotModel>

    @FormUrlEncoded
    @POST("registration")
    fun addMemberApi(
        @Field("api_key") apiKey: String,
        @Field("email") emailId: String,
        @Field("password") password: String,
        @Field("phone") mobileNumber: String,
        @Field("full_name") fullName: String,
        @Field("date_of_birth") date_of_birth: String,
        @Field("care_of") care_of: String,
        @Field("gender") gender: String,
        @Field("full_address") address: String,
        @Field("business_name") business_name: String,
        @Field("constituencies") constituencies: String,
        @Field("state") state: String,
        @Field("mandal") mandal: String,
        @Field("ward") ward: String,
        @Field("pin_code") pin_code: String,
        @Field("current_occupation") current_occupation: String,
        @Field("organization_company") organization_company: String,
        @Field("designation") designation: String,
        @Field("work_location") work_location: String,
        @Field("permanent_address") permanent_address: String,
        @Field("facebook_profile") facebook_profile: String,
        @Field("facebook_followers") facebook_followers: String,
        @Field("instagram_profile") instagram_profile: String,
        @Field("instagram_followes") instagram_followes: String,
        @Field("twitter_x_profile") twitter_x_profile: String,
        @Field("twitter_x_follwers") twitter_x_follwers: String,
        @Field("linkedin_profile") linkedin_profile: String,
        @Field("youtube_channel") youtube_channel: String,
        @Field("youtube_followers") youtube_followers: String,
        @Field("whatsapp_number") whatsapp_number: String,
        @Field("telegram_username_channel") telegram_username_channel: String,
        @Field("skills_interests") skills_interests: String,
        @Field("volunteer_experience") volunteer_experience: String,
        @Field("affiliated_organizations") affiliated_organizations: String,
        @Field("marital_status") marital_status: String,
        @Field("blood_group") blood_group: String,
        @Field("two_wheeler") two_wheeler: String,
        @Field("four_wheeler") four_wheeler: String,
    ): Call<RegisterModel>

    @FormUrlEncoded
    @POST("membership_list")
    fun memberShipListApi(
        @Field("api_key") apiKey: String,
        @Field("constituencies_id") constituencies_id: String,
        @Field("state_id") state_id: String,
    ): Call<MemberShipListModel>

    @FormUrlEncoded
    @POST("banner_list")
    fun bannerListApi(
        @Field("api_key") apiKey: String,
        @Field("constituencies_id") constituencies_id: String,
        @Field("state_id") state_id: String,
    ): Call<BannersModel>

    @FormUrlEncoded
    @POST("news_list")
    fun newsListApi(
        @Field("api_key") apiKey: String,
        @Field("constituencies_id") constituencies_id: String,
        @Field("state_id") state_id: String,
    ): Call<NewsModel>

    @FormUrlEncoded
    @POST("total_users")
    fun totalUsersApi(
        @Field("api_key") apiKey: String
    ): Call<TotalUsersModel>

    @FormUrlEncoded
    @POST("gallery_list")
    fun galleryListApi(
        @Field("api_key") apiKey: String,
        @Field("constituencies") constituencies: String,
        @Field("state_id") state_id: String
    ): Call<GalleryListModel>

    @FormUrlEncoded
    @POST("userbased_favorite")
    fun getFavouriteApi(
        @Field("api_key") apiKey: String,
        @Field("customer_id") customerId: String
    ): Call<FavouriteModel>

    @FormUrlEncoded
    @POST("update_profile")
    fun updateProfileApi(
        @Field("user_id") user_id: String,
        @Field("email") emailId: String,
        @Field("phone") mobileNumber: String,
        @Field("full_name") fullName: String,
        @Field("date_of_birth") date_of_birth: String,
        @Field("care_of") care_of: String,
        @Field("gender") gender: String,
        @Field("full_address") address: String,
        @Field("business_name") business_name: String,
        @Field("constituencies") constituencies: String,
        @Field("state") state: String,
        @Field("mandal") mandal: String,
        @Field("ward") ward: String,
        @Field("pin_code") pin_code: String,
        @Field("current_occupation") current_occupation: String,
        @Field("organization_company") organization_company: String,
        @Field("designation") designation: String,
        @Field("work_location") work_location: String,
        @Field("permanent_address") permanent_address: String,
        @Field("facebook_profile") facebook_profile: String,
        @Field("facebook_followers") facebook_followers: String,
        @Field("instagram_profile") instagram_profile: String,
        @Field("instagram_followes") instagram_followes: String,
        @Field("twitter_x_profile") twitter_x_profile: String,
        @Field("twitter_x_follwers") twitter_x_follwers: String,
        @Field("linkedin_profile") linkedin_profile: String,
        @Field("youtube_channel") youtube_channel: String,
        @Field("youtube_followers") youtube_followers: String,
        @Field("whatsapp_number") whatsapp_number: String,
        @Field("telegram_username_channel") telegram_username_channel: String,
        @Field("skills_interests") skills_interests: String,
        @Field("volunteer_experience") volunteer_experience: String,
        @Field("affiliated_organizations") affiliated_organizations: String,
        @Field("marital_status") marital_status: String,
        @Field("blood_group") blood_group: String,
        @Field("two_wheeler") two_wheeler: String,
        @Field("four_wheeler") four_wheeler: String
    ): Call<ProfileModel>

    @FormUrlEncoded
    @POST("terms")
    fun termsAndConditionsApi(
        @Field("api_key") apiKey: String
    ): Call<TermsAndConditionsModel>


    @FormUrlEncoded
    @POST("about")
    fun aboutUsApi(
        @Field("api_key") apiKey: String
    ): Call<AboutUsModel>

    @FormUrlEncoded
    @POST("privacypolicy")
    fun privacyPolicyApi(
        @Field("api_key") apiKey: String
    ): Call<PrivacyPolicyModel>

    @FormUrlEncoded
    @POST("contact")
    fun contactUsApi(
        @Field("api_key") apiKey: String
    ): Call<ContactUsModel>

    @FormUrlEncoded
    @POST("account_delete")
    fun deleteAccountApi(
        @Field("api_key") apiKey: String,
        @Field("user_id") customerId: String,
    ): Call<DeleteModel>

}