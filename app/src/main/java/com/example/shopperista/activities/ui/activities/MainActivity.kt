package com.example.shopperista.activities.ui.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.shopperista.R
import com.example.shopperista.utils.Constants
import kotlinx.android.synthetic.main.activity_main.*

//C:\Personal\Android_Development\Downloaded\Firebase
//We create a specific icon for this app using File -> New -> Image asset . We can edit the background & foreground
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}