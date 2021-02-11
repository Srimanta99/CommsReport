package com.commsreport.screens.fragments.manageuser

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteForFaultSearch
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteForUserSearch
import com.commsreport.adapter.ManageUserAdapter
import com.commsreport.databinding.ContentManageUserBinding
import com.commsreport.databinding.FragmentManageUserBinding
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SiteListModel
import com.commsreport.model.SiteUserListModel
import com.commsreport.screens.fragments.adduser.AddUserFragment
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
import java.util.ArrayList
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ManageUserFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var activity:HomeActivity?=null
    var fragmentManageUserBinding:FragmentManageUserBinding?=null
    var contentManageUserBinding:ContentManageUserBinding?=null
    var manaUserAdapter:ManageUserAdapter?=null
    var userList=ArrayList<SiteUserListModel.UserList>()
    var userdata:LoginResponseModel.Userdata?=null
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
        fragmentManageUserBinding= FragmentManageUserBinding.inflate(inflater,container,false)
        contentManageUserBinding=fragmentManageUserBinding!!.contentManageUser
        return fragmentManageUserBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        contentManageUserBinding!!.tvHeaderText.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentManageUserBinding!!.navFaultSearch.Search.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentManageUserBinding!!.navFaultSearch.tvDropdownSelectsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentManageUserBinding!!.navFaultSearch.tvSelectsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentManageUserBinding!!.navFaultSearch.tvSearchByName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentManageUserBinding!!.navFaultSearch.etsherchName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentManageUserBinding!!.navFaultSearch.tvSearch.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))

       fragmentManageUserBinding!!.navFaultSearch.Search.setOnClickListener {

       }
        fragmentManageUserBinding!!.navFaultSearch.tvDropdownSelectsite.setOnClickListener {
                val customPopUpDialogSiteList= CustomPopUpDialogSiteForUserSearch(activity,siteList,this)
                customPopUpDialogSiteList!!.show()
        }


        contentManageUserBinding!!.imgSearch.setOnClickListener {
            fragmentManageUserBinding!!.drawerLayout.openDrawer(Gravity.RIGHT)
        }
        contentManageUserBinding!!.imgMenu.setOnClickListener {
            activity!!.homeBinding!!.drawerLayout.openDrawer(Gravity.LEFT)
        }
        callApiForSiteList()
        callApiForUserList()

        contentManageUserBinding!!.tvAddUser.setOnClickListener {
            activity!!.openFragment(AddUserFragment())
        }
        fragmentManageUserBinding!!.navClose.setOnClickListener {
            fragmentManageUserBinding!!.drawerLayout.closeDrawer(Gravity.RIGHT)
        }
    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Manage Users")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.GONE
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManageUserFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    public fun callApiForSiteList() {

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

    private fun callApiForUserList() {

        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callSiteUserListApi(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<SiteUserListModel> {
                override fun onResponse(call: Call<SiteUserListModel>, response: Response<SiteUserListModel>) {
                    customProgress.hideProgress()

                    if(response.code()==200) {
                        if (response.body()!!.status){
                            userList.clear()
                            userList=response!!.body()!!.row
                            manaUserAdapter= ManageUserAdapter(activity!!,userList)
                            contentManageUserBinding!!.recManageUser.adapter=manaUserAdapter
                        }else
                            ToastAlert.CustomToasterror(activity!!,"No User found")
                    }else if(response.code()==401){
                        Alert.showalertForUnAuthorized(activity!!,"Unauthorized")

                    }
                }

                override fun onFailure(call: Call<SiteUserListModel>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}