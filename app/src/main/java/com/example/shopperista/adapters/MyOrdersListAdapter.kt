package com.example.shopperista.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopperista.R
import com.example.shopperista.activities.ui.activities.MyOrderDetailsActivity
import com.example.shopperista.models.Order
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

class MyOrdersListAdapter(private val context : Context , private var list: ArrayList<Order> ) :
                                                                        RecyclerView.Adapter<MyOrdersListAdapter.MyViewHolder>(){

    class MyViewHolder(view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(model.image!!,holder.itemView.iv_item_image)
        holder.itemView.tv_item_name.text=model.date
        holder.itemView.tv_item_price.text="₹ ${model.total_amount}"

        holder.itemView.ib_delete_product.visibility=View.GONE
        holder.itemView.ib_edit_product.visibility=View.GONE

        holder.itemView.setOnClickListener{
            val intent = Intent(context,MyOrderDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_MY_ORDERS_DETAILS,model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}