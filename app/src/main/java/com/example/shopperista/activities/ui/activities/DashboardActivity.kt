package com.example.shopperista.activities.ui.activities

import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.shopperista.R
import com.example.shopperista.utils.Base
import com.example.shopperista.utils.BaseFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class DashboardActivity : Base() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        window.statusBarColor = ContextCompat.getColor(this, R.color.purple_500)

        supportActionBar!!.setBackgroundDrawable(ContextCompat.getDrawable(this@DashboardActivity,R.drawable.app_bg_color))
        val navController = findNavController(R.id.nav_host_fragment)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_products, R.id.navigation_dashboard, R.id.navigation_orders, R.id.navigation_sold_product
                // Change this to our ID's
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onBackPressed() {
        doubleBackToExit()
    }
}
