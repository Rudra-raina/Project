package com.example.shopperista.activities.ui.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import androidx.core.content.ContextCompat
import com.example.shopperista.R
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.Product
import com.example.shopperista.utils.Base
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.GlideLoader
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_add_product.*
import kotlinx.android.synthetic.main.activity_product_details.*
import kotlinx.android.synthetic.main.activity_user_profile.*

class AddProductActivity : Base() {

    private lateinit var mProductDetails : Product

    private var mSelectedImageFileUri : Uri? = null
    private var mProductImageURL : String = ""

//    private var mUpdateProductURI : Uri? = null
//    private var mUpdateProductURL : String =" "

    private var mProductID : String =""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_product)

        btn_update.visibility=View.GONE
        mProductDetails= Product()
        if(intent.hasExtra(Constants.EDIT_PRODUCT)){
            mProductDetails=intent.getParcelableExtra(Constants.EDIT_PRODUCT)!!

            mProductID=mProductDetails.product_id!! // product id

            val updateProductURI : Uri = Uri.parse(mProductDetails.image_uri)
            mSelectedImageFileUri=updateProductURI
//            mProductImageURL=mProductDetails.image!!

            iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(
                this@AddProductActivity,R.drawable.ic_vector_edit))

            GlideLoader(this@AddProductActivity).loadUserPicture(mProductDetails.image!!,iv_product_image)


            et_product_title.setText(mProductDetails.title)
            et_product_price.setText(mProductDetails.price)
            et_product_description.setText(mProductDetails.description)
            et_product_quantity.setText(mProductDetails.stock_quantity)

            got_an_amazing_product.text="UPDATE YOUR PRODUCT"

            if(mProductDetails.category==Constants.ELECTRONICS){
                rb_electronics_add_product.isChecked=true
            }
            else if(mProductDetails.category==Constants.SPORTS){
                rb_sports_add_product.isChecked=true
            }
            else if(mProductDetails.category==Constants.APPARELS){
                rb_apparels_add_product.isChecked=true
            }
            else{
                rb_stationery_add_product.isChecked=true
            }

            btn_update.visibility=View.VISIBLE
            btn_add.visibility=View.GONE

        }

        setUpActionBar()

        iv_add_update_product.setOnClickListener{
            requestGalleryPermission()
        }

        btn_add.setOnClickListener {
            addProduct()
        }

        btn_update.setOnClickListener{
            addUpdatedProduct()
        }

    }

    private fun uploadProductDetails(){

        val username = this.getSharedPreferences(Constants.SHOP_PREFERENCES,Context.MODE_PRIVATE)
            .getString(Constants.LOGGED_IN_USERNAME,"")

        val item_category = if(rb_electronics_add_product.isChecked){
            Constants.ELECTRONICS
        }
        else if(rb_apparels_add_product.isChecked){
            Constants.APPARELS
        }
        else if(rb_sports_add_product.isChecked){
            Constants.SPORTS
        }
        else{
            Constants.STATIONERY
        }

        val product = Product(
            FirestoreClass().getCurrentUserID(),
            username,
            et_product_title.text.toString().trim{it<=' '},
            et_product_price.text.toString().trim{it<=' '},
            et_product_description.text.toString().trim{it<=' '},
            et_product_quantity.text.toString().trim{it<=' '},
            mProductImageURL,
            mSelectedImageFileUri.toString(),
            item_category
        )

        FirestoreClass().uploadProductDetails(this@AddProductActivity,product)

    }

    private fun addProduct() {
        if(validateProductDetails()){
            uploadProductImage()
        }
    }

    private fun addUpdatedProduct(){
        if(validateProductDetails()){
            if(mSelectedImageFileUri.toString()!=mProductDetails.image_uri){
                uploadUpdatedProductImage()
            }
            else{
                updateProduct()
            }

        }
    }
     private fun uploadUpdatedProductImage(){
         showProgressDialog()
         FirestoreClass().uploadImageToCloudStorageUpdated(this@AddProductActivity,mSelectedImageFileUri,Constants.PRODUCT_IMAGE)
     }

    private fun uploadProductImage(){
        showProgressDialog()
        FirestoreClass().uploadImageToCloudStorage(this@AddProductActivity,mSelectedImageFileUri,Constants.PRODUCT_IMAGE)
    }

    fun imageUploadSuccessUpdated(imageURL : String){
        mProductImageURL = imageURL
        updateProduct()
    }

    private fun setUpActionBar() {
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN // Disabling the status bar

        setSupportActionBar(toolbar_add_product_activity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=null

        toolbar_add_product_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun requestGalleryPermission() {
        Dexter.withContext(this@AddProductActivity).withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse){
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent, Constants.GALLERY)
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    showErrorSnackBar("Permissions have been denied . You can allow them via settings")
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken?){
                    token?.continuePermissionRequest()
                }
            }).check()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(requestCode==Constants.GALLERY){
            if(resultCode== Activity.RESULT_OK){
                if(data!=null){

                    iv_add_update_product.setImageDrawable(ContextCompat.getDrawable(
                        this@AddProductActivity,R.drawable.ic_vector_edit))

                    mSelectedImageFileUri = data.data!!
                    GlideLoader(this@AddProductActivity).loadUserPicture(mSelectedImageFileUri!!,iv_product_image)
                }

            }
        }
    }

    private fun validateProductDetails():Boolean{
        return when{

            mSelectedImageFileUri==null -> {
                showErrorSnackBar("Please select an image")
                false
            }

            TextUtils.isEmpty(et_product_title.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar("Please enter product title")
                false
            }

            TextUtils.isEmpty(et_product_price.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar("Please enter product price")
                false
            }

            TextUtils.isEmpty(et_product_description.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar("Please enter product description")
                false
            }

            TextUtils.isEmpty(et_product_quantity.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar("Please enter product quantity")
                false
            }
            else -> {
                true
            }
        }
    }

    fun imageUploadSuccess(imageURL : String){
        mProductImageURL = imageURL
        uploadProductDetails()
    }

    fun productUploadSuccess(){
        showErrorSnackBar("Product uploaded")
        finish()
    }

    private fun updateProduct(){

        if(validateProductDetails()){
            showProgressDialog()
            val productHashmap = HashMap<String,Any>()

            if(mSelectedImageFileUri.toString()!=mProductDetails.image_uri){
                productHashmap[Constants.IMAGE]=mProductImageURL
                productHashmap[Constants.IMAGE_URI]=mSelectedImageFileUri.toString()
            }

//            if(mProductImageURL!=mProductDetails.image!!){
//                productHashmap[Constants.IMAGE]=mProductImageURL
//                productHashmap[Constants.IMAGE_URI]=mSelectedImageFileUri.toString()
//            }

            productHashmap[Constants.PRICE]=et_product_price.text.toString().trim{it<=' '}
            productHashmap[Constants.TITLE]=et_product_title.text.toString().trim{it<=' '}
            productHashmap[Constants.DESCRIPTION]=et_product_description.text.toString().trim{it<=' '}
            productHashmap[Constants.STOCK_QUANTITY]=et_product_quantity.text.toString().trim{it<=' '}
            productHashmap[Constants.PRODUCT_ID]=mProductID

            productHashmap[Constants.CATEGORY] = if(rb_electronics_add_product.isChecked){
                Constants.ELECTRONICS
            }
            else if(rb_apparels_add_product.isChecked){
                Constants.APPARELS
            }
            else if(rb_sports_add_product.isChecked){
                Constants.SPORTS
            }
            else{
                Constants.STATIONERY
            }

            FirestoreClass().updateProduct(this,productHashmap)
        }

    }

    fun productUpdated() {
        hideProgressDialog()
        startActivity(Intent(this, DashboardActivity::class.java))
    }

    //    private fun validateUpdatedProductDetails():Boolean{
//        return when{
//
//            mUpdateProductURI==null -> {
//                showErrorSnackBar("Please select an image")
//                false
//            }
//
//            TextUtils.isEmpty(et_product_title.text.toString().trim{it<=' '}) -> {
//                showErrorSnackBar("Please enter product title")
//                false
//            }
//
//            TextUtils.isEmpty(et_product_price.text.toString().trim{it<=' '}) -> {
//                showErrorSnackBar("Please enter product price")
//                false
//            }
//
//            TextUtils.isEmpty(et_product_description.text.toString().trim{it<=' '}) -> {
//                showErrorSnackBar("Please enter product description")
//                false
//            }
//
//            TextUtils.isEmpty(et_product_quantity.text.toString().trim{it<=' '}) -> {
//                showErrorSnackBar("Please enter product quantity")
//                false
//            }
//            else -> {
//                true
//            }
//        }
//    }
}

// URI will tell us if new image or old image....url will come after that thing