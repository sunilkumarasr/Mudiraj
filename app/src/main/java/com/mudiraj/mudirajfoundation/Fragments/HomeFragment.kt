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
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mudiraj.mudirajfoundation.Activitys.DashBoardActivity
import com.mudiraj.mudirajfoundation.Activitys.GalleryActivity
import com.mudiraj.mudirajfoundation.Adapters.HomeBannersAdapter
import com.mudiraj.mudirajfoundation.Adapters.HomeMembersAdapter
import com.mudiraj.mudirajfoundation.Adapters.NewsAdapter
import com.mudiraj.mudirajfoundation.Models.MemberShipListModel
import com.mudiraj.mudirajfoundation.Models.MemberShipListResponse
import com.mudiraj.mudirajfoundation.Models.NewsModel
import com.mudiraj.mudirajfoundation.Models.NewsResponse
import com.mudiraj.mudirajfoundation.Models.StateListResponse
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

    var selectedState: String = ""
    var selectedStateName: String = ""
    var selectedConstituency: String = ""
    var selectedConstituencyName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        binding.txtDonateNow.setOnClickListener {
            openUPIApp()
        }

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
                data = Uri.parse("mailto:mudirajfoundation1@gmail.com")
                putExtra(Intent.EXTRA_SUBJECT, "Inquiry from App")
                putExtra(Intent.EXTRA_TEXT, "Hello Mudiraj Foundation,\n\n")
            }
            try {
                startActivity(intent)
            } catch (e: ActivityNotFoundException) {
                Toast.makeText(requireActivity(), "No email app found!", Toast.LENGTH_SHORT).show()
            }
        }

        binding.imgQR.setOnClickListener {
            openUpiIntent()
        }

        binding.cardViewGallery.setOnClickListener {
            val intent = Intent(requireActivity(), GalleryActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }


        binding.cardLocation.setOnClickListener {
            locationSelectDialog()
        }

    }

    private fun openUPIApp() {
        val uri = Uri.parse(
            "upi://pay?pa=mudirajfoundations@okaxis&pn=MUDIRAJ%20FOUNDATION%20TELANGANA&mc=7407&mode=02&purpose=00"
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri

        // Check if there is an app to handle this
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireActivity(), "No UPI app found to handle this request", Toast.LENGTH_SHORT).show()
        }
    }

    fun openUpiIntent() {
        val uri = Uri.parse(
            "upi://pay?pa=mudirajfoundations@okaxis&pn=MUDIRAJ%20FOUNDATION%20TELANGANA&mc=7407&mode=02&purpose=00"
        )

        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = uri

        // Check if there is an app to handle this
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(requireActivity(), "No UPI app found to handle this request", Toast.LENGTH_SHORT).show()
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
        val state = Preferences.loadStringValue(requireActivity(), Preferences.state, "")
        val stateName = Preferences.loadStringValue(requireActivity(), Preferences.stateName, "")
        val constituenciesName = Preferences.loadStringValue(requireActivity(), Preferences.constituenciesName, "")
        binding.txtState.text = stateName
        binding.txtConstituency.text = constituenciesName


        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.bannerListApi(
                getString(R.string.api_key),
                constituencies.toString(),
                state.toString()
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
        val constituencies = Preferences.loadStringValue(requireActivity(), Preferences.constituencies, "")
        val state = Preferences.loadStringValue(requireActivity(), Preferences.state, "")

        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.memberShipListApi(
                getString(R.string.api_key),
                constituencies.toString(),
                state.toString()
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
            }
        }
    }

    //News banners
    private fun newsListApi() {
        val constituencies = Preferences.loadStringValue(requireActivity(), Preferences.constituencies, "")
        val state = Preferences.loadStringValue(requireActivity(), Preferences.state, "")

        val apiServices = RetrofitClient.apiInterface
        val call =
            apiServices.newsListApi(
                getString(R.string.api_key),
                constituencies.toString(),
                state.toString()
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


    private fun locationSelectDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_location, null)
        bottomSheetDialog.setContentView(view)

        val checkboxState = view.findViewById<CheckBox>(R.id.checkboxState)
        val checkboxCity = view.findViewById<CheckBox>(R.id.checkboxCity)
        val StateSpinner = view.findViewById<Spinner>(R.id.StateSpinner)
        val ConstituenciesSpinner = view.findViewById<Spinner>(R.id.ConstituenciesSpinner)
        val linearSubmit = view.findViewById<LinearLayout>(R.id.linearSubmit)

        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.customToast(requireActivity(), "Please check your connection ")
        } else {
            StateListApi(StateSpinner)
        }

        StateSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val state = parent.getItemAtPosition(position) as StateListResponse
                selectedState = state.id
                selectedStateName = state.name
                ConstituencyListApi(ConstituenciesSpinner, selectedState)
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
        ConstituenciesSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                val state = parent.getItemAtPosition(position) as StateListResponse
                selectedConstituency = state.id
                selectedConstituencyName = state.name
            }
            override fun onNothingSelected(parent: AdapterView<*>) {
            }
        }

        linearSubmit.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)

            if (selectedState.isEmpty()) {
                ViewController.showToast(requireActivity(), "Select State")
            } else if (selectedConstituency.isEmpty()) {
                ViewController.showToast(requireActivity(), "Select Constituency")
            } else {
                if (checkboxState.isChecked && checkboxCity.isChecked) {
                    Preferences.saveStringValue(requireActivity(), Preferences.state, selectedState)
                    Preferences.saveStringValue(requireActivity(), Preferences.stateName, selectedStateName)
                    Preferences.saveStringValue(requireActivity(), Preferences.constituencies, selectedConstituency)
                    Preferences.saveStringValue(requireActivity(), Preferences.constituenciesName, selectedConstituencyName)

                    bottomSheetDialog.dismiss()

                    val intent = Intent(requireActivity(), DashBoardActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)

                } else if (checkboxState.isChecked) {
                    Preferences.saveStringValue(requireActivity(), Preferences.state, selectedState)
                    Preferences.saveStringValue(requireActivity(), Preferences.stateName, selectedStateName)
                    Preferences.saveStringValue(requireActivity(), Preferences.constituencies, "")
                    Preferences.saveStringValue(requireActivity(), Preferences.constituenciesName, "")

                    bottomSheetDialog.dismiss()

                    val intent = Intent(requireActivity(), DashBoardActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(0, 0)

                } else {
                    Toast.makeText(requireActivity(), "Please check state", Toast.LENGTH_SHORT).show()
                }
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
                                requireActivity(),
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
                                requireActivity(),
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