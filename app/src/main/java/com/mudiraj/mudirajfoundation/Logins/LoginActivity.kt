package com.mudiraj.mudirajfoundation.Logins

import android.content.Intent
import android.os.Bundle
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.messaging.FirebaseMessaging
import com.mudiraj.mudirajfoundation.Activitys.DashBoardActivity
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.Preferences
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.LoginModel
import com.mudiraj.mudirajfoundation.Models.StateListResponse
import com.mudiraj.mudirajfoundation.Models.StateModel
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityLoginBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    var passwordView: Boolean = false
    lateinit var token: String


    var selectedState: String = ""
    var selectedConstituency: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        inIts()
    }

    private fun inIts() {

        FirebaseMessaging.getInstance().token.addOnCompleteListener(OnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("Fetching FCM registration token failed", task.exception.toString())
                return@OnCompleteListener
            }

            // Get new FCM registration token
            token = task.result
            Log.e("token_", token.toString())

        })

        binding.txtForgot.setOnClickListener {
            val animations = ViewController.animation()
            binding.txtForgot.startAnimation(animations)
            val intent = Intent(this@LoginActivity, ForgotActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }
        binding.registerLinear.setOnClickListener {
            val animations = ViewController.animation()
            binding.registerLinear.startAnimation(animations)
            val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }
        binding.linearSubmit.setOnClickListener {
            if (!ViewController.noInterNetConnectivity(applicationContext)) {
                ViewController.customToast(applicationContext, "Please check your connection ")
            } else {
                loginApi()
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


    }


    private fun loginApi() {
        val mobile_ = binding.mobileEdit.text?.trim().toString()
        val password = binding.passwordEdit.text?.trim().toString()

        ViewController.hideKeyBoard(this@LoginActivity )

        if (mobile_.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter mobile")
            return
        }
        if (password.isEmpty()) {
            ViewController.customToast(applicationContext, "Enter password")
            return
        }

        if (!ViewController.validateMobile(mobile_)) {
            ViewController.customToast(applicationContext, "Enter valid mobile number")
        } else {
            binding.txtButton.visibility = View.GONE
            binding.progressBar.visibility = View.VISIBLE

            val apiServices = RetrofitClient.apiInterface
            val call =
                apiServices.loginApi(
                    getString(R.string.api_key),
                    mobile_,
                    password
                )

            call.enqueue(object : Callback<LoginModel> {
                override fun onResponse(
                    call: Call<LoginModel>,
                    response: Response<LoginModel>
                ) {
                    binding.txtButton.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    try {
                        if (response.isSuccessful) {

                            if (response.body()?.status == true){


                                Preferences.saveStringValue(this@LoginActivity, Preferences.userId,response.body()?.response!!.user_id.toString())
                                Preferences.saveStringValue(this@LoginActivity, Preferences.name,response.body()?.response!!.full_name.toString())
                                Preferences.saveStringValue(this@LoginActivity, Preferences.mobileNumber,response.body()?.response!!.phone.toString())
                                Preferences.saveStringValue(this@LoginActivity, Preferences.email,response.body()?.response!!.email.toString())
                                Preferences.saveStringValue(this@LoginActivity, Preferences.address,response.body()?.response!!.full_address.toString())


                                locationSelectDialog()


                            }else {
                                ViewController.customToast(applicationContext, "Login Failed")
                            }

                        } else {
                            ViewController.customToast(applicationContext, "Invalid Mobile Number")
                        }
                    } catch (e: NullPointerException) {
                        e.printStackTrace()
                        Log.e("error__",e.message.toString())
                    }
                }

                override fun onFailure(call: Call<LoginModel>, t: Throwable) {
                    Log.e("error_",t.message.toString())
                    binding.txtButton.visibility = View.VISIBLE
                    binding.progressBar.visibility = View.GONE
                    ViewController.customToast(applicationContext, "Invalid Credentials")
                }
            })

        }
    }


    private fun locationSelectDialog() {
        val bottomSheetDialog = BottomSheetDialog(this@LoginActivity, R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_location, null)
        bottomSheetDialog.setContentView(view)

        val StateSpinner = view.findViewById<Spinner>(R.id.StateSpinner)
        val ConstituenciesSpinner = view.findViewById<Spinner>(R.id.ConstituenciesSpinner)
        val linearSubmit = view.findViewById<LinearLayout>(R.id.linearSubmit)


        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.customToast(applicationContext, "Please check your connection ")
        } else {
            StateListApi(StateSpinner)
        }


        StateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val state = parent.getItemAtPosition(position) as StateListResponse
                selectedState = state.id
                val selectedStateName = state.name
                ConstituencyListApi(ConstituenciesSpinner, selectedState)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        ConstituenciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val state = parent.getItemAtPosition(position) as StateListResponse
                selectedConstituency = state.id
                val selectedStateName = state.name
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }


        linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            bottomSheetDialog.dismiss()

            if (selectedState.isEmpty()) {
                ViewController.customToast(applicationContext, "Select State")
            }else if (selectedConstituency.isEmpty()) {
                ViewController.customToast(applicationContext, "Select Constituency")
            }else{
                Preferences.saveStringValue(this@LoginActivity, Preferences.state, selectedState)
                Preferences.saveStringValue(this@LoginActivity, Preferences.constituencies, selectedConstituency)


                val intent = Intent(this@LoginActivity, DashBoardActivity::class.java)
                startActivity(intent)
                finish()
            }


        }
        bottomSheetDialog.show()
    }

    private fun StateListApi(StateSpinner: Spinner) {
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
                                this@LoginActivity,
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
                            StateSpinner.adapter = adapter
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
    private fun ConstituencyListApi(ConstituenciesSpinner: Spinner, selectedState: String) {
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
                                this@LoginActivity,
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
                            ConstituenciesSpinner.adapter = adapter
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

}