package com.example.shopperista.activities.ui.activities

import android.os.Bundle
import android.view.View
import com.example.shopperista.R
import com.example.shopperista.utils.Base
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_forgot_password.*

class ForgotPasswordActivity : Base() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN // Disabling the status bar

        btn_submit_email.setOnClickListener{
            resetPasswordEmail()
        }

    }

    private fun resetPasswordEmail() {
        val email = et_email_forgot.text.toString().trim{it<=' '}
        if(email.isEmpty()){
            showErrorSnackBar(resources.getString(R.string.err_msg_enter_email))
        }
        else{
            showProgressDialog()
            FirebaseAuth.getInstance().sendPasswordResetEmail(email).addOnCompleteListener{task ->
                hideProgressDialog()
                if(task.isSuccessful){
                    showErrorSnackBar("Email sent successfully")
                }
                else{
                    showErrorSnackBar(task.exception!!.message.toString())
                }
            }
        }
    }
}