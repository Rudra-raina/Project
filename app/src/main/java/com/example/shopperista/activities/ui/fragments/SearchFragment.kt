package com.example.shopperista.activities.ui.fragments

import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopperista.R
import com.example.shopperista.adapters.MySearchedListAdapter
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.Product
import com.example.shopperista.utils.BaseFragment
import kotlinx.android.synthetic.main.fragment_search.*


class SearchFragment : BaseFragment() {

    lateinit var mList:ArrayList<Product>
    var string:String=""

    override fun onResume() {
        super.onResume()
        FirestoreClass().getProduct(this@SearchFragment)
        ibSearch.setOnClickListener{
            if(validate()){
                showProgressDialog()
                val matchingList=ArrayList<Product>()
                for(i in mList){
                    val title=i.title.toString().trim{it<=' '}.lowercase()
                    val description=i.description.toString().trim(){it<=' '}.lowercase()
                    if(title.contains(string.lowercase()) || description.contains(string.lowercase())){
                        matchingList.add(i)
                    }
                }
                setUpView(matchingList)
            }
        }
    }

    private fun setUpView(list:ArrayList<Product>?){
        hideProgressDialog()
        if(list!!.size==0){
            tv_no_searched_products_found.visibility=View.VISIBLE
            rv_searched_product.visibility=View.GONE
        }else{
            tv_no_searched_products_found.visibility=View.GONE
            rv_searched_product.visibility=View.VISIBLE
            rv_searched_product.layoutManager=LinearLayoutManager(activity)
            val adapterSearchedProduct=MySearchedListAdapter(requireActivity(),list,this@SearchFragment)
            rv_searched_product.adapter=adapterSearchedProduct
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_search, container, false)
    }

    fun gotProduct(list:ArrayList<Product>){
        mList=list
        for(i in list){
            Log.e("Here",i.title!!)
        }
    }

    private fun validate(): Boolean {
        return when {
            TextUtils.isEmpty(etSearch.text.toString().trim{it<=' '}) ->{
                Toast.makeText(activity, "Can not be empty", Toast.LENGTH_SHORT).show()
                false
            }
            else -> {
                string=etSearch.text.toString().trim{it<=' '}
                true
            }
        }
    }


}