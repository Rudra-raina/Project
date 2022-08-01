package com.example.shopperista.activities.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopperista.R
import com.example.shopperista.adapters.SoldProductsListAdapter
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.SoldProduct
import com.example.shopperista.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_sold_products.*

class SoldProductsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_sold_products, container, false)
    }

    override fun onResume() {
        super.onResume()
        getSoldProducts()
    }

    private fun getSoldProducts(){
        showProgressDialog()
        FirestoreClass().getSoldProductsList(this)
    }

    fun successSoldProductsList(list: ArrayList<SoldProduct>) {
        hideProgressDialog()
        if(list.size>0){
            rv_sold_product_items.visibility=View.VISIBLE
            tv_no_sold_products_found.visibility=View.GONE

            rv_sold_product_items.layoutManager=LinearLayoutManager(activity)
            rv_sold_product_items.setHasFixedSize(true)

            val soldProductListAdapter = SoldProductsListAdapter(requireActivity(),list)
            rv_sold_product_items.adapter=soldProductListAdapter
        }
        else{
            rv_sold_product_items.visibility=View.GONE
            tv_no_sold_products_found.visibility=View.VISIBLE
        }
    }

}