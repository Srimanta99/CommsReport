 package com.commsreport.screens.fragments.faultlistdetails

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogFaultSearch
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteForFaultSearch
import com.commsreport.adapter.ManageFaultAdapter
import com.commsreport.adapter.SiteFaultAdapter
import com.commsreport.databinding.ContentFaultListBinding
import com.commsreport.databinding.FragmentFaultListBinding
import com.commsreport.model.FaultListModel
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SiteListModel
import com.commsreport.screens.home.HomeActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.sculptee.utils.customprogress.CustomProgressDialog
import com.wecompli.network.ApiInterface
import com.wecompli.network.Retrofit
import com.wecompli.utils.onitemclickinterface.OnItemClickInterface
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FaultListFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var activity: HomeActivity?=null
    var faultListBinding:FragmentFaultListBinding?=null
    var contentFaultListBinding:ContentFaultListBinding?=null
    var selected_Site_id=""
    var userdata: LoginResponseModel.Userdata? =null
    var faultList=ArrayList<FaultListModel.FaultList>()
    var selecteddate=""
    var manageFaultAdapter: ManageFaultAdapter?=null
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
        faultListBinding= FragmentFaultListBinding.inflate(inflater,container,false)
        contentFaultListBinding=faultListBinding!!.contentFaultlist
        return faultListBinding!!.root
    }
    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Manage Faults")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.GONE
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        contentFaultListBinding!!.tvHeaderText.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))
        faultListBinding!!.navFaultSearch.tvDropdownSelectsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultListBinding!!.navFaultSearch.tvScarchByDate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultListBinding!!.navFaultSearch.tvSelectsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultListBinding!!.navFaultSearch.tvDate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultListBinding!!.navFaultSearch.tvSearch.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        faultListBinding!!.navFaultSearch.tvDropdownSelectsite.setText(AppSheardPreference(activity!!).getvalue_in_preference(PreferenceConstent.selected_sitename))
        contentFaultListBinding!!.imgMenu.setOnClickListener {
            activity!!.homeBinding!!.drawerLayout.openDrawer(Gravity.LEFT)
        }
        faultListBinding!!.contentFaultlist.imgSearch.setOnClickListener {
            faultListBinding!!.contentFaultlist!!.rlback.visibility=View.GONE
            faultListBinding!!.contentFaultlist!!.imgnodata!!.visibility=View.GONE
            faultListBinding!!.drawerLayout.openDrawer(Gravity.RIGHT)
        }
        faultListBinding!!.imgNavclose.setOnClickListener {
            faultListBinding!!.drawerLayout!!.closeDrawer(Gravity.RIGHT)
        }
        faultListBinding!!.navFaultSearch.tvDropdownSelectsite.setOnClickListener {
            if(userdata!!.user_type.equals("COMPANY_ADMIN")) {
                callApiForSiteList()
            }

        }

        selected_Site_id=AppSheardPreference(activity!!).getvalue_in_preference(PreferenceConstent.selected_site_id)
        callApiforFaultList(selected_Site_id)
        faultListBinding!!.navFaultSearch.tvDate.setOnClickListener {
            datepickerdeStartDate()
        }
        faultListBinding!!.navFaultSearch.searchFault.setOnClickListener {
            if (!faultListBinding!!.navFaultSearch!!.tvDate.text.toString().equals("") || !selected_Site_id.equals("") ){
                callApiforFaultList(selected_Site_id!!)
                faultListBinding!!.drawerLayout!!.closeDrawer(Gravity.RIGHT)

            }else
                ToastAlert.CustomToastwornning(activity!!,"Please select date")
        }
        faultListBinding!!.contentFaultlist!!.tvBack.setOnClickListener {
            faultListBinding!!.contentFaultlist!!.rlback.visibility=View.GONE
            faultListBinding!!.contentFaultlist!!.imgnodata!!.visibility=View.GONE
            activity!!.getSupportFragmentManager().popBackStack();
        }
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FaultListFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
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
                        val customPopUpDialogSiteList= CustomPopUpDialogFaultSearch(activity,siteList,this@FaultListFragment)
                        customPopUpDialogSiteList!!.show()

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
        selected_Site_id=siteId
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("site_id", siteId)
            paramObject.put("check_process_type", "checks")
            paramObject.put("status_id", "1")
            paramObject.put("from_date",selecteddate)
            paramObject.put("to_date",selecteddate)
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
                                faultListBinding!!.contentFaultlist.recManagefault!!.visibility=View.VISIBLE
                                faultListBinding!!.contentFaultlist!!.imgnodata!!.visibility=View.GONE
                                faultListBinding!!.contentFaultlist!!.tvBack.visibility=View.GONE
                                setAdpterValue()

                            }else{
                                faultListBinding!!.contentFaultlist.recManagefault!!.visibility=View.GONE
                                faultListBinding!!.contentFaultlist!!.imgnodata!!.visibility=View.VISIBLE
                                faultListBinding!!.contentFaultlist!!.rlback.visibility=View.VISIBLE
                            }
                            //  ToastAlert.CustomToasterror(activity!!,"No Record Found")
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
    fun datepickerdeStartDate() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            activity!!, R.style.AppDatepickerDilogtheam,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val choosedate = padnumber(dayOfMonth) + "/" + padnumber(monthOfYear + 1) + "/" + year.toString()
                // val checkdate = year.toString() + "-" + padnumber(monthOfYear + 1) + "-" + padnumber(dayOfMonth)
                //  val listcheckdate = padnumber(monthOfYear + 1) + "/" + padnumber(dayOfMonth) + "/" + year.toString()
                faultListBinding!!.navFaultSearch.tvDate.setText(choosedate)
                selecteddate=choosedate

            }, mYear, mMonth, mDay
        )
        //  datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show()

    }
    fun padnumber(n: Int): String {
        val num: String
        if (n > 10 || n == 10)
            num = n.toString()
        else
            num = "0$n"
        return num
    }
    public fun setAdpterValue() {
        manageFaultAdapter = ManageFaultAdapter(activity,faultList!!,this)
        contentFaultListBinding!!.recManagefault!!.adapter = manageFaultAdapter
    }
}