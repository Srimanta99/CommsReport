package com.commsreport.screens.fragments.manageuser

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.adapter.ManageUserAdapter
import com.commsreport.databinding.FragmentManageUserBinding
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
    var manaUserAdapter:ManageUserAdapter?=null
    var userList=ArrayList<SiteUserListModel.UserList>()
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
        return fragmentManageUserBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApiForSiteList()
        fragmentManageUserBinding!!.tvAddUser.setOnClickListener {
            activity!!.openFragment(AddUserFragment())
        }
    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Manage Users")
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

    private fun callApiForSiteList() {
        var userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callSiteUserListApi(userdata.token,gsonObject)
            callApi.enqueue(object : Callback<SiteUserListModel> {
                override fun onResponse(call: Call<SiteUserListModel>, response: Response<SiteUserListModel>) {
                    customProgress.hideProgress()

                    if(response.code()==200) {
                        if (response.body()!!.status){
                            userList.clear()
                            userList=response!!.body()!!.row
                            manaUserAdapter= ManageUserAdapter(activity!!,userList)
                            fragmentManageUserBinding!!.recManageUser.adapter=manaUserAdapter
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