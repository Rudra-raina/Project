package com.example.shopperista.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopperista.R
import com.example.shopperista.activities.ui.activities.ProductDetailsActivity
import com.example.shopperista.models.Product
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.GlideLoader
import kotlinx.android.synthetic.main.item_dashboard_layout.view.*

class DashboardItemsListAdapter(private val context: Context,private val list:ArrayList<Product>) :
                                                                    RecyclerView.Adapter<DashboardItemsListAdapter.MyViewHolder>(){

    class MyViewHolder(view:View):RecyclerView.ViewHolder(view)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_dashboard_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(model.image!!,holder.itemView.iv_item_image_dashboard)
        holder.itemView.tv_item_name_dashboard.text = model.title
        holder.itemView.tv_item_price_dashboard.text=model.price

        holder.itemView.more_info.setOnClickListener{
            val intent = Intent(context, ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
            intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,model.user_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}

