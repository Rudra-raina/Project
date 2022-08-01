package com.example.shopperista.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopperista.R
import com.example.shopperista.activities.ui.activities.CartListActivity
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.CartItem
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.GlideLoader
import kotlinx.android.synthetic.main.item_cart_layout.view.*

class CartItemsListAdapter(
    private val context : Context,
    private val list : ArrayList<CartItem>,
    private val updateCartItems: Boolean) : RecyclerView.Adapter<CartItemsListAdapter.MyViewHolder>(){


    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_cart_layout,parent,false))
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(model.image!!,holder.itemView.iv_cart_item_image)
        holder.itemView.tv_cart_item_title.text = model.title!!
        holder.itemView.tv_cart_item_price.text="₹ ${model.price!!}"
        holder.itemView.tv_cart_quantity.text=model.cart_quantity!!

        if(updateCartItems){
            holder.itemView.ib_delete_cart_item.setOnClickListener{
                when(context){
                    is CartListActivity -> {
                        context.showProgressDialog()
                    }
                }
                FirestoreClass().removeItemFromCart(context,model.cart_id!!)
            }

            holder.itemView.ib_remove_cart_item.setOnClickListener{
                if(model.cart_quantity=="1"){
                    FirestoreClass().removeItemFromCart(context,model.cart_id!!)
                }
                else{
                    val cartQuantity = model.cart_quantity!!.toInt()

                    val itemHashMap = HashMap<String,Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity-1).toString()

                    if(context is CartListActivity){
                        context.showProgressDialog()
                    }

                    FirestoreClass().updateMyCart(context,model.cart_id!!,itemHashMap)
                }
            }

            holder.itemView.ib_add_cart_item.setOnClickListener{
                val cartQuantity = model.cart_quantity!!.toInt()

                if(cartQuantity<model.stock_quantity!!.toInt()){
                    val itemHashMap = HashMap<String,Any>()
                    itemHashMap[Constants.CART_QUANTITY] = (cartQuantity+1).toString()

                    if(context is CartListActivity){
                        context.showProgressDialog()
                    }

                    FirestoreClass().updateMyCart(context,model.cart_id!!,itemHashMap)
                }
                else{
                    if(context is CartListActivity){
                        context.showErrorSnackBar("Out of stock")
                    }
                }

            }

        }
        else{
            holder.itemView.ib_remove_cart_item.visibility=View.GONE
            holder.itemView.ib_add_cart_item.visibility=View.GONE
            holder.itemView.ib_delete_cart_item.visibility=View.GONE
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }
}