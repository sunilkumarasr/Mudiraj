package com.mudiraj.mudirajfoundation.Activitys

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.webkit.WebViewClient
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mudiraj.mudirajfoundation.Adapters.GalleryAdapter
import com.mudiraj.mudirajfoundation.Adapters.HomeMembersAdapter
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.Preferences
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.GalleryListModel
import com.mudiraj.mudirajfoundation.Models.GalleryListResponse
import com.mudiraj.mudirajfoundation.Models.MemberShipListModel
import com.mudiraj.mudirajfoundation.Models.MemberShipListResponse
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.ActivityGalleryBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GalleryActivity : AppCompatActivity() {

    val binding: ActivityGalleryBinding by lazy {
        ActivityGalleryBinding.inflate(layoutInflater)
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

        if (!ViewController.noInterNetConnectivity(applicationContext)) {
            ViewController.showToast(applicationContext, "Please check your connection ")
        } else {
            galleryListApi()
        }


    }


    //members list
    private fun galleryListApi() {
        val constituencies = Preferences.loadStringValue(this@GalleryActivity, Preferences.constituencies, "")


        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.galleryListApi(
                getString(R.string.api_key),
                "66"
            )
        call.enqueue(object : Callback<GalleryListModel> {
            override fun onResponse(
                call: Call<GalleryListModel>,
                response: Response<GalleryListModel>
            ) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {

                        val mainList = response.body()?.response
                        if (response.body()?.status == true && mainList != null) {
                            dataSet(mainList)
                        }else{
                            binding.txtNoData.visibility = View.VISIBLE
                        }

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<GalleryListModel>, t: Throwable) {
                Log.e("terror",t.message.toString())
            }
        })
    }
    private fun dataSet(galleryList: List<GalleryListResponse>) {
        binding.recyclerview.apply {
            binding.recyclerview.layoutManager = GridLayoutManager(context, 2)
            binding.recyclerview.adapter  = GalleryAdapter(this@GalleryActivity, galleryList) { item ->
                val intent = Intent(this@GalleryActivity, ZoomImageActivity::class.java).apply {
                    putExtra("image", item.image)
                }
                startActivity(intent)
                overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
        }
    }

}