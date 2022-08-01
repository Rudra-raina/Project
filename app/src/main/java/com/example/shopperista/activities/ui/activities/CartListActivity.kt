package com.example.shopperista.activities.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopperista.R
import com.example.shopperista.adapters.CartItemsListAdapter
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.CartItem
import com.example.shopperista.models.Product
import com.example.shopperista.utils.Base
import com.example.shopperista.utils.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*

class CartListActivity : Base() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart_list)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        btn_checkout.setOnClickListener{
            val intent = Intent(this,AddressListActivity::class.java)
            intent.putExtra(Constants.EXTRA_SELECT_ADDRESS,true)
            startActivity(intent)
        }
        getCartItemList()
    }

    private fun getCartItemList(){
        showProgressDialog()
        FirestoreClass().getCartList(this)
    }

    @SuppressLint("SetTextI18n")
    fun successCartItemsList(cartList: ArrayList<CartItem>){

        hideProgressDialog()


        if(cartList.size>0){
            rv_cart_items_list.visibility= View.VISIBLE
            ll_checkout.visibility=View.VISIBLE
            tv_no_cart_item_found.visibility=View.GONE

            rv_cart_items_list.layoutManager=LinearLayoutManager(this)
            rv_cart_items_list.setHasFixedSize(true)

            val adapter = CartItemsListAdapter(this,cartList,true)
            rv_cart_items_list.adapter=adapter

            var subtotal  = 0.0
            for(item in cartList){
                val price = item.price!!.toDouble()
                val quantity = item.cart_quantity!!.toDouble()
                subtotal +=(price*quantity)
            }

            tv_sub_total.text=  "₹ $subtotal"
            tv_shipping_charge.text="₹ 30.0"

            if(subtotal>0){
                ll_checkout.visibility=View.VISIBLE
                val total = subtotal+30
                tv_total_amount.text="₹ $total"
            }
            else{
                ll_checkout.visibility=View.GONE
            }
        }
        else{
            rv_cart_items_list.visibility=View.GONE
            ll_checkout.visibility=View.GONE
            tv_no_cart_item_found.visibility=View.VISIBLE
        }
    }

    fun itemRemovedSuccess(){
        hideProgressDialog()
        Toast.makeText(this,"Item removes successfully",Toast.LENGTH_SHORT).show()
        getCartItemList()
    }


    fun itemUpdateSuccess(){
        hideProgressDialog()
        getCartItemList()
    }

}