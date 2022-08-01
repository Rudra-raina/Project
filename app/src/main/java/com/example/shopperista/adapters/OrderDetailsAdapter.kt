package com.example.shopperista.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopperista.R
import com.example.shopperista.models.CartItem
import com.example.shopperista.utils.GlideLoader
import kotlinx.android.synthetic.main.my_order_details_single_item.view.*

class OrderDetailsAdapter(
    private val context : Context,
    private val list : ArrayList<CartItem> ) : RecyclerView.Adapter<OrderDetailsAdapter.MyViewHolder>() {

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.my_order_details_single_item, parent, false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        // image -> price
        // price -> title
        // title -> product_id
        // id -> stock quantity
        // cart quantity -> image
        // stock quantity -> cart quantity
        // product_id -> product owner id

        // correct -> user_id

        GlideLoader(context).loadProductPicture(model.price!!,holder.itemView.iv_my_order_details_single_item)
        holder.itemView.tv_my_order_details_single_item_title.text = model.product_id
        holder.itemView.tv_my_order_details_single_price.text="₹ ${model.title}"
        holder.itemView.tv_my_order_details_single_cart_quantity.text=model.image
    }

    override fun getItemCount(): Int {
        return list.size
    }
}