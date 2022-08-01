package com.example.shopperista.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import com.example.shopperista.R
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.User
import com.example.shopperista.utils.Base
import com.example.shopperista.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_register.*

class LoginActivity : Base() , View.OnClickListener {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN // Disabling the status bar

        tv_forgot_password.setOnClickListener(this)
        tv_register.setOnClickListener(this)
        btn_login.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if(view!=null){
            when(view.id){

                R.id.tv_forgot_password -> {
                    val intent = Intent(this@LoginActivity, ForgotPasswordActivity::class.java)
                    startActivity(intent)
                }

                R.id.btn_login -> {
                    loginRegisteredUsers()
                }

                R.id.tv_register -> {
                    val intent = Intent(this@LoginActivity, RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun loginRegisteredUsers(){

        if(validateLoginDetails()){

            showProgressDialog()
            val email = et_email_login.text.toString().trim{it<=' '}
            val password = et_password_login.text.toString().trim{it<=' '}

            FirebaseAuth.getInstance().signInWithEmailAndPassword(email,password).addOnCompleteListener{ task ->
                if(task.isSuccessful){
                    FirestoreClass().getUserDetails(this@LoginActivity)
                }
                else{
                    hideProgressDialog()
                    showErrorSnackBar(task.exception!!.message.toString())
                }
            }
        }
    }

    private fun validateLoginDetails() : Boolean{
        return when{
            TextUtils.isEmpty(et_email_login.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email))
                false
            }
            TextUtils.isEmpty(et_password_login.text.toString().trim{it<=' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password))
                false
            }
            else ->{
                true
            }
        }
    }

    fun userLoggedInSuccess(user: User) {
        hideProgressDialog()

        if(user.profileCompleted == 0){
            val intent = Intent(this@LoginActivity, UserProfileActivity::class.java)
            intent.putExtra(Constants.EXTRA_USER_DETAILS,user)
            startActivity(intent)
        }
        else{
            startActivity(Intent(this@LoginActivity, DashboardActivity::class.java))
        }
        finish()
    }


}