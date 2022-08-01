package com.example.shopperista.activities.ui.activities

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.example.shopperista.R
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.User
import com.example.shopperista.utils.Base
import com.example.shopperista.utils.Constants
import com.example.shopperista.utils.GlideLoader
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_settings.*

class SettingsActivity : Base() {

    private lateinit var mUserDetails : User
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)


        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)
        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.app_bg_color))

        getUserDetails()

        btn_logout.setOnClickListener{
            logout()
        }

        tv_edit.setOnClickListener{
            edit()
        }

        ll_address.setOnClickListener{
            addressList()
        }

    }

    private fun addressList() {
        val intent = Intent(this,AddressListActivity::class.java)
        startActivity(intent)
    }

    private fun edit() {
        val intent = Intent(this@SettingsActivity,UserProfileActivity::class.java)
        intent.putExtra(Constants.EXTRA_USER_DETAILS,mUserDetails)
        startActivity(intent)
    }

    private fun logout() {
        FirebaseAuth.getInstance().signOut()
        val intent = Intent(this@SettingsActivity,LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun getUserDetails(){

        showProgressDialog()
        FirestoreClass().getUserDetails(this@SettingsActivity)
    }

    @SuppressLint("SetTextI18n")
    fun userDetailsSuccess(user: User){

        mUserDetails=user


        GlideLoader(this@SettingsActivity).loadUserPicture(user.image!!,iv_settings_user_photo)
        tv_welcome_name.text = user.firstName!! + " !"
        tv_name.text= user.firstName + " " + user.lastName!!
        tv_gender.text = user.gender!!
        tv_email_settings.text = user.email!!
        tv_mobile_number.text = user.mobile.toString()
        hideProgressDialog()
    }

}