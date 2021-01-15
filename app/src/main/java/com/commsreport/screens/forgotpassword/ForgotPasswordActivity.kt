package com.commsreport.screens.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ActivityForgotPasswordBinding

class ForgotPasswordActivity : AppCompatActivity() {
    var activityForgotPasswordBinding:ActivityForgotPasswordBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityForgotPasswordBinding=ActivityForgotPasswordBinding.inflate(LayoutInflater.from(this))
        setContentView(activityForgotPasswordBinding!!.root)
        setTypeface()
        activityForgotPasswordBinding!!.btnLogin.setOnClickListener {
            if (!activityForgotPasswordBinding!!.etemail.text.toString().equals(""))
            callApiForForgotPassword()
            else
                activityForgotPasswordBinding!!.etemail.requestFocus()
        }
    }

    private fun callApiForForgotPassword() {

    }

    private fun setTypeface() {
        activityForgotPasswordBinding!!.tvForgotpasswordheaderr.setTypeface(CustomTypeface.getRajdhaniBold(this))
        activityForgotPasswordBinding!!.tvForgotpassword.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityForgotPasswordBinding!!.btnLogin.setTypeface(CustomTypeface.getRajdhaniBold(this))
        activityForgotPasswordBinding!!.etemail.setTypeface(CustomTypeface.getRajdhaniMedium(this))
    }
}