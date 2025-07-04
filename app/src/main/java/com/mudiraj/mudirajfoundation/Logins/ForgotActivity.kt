package com.mudiraj.mudirajfoundation.Logins

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.mudiraj.mudirajfoundation.Activitys.DashBoardActivity
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.ForgotModel
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityForgotBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ForgotActivity : AppCompatActivity() {

    val binding: ActivityForgotBinding by lazy {
        ActivityForgotBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        inIts()

    }

    private fun inIts() {
        binding.linearBack.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearBack.startAnimation(animations)
            val intent = Intent(this@ForgotActivity, LoginActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_left, R.anim.to_right)
        }

        binding.linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            binding.linearSubmit.startAnimation(animations)
            forgotApi()

        }

    }


    private fun forgotApi() {
        val email = binding.emailEdit.text?.trim().toString()


        ViewController.hideKeyBoard(this@ForgotActivity )


        if (email.isEmpty()) {
            ViewController.showToast(applicationContext, "Enter Email")
            return
        }

        if (!ViewController.validateEmail(email)) {
            ViewController.showToast(applicationContext, "Enter Valid mobile number")
        } else {
            ViewController.showLoading(this@ForgotActivity)
            val apiServices = RetrofitClient.apiInterface
            val call =
                apiServices.forgotApi(
                    getString(R.string.api_key),
                    email,
                )
            call.enqueue(object : Callback<ForgotModel> {
                override fun onResponse(
                    call: Call<ForgotModel>,
                    response: Response<ForgotModel>
                ) {
                    binding.progressBar.visibility = View.GONE
                    try {
                        if (response.isSuccessful) {
                            if (response.body()?.code==1){
                                val intent = Intent(this@ForgotActivity, DashBoardActivity::class.java)
                                startActivity(intent)
                                finish()
                            } else {
                                ViewController.showToast(applicationContext, "Invalid Email")
                            }
                        } else {
                            ViewController.showToast(applicationContext, "Invalid Email")
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                    } catch (e: TypeCastException) {
                        e.printStackTrace()
                    }
                }
                override fun onFailure(call: Call<ForgotModel>, t: Throwable) {
                    binding.progressBar.visibility = View.GONE
                    ViewController.showToast(applicationContext, "Invalid Email")
                }
            })
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        val intent = Intent(this@ForgotActivity, LoginActivity::class.java)
        startActivity(intent)
        overridePendingTransition(R.anim.from_left, R.anim.to_right)
    }

}