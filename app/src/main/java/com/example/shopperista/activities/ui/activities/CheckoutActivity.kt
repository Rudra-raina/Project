package com.example.shopperista.activities.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.shopperista.R
import com.example.shopperista.adapters.CartItemsListAdapter
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.Address
import com.example.shopperista.models.CartItem
import com.example.shopperista.models.Order
import com.example.shopperista.utils.Base
import com.example.shopperista.utils.Constants
import kotlinx.android.synthetic.main.activity_cart_list.*
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : Base() {

    private var mAddressDetails : Address? = null
    private var mCartItemsList : ArrayList<CartItem> = ArrayList()
    private var mSubtotal : Double = 0.0
    private var mTotalAmount : Double = 0.0
    private lateinit var mOrderDetails : Order

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        if(intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails=intent.getParcelableExtra(Constants.EXTRA_SELECTED_ADDRESS)

            tv_checkout_address_type.text = mAddressDetails!!.type
            tv_checkout_full_name.text=mAddressDetails!!.name
            tv_checkout_mobile_number.text=mAddressDetails!!.mobileNumber
            tv_checkout_address.text = "${mAddressDetails!!.address} , ${mAddressDetails!!.zipCode}"
        }

        getCartItems()

        btn_place_order.setOnClickListener{
            placeAnOrder()
        }
    }


    private  fun getCartItems(){
        showProgressDialog()
        FirestoreClass().getCartList(this@CheckoutActivity)
    }

    @SuppressLint("SetTextI18n")
    fun successCartItemsList(cartList : ArrayList<CartItem>){
        hideProgressDialog()

        rv_cart_list_items.layoutManager=LinearLayoutManager(this)
        rv_cart_list_items.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this,cartList,false)
        rv_cart_list_items.adapter=cartListAdapter

        mCartItemsList=cartList

        for(item in cartList){
            val price = item.price!!.toDouble()
            val quantity = item.cart_quantity!!.toInt()
            mSubtotal += (price*quantity)
        }

        tv_checkout_sub_total.text = "₹ $mSubtotal"
        tv_checkout_shipping_charge.text = "₹ 30"

        mTotalAmount = mSubtotal + 30
        tv_checkout_total_amount.text = "₹ $mTotalAmount"
    }

    private fun placeAnOrder(){
        showProgressDialog()

        val cal = Calendar.getInstance()
        val myFormat = "dd.MMM.yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.getDefault())
        val myDate =sdf.format(cal.time).toString()

        mOrderDetails = Order(
            FirestoreClass().getCurrentUserID(),
            mCartItemsList,
            mAddressDetails,
            System.currentTimeMillis().toString(),
            myDate,
            mCartItemsList[0].image,
            mSubtotal.toString(),
            "10",
            mTotalAmount.toString()
        )

        FirestoreClass().placeOrder(this,mOrderDetails)
    }

    fun orderPlacedSuccess() {
        FirestoreClass().updateAllDetails(this,mCartItemsList,mOrderDetails)

    }

    fun addDetailsUpdatedSuccessfully() {
        hideProgressDialog()
        Toast.makeText(this, "Order Placed Successfully", Toast.LENGTH_SHORT).show()

        val intent = Intent(this,DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
        startActivity(intent)
        finish()
    }
}