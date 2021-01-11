package com.commsreport.screens.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.commsreport.R
import com.commsreport.Utils.ApplicationConstant
import com.commsreport.screens.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed(Runnable {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        },ApplicationConstant.SPLASHTIME)
    }
}