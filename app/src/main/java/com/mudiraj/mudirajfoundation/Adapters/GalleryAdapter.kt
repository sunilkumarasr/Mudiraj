package com.mudiraj.mudirajfoundation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.GalleryListResponse
import com.mudiraj.mudirajfoundation.Models.MemberShipListResponse
import com.mudiraj.mudirajfoundation.R

class GalleryAdapter(
    private val context: Context,
    private val items: List<GalleryListResponse>,
    private val onItemClick: (GalleryListResponse) -> Unit // Click listener function
) : RecyclerView.Adapter<GalleryAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgGallery: ImageView = itemView.findViewById(R.id.imgGallery)

        init {
            itemView.setOnClickListener {
                val animations = ViewController.animation()
                itemView.startAnimation(animations)
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onItemClick(items[position])
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.gallery_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]

        Glide.with(holder.imgGallery)
            .load(RetrofitClient.Image_URL+item.image)
            .error(R.drawable.logo)
            .into(holder.imgGallery)

    }

    override fun getItemCount(): Int {
        return items.size
    }

}