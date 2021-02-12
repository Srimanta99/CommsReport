package com.commsreport.screens.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.databinding.FragmentSettingsBinding
import com.commsreport.model.AddUserResponse
import com.commsreport.model.DocumentListModel
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

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class SettingFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var settingsBinding: FragmentSettingsBinding?=null
    var activity :HomeActivity?=null
    var userdata: LoginResponseModel.Userdata? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsBinding!!.etName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        settingsBinding!!.tvName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        settingsBinding!!.etEmail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        settingsBinding!!.tvEmail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        settingsBinding!!.tvPassword.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))
        settingsBinding!!.tvCurrentpassword.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        settingsBinding!!.etCurrentpassword.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        settingsBinding!!.tvNewpassword.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        settingsBinding!!.etNewpassword.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        settingsBinding!!.tvConfirmpass.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        settingsBinding!!.etConpass.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        settingsBinding!!.submitTvid.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        settingsBinding!!.etName.setText(userdata!!.full_name)
        settingsBinding!!.etEmail.setText(userdata!!.email)
        settingsBinding!!.etCompanyname.setText(userdata!!.company_name)
        settingsBinding!!.submitTvid.setOnClickListener {
            if(checkvalidation()){
            callApiforCallChangePassword()
            }
        }
        if(userdata!!.user_type.equals("COMPANY_ADMIN")){
            settingsBinding!!.etCompanyname.isEnabled=true
        }
    }

    private fun callApiforCallChangePassword() {
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("user_id", userdata!!.user_id)
            paramObject.put("user_first_name",settingsBinding!!.etName.text.toString())
            paramObject.put("user_email_ID",settingsBinding!!.etEmail.text.toString())
            paramObject.put("user_password", settingsBinding!!.etNewpassword.text.toString())
            paramObject.put("user_old_password", settingsBinding!!.etCurrentpassword.text.toString())
            paramObject.put("user_confirm_password", settingsBinding!!.etConpass.text.toString())
            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApifordochangepassword(userdata!!.token, gsonObject)
            callApi.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(call: Call<AddUserResponse>, response: Response<AddUserResponse>) {
                    customProgress.hideProgress()

                    if (response.code() == 200) {

                        if (response.body()!!.status){
                            ToastAlert.CustomToastSuccess(activity!!, response!!.body()!!.message)

                        }else
                            ToastAlert.CustomToasterror(activity!!, response!!.body()!!.message)

                    } else if (response.code() == 401) {
                        Alert.showalertForUnAuthorized(activity!!, "Unauthorized")

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

    private fun checkvalidation():Boolean {
        if (settingsBinding!!.etCurrentpassword.text.toString().equals("")){
            settingsBinding!!.etCurrentpassword.requestFocus()
            ToastAlert.CustomToastwornning(activity!!,"Enter current password")
            return false
        }
        if (settingsBinding!!.etNewpassword.text.toString().equals("")){
            settingsBinding!!.etNewpassword.requestFocus()
            ToastAlert.CustomToastwornning(activity!!,"Enter new password")
            return false
         }
       if (settingsBinding!!.etConpass.text.toString().equals("")){
            settingsBinding!!.etConpass.requestFocus()
            ToastAlert.CustomToastwornning(activity!!,"Enter confirm password")
           return false
        }
        if (!settingsBinding!!.etNewpassword.text.toString().equals(settingsBinding!!.etConpass.text.toString())){
            ToastAlert.CustomToastwornning(activity!!,"Password and confirm password not match")
            return false
        }
        return true

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsBinding= FragmentSettingsBinding.inflate(inflater,container,false)
        return settingsBinding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("SETTINGS")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.VISIBLE
    }
}