package com.example.shopperista.activities.ui.activities

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.shopperista.R
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.Address
import com.example.shopperista.utils.Base
import com.example.shopperista.utils.Constants
import kotlinx.android.synthetic.main.activity_add_edit_address.*

class AddEditAddressActivity : Base() {

    private var mAddressDetails : Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_edit_address)

        if(intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            mAddressDetails=intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)

            tv_title.text = resources.getString(R.string.title_edit_address)
            btn_add_address.text=resources.getString(R.string.update)

            et_full_name.setText(mAddressDetails!!.name)
            et_phone_number.setText(mAddressDetails!!.mobileNumber)
            et_address.setText(mAddressDetails!!.address)
            et_zip_code.setText(mAddressDetails!!.zipCode)

            when(mAddressDetails!!.type){
                Constants.HOME -> {
                    rb_home.isChecked=true
                }
                Constants.OFFICE -> {
                    rb_office.isChecked=true
                }
                else->{
                    rb_other.isChecked=true
                }
            }
        }

        setUpActionBar()

        btn_add_address.setOnClickListener{
            saveAddressToFirestore()
        }

    }

    private fun setUpActionBar(){
        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN // Disabling the status bar

        setSupportActionBar(toolbar_add_edit_address_activity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=null

        toolbar_add_edit_address_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    private fun saveAddressToFirestore(){

        val fullName: String = et_full_name.text.toString().trim { it <= ' ' }
        val phoneNumber: String = et_phone_number.text.toString().trim { it <= ' ' }
        val address: String = et_address.text.toString().trim { it <= ' ' }
        val zipCode: String = et_zip_code.text.toString().trim { it <= ' ' }

        if(validateData()){
            showProgressDialog()

            val addressType : String = when {
                rb_home.isChecked -> {
                    Constants.HOME
                }
                rb_office.isChecked -> {
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }

            val addressModel = Address(
                FirestoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                addressType
            )

            if(mAddressDetails!=null && mAddressDetails!!.id!!.isNotEmpty()){
                FirestoreClass().updateAddress(this,addressModel, mAddressDetails!!.id!!)
            }
            else{
                FirestoreClass().addAddress(this,addressModel)
            }
        }

    }

    private fun validateData() : Boolean{
        return when {

            TextUtils.isEmpty(et_full_name.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar("Please enter full name")
                false
            }

            TextUtils.isEmpty(et_phone_number.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar("Please enter phone number")
                false
            }

            TextUtils.isEmpty(et_address.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar("Please enter address")
                false
            }

            TextUtils.isEmpty(et_zip_code.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar("Please enter zip code")
                false
            }

            else -> {
                return true
            }

        }
    }

    fun addUpdateAddressSuccess() {
        hideProgressDialog()
        val notifySuccessMessage : String = if (mAddressDetails!=null && mAddressDetails!!.id!!.isNotEmpty()){
            "Your address updated successfully"
        }
        else{
            "Your address added successfully"
        }
        Toast.makeText(this,notifySuccessMessage,Toast.LENGTH_SHORT).show()
        setResult(RESULT_OK)
        finish()
    }
}