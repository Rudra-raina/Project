package com.example.shopperista.utils

import android.app.Dialog
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.shopperista.R
import com.google.android.material.snackbar.Snackbar

open class Base : AppCompatActivity(){

    private lateinit var mProgressDialog: Dialog
    private var doubleBackToExitPressedOnce = false

    fun showErrorSnackBar(message:String){
        val snackBar : Snackbar = Snackbar.make(findViewById(android.R.id.content),message, Snackbar.LENGTH_SHORT)
        val snackBarView = snackBar.view

        snackBarView.setBackgroundColor(ContextCompat.getColor(this@Base, R.color.colorSnackBarError))
        snackBar.show()
    }

    fun showProgressDialog(){
        mProgressDialog = Dialog(this,R.style.MyAlertDialogStyle)
        mProgressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

    fun doubleBackToExit(){

        if(doubleBackToExitPressedOnce){
            super.onBackPressed()
            return
        }

        doubleBackToExitPressedOnce= true
        Toast.makeText(this,"Please click back again to exit",Toast.LENGTH_SHORT).show()

        android.os.Handler(Looper.getMainLooper()).postDelayed(
            {
                doubleBackToExitPressedOnce=false
            },
            2000
        )


    }
}