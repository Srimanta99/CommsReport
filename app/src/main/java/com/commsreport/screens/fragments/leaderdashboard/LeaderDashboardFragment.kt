package com.commsreport.screens.fragments.leaderdashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.adapter.ManageDocumentAdapter
import com.commsreport.databinding.FragmentLeaderDashboardBinding
import com.commsreport.model.DocumentListModel
import com.commsreport.model.FaultCountModel
import com.commsreport.model.LoginResponseModel
import com.commsreport.screens.fragments.document.DocumentUploadFragment
import com.commsreport.screens.fragments.faultlistdetails.FaultListFragment
import com.commsreport.screens.fragments.faults.FaultFragment
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

class LeaderDashboardFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var activity: HomeActivity?=null
    var userdata: LoginResponseModel.Userdata? =null
    var fragmentLeaderDashboardBinding:FragmentLeaderDashboardBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=  getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        fragmentLeaderDashboardBinding= FragmentLeaderDashboardBinding.inflate(inflater, container, false);
        return fragmentLeaderDashboardBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLeaderDashboardBinding!!.tvReportfault.setTypeface(CustomTypeface.getRajdhaniSemiBold(getActivity()!!))
        fragmentLeaderDashboardBinding!!.tvUploadDocument.setTypeface(CustomTypeface.getRajdhaniSemiBold(getActivity()!!))
        fragmentLeaderDashboardBinding!!.tvFaultereport.setTypeface(CustomTypeface.getRajdhaniBold(getActivity()!!))
        fragmentLeaderDashboardBinding!!.tvfaultcount.setTypeface(CustomTypeface.getRajdhaniBold(getActivity()!!))
        fragmentLeaderDashboardBinding!!.tvUploadDocument.setOnClickListener {
            activity!!.openFragment(DocumentUploadFragment())
        }
        fragmentLeaderDashboardBinding!!.tvReportfault.setOnClickListener {
            activity!!.openFragment(ReportFaultFragment())
        }
        fragmentLeaderDashboardBinding!!.rlFaultcount.setOnClickListener {
            if(userdata!!.user_type.equals("COMPANY_ADMIN")){
                activity!!.openFragment(FaultFragment())
            }else{
                AppSheardPreference(activity!!).setvalue_in_preference(PreferenceConstent.selected_site_id,userdata!!.site_id)
                activity!!.openFragment(FaultListFragment())
            }



        }
        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        callApiForFaultCount()
    }
    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Dashboard")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.VISIBLE
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LeaderDashboardFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun callApiForFaultCount() {

        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)
            paramObject.put("status_id", "1")
            if(!userdata!!.user_type.equals("COMPANY_ADMIN")){
                    paramObject.put("site_id", userdata!!.site_id)
            }
            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callFaultCountApt(userdata!!.token, gsonObject)
            callApi.enqueue(object : Callback<FaultCountModel> {
                override fun onResponse(call: Call<FaultCountModel>, response: Response<FaultCountModel>) {
                    customProgress.hideProgress()
                    System.out.println("response g"+response!!.body()!!.fault_count)
                    if (response.code() == 200) {
                        if (response.body()!!.status ){
                           // if (response.body()!!.response!=null)
                              fragmentLeaderDashboardBinding!!.tvfaultcount!!.setText((response!!.body()!!.fault_count).toString())
                        }else
                            fragmentLeaderDashboardBinding!!.tvfaultcount!!.setText("0")

                    } else if (response.code() == 401) {
                        Alert.showalertForUnAuthorized(activity!!, "Unauthorized")

                    }
                    else{
                        ToastAlert.CustomToasterror(activity!!,"Something wrong. try later")
                    }
                }

                override fun onFailure(call: Call<FaultCountModel>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}