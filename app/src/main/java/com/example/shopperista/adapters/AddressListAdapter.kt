package com.example.shopperista.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.shopperista.R
import com.example.shopperista.activities.ui.activities.AddEditAddressActivity
import com.example.shopperista.activities.ui.activities.AddressListActivity
import com.example.shopperista.activities.ui.activities.CheckoutActivity
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.Address
import com.example.shopperista.utils.Constants
import kotlinx.android.synthetic.main.item_address_layout.view.*

class AddressListAdapter(
    private val context : Context,
    private val list : ArrayList<Address>,
    private val selectAddress : Boolean) :
    RecyclerView.Adapter<AddressListAdapter.MyViewHolder>(){

    class MyViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(LayoutInflater.from(context).inflate(R.layout.item_address_layout, parent, false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val model = list[position]

        holder.itemView.tv_address_full_name.text = model.name
        holder.itemView.tv_address_type.text = model.type
        holder.itemView.tv_address_details.text = "${model.address}, ${model.zipCode}"
        holder.itemView.tv_address_mobile_number.text = model.mobileNumber

        if(selectAddress){
            holder.itemView.setOnClickListener{
                val intent = Intent(context, CheckoutActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, model)
                context.startActivity(intent)
            }
        }


    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun notifyEditItem(activity : Activity, position: Int){
        val intent = Intent(context,AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS,list[position])
        activity.startActivity(intent)
        notifyItemChanged(position)
    }

    fun notifyDeleteItem(activity: AddressListActivity, position: Int) {
        activity.showProgressDialog()
        FirestoreClass().deleteAddress(activity,list[position].id!!)
        notifyItemRemoved(position)

    }


}