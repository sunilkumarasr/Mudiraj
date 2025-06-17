package com.mudiraj.mudirajfoundation.Activitys

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.Preferences
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Logins.LoginActivity
import com.mudiraj.mudirajfoundation.Models.ProfileModel
import com.mudiraj.mudirajfoundation.Models.RegisterModel
import com.mudiraj.mudirajfoundation.Models.StateListResponse
import com.mudiraj.mudirajfoundation.Models.StateModel
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityEditProfileBinding
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream

class EditProfileActivity : AppCompatActivity() {

    val binding: ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }

    //gender
    var genderName: String = ""

    var selectedState: String = ""
    var selectedConstituency: String = ""


    //single image selection
    private val IMAGE_PICK_CODE = 1000
    private var selectedImageUri: Uri? = null
    val requestPermissions = registerForActivityResult(RequestMultiplePermissions()) { results ->
        var permission = false;
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
            (
                    ContextCompat.checkSelfPermission(
                        applicationContext,
                        READ_MEDIA_IMAGES
                    ) == PERMISSION_GRANTED ||
                            ContextCompat.checkSelfPermission(
                                applicationContext,
                                READ_MEDIA_VIDEO
                            ) == PERMISSION_GRANTED
                    )
        ) {
            permission = true
            // Full access on Android 13 (API level 33) or higher
        } else if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE &&
            ContextCompat.checkSelfPermission(
                applicationContext,
                READ_MEDIA_VISUAL_USER_SELECTED
            ) == PERMISSION_GRANTED
        ) {
            permission = true
            // Partial access on Android 14 (API level 34) or higher
        } else if (ContextCompat.checkSelfPermission(
                applicationContext,
                READ_EXTERNAL_STORAGE
            ) == PERMISSION_GRANTED
        ) {
            permission = true
            // Full access up to Android 12 (API level 32)
        } else {
            permission = false
        }
        if (permission) {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, IMAGE_PICK_CODE)
        } else {
            ViewController.showToast(this@EditProfileActivity, "Accept permissions")
        }
    }
    var imageType: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()

    }

    private fun inIts() {
        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.imgBack.startAnimation(animations)
            finish()
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        val name = Preferences.loadStringValue(this@EditProfileActivity, Preferences.name, "")
        val care_of = Preferences.loadStringValue(this@EditProfileActivity, Preferences.care_of, "")
        val date_of_birth = Preferences.loadStringValue(this@EditProfileActivity, Preferences.date_of_birth, "")
        genderName = Preferences.loadStringValue(this@EditProfileActivity, Preferences.gender, "").toString()
        val mobileNumber = Preferences.loadStringValue(this@EditProfileActivity, Preferences.mobileNumber, "")
        val business_name = Preferences.loadStringValue(this@EditProfileActivity, Preferences.business_name, "")
        val email = Preferences.loadStringValue(this@EditProfileActivity, Preferences.email, "")
        val address = Preferences.loadStringValue(this@EditProfileActivity, Preferences.address, "")
        val permanent_address = Preferences.loadStringValue(this@EditProfileActivity, Preferences.permanent_address, "")
        selectedState = Preferences.loadStringValue(this@EditProfileActivity, Preferences.state, "").toString()
        selectedConstituency = Preferences.loadStringValue(this@EditProfileActivity, Preferences.constituencies, "").toString()
        val mandal = Preferences.loadStringValue(this@EditProfileActivity, Preferences.mandal, "")
        val ward = Preferences.loadStringValue(this@EditProfileActivity, Preferences.ward, "")
        val pin_code = Preferences.loadStringValue(this@EditProfileActivity, Preferences.pin_code, "")
        val current_occupation = Preferences.loadStringValue(this@EditProfileActivity, Preferences.current_occupation, "")
        val organization_company = Preferences.loadStringValue(this@EditProfileActivity, Preferences.organization_company, "")
        val role = Preferences.loadStringValue(this@EditProfileActivity, Preferences.role, "")
        val work_location = Preferences.loadStringValue(this@EditProfileActivity, Preferences.work_location, "")
        val facebook_profile = Preferences.loadStringValue(this@EditProfileActivity, Preferences.facebook_profile, "")
        val facebook_followers = Preferences.loadStringValue(this@EditProfileActivity, Preferences.facebook_followers, "")
        val instagram_profile = Preferences.loadStringValue(this@EditProfileActivity, Preferences.instagram_profile, "")
        val instagram_followes = Preferences.loadStringValue(this@EditProfileActivity, Preferences.instagram_followes, "")
        val twitter_x_profile = Preferences.loadStringValue(this@EditProfileActivity, Preferences.twitter_x_profile, "")
        val twitter_x_follwers = Preferences.loadStringValue(this@EditProfileActivity, Preferences.twitter_x_follwers, "")
        val youtube_channel = Preferences.loadStringValue(this@EditProfileActivity, Preferences.youtube_channel, "")
        val youtube_followers = Preferences.loadStringValue(this@EditProfileActivity, Preferences.youtube_followers, "")
        val linkedin_profile = Preferences.loadStringValue(this@EditProfileActivity, Preferences.linkedin_profile, "")
        val whatsapp_number = Preferences.loadStringValue(this@EditProfileActivity, Preferences.whatsapp_number, "")
        val telegram_username_channel = Preferences.loadStringValue(this@EditProfileActivity, Preferences.telegram_username_channel, "")
        val skills_interests = Preferences.loadStringValue(this@EditProfileActivity, Preferences.skills_interests, "")
        val volunteer_experience = Preferences.loadStringValue(this@EditProfileActivity, Preferences.volunteer_experience, "")
        val marital_status = Preferences.loadStringValue(this@EditProfileActivity, Preferences.marital_status, "")
        val blood_group = Preferences.loadStringValue(this@EditProfileActivity, Preferences.blood_group, "")
        val two_wheeler = Preferences.loadStringValue(this@EditProfileActivity, Preferences.two_wheeler, "")
        val four_wheeler = Preferences.loadStringValue(this@EditProfileActivity, Preferences.four_wheeler, "")
        val affiliated_organizations = Preferences.loadStringValue(this@EditProfileActivity, Preferences.affiliated_organizations, "")
        binding.editName.setText(name.toString())
        binding.editCAREOF.setText(care_of.toString())
        binding.editDOB.setText(date_of_birth.toString())
        binding.editMobile.setText(mobileNumber.toString())
        binding.editBusiness.setText(business_name.toString())
        binding.EditEmail.setText(email.toString())
        binding.editPresentAddress.setText(address.toString())
        binding.editPermanentAddress.setText(permanent_address.toString())
        binding.editMandal.setText(mandal.toString())
        binding.editVillage.setText(work_location.toString())
        binding.editWard.setText(ward.toString())
        binding.editPincode.setText(pin_code.toString())
        binding.editOcuppation.setText(current_occupation.toString())
        binding.editCompany.setText(organization_company.toString())
        binding.editDesignation.setText(role.toString())
        binding.editLocation.setText(work_location.toString())
        binding.editFBProfile.setText(facebook_profile.toString())
        binding.editFBFollowers.setText(facebook_followers.toString())
        binding.editInstaProfile.setText(instagram_profile.toString())
        binding.editInstaFollowers.setText(instagram_followes.toString())
        binding.editTwitterProfile.setText(twitter_x_profile.toString())
        binding.editTwitterFollowers.setText(twitter_x_follwers.toString())
        binding.editYoutubeChannel.setText(youtube_channel.toString())
        binding.editYoutubeFollower.setText(youtube_followers.toString())
        binding.editLinkedIn.setText(linkedin_profile.toString())
        binding.editWhatsAppNumber.setText(whatsapp_number.toString())
        binding.editTelegram.setText(telegram_username_channel.toString())
        binding.editSkills.setText(skills_interests.toString())
        binding.editVolunteer.setText(volunteer_experience.toString())
        binding.editMaritalStatus.setText(marital_status.toString())
        binding.editBloodGroup.setText(blood_group.toString())
        binding.editTwoWheeler.setText(two_wheeler.toString())
        binding.editFourWheeler.setText(four_wheeler.toString())
        binding.editOrganizations.setText(affiliated_organizations.toString())


        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.customToast(applicationContext, "Please check your connection ")
        } else {
            StateListApi()
            //getProfileApi()
        }

        binding.linearSubmit.setOnClickListener {
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.customToast(applicationContext, "Please check your connection ")
            } else {
                updateProfileApi()
            }
        }

        binding.relativeImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES, READ_MEDIA_VIDEO))
            } else {
                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
            }
            imageType = "single"
        }

        //gender
        GenderSelection()

    }


    private fun updateProfileApi() {
        val userId = Preferences.loadStringValue(this@EditProfileActivity, Preferences.userId, "").toString()
        val name = binding.editName.text?.trim().toString()
        val careOf = binding.editCAREOF.text?.trim().toString()
        val mobile = binding.editMobile.text?.trim().toString()
        val email = binding.EditEmail.text?.trim().toString()
        val presentAddress = binding.editPresentAddress.text?.trim().toString()
        val mandal = binding.editMandal.text?.trim().toString()
        val DOB = binding.editDOB.text?.trim().toString()
        val village = binding.editVillage.text?.trim().toString()
        val Business = binding.editBusiness.text?.trim().toString()
        val ward = binding.editWard.text?.trim().toString()
        val pincode = binding.editPincode.text?.trim().toString()
        val permanentAddress = binding.editPermanentAddress.text?.trim().toString()
        val ocuppation = binding.editOcuppation.text?.trim().toString()
        val company = binding.editCompany.text?.trim().toString()
        val designation = binding.editDesignation.text?.trim().toString()
        val location = binding.editLocation.text?.trim().toString()
        val FBProfile = binding.editFBProfile.text?.trim().toString()
        val FBFollowers = binding.editFBFollowers.text?.trim().toString()
        val InstaProfile = binding.editInstaProfile.text?.trim().toString()
        val InstaFollowers = binding.editInstaFollowers.text?.trim().toString()
        val TwitterProfile = binding.editTwitterProfile.text?.trim().toString()
        val TwitterFollowers = binding.editTwitterFollowers.text?.trim().toString()
        val YoutubeChannel = binding.editYoutubeChannel.text?.trim().toString()
        val YoutubeFollower = binding.editYoutubeFollower.text?.trim().toString()
        val LinkedIn = binding.editLinkedIn.text?.trim().toString()
        val WhatsAppNumber = binding.editWhatsAppNumber.text?.trim().toString()
        val Telegram = binding.editTelegram.text?.trim().toString()
        val Skills = binding.editSkills.text?.trim().toString()
        val Volunteer = binding.editVolunteer.text?.trim().toString()
        val Organizations = binding.editOrganizations.text?.trim().toString()
        val MaritalStatus = binding.editMaritalStatus.text?.trim().toString()
        val BloodGroup = binding.editBloodGroup.text?.trim().toString()
        val TwoWheeler = binding.editTwoWheeler.text?.trim().toString()
        val FourWheeler = binding.editFourWheeler.text?.trim().toString()


        ViewController.hideKeyBoard(this@EditProfileActivity )

        if (name.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Your Name")
            return
        }
        if (email.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Email")
            return
        }
        if (mobile.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Mobile Number")
            return
        }
        if (Business.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Business")
            return
        }
        if (selectedState.isEmpty()) {
            ViewController.customToast(applicationContext, "Select State")
            return
        }
        if (selectedConstituency.isEmpty()) {
            ViewController.customToast(applicationContext, "Select Constituency")
            return
        }
        if (mandal.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter mandal")
            return
        }
        if (village.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter village")
            return
        }

        if (!ViewController.validateEmail(email)) {
            ViewController.customToast(applicationContext, "Enter Valid Email")
        }else if (!ViewController.validateMobile(mobile)) {
            ViewController.customToast(applicationContext, "Enter Valid mobile number")
        } else {
            ViewController.showLoading(this@EditProfileActivity)


            val apiServices = RetrofitClient.apiInterface
            val call = apiServices.updateProfileApi(
                userId,
                email,
                mobile,
                name,
                DOB,
                careOf,
                genderName,
                presentAddress,
                Business,
                selectedConstituency,
                selectedState,
                mandal,
                ward,
                pincode,
                ocuppation,
                company,
                designation,
                location,
                permanentAddress,
                FBProfile,
                FBFollowers,
                InstaProfile,
                InstaFollowers,
                TwitterProfile,
                TwitterFollowers,
                LinkedIn,
                YoutubeChannel,
                YoutubeFollower,
                WhatsAppNumber,
                Telegram,
                Skills,
                Volunteer,
                Organizations,
                MaritalStatus,
                BloodGroup,
                TwoWheeler,
                FourWheeler
            )
            call.enqueue(object : Callback<ProfileModel> {
                override fun onResponse(
                    call: Call<ProfileModel>,
                    response: Response<ProfileModel>
                ) {
                    ViewController.hideLoading()
                    try {
                        if (response.isSuccessful) {

                            if (response.body()?.status == true){
                                ViewController.customToast(applicationContext, "success")

                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.userId,response.body()?.response!!.id.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.name,response.body()?.response!!.full_name.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.mobileNumber,response.body()?.response!!.phone.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.email,response.body()?.response!!.email.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.address,response.body()?.response!!.full_address.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.id,response.body()?.response!!.id.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.role,response.body()?.response!!.role.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.date_of_birth,response.body()?.response!!.date_of_birth.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.care_of,response.body()?.response!!.care_of.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.gender,response.body()?.response!!.gender.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.password,response.body()?.response!!.password.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.image,response.body()?.response!!.image.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.business_name,response.body()?.response!!.business_name.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.mandal,response.body()?.response!!.mandal.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.ward,response.body()?.response!!.ward.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.pin_code,response.body()?.response!!.pin_code.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.current_occupation,response.body()?.response!!.current_occupation.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.organization_company,response.body()?.response!!.organization_company.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.designation,response.body()?.response!!.designation.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.work_location,response.body()?.response!!.work_location.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.permanent_address,response.body()?.response!!.permanent_address.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.membership_type,response.body()?.response!!.membership_type.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.amount,response.body()?.response!!.amount.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.facebook_profile,response.body()?.response!!.facebook_profile.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.facebook_followers,response.body()?.response!!.facebook_followers.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.instagram_profile,response.body()?.response!!.instagram_profile.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.instagram_followes,response.body()?.response!!.instagram_followes.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.twitter_x_profile,response.body()?.response!!.twitter_x_profile.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.twitter_x_follwers,response.body()?.response!!.twitter_x_follwers.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.linkedin_profile,response.body()?.response!!.linkedin_profile.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.youtube_channel,response.body()?.response!!.youtube_channel.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.youtube_followers,response.body()?.response!!.youtube_followers.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.whatsapp_number,response.body()?.response!!.whatsapp_number.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.telegram_username_channel,response.body()?.response!!.telegram_username_channel.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.skills_interests,response.body()?.response!!.skills_interests.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.volunteer_experience,response.body()?.response!!.volunteer_experience.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.affiliated_organizations,response.body()?.response!!.affiliated_organizations.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.marital_status,response.body()?.response!!.marital_status.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.blood_group,response.body()?.response!!.blood_group.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.two_wheeler,response.body()?.response!!.two_wheeler.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.four_wheeler,response.body()?.response!!.four_wheeler.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.status,response.body()?.response!!.status.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.state,response.body()?.response!!.state.toString())
                                Preferences.saveStringValue(this@EditProfileActivity, Preferences.constituencies,response.body()?.response!!.constituencies.toString())

                                val intent = Intent(this@EditProfileActivity, DashBoardActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else {
                                ViewController.customToast(applicationContext, response.body()?.message.toString())
                            }

                        } else {
                            ViewController.customToast(applicationContext, "Update Failed")
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<ProfileModel>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.customToast(applicationContext, "Update Failed")
                }
            })

        }
    }

    //image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //single image selection

        if (imageType.equals("single")){
            //single image selection
            if (resultCode == Activity.RESULT_OK && data != null) {
                selectedImageUri = data.data!!
                binding.profileImg.setImageURI(selectedImageUri)
            }
        }
    }
    private fun coverEmptyImagePart(): MultipartBody.Part {
        // Create an empty RequestBody
        val requestFile = RequestBody.create(MultipartBody.FORM, ByteArray(0))
        return MultipartBody.Part.createFormData("image", "", requestFile)
    }



    private fun GenderSelection() {
        val genderList = listOf("Male", "Female", "Other")
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genderList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.GenderSpinner.adapter = adapter
        val genderIndex = genderList.indexOf(genderName)
        if (genderIndex >= 0) {
            binding.GenderSpinner.setSelection(genderIndex)
        }
        binding.GenderSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                genderName = genderList[position]
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }


    private fun StateListApi() {
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.StateListApi(getString(R.string.api_key))

        call.enqueue(object : Callback<StateModel> {
            override fun onResponse(call: Call<StateModel>, response: Response<StateModel>) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {
                        val stateList = response.body()?.response
                        if (response.body()?.status == true && stateList != null) {
                            val adapter = object : ArrayAdapter<StateListResponse>(
                                this@EditProfileActivity,
                                android.R.layout.simple_spinner_item,
                                stateList
                            ) {
                                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                                    val view = super.getView(position, convertView, parent)
                                    (view as TextView).text = stateList[position].name
                                    return view
                                }

                                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                                    val view = super.getDropDownView(position, convertView, parent)
                                    (view as TextView).text = stateList[position].name
                                    return view
                                }
                            }

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            binding.StateSpinner.adapter = adapter

                            // Set previously selected state if available
                            val index = stateList.indexOfFirst { it.id == selectedState }
                            if (index >= 0) {
                                binding.StateSpinner.setSelection(index)
                            }

                            // Set listener AFTER setting adapter and selection
                            binding.StateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                                    val state = parent.getItemAtPosition(position) as StateListResponse
                                    selectedState = state.id
                                    val selectedStateName = state.name
                                    ConstituencyListApi(selectedState) // fetch based on selection
                                }

                                override fun onNothingSelected(parent: AdapterView<*>) {}
                            }
                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<StateModel>, t: Throwable) {
                Log.e("terror", t.message.toString())
            }
        })
    }
    private fun ConstituencyListApi(selectedState: String) {
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.ConstituencyListApi(
                getString(R.string.api_key),
                selectedState
            )
        call.enqueue(object : Callback<StateModel> {
            override fun onResponse(
                call: Call<StateModel>,
                response: Response<StateModel>
            ) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {

                        val cList = response.body()?.response
                        if (response.body()?.status == true && cList != null) {
                            val constituenciesNames = cList.map { it.name }

                            val adapter = object : ArrayAdapter<StateListResponse>(
                                this@EditProfileActivity,
                                android.R.layout.simple_spinner_item,
                                cList
                            ) {
                                override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                                    val view = super.getView(position, convertView, parent)
                                    (view as TextView).text = cList[position].name
                                    return view
                                }

                                override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
                                    val view = super.getDropDownView(position, convertView, parent)
                                    (view as TextView).text = cList[position].name
                                    return view
                                }
                            }

                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            findViewById<Spinner>(R.id.ConstituenciesSpinner).adapter = adapter

                            // Set previously selected state if available
                            val index = cList.indexOfFirst { it.id == selectedConstituency }
                            if (index >= 0) {
                                binding.ConstituenciesSpinner.setSelection(index)
                            }

                            binding.ConstituenciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                                override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                                    val state = parent.getItemAtPosition(position) as StateListResponse
                                    selectedConstituency = state.id
                                    val selectedStateName = state.name
                                }
                                override fun onNothingSelected(parent: AdapterView<*>) {}
                            }


                        }

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<StateModel>, t: Throwable) {
                Log.e("terror",t.message.toString())
            }
        })


    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}