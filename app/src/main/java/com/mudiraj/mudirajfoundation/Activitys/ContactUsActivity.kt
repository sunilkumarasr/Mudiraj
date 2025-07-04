package com.mudiraj.mudirajfoundation.Activitys

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.ContactUsModel
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityContactUsBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactUsActivity : AppCompatActivity() {

    val binding: ActivityContactUsBinding by lazy {
        ActivityContactUsBinding.inflate(layoutInflater)
    }

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

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            contactUsApi()
        }

    }

    private fun contactUsApi() {
        binding.progressBar.visibility = View.VISIBLE
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.contactUsApi(
                getString(R.string.api_key)
            )
        call.enqueue(object : Callback<ContactUsModel> {
            override fun onResponse(
                call: Call<ContactUsModel>,
                response: Response<ContactUsModel>
            ) {
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        response.body()?.contactResponse?.let { listOfcategories ->
                            // Display the first mobile number in the TextView (for example)
                            if (listOfcategories.isNotEmpty()) {
                                val allMobileNumbers = listOfcategories.joinToString("\n") { it.mobileno }
                                binding.txtMoblile.setText(allMobileNumbers)
                            } else {
                                binding.txtMoblile.text = "No data available"
                            }
                        } ?: run {
                            // Handle null response or failure
                            binding.txtMoblile.text = "Response is null or invalid"
                        }
                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<ContactUsModel>, t: Throwable) {
                binding.progressBar.visibility = View.GONE
                Log.e("onFailure",t.message.toString())
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}