package com.mudiraj.mudirajfoundation.Fragments

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mudiraj.mudirajfoundation.Adapters.NotificationsAdapter
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.Preferences
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.FavouriteModel
import com.mudiraj.mudirajfoundation.Models.FavouriteResponse
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.FragmentGroupBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GroupChatFragment : Fragment() {

    private lateinit var binding: FragmentGroupBinding

    private lateinit var groupChatList: MutableList<FavouriteResponse>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGroupBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.showToast(requireActivity(), "Please check your connection ")
        } else {
          //  getFavouriteApi()
        }



    }

    private fun getFavouriteApi() {
        val userId = Preferences.loadStringValue(requireActivity(), Preferences.userId, "")
        val apiServices = RetrofitClient.apiInterface
        val call = apiServices.getFavouriteApi(getString(R.string.api_key), userId.toString())
        call.enqueue(object : Callback<FavouriteModel> {
            override fun onResponse(call: Call<FavouriteModel>, response: Response<FavouriteModel>) {
                try {
                    if (response.isSuccessful) {
                        groupChatList = response.body()?.response!!
                        if (groupChatList.isEmpty()){
                            binding.linearNoData.visibility = View.VISIBLE
                        }else{
                            DataSet()
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    Log.e("onResponseException", e.message.toString())
                    binding.linearNoData.visibility = View.VISIBLE
                }
            }

            override fun onFailure(call: Call<FavouriteModel>, t: Throwable) {
                Log.e("onFailuregetProductsApi", "API Call Failed: ${t.message}")
                binding.linearNoData.visibility = View.VISIBLE
            }
        })
    }

    private fun DataSet() {
        binding.recyclerview.layoutManager = LinearLayoutManager(requireActivity()).apply {
            isSmoothScrollbarEnabled = true  // Enable smooth scrolling
        }
        binding.recyclerview.setHasFixedSize(true)
        binding.recyclerview.adapter = NotificationsAdapter(requireActivity(), groupChatList)
    }



}