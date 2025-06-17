package com.mudiraj.mudirajfoundation.Activitys

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.content.ContextCompat
import com.mudiraj.mudirajfoundation.Config.Preferences
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Logins.LoginActivity
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivitySplashBinding
import java.util.Locale

class SplashActivity : AppCompatActivity() {

    val binding: ActivitySplashBinding by lazy {
        ActivitySplashBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        ViewController.changeStatusBarColor(
            this,
            ContextCompat.getColor(this, R.color.black),
            false
        )

        inIts()

    }


    private fun inIts() {

        LogoAnimation()
        
        //language
        val languageCode = Preferences.loadStringValue(applicationContext, Preferences.languageCode, "")
        if (languageCode != null) {
            setLocale(languageCode)
        }

        val loginCheck = Preferences.loadStringValue(applicationContext, Preferences.LOGINCHECK, "")
        Handler(Looper.getMainLooper()).postDelayed({
            if (loginCheck.equals("Open")) {
                val intent = Intent(this@SplashActivity, DashBoardActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right, R.anim.to_left)
            } else {
                val intent = Intent(this@SplashActivity, LoginActivity::class.java)
                startActivity(intent)
                overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
        }, 3000)
    }


    private fun LogoAnimation() {
        val splashLogo: LinearLayout = findViewById(R.id.imgLogo)
        // Create ObjectAnimators for the different effects
        val scaleX = ObjectAnimator.ofFloat(splashLogo, "scaleX", 0f, 1f)
        val scaleY = ObjectAnimator.ofFloat(splashLogo, "scaleY", 0f, 1f)
        val fadeIn = ObjectAnimator.ofFloat(splashLogo, "alpha", 0f, 1f)
        val moveUp = ObjectAnimator.ofFloat(splashLogo, "translationY", 1000f, 0f) // Move logo up

        // Set durations for animations
        scaleX.duration = 1000
        scaleY.duration = 1000
        fadeIn.duration = 1000
        moveUp.duration = 1000

        // Combine animations in AnimatorSet to run together
        val animatorSet = AnimatorSet()
        animatorSet.playTogether(scaleX, scaleY, fadeIn, moveUp)

        // Start animation
        animatorSet.start()
    }


    private fun setLocale(languageCode: String) {
        val locale = Locale(languageCode)
        Locale.setDefault(locale)
        val config = Configuration()
        config.locale = locale
        resources.updateConfiguration(config, resources.displayMetrics)
    }


    override fun onResume() {
        super.onResume()
        inIts()
    }


}