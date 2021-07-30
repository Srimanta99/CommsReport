package com.commsreport.screens.forgotpassword

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import androidx.databinding.DataBindingUtil
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.databinding.ActivityForgotPasswordBinding
import com.commsreport.model.AddUserResponse
import com.commsreport.model.LoginResponseModel
import com.commsreport.screens.home.HomeActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.sculptee.utils.customprogress.CustomProgressDialog
import com.wecompli.network.ApiInterface
import com.wecompli.network.Retrofit
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            else{
                activityForgotPasswordBinding!!.etemail.requestFocus()
                ToastAlert.CustomToasterror(this@ForgotPasswordActivity,"Please enter email")
            }

        }
    }

    private fun callApiForForgotPassword() {
        // ToastAlert.CustomToastwornning(this,"Under development")
        // activity.startActivity(Intent(activity,HomeActivity::class.java))
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(this,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("user_email",activityForgotPasswordBinding!!.etemail.text.toString() )
          //  paramObject.put("password",activityLoginBinding.etPass.text.toString())

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val loginApiCall = apiInterface.callLogForgotPassword(gsonObject)
            loginApiCall.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(call: Call<AddUserResponse>, response: Response<AddUserResponse>) {
                    customProgress.hideProgress()
                    if (response.isSuccessful) {
                        if (response.body()!!.status) {
                            ToastAlert.CustomToastSuccess(
                                this@ForgotPasswordActivity,
                                response!!.body()!!.message
                            )
                            finish()
                        } else {
                            ToastAlert.CustomToasterror(
                                this@ForgotPasswordActivity,
                                response!!.body()!!.message
                            )

                        }
                    }
                }

                override fun onFailure(call: Call<AddUserResponse>, t: Throwable) {
                    customProgress.hideProgress()
                   // Alert.showalert(this@ForgotPasswordActivity,"wqgdowqd")
                }
            })
        }catch (e:Exception){
            e.printStackTrace()
        }

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