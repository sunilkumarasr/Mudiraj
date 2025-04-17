package com.mudiraj.mudirajfoundation.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mudiraj.mudirajfoundation.Activitys.ProductsDetailsActivity
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.AddFavouriteModel
import com.mudiraj.mudirajfoundation.Models.FavouriteResponse
import com.mudiraj.mudirajfoundation.R
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NotificationsAdapter(
    private val context: Context,
    private val itemList: MutableList<FavouriteResponse>,
    ) : RecyclerView.Adapter<NotificationsAdapter.ItemViewHolder>() {

    class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducts: ImageView = itemView.findViewById(R.id.imgProducts)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtDec: TextView = itemView.findViewById(R.id.txtDec)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.favorite_products_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = itemList[position]

        Glide.with(holder.imgProducts)
            .load(RetrofitClient.Image_URL2+item.productImage)
            .placeholder(R.drawable.logo)
            .error(R.drawable.logo)
            .into(holder.imgProducts)
        holder.txtTitle.text = item.productName


    }

    override fun getItemCount(): Int {
        return itemList.size
    }

}