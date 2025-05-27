package com.mudiraj.mudirajfoundation.Activitys

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.bumptech.glide.Glide
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityAddMembersBinding
import com.mudiraj.mudirajfoundation.databinding.ActivityMemberDetailsBinding

class MemberDetailsActivity : AppCompatActivity() {

    val binding: ActivityMemberDetailsBinding by lazy {
        ActivityMemberDetailsBinding.inflate(layoutInflater)
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

        val name = intent.getStringExtra("name")
        val userId = intent.getStringExtra("userId")
        val role = intent.getStringExtra("role")
        val fullName = intent.getStringExtra("fullName")
        val email = intent.getStringExtra("email")
        val phone = intent.getStringExtra("phone")
        val image = intent.getStringExtra("image")
        val businessName = intent.getStringExtra("businessName")
        val fullAddress = intent.getStringExtra("fullAddress")
        val state = intent.getStringExtra("state")
        val constituencies = intent.getStringExtra("constituencies")
        val membershipType = intent.getStringExtra("membershipType")

        Glide.with(binding.imgProfile.context)
            .load(RetrofitClient.Image_URL + image)
            .placeholder(R.drawable.logo)
            .into(binding.imgProfile)

        binding.txtType.text = name
        binding.txtFullName.text = fullName
        binding.txtEmail.text = email
        binding.txtMobile.text = phone
        binding.txtBusinessName.text = businessName
        binding.txtState.text = state
        binding.txtConstituency.text = constituencies
        binding.txtAddress.text = fullAddress


    }

}