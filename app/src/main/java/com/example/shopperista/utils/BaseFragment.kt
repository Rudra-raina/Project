package com.example.shopperista.utils

import android.app.Dialog
import androidx.fragment.app.Fragment
import com.example.shopperista.R


open class BaseFragment : Fragment() {

    private lateinit var mProgressDialog : Dialog

    fun showProgressDialog(){
        mProgressDialog = Dialog(requireActivity(),R.style.MyAlertDialogStyle)
        mProgressDialog.window!!.setBackgroundDrawableResource(android.R.color.transparent)
        mProgressDialog.setContentView(R.layout.dialog_progress)
        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)
        mProgressDialog.show()
    }

    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }

}