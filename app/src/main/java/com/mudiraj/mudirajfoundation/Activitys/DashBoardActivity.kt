package com.mudiraj.mudirajfoundation.Activitys

import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieProperty
import com.airbnb.lottie.model.KeyPath
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mudiraj.mudirajfoundation.Adapters.Cart.CartListResponse
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.Preferences
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Logins.LoginActivity
import com.mudiraj.mudirajfoundation.Models.CityModel
import com.mudiraj.mudirajfoundation.Models.SettingsModel
import com.mudiraj.mudirajfoundation.Models.StateListResponse
import com.mudiraj.mudirajfoundation.Models.StateModel
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityDashBoardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashBoardActivity : AppCompatActivity() {

    val binding: ActivityDashBoardBinding by lazy {
        ActivityDashBoardBinding.inflate(layoutInflater)
    }

    //bottom menu
    private lateinit var bottomNavigationView: BottomNavigationView
    private lateinit var navController: NavController

    //exit
    private var isHomeFragmentDisplayed = false


    var selectedState: String = ""
    var selectedConstituency: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.colorPrimary),
            false
        )

        inIts()

    }

    private fun inIts() {

        Preferences.saveStringValue(this@DashBoardActivity, Preferences.LOGINCHECK, "Open")

        bottomMenu()

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {

        }

        binding.cardNewForm.setOnClickListener {
            val intent = Intent(this@DashBoardActivity, AddMembersActivity::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }

        binding.cardLocation.setOnClickListener {
            locationSelectDialog()
        }

    }


    private fun bottomMenu() {
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        navController = findNavController(R.id.frame_layout)
        // Setup Bottom Navigation
        NavigationUI.setupWithNavController(bottomNavigationView, navController)
        bottomNavigationView.setOnNavigationItemSelectedListener { menuItem ->
            menuItem.isChecked = true
            when (menuItem.itemId) {
                R.id.home -> {
                    navController.navigate(R.id.homeFragment)
                    true
                }

                R.id.groupChat -> {
                    navController.navigate(R.id.gropChatFragment)
                    true
                }

                R.id.upload -> {
                    navController.navigate(R.id.uploadFragment)
                    true
                }

                R.id.notications -> {
                    navController.navigate(R.id.notificationsFragment)
                    true
                }

                R.id.menu -> {
                    navController.navigate(R.id.menuFragment)
                    true
                }

                else -> false
            }
        }
    }


    private fun locationSelectDialog() {
        val bottomSheetDialog = BottomSheetDialog(this@DashBoardActivity, R.style.AppBottomSheetDialogTheme)
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
            if (selectedState.isEmpty()) {
                ViewController.showToast(applicationContext, "Select State")
            }else if (selectedConstituency.isEmpty()) {
                ViewController.showToast(applicationContext, "Select Constituency")
            }else{
                Preferences.saveStringValue(this@DashBoardActivity, Preferences.state, selectedState)
                Preferences.saveStringValue(this@DashBoardActivity, Preferences.constituencies, selectedConstituency)

                bottomSheetDialog.dismiss()

                val intent = Intent(this@DashBoardActivity, DashBoardActivity::class.java)
                startActivity(intent)
                overridePendingTransition(0, 0)
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
                                this@DashBoardActivity,
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
                                this@DashBoardActivity,
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



    override fun onBackPressed() {
        if (isHomeFragmentDisplayed) {
            exitDialog()
        } else {
            isHomeFragmentDisplayed = true
            // Navigate to HomeFragment
            navigateToHomeFragment()
        }
    }
    private fun navigateToHomeFragment() {
        navController.navigate(R.id.homeFragment)
        bottomNavigationView.selectedItemId = R.id.home
    }

    private fun exitDialog() {
        isHomeFragmentDisplayed = false
        val dialogBuilder = AlertDialog.Builder(this@DashBoardActivity)
        dialogBuilder.setTitle("Exit")
        dialogBuilder.setMessage("Are you sure want to exit this app?")
        dialogBuilder.setPositiveButton("OK") { dialog, _ ->
            finishAffinity()
            dialog.dismiss()
        }
        dialogBuilder.setNegativeButton("Cancel") { dialog, _ ->
            dialog.dismiss()
        }
        val b = dialogBuilder.create()
        b.show()
    }

}