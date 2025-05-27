package com.mudiraj.mudirajfoundation.Adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Models.MemberShipListResponse
import com.mudiraj.mudirajfoundation.R

class HomeMembersAdapter(
    private val context: Context,
    private val items: List<MemberShipListResponse>,
    private val onItemClick: (MemberShipListResponse) -> Unit // Click listener function
) : RecyclerView.Adapter<HomeMembersAdapter.ItemViewHolder>() {

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val recyclerview: RecyclerView = itemView.findViewById(R.id.recyclerview)

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
            .inflate(R.layout.home_members_items_list, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.txtTitle.text = item.name

        // Setup nested RecyclerView
        holder.recyclerview.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        holder.recyclerview.adapter = MemberSubListAdapter(context,item.membershipList,item.name)

    }

    override fun getItemCount(): Int {
        return items.size
    }
}