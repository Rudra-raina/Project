package com.example.shopperista.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.shopperista.R
import com.example.shopperista.adapters.AddressListAdapter
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.Address
import com.example.shopperista.utils.Base
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.SwipeToDeleteCallback
import com.example.shopperista.utils.SwipeToEditCallback
import kotlinx.android.synthetic.main.activity_address_list.*

class AddressListActivity : Base() {

    private var mSelectAddress : Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_address_list)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)


        if(intent.hasExtra(Constants.EXTRA_SELECT_ADDRESS)){
            mSelectAddress = intent.getBooleanExtra(Constants.EXTRA_SELECT_ADDRESS,false)
            tv_add_address.visibility=View.GONE
            tv_select_address.visibility = View.VISIBLE
        }

        tv_add_address.setOnClickListener {
            val intent = Intent(this, AddEditAddressActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getListOfAddress()
    }

    private fun getListOfAddress() {
        showProgressDialog()
        FirestoreClass().getAddressesList(this)
    }

    fun successAddressListFromFireStore(addressList: ArrayList<Address>) {
        hideProgressDialog()

        if (addressList.size > 0) {
            rv_address_list.visibility = View.VISIBLE
            tv_no_address_found.visibility = View.GONE

            rv_address_list.layoutManager = LinearLayoutManager(this@AddressListActivity)
            rv_address_list.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this, addressList,mSelectAddress)
            rv_address_list.adapter = addressAdapter

            if(!mSelectAddress){
                val editSwipeHandler = object : SwipeToEditCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = rv_address_list.adapter as AddressListAdapter
                        adapter.notifyEditItem(this@AddressListActivity, viewHolder.adapterPosition)
                    }
                }

                val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
                editItemTouchHelper.attachToRecyclerView(rv_address_list)

                val deleteSwipeHandler = object : SwipeToDeleteCallback(this) {
                    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                        val adapter = rv_address_list.adapter as AddressListAdapter
                        adapter.notifyDeleteItem(this@AddressListActivity, viewHolder.adapterPosition)
                    }
                }

                val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
                deleteItemTouchHelper.attachToRecyclerView(rv_address_list)
            }

        } else {
            rv_address_list.visibility = View.GONE
            tv_no_address_found.visibility = View.VISIBLE
        }
    }

    fun deleteSuccess() {
        hideProgressDialog()
        Toast.makeText(this@AddressListActivity, resources.getString(R.string.err_your_address_deleted_successfully), Toast.LENGTH_SHORT).show()
        getListOfAddress()
    }
}
