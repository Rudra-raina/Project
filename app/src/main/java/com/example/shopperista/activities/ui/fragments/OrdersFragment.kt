package com.example.shopperista.activities.ui.fragments

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopperista.R
import com.example.shopperista.adapters.MyOrdersListAdapter
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.Order
import com.example.shopperista.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_orders.*

class OrdersFragment : BaseFragment() {


    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_orders, container, false)
    }

    override fun onResume() {
        super.onResume()
        getMyOrdersList()
    }

    private fun getMyOrdersList(){
        showProgressDialog()
        FirestoreClass().getMyOrdersList(this)
    }
    fun populateOrderListInUI(ordersList: ArrayList<Order>) {
        hideProgressDialog()

        if(ordersList.size>0){
            rv_my_order_items.visibility=View.VISIBLE
            tv_no_orders_found.visibility=View.GONE

            rv_my_order_items.layoutManager=LinearLayoutManager(requireActivity())
            rv_my_order_items.setHasFixedSize(true)

            val myOrderAdapter = MyOrdersListAdapter(requireActivity(),ordersList)
            rv_my_order_items.adapter=myOrderAdapter
        }
        else{
            rv_my_order_items.visibility=View.GONE
            tv_no_orders_found.visibility=View.VISIBLE
        }
    }


}