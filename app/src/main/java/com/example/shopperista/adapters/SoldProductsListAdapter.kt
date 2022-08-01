package com.example.shopperista.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopperista.R
import com.example.shopperista.activities.ui.activities.SoldProductDetailsActivity
import com.example.shopperista.models.SoldProduct
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

class SoldProductsListAdapter(private var context : Context , private var list : ArrayList<SoldProduct>)
    : RecyclerView.Adapter<SoldProductsListAdapter.MyViewHolder>() {

    class MyViewHolder(view:View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SoldProductsListAdapter.MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false))
    }

    override fun onBindViewHolder(holder: SoldProductsListAdapter.MyViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(model.image, holder.itemView.iv_item_image)

        holder.itemView.tv_item_name.text = model.title
        holder.itemView.tv_item_price.text = "₹ ${model.price}"

        holder.itemView.ib_delete_product.visibility = View.GONE
        holder.itemView.ib_edit_product.visibility= View.GONE

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SoldProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS, model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

