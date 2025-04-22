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
import androidx.activity.result.contract.ActivityResultContracts.RequestMultiplePermissions
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.RegisterModel
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityRegisterBinding
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class RegisterActivity : AppCompatActivity() {

    val binding: ActivityRegisterBinding by lazy {
        ActivityRegisterBinding.inflate(layoutInflater)
    }

    var passwordView: Boolean = false


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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()
    }

    private fun inIts() {


        binding.relativeProfile.setOnClickListener {
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
    }


    private fun registerApi() {

        val name = binding.nameEdit.text?.trim().toString()
        val email = binding.emailEdit.text?.trim().toString()
        val mobile = binding.mobileEdit.text?.trim().toString()
        val businessName = binding.BusinessNameEdit.text?.trim().toString()
        val address = binding.addressEdit.text?.trim().toString()
        val password = binding.passwordEdit.text?.trim().toString()

        ViewController.hideKeyBoard(this@RegisterActivity )

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
        if (businessName.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Business Name")
            return
        }
        if (address.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter Address")
            return
        }
        if (password.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter password")
            return
        }

        if (!ViewController.validateMobile(mobile)) {
            ViewController.customToast(applicationContext, "Enter Valid mobile number")
        }else if (!binding.checkbox.isChecked) {
            ViewController.customToast(applicationContext, "Please agree to the terms and conditions.")
        } else {
            ViewController.showLoading(this@RegisterActivity)

            val apiServices = RetrofitClient.apiInterface
            val call =
                apiServices.registerApi(
                    getString(R.string.api_key),
                    name,
                    mobile,
                    email,
                    businessName,
                    address,
                    password
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
            binding.imgProfile.setImageURI(selectedImageUri) // Display selected image
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