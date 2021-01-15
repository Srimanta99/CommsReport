package com.commsreport.screens.login

import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ActivityLoginBinding
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent


class LoginActivity : AppCompatActivity() {
      var activityLoginBinding:ActivityLoginBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityLoginBinding= ActivityLoginBinding.inflate(LayoutInflater.from(this))
        setContentView(activityLoginBinding!!.root)
        var loginOnclick=LoginOnclick(this, activityLoginBinding!!)
        setTypeface()
        setSaveUserCredential()

    }

    private fun setSaveUserCredential() {
        if (AppSheardPreference(this).getvalue_in_preference(PreferenceConstent.isremember).equals("1")){
            activityLoginBinding!!.etEmail.setText(AppSheardPreference(this).getvalue_in_preference(PreferenceConstent.useremail_key))
            activityLoginBinding!!.etPass.setText(AppSheardPreference(this).getvalue_in_preference(PreferenceConstent.password_key))
            activityLoginBinding!!.chkRemember.isChecked=true
        }
    }

    private fun setTypeface() {
        activityLoginBinding!!.tvLoginhader.typeface=CustomTypeface.getRajdhaniBold(this);
        activityLoginBinding!!.tvemail.typeface=CustomTypeface.getRajdhaniMedium(this)
        activityLoginBinding!!.etEmail.typeface=CustomTypeface.getRajdhaniMedium(this)
        activityLoginBinding!!.tvpass.typeface=CustomTypeface.getRajdhaniMedium(this)
        activityLoginBinding!!.etPass.typeface=CustomTypeface.getRajdhaniMedium(this)
        activityLoginBinding!!.chkRemember.typeface=CustomTypeface.getRajdhaniMedium(this)
        activityLoginBinding!!.tvForgotpassword.typeface=CustomTypeface.getRajdhaniSemiBold(this)
        activityLoginBinding!!.btnLogin.typeface=CustomTypeface.getRajdhaniSemiBold(this)

    }

}