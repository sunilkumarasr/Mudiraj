package com.mudiraj.mudirajfoundation.Activitys

import android.os.Bundle
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityAboutUsBinding
import com.mudiraj.mudirajfoundation.databinding.ActivityZoomImageBinding

class ZoomImageActivity : AppCompatActivity() {

    val binding: ActivityZoomImageBinding by lazy {
        ActivityZoomImageBinding.inflate(layoutInflater)
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

        val imageUrl = intent.getStringExtra("image")

        if (!imageUrl.isNullOrEmpty()) {
            // Or using Glide
            Glide.with(this)
                .load(RetrofitClient.Image_URL+imageUrl)
                .into(binding.photoView)
        } else {
            Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
        }


    }


}