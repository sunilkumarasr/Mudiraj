package com.mudiraj.mudirajfoundation.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.Preferences
import com.mudiraj.mudirajfoundation.Config.ViewController
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


    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
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