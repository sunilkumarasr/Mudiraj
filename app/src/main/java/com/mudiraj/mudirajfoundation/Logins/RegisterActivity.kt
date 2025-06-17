package com.mudiraj.mudirajfoundation.Logins

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.Manifest.permission.READ_MEDIA_VIDEO
import android.Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.mudiraj.mudirajfoundation.Activitys.DashBoardActivity
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.RegisterModel
import com.mudiraj.mudirajfoundation.Models.StateListResponse
import com.mudiraj.mudirajfoundation.Models.StateModel
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityRegisterBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterActivity : AppCompatActivity() {

    val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    var passwordView: Boolean = false
    var selectedState: String = ""
    var selectedConstituency: String = ""


    //image selection
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
            ViewController.showToast(this@RegisterActivity, "Accept permissions")
        }
    }
    private val IMAGE_PICK_CODE = 1000
    private var selectedImageUri: Uri? = null


    //gender
    val genderList = listOf("Male", "Female", "Other")
    var genderName: String = ""

    //more fields
    var moreFieldsStatus: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()
    }

    private fun inIts() {

        //gender
        val adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item,
            genderList
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        binding.GenderSpinner.adapter = adapter
        genderName = binding.GenderSpinner.selectedItem.toString()


        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.customToast(applicationContext, "Please check your connection ")
        } else {
            StateListApi()
        }


        binding.txtImage.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions.launch(arrayOf(READ_MEDIA_IMAGES))
            } else {
                requestPermissions.launch(arrayOf(READ_EXTERNAL_STORAGE))
            }
        }


        // Toggle password visibility
        binding.passwordToggle.setOnClickListener {
            binding.passwordEdit.transformationMethod = PasswordTransformationMethod.getInstance()
            if (passwordView){
                passwordView = false
                // Hide the password
                binding.passwordEdit.transformationMethod = PasswordTransformationMethod.getInstance()
                binding.passwordToggle.setImageResource(R.drawable.close_eye_ic)
            }else{
                passwordView = true
                // Show the password
                binding.passwordEdit.transformationMethod = HideReturnsTransformationMethod.getInstance()
                binding.passwordToggle.setImageResource(R.drawable.open_eye_ic)
            }

        }

        binding.loginLinear.setOnClickListener {
            val animations = ViewController.animation()
            binding.loginLinear.startAnimation(animations)
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.imgBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.loginLinear.startAnimation(animations)
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmit.startAnimation(animations)

            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.customToast(applicationContext, "Please check your connection ")
            } else {
                registerApi()
            }
        }


        binding.StateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val state = parent.getItemAtPosition(position) as StateListResponse
                selectedState = state.id
                val selectedStateName = state.name
                ConstituencyListApi(selectedState)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        binding.ConstituenciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val state = parent.getItemAtPosition(position) as StateListResponse
                selectedConstituency = state.id
                val selectedStateName = state.name
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }


        binding.txtMore.setOnClickListener {
            if (!moreFieldsStatus){
                moreFieldsStatus = true
                binding.txtMore.text = "Less.."
                binding.linearMore.visibility = View.VISIBLE
            }else{
                moreFieldsStatus = false
                binding.txtMore.text = "More.."
                binding.linearMore.visibility = View.GONE
            }
        }

    }

    private fun registerApi() {
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
        val password = binding.passwordEdit.text?.trim().toString()
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


        ViewController.hideKeyBoard(this@RegisterActivity )

        if (name.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Your Name")
            return
        }

        if (mobile.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Mobile Number")
            return
        }
        if (email.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Email")
            return
        }

        if (Business.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Business Name")
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
            ViewController.customToast(applicationContext, "Enter Your Mandal")
            return
        }
        if (village.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Your village")
            return
        }

        if (password.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter password")
            return
        }

        if (!ViewController.validateEmail(email)) {
            ViewController.customToast(applicationContext, "Enter Valid Email")
        }else if (!ViewController.validateMobile(mobile)) {
            ViewController.customToast(applicationContext, "Enter Valid mobile number")
        } else {
            ViewController.showLoading(this@RegisterActivity)

            val apiServices = RetrofitClient.apiInterface
            val call =
                apiServices.registerApi(
                    getString(R.string.api_key),
                    email,
                    password,
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
            call.enqueue(object : Callback<RegisterModel> {
                override fun onResponse(
                    call: Call<RegisterModel>,
                    response: Response<RegisterModel>
                ) {
                    ViewController.hideLoading()
                    try {
                        if (response.isSuccessful) {

                            if (response.body()?.status == true){
                                ViewController.customToast(applicationContext, "success")
                                val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
                                startActivity(intent)
                                finish()
                            }else {
                                ViewController.customToast(applicationContext, response.body()?.message.toString())
                            }

                        } else {
                            ViewController.customToast(applicationContext, "Register Failed")
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    }
                }

                override fun onFailure(call: Call<RegisterModel>, t: Throwable) {
                    ViewController.hideLoading()
                    ViewController.customToast(applicationContext, "Register Failed")
                }
            })

        }
    }

    private fun StateListApi() {
            val apiServices = RetrofitClient.apiInterface
            val call =
                apiServices.StateListApi(
                    getString(R.string.api_key)
                )
            call.enqueue(object : Callback<StateModel> {
                override fun onResponse(
                    call: Call<StateModel>,
                    response: Response<StateModel>
                ) {
                    ViewController.hideLoading()
                    try {
                        if (response.isSuccessful) {

                            val stateList = response.body()?.response
                            if (response.body()?.status == true && stateList != null) {
                                val stateNames = stateList.map { it.name }

                                val adapter = object : ArrayAdapter<StateListResponse>(
                                    this@RegisterActivity,
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
                                findViewById<Spinner>(R.id.StateSpinner).adapter = adapter
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

                        val stateList = response.body()?.response
                        if (response.body()?.status == true && stateList != null) {
                            val constituenciesNames = stateList.map { it.name }

                            val adapter = object : ArrayAdapter<StateListResponse>(
                                this@RegisterActivity,
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
                            findViewById<Spinner>(R.id.ConstituenciesSpinner).adapter = adapter
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

    private fun getRealPathFromURI(uri: Uri): String {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            cursor.moveToFirst()
            return cursor.getString(columnIndex)
        }
        return ""
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        //single image selection
        if (data != null) {
            selectedImageUri = data.data!!
        }
        if (selectedImageUri != null) {
           // binding.imgProfile.setImageURI(selectedImageUri) // Display selected image
        }
    }

    //update profile
    private fun createEmptyImagePart(): MultipartBody.Part {
        // Create an empty RequestBody
        val requestFile = RequestBody.create(MultipartBody.FORM, ByteArray(0))
        return MultipartBody.Part.createFormData("image", "", requestFile)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }
}