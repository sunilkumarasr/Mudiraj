package com.mudiraj.mudirajfoundation.Adapters.Cart

import com.google.gson.annotations.SerializedName

data class CartListResponse(
    @SerializedName("Status") val status: Boolean,
    @SerializedName("Message") val message: String,
    @SerializedName("Response") val ResponseCartList: List<CartItems>,
    @SerializedName("code") val code: Int,
    @SerializedName("customer_category") val customer_category: Int
)

data class CartItems(
    @SerializedName("id") val id: String,
    @SerializedName("customer_id") val customer_id: String,
    @SerializedName("product_id") val product_id: String,
    @SerializedName("quantity") val quantity: String,
    @SerializedName("cart_quantity") var cart_quantity: String,
    @SerializedName("products_id") val products_id: String,
    @SerializedName("categories_id") val categories_id: String,
    @SerializedName("product_num") val product_num: String,
    @SerializedName("product_name") val product_name: String,
    @SerializedName("product_information") val product_information: String,
    @SerializedName("product_image") val product_image: String,
    @SerializedName("sales_price") val sales_price: String,
    @SerializedName("offer_price") val offer_price: String,
    @SerializedName("category_2_price") val category_2_price: String,
    @SerializedName("stock") val stock: String,
    @SerializedName("status") val status: String,
    @SerializedName("order_by") val order_by: String,
    @SerializedName("tax_number") val tax_number: String,
    @SerializedName("item_name") val item_name: String,
    @SerializedName("batch_name") val batch_name: String,
    @SerializedName("created_date") val created_date: String,
    @SerializedName("updated_date") val updated_date: String,
    @SerializedName("product_quantity") val product_quantity: String,
)