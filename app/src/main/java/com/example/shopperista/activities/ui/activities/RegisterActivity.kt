package com.example.shopperista.activities.ui.activities

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import com.example.shopperista.R
import com.example.shopperista.firestore.FirestoreClass
import com.example.shopperista.models.User
import com.example.shopperista.utils.Base
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_forgot_password.*
import kotlinx.android.synthetic.main.activity_register.*
import kotlinx.android.synthetic.main.dialog_progress.*


class RegisterActivity : Base(){


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)


        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN // Disabling the status bar

        setSupportActionBar(toolbar_register_activity)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        supportActionBar!!.title=null

        toolbar_register_activity.setNavigationOnClickListener {
            onBackPressed()
        }

        tv_login.setOnClickListener {
            val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
            startActivity(intent)
        }

        btn_register.setOnClickListener{
            registerUser()
        }

    }

    private fun registerUser(){
        if(validateRegisterDetails()){

            showProgressDialog()

            val email : String = et_email.text.toString().trim{it<=' '}
            val password : String = et_password.text.toString().trim{it<=' '}

            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password).addOnCompleteListener { task ->

                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!

                    val user = User(
                        firebaseUser.uid,
                        et_first_name.text.toString().trim{it<=' '},
                        et_last_name.text.toString().trim{it<=' '},
                        et_email.text.toString().trim{it<= ' '}
                    )

                    FirestoreClass().registerUser(this@RegisterActivity,user)


//                    FirebaseAuth.getInstance().signOut()
                }
                else {
                    hideProgressDialog()
                    showErrorSnackBar(task.exception!!.message.toString())
                }

            }
        }
    }


    private fun validateRegisterDetails(): Boolean {
        return when {
            TextUtils.isEmpty(et_first_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name))
                false
            }

            TextUtils.isEmpty(et_last_name.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name))
                false
            }

            TextUtils.isEmpty(et_email.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email))
                false
            }

            TextUtils.isEmpty(et_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password))
                false
            }

            TextUtils.isEmpty(et_confirm_password.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password))
                false
            }

            et_password.text.toString().trim { it <= ' ' } != et_confirm_password.text.toString()
                .trim { it <= ' ' } -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch))
                false
            }
            !cb_terms_and_condition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition))
                false
            }
            else -> {
                true
            }
        }
    }

    fun userRegistrationSuccess(){
        val intent = Intent(this@RegisterActivity, LoginActivity::class.java)
        startActivity(intent)
        hideProgressDialog()
        Toast.makeText(this, "Registered Successfully", Toast.LENGTH_SHORT).show()
    }

}