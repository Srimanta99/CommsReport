package com.commsreport.screens.fragments.addemail

import android.os.Bundle
import android.text.Html
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteForEmailAdd
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteForEmailSearch
import com.commsreport.adapter.AddEmailAdapter
import com.commsreport.databinding.FragmentAddEmailBinding
import com.commsreport.model.AddUserResponse
import com.commsreport.model.EmailModel
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SiteListModel
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


class AddEmailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var activity: HomeActivity?=null
    var addEmailBinding:FragmentAddEmailBinding?=null
    var addemaillist=ArrayList<EmailModel>()
    var addEmailAdapter:AddEmailAdapter?=null
    var siteList= java.util.ArrayList<SiteListModel.RowList>()
    var userdata: LoginResponseModel.Userdata?=null
    var selectedSite=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2) }
        activity=  getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        addEmailBinding= FragmentAddEmailBinding.inflate(inflater, container, false)
        return addEmailBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEmailBinding!!.tvNoteEmail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        val text="<font color=#FE0100>Note: </font> <font color=#1E3F6C>Please enter the email address that you want to notify you once you submit it</font>";
        addEmailBinding!!.tvNoteEmail.setText(Html.fromHtml(text))
        val textnote="<font color=#FE0100>Note: </font> <font color=#1E3F6C>[*] feilds are all mendatory feild</font>";
        addEmailBinding!!.tvNote.setText(Html.fromHtml(textnote))

        addEmailBinding!!.emailAdd!!.etName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        addEmailBinding!!.emailAdd!!.tvName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        addEmailBinding!!.emailAdd!!.etEmail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        addEmailBinding!!.emailAdd!!.tvEmail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        addEmailBinding!!.emailAdd!!.tvSite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        addEmailBinding!!.emailAdd!!.tvsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
       /* val emailModel=EmailModel("name","test@gmail.com")
        addemaillist.add(emailModel)
        addEmailAdapter=AddEmailAdapter(activity!!,addemaillist!!,this)
        addEmailBinding!!.recAddItem.adapter=addEmailAdapter*/

        addEmailBinding!!.emailAdd.tvsite.setOnClickListener {
            val customPopUpDialogSiteList= CustomPopUpDialogSiteForEmailAdd(activity,siteList,this)
            customPopUpDialogSiteList!!.show()
        }
        addEmailBinding!!.tvSubmit.setOnClickListener {
           /* for ( i in 0 until  addemaillist.size){
                addemaillist.get(i).name
            }*/
            if (!addEmailBinding!!.emailAdd.etName.text.toString().equals("")){
                if (!addEmailBinding!!.emailAdd.etEmail.text.toString().equals("")){
                    if(!selectedSite.equals(""))
                      callApiforAddEmail()
                    else{
                        ToastAlert.CustomToastwornning(activity!!,"Please Select site.")
                    }
                }else{
                    addEmailBinding!!.emailAdd.etEmail.requestFocus()
                    ToastAlert.CustomToastwornning(activity!!,"Please provide Email.")
                }

            }else{
                addEmailBinding!!.emailAdd.etName.requestFocus()
                ToastAlert.CustomToastwornning(activity!!,"Please provide name.")
            }

        }

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

    private fun callApiforAddEmail() {
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)
            paramObject.put("site_id", selectedSite)
            paramObject.put("name", addEmailBinding!!.emailAdd.etName.text.toString())
            paramObject.put("email", addEmailBinding!!.emailAdd.etEmail.text.toString())
            paramObject.put("purpose", "NW")
            paramObject.put("status_id", "1")

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApiforemailadd(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(call: Call<AddUserResponse>, response: Response<AddUserResponse>) {
                    customProgress.hideProgress()
                    if(response.code()==200) {
                      ToastAlert.CustomToastSuccess(activity!!,response.body()!!.message)
                        activity!!.getSupportFragmentManager().popBackStack();

                    }else if(response.code()==401){
                        Alert.showalertForUnAuthorized(activity!!,"Unauthorized")

                    }
                }

                override fun onFailure(call: Call<AddUserResponse>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Add Emails")
        activity!!.homeBinding!!.mainView.rlheader.visibility=View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddEmailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}