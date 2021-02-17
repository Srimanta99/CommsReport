package com.commsreport.screens.fragments.manageemail

import android.os.Bundle
import android.text.Html
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteForEmailSearch
import com.commsreport.adapter.ManageEmailAdapter
import com.commsreport.databinding.ContentManageEmailBinding
import com.commsreport.databinding.FragmentEmailBinding
import com.commsreport.model.EmailListModel
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SiteListModel
import com.commsreport.screens.fragments.addemail.AddEmailFragment
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

/**
 * A simple [Fragment] subclass.
 * Use the [EmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var activity:HomeActivity?=null
    var emailBinding: FragmentEmailBinding?=null
    var manageEmailAdapter:ManageEmailAdapter?=null
    var contentManageEmailBinding: ContentManageEmailBinding?=null
    var emailList:ArrayList<EmailListModel.EmailRow>?=null
    var siteList= java.util.ArrayList<SiteListModel.RowList>()
    var userdata:LoginResponseModel.Userdata?=null
    var selectedSite=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        emailBinding= FragmentEmailBinding.inflate(inflater,container,false)
        contentManageEmailBinding=emailBinding!!.contentEmail
        return emailBinding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contentManageEmailBinding!!.tvAddemail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        contentManageEmailBinding!!.tvNote.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        contentManageEmailBinding!!.tvHeaderText.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))
        emailBinding!!.navEmailSearch.tvSearch.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        emailBinding!!.navEmailSearch.tvSitename.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        emailBinding!!.navEmailSearch.tvsherchName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        emailBinding!!.navEmailSearch.Searchsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))

       // emailBinding!!.contentEmail.recManageemail.visibility=View.GONE
       // emailBinding!!.contentEmail.tvNote.visibility=View.GONE

        val textnote="<font color=#FE0100>Note: </font> <font color=#1E3F6C>This email are user to notify who emails</font>";
        contentManageEmailBinding!!.tvNote.setText(Html.fromHtml(textnote))
         userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        contentManageEmailBinding!!.tvAddemail.setOnClickListener {
            activity!!.openFragment(AddEmailFragment())
        }

        contentManageEmailBinding!!.imgMenu.setOnClickListener {
            activity!!.homeBinding!!.drawerLayout.openDrawer(Gravity.LEFT)
        }

        emailBinding!!.imgNavopen.setOnClickListener {
            emailBinding!!.drawerLayout!!.closeDrawer(Gravity.RIGHT)
        }

        emailBinding!!.contentEmail.imgSearch.setOnClickListener {
            emailBinding!!.drawerLayout!!.openDrawer(Gravity.RIGHT)
        }
        emailBinding!!.navEmailSearch.Searchsite.setOnClickListener {
            callApiforEmaillist(selectedSite)
            emailBinding!!.drawerLayout!!.closeDrawer(Gravity.RIGHT)
        }
        emailBinding!!.navEmailSearch.tvsherchName.setOnClickListener {
            val customPopUpDialogSiteList= CustomPopUpDialogSiteForEmailSearch(activity,siteList,this)
            customPopUpDialogSiteList!!.show()
        }

      //  callApiforEmaillist()
        callApiForSiteList()

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
    public fun callApiforEmaillist(id: String) {
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)
            paramObject.put("site_id",id)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApiforemaillist(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<EmailListModel> {
                override fun onResponse(call: Call<EmailListModel>, response: Response<EmailListModel>) {
                    customProgress.hideProgress()

                    if(response.code()==200) {
                        if (response.body()!!.row.size>0) {
                            emailList = response!!.body()!!.row
                            emailBinding!!.contentEmail.recManageemail.visibility=View.VISIBLE
                            emailBinding!!.contentEmail.tvNote.visibility=View.VISIBLE
                            emailBinding!!.contentEmail.noData.visibility=View.GONE
                            manageEmailAdapter= ManageEmailAdapter(activity!!,emailList!!)
                            contentManageEmailBinding!!.recManageemail.adapter=manageEmailAdapter

                        }else
                            ToastAlert.CustomToasterror(activity!!,response!!.body()!!.message)

                    }else if(response.code()==401){
                        Alert.showalertForUnAuthorized(activity!!,"Unauthorized")

                    }
                }

                override fun onFailure(call: Call<EmailListModel>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView!!.tvHeaderText.setText("Email")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.GONE
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}