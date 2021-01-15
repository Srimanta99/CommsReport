package com.commsreport.screens.login

import android.content.Intent
import android.text.method.PasswordTransformationMethod
import android.text.method.SingleLineTransformationMethod
import android.view.View
import com.commsreport.R
import com.commsreport.databinding.ActivityLoginBinding
import com.commsreport.screens.forgotpassword.ForgotPasswordActivity
import com.commsreport.screens.home.HomeActivity
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent

class LoginOnclick(val activity: LoginActivity,val activityLoginBinding: ActivityLoginBinding):
    View.OnClickListener {
    init {
        activityLoginBinding.btnLogin.setOnClickListener(this)
        activityLoginBinding.tvForgotpassword.setOnClickListener(this)
        activityLoginBinding.showPassBtn.setOnClickListener(this)
        activityLoginBinding.chkRemember.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                AppSheardPreference(activity).setvalue_in_preference(PreferenceConstent.isremember,"1")
                AppSheardPreference(activity).setvalue_in_preference(PreferenceConstent.useremail_key,activityLoginBinding.etEmail.text.toString())
                AppSheardPreference(activity).setvalue_in_preference(PreferenceConstent.password_key,activityLoginBinding.etPass.text.toString())

            }else{
                AppSheardPreference(activity).setvalue_in_preference(PreferenceConstent.isremember,"0")
                AppSheardPreference(activity).setvalue_in_preference(PreferenceConstent.useremail_key,"")
                AppSheardPreference(activity).setvalue_in_preference(PreferenceConstent.password_key,"")

            }
        }
    }
    override fun onClick(p0: View?) {
      when(p0!!.id){
          R.id.btn_login->{
              if (loginvalidation())
                  callApiforLogin()
          }
          R.id.tv_forgotpassword->{
              activity.startActivity(Intent(activity,ForgotPasswordActivity::class.java))
          }
          R.id.show_pass_btn->{
              if (activityLoginBinding.etPass.getTransformationMethod().javaClass.getSimpleName().equals("PasswordTransformationMethod")) {
                  activityLoginBinding.showPassBtn!!.setImageResource(R.drawable.hide1);
                  // loginViewBind.et_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.password_show_hide, 0, 0, 0);
                  activityLoginBinding.etPass.setTransformationMethod(SingleLineTransformationMethod())
              } else {
                  activityLoginBinding.showPassBtn!!.setImageResource(R.drawable.view);


                  // loginViewBind.et_pass.setCompoundDrawablesWithIntrinsicBounds(R.drawable.hide, 0, 0, 0);
                  activityLoginBinding.etPass.setTransformationMethod(PasswordTransformationMethod())
              }
              activityLoginBinding.etPass.setSelection( activityLoginBinding.etPass.text.length)
          }
      }
    }

    private fun callApiforLogin() {
        activity.startActivity(Intent(activity,HomeActivity::class.java))
    }

    private fun loginvalidation(): Boolean {
        if (activityLoginBinding.etEmail.text.toString().equals("")){
            activityLoginBinding.etEmail.requestFocus()
            return false
        }
        if (activityLoginBinding.etPass.text.toString().equals("")){
            activityLoginBinding.etPass.requestFocus()
            return false
        }
        return  true
    }
}