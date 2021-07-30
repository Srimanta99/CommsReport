package com.commsreport.screens.romovesiteuser

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.adapter.RemoveSiteAdapter
import com.commsreport.adapter.RemoveUserAdapter
import com.commsreport.databinding.ActivittyRemoveUserSiteBinding
import com.commsreport.model.AddUserResponse
import com.commsreport.model.SiteAndUserModel
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

class RemoveSiteUserActivity:AppCompatActivity() {
    var activittyRemoveUserSiteBinding:ActivittyRemoveUserSiteBinding?=null
    var removeUserAdapter:RemoveUserAdapter?=null
    var removeSiteAdapter:RemoveSiteAdapter?=null
    var row_user:ArrayList<SiteAndUserModel.UserRow>?= ArrayList()
    var row_site:ArrayList<SiteAndUserModel.SiteRow>?= ArrayList()
    var seletedUser=ArrayList<String>()
    var selectedSite=ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activittyRemoveUserSiteBinding= ActivittyRemoveUserSiteBinding.inflate(
            LayoutInflater.from(
                this
            )
        )
        setContentView(activittyRemoveUserSiteBinding!!.root)

        activittyRemoveUserSiteBinding!!.tvHeaderText.setTypeface(
            CustomTypeface.getRajdhaniMedium(
                this
            )
        )
        activittyRemoveUserSiteBinding!!.btnSite.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activittyRemoveUserSiteBinding!!.btnUser.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activittyRemoveUserSiteBinding!!.btnUser.setOnClickListener {
            activittyRemoveUserSiteBinding!!.btnUser.background=resources.getDrawable(R.drawable.rectangular_shape_left_two_rounded_corner_blue)
            activittyRemoveUserSiteBinding!!.btnSite.background=resources.getDrawable(R.drawable.rectangular_shape_right_two_rounded_corner_white)
           activittyRemoveUserSiteBinding!!.btnUser.setTextColor(resources.getColor(R.color.white))
            activittyRemoveUserSiteBinding!!.btnSite.setTextColor(resources.getColor(R.color.black))
            activittyRemoveUserSiteBinding!!.recRemovesite.visibility= View.GONE
            activittyRemoveUserSiteBinding!!.recRemoveuser.visibility= View.VISIBLE
        }
        activittyRemoveUserSiteBinding!!.btnSite.setOnClickListener {
            activittyRemoveUserSiteBinding!!.btnSite.background=resources.getDrawable(R.drawable.rectangular_shape_right_two_rounded_corner_blue)
            activittyRemoveUserSiteBinding!!.btnUser.background=resources.getDrawable(R.drawable.rectangular_shape_left_two_rounded_corner_white)
            activittyRemoveUserSiteBinding!!.btnSite.setTextColor(resources.getColor(R.color.white))
            activittyRemoveUserSiteBinding!!.btnUser.setTextColor(resources.getColor(R.color.black))
            activittyRemoveUserSiteBinding!!.recRemovesite.visibility= View.VISIBLE
            activittyRemoveUserSiteBinding!!.recRemoveuser.visibility= View.GONE
        }

        activittyRemoveUserSiteBinding!!.imgBack.setOnClickListener {
            finish()
        }
        activittyRemoveUserSiteBinding!!.tvRemove.setOnClickListener {
            if(seletedUser.size>0 || selectedSite.size>0)
            Alert.showalertForConfirmRemoveUser(this)
            else
                ToastAlert.CustomToastwornning(this,"Select user and site for remove from list")
        //    callApiforUserSiteRemove();

        }
        callApiforDowngradepackage()
    }

    public fun callApiforUserSiteRemove() {
        var userString=""
        var siteString=""
        var userdata= AppSheardPreference(this).getUser(PreferenceConstent.userData)
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(this, "Please Wait..", false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {

            val userarr: Array<String> = seletedUser.toArray(arrayOfNulls<String>(seletedUser.size))
            val sitearr: Array<String> = selectedSite.toArray(arrayOfNulls<String>(selectedSite.size))

            for (s in userarr)
              userString=userString+","+s

            for (s in sitearr)
                siteString=siteString+","+s

            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)
            paramObject.put("user_ids", userString.replaceFirst(",",""))
            paramObject.put("site_ids", siteString.replaceFirst(",",""))
            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApiforRemovesiteandUser(userdata!!.token, gsonObject)
            callApi.enqueue(object : Callback<SiteAndUserModel> {
                override fun onResponse(call: Call<SiteAndUserModel>, response: Response<SiteAndUserModel>) {
                    customProgress.hideProgress()
                    if (response.code() == 200) {
                        if (response.body()!!.status) {

                            row_user = response!!.body()!!.row_user
                            row_site = response!!.body()!!.row_site
                            removeUserAdapter = RemoveUserAdapter(this@RemoveSiteUserActivity, row_user)
                            removeSiteAdapter = RemoveSiteAdapter(this@RemoveSiteUserActivity, row_site)
                            activittyRemoveUserSiteBinding!!.recRemovesite.adapter = removeSiteAdapter
                            activittyRemoveUserSiteBinding!!.recRemoveuser.adapter = removeUserAdapter
                        }

                    } else if (response.code() == 401) {
                        Alert.showalertForUnAuthorized(
                            this@RemoveSiteUserActivity!!,
                            "Unauthorized"
                        )
                    }
                }

                override fun onFailure(call: Call<SiteAndUserModel>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e: Exception){
            e.printStackTrace()
        }

    }


    private fun callApiforDowngradepackage() {
        var userdata= AppSheardPreference(this).getUser(PreferenceConstent.userData)
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(this, "Please Wait..", false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)
          //  paramObject.put("subscription_package_id",selectedPackage!!.id)
            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApiforsiteandUser(userdata!!.token, gsonObject)
            callApi.enqueue(object : Callback<SiteAndUserModel> {
                override fun onResponse(
                    call: Call<SiteAndUserModel>,
                    response: Response<SiteAndUserModel>
                ) {
                    customProgress.hideProgress()
                    if (response.code() == 200) {
                        if (response.body()!!.status) {
                            row_user = response!!.body()!!.row_user
                            row_site = response!!.body()!!.row_site
                            removeUserAdapter = RemoveUserAdapter(this@RemoveSiteUserActivity, row_user)
                            removeSiteAdapter = RemoveSiteAdapter(this@RemoveSiteUserActivity, row_site)
                            activittyRemoveUserSiteBinding!!.recRemovesite.adapter = removeSiteAdapter
                            activittyRemoveUserSiteBinding!!.recRemoveuser.adapter = removeUserAdapter

                        }

                    } else if (response.code() == 401) {
                        Alert.showalertForUnAuthorized(
                            this@RemoveSiteUserActivity!!,
                            "Unauthorized"
                        )

                    }
                }

                override fun onFailure(call: Call<SiteAndUserModel>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e: Exception){
            e.printStackTrace()
        }

    }
}