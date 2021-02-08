package com.commsreport.screens.fragments.faults

import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteForFault
import com.commsreport.adapter.ManageFaultAdapter
import com.commsreport.databinding.ContentManageFaultBinding
import com.commsreport.databinding.FragmentFaultBinding
import com.commsreport.model.FaultListModel
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SiteListModel
import com.commsreport.screens.fragments.reportfault.ReportFaultFragment
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


class FaultFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var faultBinding:FragmentFaultBinding?=null
    var contentManageFaultBinding:ContentManageFaultBinding?=null
    var activity:HomeActivity?=null
    var manageFaultAdapter:ManageFaultAdapter?=null
    var userdata: LoginResponseModel.Userdata? =null
    var siteList=ArrayList<SiteListModel.RowList>()
    var faultList=ArrayList<FaultListModel.FaultList>()

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
        faultBinding= FragmentFaultBinding.inflate(inflater,container,false)
        contentManageFaultBinding=faultBinding!!.contentManageFault
        return faultBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        contentManageFaultBinding!!.tvAddfault.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultBinding!!.tvSelectedsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultBinding!!.contentManageFault.tvHeaderText.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))

        if(userdata!!.user_type.equals("COMPANY_ADMIN")){
          //  faultBinding!!.tvSelectedsite.visibility=View.VISIBLE
            callApiForSiteList()

        }else {
            selectedSiteId = userdata!!.site_id
            callApiforFaultList(selectedSiteId!!)
        }

        contentManageFaultBinding!!.tvAddfault.setOnClickListener {
            activity!!.openFragment(ReportFaultFragment())
        }

        contentManageFaultBinding!!.imgMenu.setOnClickListener {
            activity!!.homeBinding!!.drawerLayout.openDrawer(Gravity.LEFT)
        }
        faultBinding!!.contentManageFault.imgSearch.setOnClickListener {
            faultBinding!!.drawerLayout.openDrawer(Gravity.RIGHT)
        }
        faultBinding!!.tvSelectedsite.setOnClickListener {
            val customPopUpDialogSiteList= CustomPopUpDialogSiteForFault(activity,siteList,this)
            customPopUpDialogSiteList!!.show()
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FaultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Manage Faults")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.GONE
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
   public  fun callApiforFaultList(siteId:String){
       val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
       customProgress.showProgress(activity!!,"Please Wait..",false)
       val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
       try {
           val paramObject = JSONObject()
           paramObject.put("site_id", siteId)
           paramObject.put("check_process_type", "checks")
           paramObject.put("status_id", "1")
           paramObject.put("company_id", userdata!!.company_id)

           var obj: JSONObject = paramObject
           var jsonParser: JsonParser = JsonParser()
           var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
           val callApi=apiInterface.callFaultApi(userdata!!.token,gsonObject)
           callApi.enqueue(object : Callback<FaultListModel> {
               override fun onResponse(call: Call<FaultListModel>, response: Response<FaultListModel>) {
                   customProgress.hideProgress()
                   if(response.code()==200) {
                       if (response.body()!!.status){
                           if (response!!.body()!!.row.size>0) {
                               faultList.clear()
                               faultList=response!!.body()!!.row
                               setAdpterValue()

                           }else
                               ToastAlert.CustomToasterror(activity!!,"No Record Found")
                       }


                   }else if(response.code()==401){
                       Alert.showalertForUnAuthorized(activity!!,"Unauthorized")

                   }
               }

               override fun onFailure(call: Call<FaultListModel>, t: Throwable) {
                   customProgress.hideProgress()
               }
           })

       }catch (e:Exception){
           e.printStackTrace()
       }
   }

    public fun setAdpterValue() {
        manageFaultAdapter = ManageFaultAdapter(activity,faultList!!,this)
        contentManageFaultBinding!!.recManagefault!!.adapter = manageFaultAdapter
    }



}