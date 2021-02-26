package com.commsreport.screens.fragments.faults

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteForFault
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteForFaultSearch
import com.commsreport.adapter.ManageFaultAdapter
import com.commsreport.adapter.SiteFaultAdapter
import com.commsreport.databinding.ContentManageFaultBinding
import com.commsreport.databinding.FragmentFaultBinding


import com.commsreport.model.FaultListModel
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SiteListModel
import com.commsreport.screens.fragments.faultlistdetails.FaultListFragment
import com.commsreport.screens.fragments.reportfault.ReportFaultFragment
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

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class FaultFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var faultBinding: FragmentFaultBinding?=null
    var contentManageFaultBinding: ContentManageFaultBinding?=null
    var activity:HomeActivity?=null
    var manageFaultAdapter:ManageFaultAdapter?=null
    var userdata: LoginResponseModel.Userdata? =null
    var siteList=ArrayList<SiteListModel.RowList>()
    var faultList=ArrayList<FaultListModel.FaultList>()
    var selecteddate=""
    var siteFaultAdapter: SiteFaultAdapter?=null
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
        faultBinding!!.contentManageFault!!.tvSelectedsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultBinding!!.contentManageFault.tvHeaderText.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))
        faultBinding!!.navFaultSearch.tvSearch.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        faultBinding!!.navFaultSearch.tvScarchByDate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultBinding!!.navFaultSearch.tvDate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultBinding!!.navFaultSearch.tvSelectsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultBinding!!.navFaultSearch.tvDropdownSelectsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultBinding!!.navFaultSearch.searchFault.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        faultBinding!!.contentManageFault.tvTotalFault.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))
        faultBinding!!.contentManageFault.tvFaultcount.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))

        faultBinding!!.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
       /* faultBinding!!.navFaultSearch.tvDate.setOnClickListener {
            datepickerdeStartDate()
        }
        faultBinding!!.navFaultSearch.searchFault.setOnClickListener {
            if (!faultBinding!!.navFaultSearch!!.tvDate.text.toString().equals("") || !faultBinding!!.navFaultSearch.tvDropdownSelectsite.text.toString().equals("")){
                callApiforFaultList(selectedSiteId!!)
            }else
                ToastAlert.CustomToastwornning(activity!!,"Please enter some value")
        }*/
        if(userdata!!.user_type.equals("COMPANY_ADMIN")){
          //  faultBinding!!.contentManageFault.tvSelectedsite.visibility=View.VISIBLE

            callApiForSiteList()
            selectedSiteId = ""
           // callApiforFaultList(selectedSiteId!!)

        }else {
            faultBinding!!.navFaultSearch.tvDropdownSelectsite.setText(userdata!!.site_name)
            faultBinding!!.navFaultSearch.tvDropdownSelectsite.isEnabled=false
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
        faultBinding!!.imgNavclose.setOnClickListener {
            faultBinding!!.drawerLayout!!.closeDrawer(Gravity.RIGHT)
        }
        faultBinding!!.contentManageFault.tvSelectedsite.setOnClickListener {
            val customPopUpDialogSiteList= CustomPopUpDialogSiteForFault(activity,siteList,this)
            customPopUpDialogSiteList!!.show()
        }
       /* faultBinding!!.navFaultSearch.tvDropdownSelectsite.setOnClickListener {
            val customPopUpDialogSiteList= CustomPopUpDialogSiteForFaultSearch(activity,siteList,this)
            customPopUpDialogSiteList!!.show()
        }*/
        faultBinding!!.navFaultSearch.searchFault.setOnClickListener {
            if(!selectedSiteId.equals("")) {
                callApiforFaultList(selectedSiteId!!)
                faultBinding!!.drawerLayout!!.closeDrawer(Gravity.RIGHT)
            }else
                ToastAlert.CustomToastwornning(activity!!,"Please select site")
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            FaultFragment().apply {
                arguments = Bundle().apply { putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Manage Faults")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.GONE
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
                faultBinding!!.navFaultSearch.tvDate.setText(choosedate)
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
                        faultBinding!!.contentManageFault.tvFaultcount.setText(padnumber(response!!.body()!!.total_fault).toString())
                        siteFaultAdapter = SiteFaultAdapter(activity!!,siteList!!,this@FaultFragment,object :OnItemClickInterface{
                            override fun OnItemClick(position: Int) {
                                AppSheardPreference(activity!!).setvalue_in_preference(PreferenceConstent.selected_site_id,siteList.get(position).id)
                                AppSheardPreference(activity!!).setvalue_in_preference(PreferenceConstent.selected_sitename,siteList.get(position).site_name)
                                activity!!.openFragment(FaultListFragment())

                            }
                        })
                        contentManageFaultBinding!!.recManagefault!!.adapter = siteFaultAdapter

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
       selectedSiteId=siteId
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
                               faultBinding!!.contentManageFault.recManagefault!!.visibility=View.VISIBLE
                              // faultBinding!!.contentManageFault!!.noData!!.visibility=View.GONE
                              // setAdpterValue()

                           }else{
                               faultBinding!!.contentManageFault.recManagefault!!.visibility=View.GONE
                             //  faultBinding!!.contentManageFault.noData!!.visibility=View.VISIBLE
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

   /* public fun setAdpterValue() {
        manageFaultAdapter = ManageFaultAdapter(activity,faultList!!,this)
        contentManageFaultBinding!!.recManagefault!!.adapter = manageFaultAdapter
    }*/



}