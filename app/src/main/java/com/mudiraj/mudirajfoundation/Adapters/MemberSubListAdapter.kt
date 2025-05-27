package com.mudiraj.mudirajfoundation.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mudiraj.mudirajfoundation.Activitys.MemberDetailsActivity
import com.mudiraj.mudirajfoundation.Api.RetrofitClient
import com.mudiraj.mudirajfoundation.Models.MemberShipList
import com.mudiraj.mudirajfoundation.R

class MemberSubListAdapter(
    private val context: Context,
    private val users: List<MemberShipList>,
    private val name: String,
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

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MemberDetailsActivity::class.java).apply {
                putExtra("name", name)
                putExtra("userId", user.userId)
                putExtra("role", user.role)
                putExtra("fullName", user.fullName)
                putExtra("email", user.email)
                putExtra("phone", user.phone)
                putExtra("image", user.image)
                putExtra("businessName", user.businessName)
                putExtra("fullAddress", user.fullAddress)
                putExtra("state", user.state)
                putExtra("constituencies", user.constituencies)
                putExtra("membershipType", user.membershipType)
            }
            context.startActivity(intent)
            if (context is Activity) {
                context.overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
        }
    }

    override fun getItemCount(): Int = users.size
}