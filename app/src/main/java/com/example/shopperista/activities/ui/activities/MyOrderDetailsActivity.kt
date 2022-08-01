package com.example.shopperista.activities.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopperista.R
import com.example.shopperista.adapters.CartItemsListAdapter
import com.example.shopperista.adapters.OrderDetailsAdapter
import com.example.shopperista.models.Order
import com.example.shopperista.utils.Constants
import kotlinx.android.synthetic.main.activity_my_order_details.*

class MyOrderDetailsActivity : AppCompatActivity() {

    private lateinit var mMyOrderDetails : Order

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_order_details)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        if(intent.hasExtra(Constants.EXTRA_MY_ORDERS_DETAILS)){
            mMyOrderDetails=intent.getParcelableExtra(Constants.EXTRA_MY_ORDERS_DETAILS)!!

            setUpUI(mMyOrderDetails)
        }

    }

    @SuppressLint("SetTextI18n")
    private fun setUpUI(orderDetails: Order) {
        val substring = orderDetails.order_title!!.substring(0,9)
        tv_order_details_id.text = substring
        tv_order_details_date.text=orderDetails.date

        rv_my_order_items_list.layoutManager=LinearLayoutManager(this)
        rv_my_order_items_list.setHasFixedSize(true)

//        val cartListAdapter = CartItemsListAdapter(this,orderDetails.items!!,false)
        val cartListAdapter = OrderDetailsAdapter(this,orderDetails.items!!)
        rv_my_order_items_list.adapter=cartListAdapter

        tv_my_order_details_address_type.text = orderDetails.address!!.type
        tv_my_order_details_full_name.text = orderDetails.address.name
        tv_my_order_details_address.text = "${orderDetails.address.address}, ${orderDetails.address.zipCode}"

        tv_order_details_sub_total.text = "₹ ${orderDetails.sub_total_amount}"
        tv_order_details_shipping_charge.text = "₹ ${orderDetails.shipping_charge}"
        tv_order_details_total_amount.text = "₹ ${orderDetails.total_amount}"

    }
}