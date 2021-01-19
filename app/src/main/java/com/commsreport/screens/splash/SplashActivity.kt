package com.commsreport.screens.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.WindowManager
import com.commsreport.R
import com.commsreport.Utils.ApplicationConstant
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ActivitySplashBinding
import com.commsreport.screens.login.LoginActivity

class SplashActivity : AppCompatActivity() {
    var activitySplashBinding:ActivitySplashBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activitySplashBinding= ActivitySplashBinding.inflate(LayoutInflater.from(this))
        setContentView(activitySplashBinding!!.root)
        activitySplashBinding!!.tvLoading.setTypeface(CustomTypeface.getrobotoLight(this))

        Handler().postDelayed(Runnable {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        },ApplicationConstant.SPLASHTIME)
    }
}