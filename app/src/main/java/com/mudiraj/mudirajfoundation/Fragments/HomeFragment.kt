package com.mudiraj.mudirajfoundation.Fragments

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.Preferences
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.BannersModel
import com.mudiraj.mudirajfoundation.Models.BannersResponse
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.FragmentHomeBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.showToast(requireActivity(), "Please check your connection ")
            return
        } else {

        }

        binding.linearWhatsApp.setOnClickListener {
            val phoneNumber = "9493409050"
            val countryCode = "91" // India country code, change if needed
            val url = "https://wa.me/$countryCode$phoneNumber"

            try {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                intent.setPackage("com.whatsapp")
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // WhatsApp not installed, handle this case
                Toast.makeText(requireActivity(), "WhatsApp not installed on this device", Toast.LENGTH_SHORT).show()
            }

        }

        binding.linearYoutube.setOnClickListener {
            val youtubeChannelUrl = "https://www.youtube.com/@Mudirajfoundation"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(youtubeChannelUrl)
                setPackage("com.google.android.youtube") // Try to open in YouTube app
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // YouTube app not found, open in browser
                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(youtubeChannelUrl))
                startActivity(fallbackIntent)
            }
        }

        binding.linearInstagram.setOnClickListener {
            val instagramUsername = "mudirajfoundation"
            val uri = Uri.parse("http://instagram.com/_u/$instagramUsername") // Direct to user profile in app
            val likeIng = Intent(Intent.ACTION_VIEW, uri).apply {
                setPackage("com.instagram.android") // Open in Instagram app
            }
            try {
                startActivity(likeIng)
            } catch (e: ActivityNotFoundException) {
                // Instagram app not installed, open in browser
                val webUri = Uri.parse("https://www.instagram.com/$instagramUsername")
                val webIntent = Intent(Intent.ACTION_VIEW, webUri)
                startActivity(webIntent)
            }
        }

        binding.linearFacebook.setOnClickListener {
            val facebookUrl = "https://www.facebook.com/4umudiraj"
            val intent = Intent(Intent.ACTION_VIEW)

            try {
                val uri = Uri.parse("fb://facewebmodal/f?href=$facebookUrl")
                intent.data = uri
                intent.setPackage("com.facebook.katana")

                val pm = requireActivity().packageManager
                if (intent.resolveActivity(pm) != null) {
                    startActivity(intent)
                } else {
                    intent.data = Uri.parse(facebookUrl)
                    startActivity(intent)
                }
            } catch (e: Exception) {
                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse(facebookUrl))
                startActivity(fallbackIntent)
            }
        }

        binding.linearWhatsAppChannel.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://whatsapp.com/channel/0029Vb5qYzI9sBI8FqGycJ1f")
                setPackage("com.whatsapp") // Optional: opens directly in WhatsApp if installed
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                // WhatsApp not installed or link can't be handled
                Toast.makeText(requireActivity(), "WhatsApp not found", Toast.LENGTH_SHORT).show()
                // Optionally open in browser
                val fallbackIntent = Intent(Intent.ACTION_VIEW, Uri.parse("https://whatsapp.com/channel/0029Vb5qYzI9sBI8FqGycJ1f"))
                startActivity(fallbackIntent)
            }
        }

        binding.linearWeb.setOnClickListener {
            val url = "https://www.mudhirajfoundation.com/"
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(url)
            }

            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireActivity(), "No browser app found!", Toast.LENGTH_SHORT).show()
            }

        }

        binding.linearEmail.setOnClickListener {
            val intent = Intent(Intent.ACTION_SENDTO).apply {
                data = Uri.parse("mailto:hmudhirajfoundation1@gmail.com")
                putExtra(Intent.EXTRA_SUBJECT, "Inquiry from App")
                putExtra(Intent.EXTRA_TEXT, "Hello Mudhiraj Foundation,\n\n")
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireActivity(), "No email app found!", Toast.LENGTH_SHORT).show()
            }
        }


    }



}