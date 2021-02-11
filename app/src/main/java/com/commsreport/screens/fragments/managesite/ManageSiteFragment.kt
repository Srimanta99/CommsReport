package com.commsreport.screens.fragments.managesite

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.adapter.ManageSiteAdapter
import com.commsreport.databinding.ContentManageSiteBinding
import com.commsreport.databinding.FragmentManageSiteBinding
import com.commsreport.model.SiteListModel
import com.commsreport.screens.fragments.site.SiteFragment
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

class ManageSiteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var fragmentManageSiteBinding:FragmentManageSiteBinding?=null
    var manageSiteAdapter:ManageSiteAdapter?=null
    var activity:HomeActivity?=null
    var siteList=ArrayList<SiteListModel.RowList>()
    var contentManageSiteBinding:ContentManageSiteBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentManageSiteBinding=FragmentManageSiteBinding.inflate(inflater,container,false)
        contentManageSiteBinding=fragmentManageSiteBinding!!.contentManagesite
        return fragmentManageSiteBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
      //  manageSiteAdapter= ManageSiteAdapter(activity!!,siteList)
       //contentManageSiteBinding!!.recManagesite.adapter=manageSiteAdapter
        contentManageSiteBinding!!.tvHeaderText.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))
        fragmentManageSiteBinding!!.navSiteSearch.tvSearch.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentManageSiteBinding!!.navSiteSearch.tvSitename.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentManageSiteBinding!!.navSiteSearch.etsherchName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentManageSiteBinding!!.navSiteSearch.Searchsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        contentManageSiteBinding!!.imgMenu.setOnClickListener {
            activity!!.homeBinding!!.drawerLayout.openDrawer(Gravity.LEFT)
        }

        fragmentManageSiteBinding!!.imgNavopen.setOnClickListener {
            fragmentManageSiteBinding!!.drawerLayout!!.closeDrawer(Gravity.RIGHT)
        }

        fragmentManageSiteBinding!!.contentManagesite.imgSearch.setOnClickListener {
            fragmentManageSiteBinding!!.drawerLayout!!.openDrawer(Gravity.RIGHT)
        }

        callApiForSiteList()
        contentManageSiteBinding!!.tvAddSite.setOnClickListener {
            activity!!.openFragment(SiteFragment())
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
            val callApi=apiInterface.callSiteListApi(userdata.token,gsonObject)
            callApi.enqueue(object :Callback<SiteListModel>{
                override fun onResponse(call: Call<SiteListModel>, response: Response<SiteListModel>) {
                    customProgress.hideProgress()

                    if(response.code()==200) {
                        siteList.clear()
                        siteList = response.body()!!.row
                        if (siteList.size>=0) {
                            manageSiteAdapter = ManageSiteAdapter(activity!!, siteList)
                            contentManageSiteBinding!!.recManagesite.adapter = manageSiteAdapter
                        }else
                            ToastAlert.CustomToasterror(activity!!, "No Site Found")
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

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Manage Sites")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.GONE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManageSiteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}