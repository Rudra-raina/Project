package com.example.shopperista.activities.ui.activities

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.example.shopperista.R
import com.example.shopperista.models.SoldProduct
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.GlideLoader
import kotlinx.android.synthetic.main.activity_sold_product_details.*
import java.text.SimpleDateFormat
import java.util.*

class SoldProductDetailsActivity : AppCompatActivity() {

    private lateinit var mProductDetails : SoldProduct

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_product_details)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        if(intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)){
            mProductDetails=intent.getParcelableExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }
        
        setUpUI(mProductDetails)
    }

    @SuppressLint("SetTextI18n")
    private fun setUpUI(productDetails: SoldProduct) {

        val substring = productDetails.order_title.substring(0,9)
        tv_sold_product_details_id.text = substring

        tv_sold_product_details_date.text =productDetails.order_date

        GlideLoader(this@SoldProductDetailsActivity).loadProductPicture(productDetails.image, iv_product_item_image)

        tv_product_item_name.text = productDetails.title
        tv_product_item_price.text ="₹ ${productDetails.price}"
        tv_sold_product_quantity.text = productDetails.sold_quantity

        tv_sold_details_address_type.text = productDetails.address.type
        tv_sold_details_full_name.text = productDetails.address.name
        tv_sold_details_address.text = "${productDetails.address.address}, ${productDetails.address.zipCode}"

        tv_sold_product_sub_total.text = productDetails.sub_total_amount
        tv_sold_product_shipping_charge.text = productDetails.shipping_charge
        tv_sold_product_total_amount.text = productDetails.total_amount

    }
}