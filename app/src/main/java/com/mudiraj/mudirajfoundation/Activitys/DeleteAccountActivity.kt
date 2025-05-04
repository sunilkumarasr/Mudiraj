package com.mudiraj.mudirajfoundation.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.Preferences
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Logins.LoginActivity
import com.mudiraj.mudirajfoundation.Models.DeleteModel
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityDeleteAccountBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DeleteAccountActivity : AppCompatActivity() {

    val binding: ActivityDeleteAccountBinding by lazy {
        ActivityDeleteAccountBinding.inflate(layoutInflater)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
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


        binding.linearSubmit.setOnClickListener {
            val selectedOptionId = binding.radioGroup.checkedRadioButtonId
            if (selectedOptionId != -1) {
                val selectedRadioButton = findViewById<RadioButton>(selectedOptionId)
                val selectedText = selectedRadioButton.text.toString()
                if (!ViewController.noInterNetConnectivity(applicationContext)) {
                    ViewController.showToast(applicationContext, "Please check your connection ")
                } else {
                    deleteAccountApi()
                }
            } else {
                Toast.makeText(this, "Please select an option", Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun deleteAccountApi() {
        val userId = Preferences.loadStringValue(applicationContext, Preferences.userId, "")
        binding.txtButton.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.deleteAccountApi(
                getString(R.string.api_key),
                userId.toString()
            )
        call.enqueue(object : Callback<DeleteModel> {
            override fun onResponse(
                call: Call<DeleteModel>,
                response: Response<DeleteModel>
            ) {
                binding.txtButton.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
                try {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DeleteAccountActivity, "Account Deleted Successfully", Toast.LENGTH_SHORT).show()
                        Preferences.deleteSharedPreferences(this@DeleteAccountActivity)
                        startActivity(Intent(this@DeleteAccountActivity, LoginActivity::class.java))
                        finishAffinity()

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                    Log.e("onFailure",e.message.toString())
                }
            }
            override fun onFailure(call: Call<DeleteModel>, t: Throwable) {
                Log.e("onFailure",t.message.toString())
                binding.txtButton.visibility = View.VISIBLE
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finish()
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }


}