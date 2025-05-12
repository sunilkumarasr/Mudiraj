package com.mudiraj.mudirajfoundation.Fragments

import android.content.ActivityNotFoundException
import android.content.Context
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
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.mudiraj.mudirajfoundation.Activitys.GalleryActivity
import com.mudiraj.mudirajfoundation.Activitys.MemberDetailsActivity
import com.mudiraj.mudirajfoundation.Adapters.HomeBannersAdapter
import com.mudiraj.mudirajfoundation.Adapters.HomeMembersAdapter
import com.mudiraj.mudirajfoundation.Adapters.NewsAdapter
import com.mudiraj.mudirajfoundation.Logins.LoginActivity
import com.mudiraj.mudirajfoundation.Models.MemberShipListModel
import com.mudiraj.mudirajfoundation.Models.MemberShipListResponse
import com.mudiraj.mudirajfoundation.Models.NewsModel
import com.mudiraj.mudirajfoundation.Models.NewsResponse
import com.mudiraj.mudirajfoundation.Models.StateModel
import com.mudiraj.mudirajfoundation.Models.TotalUsersModel


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding



    //banners
    private lateinit var handler : Handler
    private lateinit var adapter: HomeBannersAdapter

    //banners
    private lateinit var handlerNews : Handler
    private lateinit var adapterNews: NewsAdapter

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
            totalUsersApi()
            bannerListApi()
            memberShipListApi()
            newsListApi()
        }

        //banners
        handler = Handler(Looper.myLooper()!!)
        setUpTransformer()
        binding.viewPagerBanners.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                handler.postDelayed(runnable , 4000)
            }
        })


        //news
        handlerNews = Handler(Looper.myLooper()!!)
        setUpTransformerNews()
        binding.viewPagerNews.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handlerNews.removeCallbacks(runnableNews)
                handlerNews.postDelayed(runnableNews , 4000)
            }
        })


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

        binding.imgQR.setOnClickListener {
            val upiUri = "upi://pay?pa=43909649794@sbi&pn=MUDIRAJ%20FOUNDATION%20TELANGANA&mc=7407&tr=TXN123456&tn=Donation&am=1.00&cu=INR"
            openUpiIntent(upiUri, requireActivity())
        }

        binding.cardViewGallery.setOnClickListener {
            val intent = Intent(requireActivity(), GalleryActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }


    }

    fun openUpiIntent(upiUri: String, context: Context) {
        val uri = Uri.parse(upiUri)

        // Try Google Pay
        val gpayIntent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
            setPackage("com.google.android.apps.nbu.paisa.user")
        }

        // Try PhonePe
        val phonePeIntent = Intent(Intent.ACTION_VIEW).apply {
            data = uri
            setPackage("com.phonepe.app")
        }

        try {
            context.startActivity(gpayIntent)
        } catch (e: Exception) {
            try {
                context.startActivity(phonePeIntent)
            } catch (e: Exception) {
                Toast.makeText(context, "No supported UPI app found", Toast.LENGTH_SHORT).show()
            }
        }
    }


    private fun totalUsersApi() {
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.totalUsersApi(
                getString(R.string.api_key)
            )
        call.enqueue(object : Callback<TotalUsersModel> {
            override fun onResponse(
                call: Call<TotalUsersModel>,
                response: Response<TotalUsersModel>
            ) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {

                        val TotalUsers = response.body()?.response
                        if (response.body()?.status == true) {
                            if (TotalUsers != null) {
                                binding.txtTotalUsers.text = "Total Users:"+TotalUsers.count.toString()
                            }
                        }

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<TotalUsersModel>, t: Throwable) {
                Log.e("terror",t.message.toString())
            }
        })
    }
    //banners
    private fun bannerListApi() {
        val constituencies = Preferences.loadStringValue(requireActivity(), Preferences.constituencies, "")

        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.bannerListApi(
                getString(R.string.api_key),
                constituencies.toString()
            )
        call.enqueue(object : Callback<BannersModel> {
            override fun onResponse(
                call: Call<BannersModel>,
                response: Response<BannersModel>
            ) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {

                        val bannersServicesList = response.body()?.response
                        //empty
                        if (bannersServicesList.isNullOrEmpty()) {
                            binding.viewPagerBanners.visibility = View.GONE
                            return
                        }
                        dataSetBanners(bannersServicesList)

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<BannersModel>, t: Throwable) {
                Log.e("terror",t.message.toString())
            }
        })
    }
    private fun dataSetBanners(bannersselectedServicesList: ArrayList<BannersResponse>) {
        adapter = HomeBannersAdapter(bannersselectedServicesList, binding.viewPagerBanners)
        binding.viewPagerBanners.adapter = adapter
        binding.viewPagerBanners.offscreenPageLimit = 3
        binding.viewPagerBanners.clipToPadding = false
        binding.viewPagerBanners.clipChildren = false
        binding.viewPagerBanners.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

    }
    private val runnable = Runnable {
        binding.viewPagerBanners.currentItem = binding.viewPagerBanners.currentItem + 1
    }
    private fun setUpTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(20))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
