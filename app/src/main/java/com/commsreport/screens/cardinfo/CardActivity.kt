package com.commsreport.screens.cardinfo

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.databinding.ActivityCardBinding
import com.commsreport.model.AddUserResponse
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SubCriptionPackagResponseemodel
import com.commsreport.screens.thankyoupage.ThankyouActivity
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

class CardActivity : AppCompatActivity() {
    var activityCardBinding:ActivityCardBinding?=null
    var selectedpackage:SubCriptionPackagResponseemodel.PackageItem?=null
    private var lock = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityCardBinding= ActivityCardBinding.inflate(LayoutInflater.from(this))
        selectedpackage = intent.getSerializableExtra("package") as? SubCriptionPackagResponseemodel.PackageItem
        setContentView(activityCardBinding!!.root)
        activityCardBinding!!.tvEnterCardDetails.setTypeface(CustomTypeface.getRajdhaniSemiBold(this))
        activityCardBinding!!.tvCardNo.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityCardBinding!!.etnCardnumber.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityCardBinding!!.tvName.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityCardBinding!!.etnNameid.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityCardBinding!!.tvExpdate.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityCardBinding!!.tvCvv.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityCardBinding!!.tvPowerdby.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityCardBinding!!.etExpMonth.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityCardBinding!!.etExpYear.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activityCardBinding!!.etCvv.setTypeface(CustomTypeface.getRajdhaniMedium(this))



        activityCardBinding!!.loginUnderline.setTypeface(CustomTypeface.getRajdhaniBold(this))
        activityCardBinding!!.tvPay.setTypeface(CustomTypeface.getRajdhaniSemiBold(this))
        activityCardBinding!!.tvHeaderText.setTypeface(CustomTypeface.getRajdhaniBold(this))

        activityCardBinding!!.etExpMonth.addTextChangedListener(object : TextWatcher {

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0!!.length >= 2)
                    activityCardBinding!!.etExpYear.requestFocus()
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })

       /* activityCardBinding!!.etnCardnumber.addTextChangedListener(object : TextWatcher {
            private val space = ' '
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(s: Editable?) {
                if (lock || s!!.length > 16) {
                    return
                }
                lock = true

                  for(i in 4..20 step 5 ){
                      if (s.toString()[i] != ' ') {
                          s.insert(i, " ");
                      }

                  }

                lock = false
                *//*  if(s!!.length%4==0)
                {
                    activityCardBinding!!.etnCardnumber.setText( activityCardBinding!!.etnCardnumber.getText().toString() + " ");
                    activityCardBinding!!.etnCardnumber.setSelection(s.length)
                }*//*
                *//* var pos = 0
                while (true) {
                    if (pos >= s!!.length) break
                    if (space === s.get(pos) && ((pos + 1) % 11 != 0 || pos + 1 == s.length)) {
                        s.delete(pos, pos + 1)
                    } else {
                        pos++
                    }
                }
                pos = 10
                while (true) {
                    if (pos >= s!!.length) break
                    val c: Char = s!!.get(pos)
                    if (Character.isDigit(c)) {
                        s!!.insert(pos, "" + space)
                    }
                    pos += 11
                }*//*
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })*/

        activityCardBinding!!.tvPay.setOnClickListener {
            if(!activityCardBinding!!.etnCardnumber.text.toString().equals("")) {
                if (!activityCardBinding!!.etnNameid.text.toString().equals("")) {
                    if (!activityCardBinding!!.etExpMonth.text.toString().equals("")) {
                        if (!activityCardBinding!!.etExpYear.text.toString().equals("")) {
                            if (!activityCardBinding!!.etCvv.text.toString().equals("")) {
                                callApiforpayment()
                            }else
                                ToastAlert.CustomToastwornning(this, "Enter card cvv")
                        }else
                        ToastAlert.CustomToastwornning(this, "Enter card expiry Year")

                }else
                        ToastAlert.CustomToastwornning(this, "Enter card expiry month")
                }else
                    ToastAlert.CustomToastwornning(this, "Enter card name")
            }else
                ToastAlert.CustomToastwornning(this, "Enter card number")

        }
        activityCardBinding!!.imgBack.setOnClickListener {
            finish()
        }
    }

    private fun callApiforpayment() {
        var userdata: LoginResponseModel.Userdata? = AppSheardPreference(this!!).getUser(
            PreferenceConstent.userData
        )
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(this, "Please Wait..", false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {

            val paramObject = JSONObject()
            paramObject.put("user_id", userdata!!.user_id)
            paramObject.put("subscription_package_id", selectedpackage!!.id)
            paramObject.put("card_number", activityCardBinding!!.etnCardnumber.text.toString())
            paramObject.put("exp_month", activityCardBinding!!.etExpMonth.text.toString())
            paramObject.put("exp_year", activityCardBinding!!.etExpYear.text.toString())
            paramObject.put("cvc", activityCardBinding!!.etCvv.text.toString())
            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApiforpayment(userdata!!.token, gsonObject)
            callApi.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(
                    call: Call<AddUserResponse>,
                    response: Response<AddUserResponse>
                ) {
                    customProgress.hideProgress()

                    if (response.code() == 200) {
                        if (response.body()!!.status) {
                            ToastAlert.CustomToastSuccess(this@CardActivity,response!!.body()!!.message)
                            val intent = Intent(this@CardActivity, ThankyouActivity::class.java)
                            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        } else {
                            Alert.showalert(this@CardActivity!!, response!!.body()!!.message)
                        }


                    } else if (response.code() == 401) {
                        Alert.showalertForUnAuthorized(this@CardActivity!!, "Unauthorized")

                    }
                }

                override fun onFailure(call: Call<AddUserResponse>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e: Exception){
            e.printStackTrace()
        }

    }
}