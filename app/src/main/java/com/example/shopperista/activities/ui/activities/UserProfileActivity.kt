package com.example.shopperista.activities.ui.activities

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.TextUtils
import android.view.View
import com.example.shopperista.R
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.User
import com.example.shopperista.utils.Base
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.GlideLoader
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.activity_user_profile.*
import java.net.URI


class UserProfileActivity : Base() {

    private lateinit var mUserDetails : User
    private var mSelectedImageFileUri : Uri? = null
    private var mUserProfileImageURL : String = " "

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_profile)


        mUserDetails = User()
        if(intent.hasExtra(Constants.EXTRA_USER_DETAILS)){
            mUserDetails = intent.getParcelableExtra(Constants.EXTRA_USER_DETAILS)!!
        }

        if(mUserDetails.profileCompleted==0){

            tv_complete_profile.text = resources.getString(R.string.complete_your_profile_2)

            et_first_name_user_profile.isEnabled=false
            et_last_name_user_profile.isEnabled=false

        }
        else{

            setUpActionBar()
            tv_complete_profile.text = resources.getString(R.string.edit_your_profile)

            GlideLoader(this@UserProfileActivity).loadUserPicture(mUserDetails.image!!,iv_user_photo)
//            mUserProfileImageURL=mUserDetails.image!!
            val userImageUri : Uri? = Uri.parse(mUserDetails.image_uri!!)
            mSelectedImageFileUri=userImageUri

            et_phone_user_profile.setText(mUserDetails.mobile.toString())

            if(mUserDetails.gender==Constants.BINARY){
                rb_binary.isChecked=true
            }
            else{
                rb_non_binary.isChecked=true
            }
        }

        et_first_name_user_profile.setText(mUserDetails.firstName!!)
        et_last_name_user_profile.setText(mUserDetails.lastName!!)
        et_email_user_profile.setText(mUserDetails.email!!)

        et_email_user_profile.isEnabled=false



        iv_user_photo.setOnClickListener{
            requestGalleryPermission()
        }
        btn_submit.setOnClickListener{
            imageToCloud()
        }
    }

    private fun setUpActionBar() {

        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN // Disabling the status bar
        setSupportActionBar(toolbar_user_profile_Activity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=null

        toolbar_user_profile_Activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun imageToCloud(){

        if(validateUserProfileDetails()){

            showProgressDialog()

            if(mSelectedImageFileUri!=null && mSelectedImageFileUri.toString()!=mUserDetails.image_uri){ // important to show progress dialog before this step cause this take times
                FirestoreClass().uploadImageToCloudStorage(this@UserProfileActivity,mSelectedImageFileUri,Constants.USER_PROFILE_IMAGE)
            }
            else{
                userDetailsHashmap()
            }
        }
    }

    private fun userDetailsHashmap(){


        val userHashMap = HashMap<String,Any>()

        val mobileNumber = et_phone_user_profile.text.toString().trim{it<=' '}

        val gender = if(rb_binary.isChecked){
            Constants.BINARY
        }
        else{
            Constants.NON_BINARY
        }

        if(gender.isNotEmpty() && gender!=mUserDetails.gender){
            userHashMap[Constants.GENDER] = gender
        }

        if(mobileNumber.isNotEmpty() && mobileNumber!=mUserDetails.mobile.toString()){
            userHashMap[Constants.MOBILE] = mobileNumber.toLong()
        }
        if(mSelectedImageFileUri!=null && mSelectedImageFileUri.toString()!=mUserDetails.image_uri ){
            userHashMap[Constants.IMAGE] = mUserProfileImageURL
            userHashMap[Constants.IMAGE_URI]=mSelectedImageFileUri.toString()
        }

        val firstName = et_first_name_user_profile.text.toString().trim{it<' '}
        if(firstName!=mUserDetails.firstName){
            userHashMap[Constants.FIRST_NAME]=firstName
        }

        val lastName = et_last_name_user_profile.text.toString().trim{it<' '}
        if(firstName!=mUserDetails.lastName){
            userHashMap[Constants.LAST_NAME]=lastName
        }

        userHashMap[Constants.COMPLETE_PROFILE] = 1

        FirestoreClass().updateUserProfileData(this@UserProfileActivity,userHashMap)

    }

    fun userProfileUpdateSuccess(){
        hideProgressDialog()
        startActivity(Intent(this@UserProfileActivity, DashboardActivity::class.java))
    }

    fun imageUploadSuccess(imageURL : String){
        mUserProfileImageURL = imageURL
        userDetailsHashmap()
    }

    private fun requestGalleryPermission() {
        Dexter.withContext(this).withPermission(android.Manifest.permission.READ_EXTERNAL_STORAGE)
            .withListener(object : PermissionListener {
                override fun onPermissionGranted(response: PermissionGrantedResponse){
                    val galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                    startActivityForResult(galleryIntent,Constants.GALLERY)
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
                    mSelectedImageFileUri = data.data!!
                    GlideLoader(this@UserProfileActivity).loadUserPicture(mSelectedImageFileUri!!,iv_user_photo)
                }

            }
        }
    }

    private fun validateUserProfileDetails() : Boolean{
        return when{
            TextUtils.isEmpty(et_phone_user_profile.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_mobile_number))
                false
            }
            else ->{
                true
            }
        }
    }
}