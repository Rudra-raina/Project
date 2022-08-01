package com.example.shopperista.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopperista.R
import com.example.shopperista.activities.ui.activities.AddProductActivity
import com.example.shopperista.activities.ui.activities.ProductDetailsActivity
import com.example.shopperista.activities.ui.fragments.ProductsFragment
import com.example.shopperista.models.Product
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.GlideLoader
import kotlinx.android.synthetic.main.item_list_layout.view.*

class MyProductsListAdapter(
    private val context: Context,
    private var list : ArrayList<Product>,
    private val fragment : ProductsFragment) : RecyclerView.Adapter<MyProductsListAdapter.MyViewHolder>(){


    class MyViewHolder ( view : View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_list_layout,parent,false))
    }


    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(model.image!!,holder.itemView.iv_item_image)
        holder.itemView.tv_item_name.text = model.title
        holder.itemView.tv_item_price.text="Rs. ${model.price}"

        holder.itemView.ib_delete_product.setOnClickListener{
            fragment.deleteProduct(model.product_id!!)
        }
        holder.itemView.ib_edit_product.setOnClickListener{
            val intent = Intent(context,AddProductActivity::class.java)
            intent.putExtra(Constants.EDIT_PRODUCT,model)
            context.startActivity(intent)
        }

        holder.itemView.setOnClickListener{
            val intent = Intent(context,ProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_PRODUCT_ID,model.product_id)
            intent.putExtra(Constants.EXTRA_PRODUCT_OWNER_ID,model.user_id)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}