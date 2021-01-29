package com.commsreport.screens.fragments.edituser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteForUser
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteListAddUser
import com.commsreport.databinding.FragmentEditUserBinding
import com.commsreport.model.AddUserResponse
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SiteListModel
import com.commsreport.model.SiteUserListModel
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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class EditUserFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var activity: HomeActivity?=null
    var editUserBinding:FragmentEditUserBinding?=null
    var userdetails:SiteUserListModel.UserList?=null
    var userdata: LoginResponseModel.Userdata? =null
    var siteList=ArrayList<SiteListModel.RowList>()
    var selectedSiteId:String?=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        editUserBinding=FragmentEditUserBinding.inflate(inflater,container,false)
        return editUserBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        if(userdata!!.user_type.equals("COMPANY_ADMIN")){
            editUserBinding!!.llSelectsite.visibility=View.VISIBLE

        }else
            selectedSiteId=userdata!!.site_id
        editUserBinding!!.tvNameAddUser.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        editUserBinding!!.etnName.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        editUserBinding!!.tvEmailAddUser.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        editUserBinding!!.etnEmail.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        editUserBinding!!.tvContactno.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        editUserBinding!!.etnContactno.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        editUserBinding!!.tvSelectsite.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        editUserBinding!!.tvDropdownSelectsite.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        editUserBinding!!.submitTvid.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        editUserBinding!!.tvDropdownSelectsite.setOnClickListener {
            val customPopUpDialogSiteList= CustomPopUpDialogSiteForUser(activity,siteList,this)
            customPopUpDialogSiteList!!.show()
        }
        editUserBinding!!.submitTvid.setOnClickListener {
            if (checkValidation())
                callApiforUpdateUser()
        }

        userdetails=AppSheardPreference(activity!!).getSelectedUser(PreferenceConstent.selectedUser)
        editUserBinding!!.etnName.setText(userdetails!!.user_first_name)
        editUserBinding!!.etnEmail.setText(userdetails!!.user_email_ID)
        editUserBinding!!.etnContactno.setText(userdetails!!.user_contactno)
        editUserBinding!!.etnAddress.setText(userdetails!!.user_address)
        editUserBinding!!.tvDropdownSelectsite.setText(userdetails!!.site_name)
        callApiForSiteList()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Edit User")
    }
    private fun callApiForSiteList() {

        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callSiteListApi(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<SiteListModel> {
                override fun onResponse(call: Call<SiteListModel>, response: Response<SiteListModel>) {
                    customProgress.hideProgress()
                    if(response.code()==200) {
                        siteList = response.body()!!.row

                    }else if(response.code()==401){
                        Alert.showalertForUnAuthorized(activity!!,"Unauthorized")

                    }
                }

                override fun onFailure(call: Call<SiteListModel>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun checkValidation():Boolean{
        if(editUserBinding!!.etnName.text.toString().equals("")){
            editUserBinding!!.etnName.requestFocus()
            return false
        }
        if(editUserBinding!!.etnEmail.text.toString().equals("")){
            editUserBinding!!.etnEmail.requestFocus()
            return false
        }
        if(editUserBinding!!.etnContactno.text.toString().equals("")){
            editUserBinding!!.etnContactno.requestFocus()
            return false
        }
        if(editUserBinding!!.etnAddress.text.toString().equals("")){
            editUserBinding!!.etnAddress.requestFocus()
            return false
        }
        if (selectedSiteId.equals("")){
            Alert.showalert(activity!!,"Select Site")
            return false
        }
        return true

    }

    private fun callApiforUpdateUser() {
        val customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val apiInterface = Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("user_first_name", editUserBinding!!.etnName.text.toString())
            paramObject.put("user_email_ID", editUserBinding!!.etnEmail.text.toString())
            paramObject.put("user_address", editUserBinding!!.etnAddress.text.toString())
            paramObject.put("user_contactno", editUserBinding!!.etnContactno.text.toString())
            paramObject.put("company_id", userdata!!.company_id)
            paramObject.put("site_id",selectedSiteId)
            paramObject.put("status_id","1")
            paramObject.put("user_profile_image","")

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi= apiInterface.callSiteUserUpdateApi(userdata!!.token, gsonObject!!)
            callApi.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(call: Call<AddUserResponse>, response: Response<AddUserResponse>) {
                    customProgress.hideProgress()
                    if (response.isSuccessful) {
                        if (response.body()!!.status) {
                            Alert.showalert(activity!!,response!!.body()!!.message)
                            editUserBinding!!.etnName.setText("")
                            editUserBinding!!.etnEmail.setText("")
                            editUserBinding!!.etnContactno.setText("")
                            editUserBinding!!.etnAddress.setText("")
                        }
                        else
                            Alert.showalert(activity!!,response!!.body()!!.message)

                    }
                    else
                        Toast.makeText(activity, "Try later. Something Wrong.", Toast.LENGTH_LONG).show()
                }
                override fun onFailure(call: Call<AddUserResponse>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e: java.lang.Exception){
            e.printStackTrace()
        }
    }

}