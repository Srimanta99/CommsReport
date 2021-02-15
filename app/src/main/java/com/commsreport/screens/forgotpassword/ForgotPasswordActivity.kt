package com.commsreport.screens.forgotpassword

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.ToastAlert
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
         ToastAlert.CustomToastwornning(this,"Under development")
    }

    private fun setTypeface() {
        activityForgotPasswordBinding!!.tvForgotpasswordheaderr.setTypeface(CustomTypeface.getRajdhaniBold(this))
        activityForgotPasswordBinding!!.tvForgotpassword.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityForgotPasswordBinding!!.btnLogin.setTypeface(CustomTypeface.getRajdhaniBold(this))
        activityForgotPasswordBinding!!.etemail.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        val text="<font color=#FE0100>Note: </font> <font color=#1E3F6C>Please enter the email address that you want to notify you once you submit it</font>";
        activityForgotPasswordBinding!!.tvForgotpassword.setText(Html.fromHtml(text));
    }
}