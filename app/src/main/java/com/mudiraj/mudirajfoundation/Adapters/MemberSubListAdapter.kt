package com.mudiraj.mudirajfoundation.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Models.MemberShipList
import com.mudiraj.mudirajfoundation.R

class MemberSubListAdapter(
    private val users: List<MemberShipList>
) : RecyclerView.Adapter<MemberSubListAdapter.UserViewHolder>() {

    inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgUser: ImageView = itemView.findViewById(R.id.imgUser)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_member_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = users[position]

        Glide.with(holder.imgUser.context)
            .load(RetrofitClient.Image_URL + user.image)
            .placeholder(R.drawable.logo)
            .into(holder.imgUser)
    }

    override fun getItemCount(): Int = users.size
}