//            page.alpha = 0.50f + (1 - abs(position)) * 0.50f
        }
        binding.viewPagerBanners.setPageTransformer(transformer)
    }

    //members list
    private fun memberShipListApi() {
        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.memberShipListApi(
                getString(R.string.api_key)
            )
        call.enqueue(object : Callback<MemberShipListModel> {
            override fun onResponse(
                call: Call<MemberShipListModel>,
                response: Response<MemberShipListModel>
            ) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {

                        val mainList = response.body()?.response
                        if (response.body()?.status == true && mainList != null) {
                            dataSet(mainList)
                        }else{

                        }

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<MemberShipListModel>, t: Throwable) {
                Log.e("terror",t.message.toString())
            }
        })
    }
    private fun dataSet(membersList: List<MemberShipListResponse>) {
        binding.recyclerview.apply {
            binding.recyclerview.layoutManager = LinearLayoutManager(context)
            binding.recyclerview.adapter  = HomeMembersAdapter(requireActivity(), membersList) { item ->
                val intent = Intent(activity, MemberDetailsActivity::class.java).apply {
                    putExtra("membershipId", item.membershipId)
                }
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
        }
    }

    //News banners
    private fun newsListApi() {
        val constituencies = Preferences.loadStringValue(requireActivity(), Preferences.constituencies, "")

        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.newsListApi(
                getString(R.string.api_key),
                constituencies.toString()
            )
        call.enqueue(object : Callback<NewsModel> {
            override fun onResponse(
                call: Call<NewsModel>,
                response: Response<NewsModel>
            ) {
                ViewController.hideLoading()
                try {
                    if (response.isSuccessful) {

                        val newsServicesList = response.body()?.response
                        //empty
                        if (newsServicesList.isNullOrEmpty()) {
                            binding.viewPagerNews.visibility = View.GONE
                            return
                        }
                        dataSetNews(newsServicesList)

                    }
                } catch (e: NullPointerException) {
                    e.printStackTrace()
                }
            }

            override fun onFailure(call: Call<NewsModel>, t: Throwable) {
                Log.e("terror",t.message.toString())
            }
        })
    }
    private fun dataSetNews(newsSelectedServicesList: ArrayList<NewsResponse>) {
        adapterNews = NewsAdapter(newsSelectedServicesList, binding.viewPagerNews)
        binding.viewPagerNews.adapter = adapterNews
        binding.viewPagerNews.offscreenPageLimit = 3
        binding.viewPagerNews.clipToPadding = false
        binding.viewPagerNews.clipChildren = false
        binding.viewPagerNews.getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER

    }
    private val runnableNews = Runnable {
        binding.viewPagerNews.currentItem = binding.viewPagerNews.currentItem + 1
    }
    private fun setUpTransformerNews() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(20))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }
        binding.viewPagerNews.setPageTransformer(transformer)
    }




}