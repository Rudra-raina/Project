package com.example.shopperista.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.example.shopperista.R
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.CartItem
import com.example.shopperista.models.Product
import com.example.shopperista.utils.Base
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class ProductDetailsActivity : Base(){

    private var mProductID : String = " "
    private var mProductOwnerID : String =" "
    private lateinit var mProductDetails : Product

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_details)


        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        if(intent. hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductID = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }

        if(intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            mProductOwnerID=intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        getProductDetails()

        btn_add_to_cart.setOnClickListener{
            addToCart()
        }

        btn_go_to_cart.setOnClickListener{
            startActivity(Intent(this@ProductDetailsActivity, CartListActivity::class.java))
        }
    }

    private fun getProductDetails(){
        showProgressDialog()
        FirestoreClass().getProductDetails(this,mProductID)
    }

    fun productDetailsSuccess(product: Product) {
        mProductDetails = product

        GlideLoader(this).loadProductPicture(product.image!!,iv_product_photo)
        title_details.text=product.title
        description_details.text=product.description
        price_details.text="Rs. ${product.price}"
        quantity_details.text="In Stock : ${product.stock_quantity} "

        if(product.stock_quantity!!.toInt()==0){
            hideProgressDialog()
            quantity_details.text=resources.getString(R.string.out_of_stock)
            quantity_details.setTextColor(ContextCompat.getColor(this,R.color.red))
        }
        else{
            if(FirestoreClass().getCurrentUserID()==mProductOwnerID){
                hideProgressDialog()
            }
            else{
                FirestoreClass().checkIfItemExistsInCart(this,mProductID)
            }
        }

    }

    fun productExistsInCart(){
        hideProgressDialog()
        btn_add_to_cart.visibility=View.GONE
        btn_go_to_cart.visibility=View.VISIBLE
    }

    fun productNotInCart() {
        hideProgressDialog()
        btn_add_to_cart.visibility=View.VISIBLE
        btn_go_to_cart.visibility=View.GONE
    }

    private fun addToCart(){
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductOwnerID,
            mProductID,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY,
            mProductDetails.stock_quantity
        )

        showProgressDialog()
        FirestoreClass().addCartItems(this,cartItem)
    }

    fun addToCartSuccess() {
        hideProgressDialog()
        Toast.makeText(this,"add",Toast.LENGTH_SHORT).show()
        btn_add_to_cart.visibility=View.GONE
        btn_go_to_cart.visibility=View.VISIBLE
    }


}