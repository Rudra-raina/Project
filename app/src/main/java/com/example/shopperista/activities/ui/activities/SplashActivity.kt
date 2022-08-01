package com.example.shopperista.activities.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.widget.ProgressBar
import com.example.shopperista.R
import com.github.ybq.android.spinkit.sprite.Sprite
import com.github.ybq.android.spinkit.style.Wave

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.decorView.systemUiVisibility= View.SYSTEM_UI_FLAG_FULLSCREEN // Disabling the status bar

//      Post delayed is a function through which we can add an operation that we want to operate after a given time frame into a
//      message queue. In it's constructor , we need to define the operation we want to execute & the time frame in milli second
//      It Returns true if the operation was successfully placed into the  queue.

//      Handler enqueues task in the MessageQueue using Looper and also executes them when the task comes out of the MessageQueue.
//      Looper is a worker that keeps a thread alive, loops through MessageQueue and sends messages to the corresponding handler
//      to process.


        Handler(Looper.getMainLooper()).postDelayed(
            {
                startActivity(Intent(this@SplashActivity, LoginActivity::class.java)) // Takes us to the next activity
                finish() // Closes this activity
            },
            1500 // Amount of time we want to splash screen to be visible before going to main screen ( In millisecond )
        )




    }
}