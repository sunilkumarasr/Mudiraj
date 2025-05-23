package com.mudiraj.mudirajfoundation.Adapters.Search

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.R

class SearchAdapter(
    private val items: List<SearchItems>,
    private val onItemClick: (SearchItems) -> Unit
) : RecyclerView.Adapter<SearchAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgProducts: ImageView = itemView.findViewById(R.id.imgProducts)
        val txtTitle: TextView = itemView.findViewById(R.id.txtTitle)
        val txtOfferPrice: TextView = itemView.findViewById(R.id.txtOfferPrice)
        val txtActualPrice: TextView = itemView.findViewById(R.id.txtActualPrice)
        val txtQuantity: TextView = itemView.findViewById(R.id.txtQuantity)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.search_items_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        Glide.with(holder.imgProducts)
            .load(item.product_image)
            .error(R.drawable.logo)
            .into(holder.imgProducts)
        holder.txtTitle.text = item.product_name
        holder.txtOfferPrice.text = "₹"+item.offer_price
        holder.txtQuantity.text = item.quantity

        holder.txtActualPrice.text = "₹"+item.sales_price
        holder.txtActualPrice.paintFlags = holder.txtActualPrice.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

    }

    override fun getItemCount(): Int {
        return items.size
    }

}