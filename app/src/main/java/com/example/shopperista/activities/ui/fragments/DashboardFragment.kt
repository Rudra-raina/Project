package com.example.shopperista.activities.ui.fragments

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.example.shopperista.R
import com.example.shopperista.activities.ui.activities.CartListActivity
import com.example.shopperista.activities.ui.activities.SettingsActivity
import com.example.shopperista.adapters.DashboardItemsListAdapter
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.Product
import com.example.shopperista.utils.BaseFragment
import com.example.shopperista.utils.Constants
import kotlinx.android.synthetic.main.fragment_dashboard.*

class DashboardFragment : BaseFragment() {

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        showProgressDialog()

    }

    override fun onResume() {


        if(rb_items_dashboard.isChecked){
            FirestoreClass().getDashboardItems(this,Constants.ALL_ITEMS)
        }

        rg_dashboard.setOnCheckedChangeListener{ _, checkID ->
            if(checkID==R.id.rb_items_dashboard){
                FirestoreClass().getDashboardItems(this,Constants.ALL_ITEMS)
            }
            else if(checkID==R.id.rb_electronics_dashboard){
                FirestoreClass().getDashboardItems(this,Constants.ELECTRONICS)
            }
            else if(checkID==R.id.rb_apparels_dashboard){
                FirestoreClass().getDashboardItems(this,Constants.APPARELS)
            }
            else if(checkID==R.id.rb_sports_dashboard){
                FirestoreClass().getDashboardItems(this,Constants.SPORTS)
            }
            else{
                FirestoreClass().getDashboardItems(this,Constants.STATIONERY)
            }
        }
        super.onResume()
    }

    fun successDashboardItemsList(productList: ArrayList<Product>, itemType: String) {


        when(itemType){
            Constants.ALL_ITEMS ->{
                hideProgressDialog()
                if(productList.size>0){
                    rv_dashboard_items.visibility=View.VISIBLE
                    rv_dashboard_items_electronics.visibility=View.GONE
                    rv_dashboard_items_apparels.visibility=View.GONE
                    rv_dashboard_items_sports.visibility=View.GONE
                    rv_dashboard_items_stationery.visibility=View.GONE
                    tv_no_dashboard_item_found.visibility=View.GONE

                    rv_dashboard_items.layoutManager= GridLayoutManager(activity,2)
                    rv_dashboard_items.setHasFixedSize(true)
                    val adapter = DashboardItemsListAdapter(requireActivity(),productList)
                    rv_dashboard_items.adapter=adapter
                }
                else{
                    handleVisibilityWhenNoItemsFound()
                }
            }
            Constants.ELECTRONICS -> {
                hideProgressDialog()
                if(productList.size>0){
                    rv_dashboard_items.visibility=View.GONE
                    rv_dashboard_items_electronics.visibility=View.VISIBLE
                    rv_dashboard_items_apparels.visibility=View.GONE
                    rv_dashboard_items_sports.visibility=View.GONE
                    rv_dashboard_items_stationery.visibility=View.GONE
                    tv_no_dashboard_item_found.visibility=View.GONE

                    rv_dashboard_items_electronics.layoutManager= GridLayoutManager(activity,2)
                    rv_dashboard_items_electronics.setHasFixedSize(true)
                    val adapter = DashboardItemsListAdapter(requireActivity(),productList)
                    rv_dashboard_items_electronics.adapter=adapter
                }
                else{
                    handleVisibilityWhenNoItemsFound()
                }

            }
            Constants.APPARELS -> {
                hideProgressDialog()
                if(productList.size>0){
                    rv_dashboard_items.visibility=View.GONE
                    rv_dashboard_items_electronics.visibility=View.GONE
                    rv_dashboard_items_apparels.visibility=View.VISIBLE
                    rv_dashboard_items_sports.visibility=View.GONE
                    rv_dashboard_items_stationery.visibility=View.GONE
                    tv_no_dashboard_item_found.visibility=View.GONE

                    rv_dashboard_items_apparels.layoutManager= GridLayoutManager(activity,2)
                    rv_dashboard_items_apparels.setHasFixedSize(true)
                    val adapter = DashboardItemsListAdapter(requireActivity(),productList)
                    rv_dashboard_items_apparels.adapter=adapter
                }
                else{
                    handleVisibilityWhenNoItemsFound()
                }

            }
            Constants.SPORTS ->{
                hideProgressDialog()
                if(productList.size>0){
                    rv_dashboard_items.visibility=View.GONE
                    rv_dashboard_items_electronics.visibility=View.GONE
                    rv_dashboard_items_apparels.visibility=View.GONE
                    rv_dashboard_items_sports.visibility=View.VISIBLE
                    rv_dashboard_items_stationery.visibility=View.GONE
                    tv_no_dashboard_item_found.visibility=View.GONE

                    rv_dashboard_items_sports.layoutManager= GridLayoutManager(activity,2)
                    rv_dashboard_items_sports.setHasFixedSize(true)
                    val adapter = DashboardItemsListAdapter(requireActivity(),productList)
                    rv_dashboard_items_sports.adapter=adapter
                }
                else{
                    handleVisibilityWhenNoItemsFound()
                }

            }
            Constants.STATIONERY ->{
                hideProgressDialog()
                if(productList.size>0){
                    rv_dashboard_items.visibility=View.GONE
                    rv_dashboard_items_electronics.visibility=View.GONE
                    rv_dashboard_items_apparels.visibility=View.GONE
                    rv_dashboard_items_sports.visibility=View.GONE
                    rv_dashboard_items_stationery.visibility=View.VISIBLE
                    tv_no_dashboard_item_found.visibility=View.GONE

                    rv_dashboard_items_stationery.layoutManager= GridLayoutManager(activity,2)
                    rv_dashboard_items_stationery.setHasFixedSize(true)
                    val adapter = DashboardItemsListAdapter(requireActivity(),productList)
                    rv_dashboard_items_stationery.adapter=adapter
                }
                else{
                    handleVisibilityWhenNoItemsFound()
                }

            }
        }
    }

    private fun handleVisibilityWhenNoItemsFound(){
        rv_dashboard_items.visibility=View.GONE
        rv_dashboard_items_electronics.visibility=View.GONE
        rv_dashboard_items_apparels.visibility=View.GONE
        rv_dashboard_items_sports.visibility=View.GONE
        rv_dashboard_items_stationery.visibility=View.GONE
        tv_no_dashboard_item_found.visibility=View.VISIBLE
    }

    @SuppressLint("SetTextI18n")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_dashboard, container, false)

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.dashboard_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_settings ->{
                startActivity(Intent(activity, SettingsActivity::class.java))
                return true
            }
            R.id.action_cart ->{
                startActivity(Intent(activity, CartListActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

}