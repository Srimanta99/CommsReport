package com.commsreport.screens.fragments.subcriptionpackage

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.adapter.SubcriptionAdapter
import com.commsreport.databinding.FragmentSubcriptionPackageBinding
import com.commsreport.model.AddUserResponse
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SiteListModel
import com.commsreport.model.SubCriptionPackagResponseemodel
import com.commsreport.screens.home.HomeActivity
import com.commsreport.screens.subcription.PackageActivity
import com.commsreport.screens.thankyoupage.ThankyouActivity
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

class SubcriptionPackageFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var homeActivity:HomeActivity?=null
    var subcriptionAdapter:SubcriptionAdapter?=null
    var fragmentSubcriptionPackageBinding: FragmentSubcriptionPackageBinding?=null
    var subCriptionPackagemodelList=ArrayList<SubCriptionPackagResponseemodel.PackageItem>()
    public var selectedPackage: SubCriptionPackagResponseemodel.PackageItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        homeActivity=activity as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentSubcriptionPackageBinding=FragmentSubcriptionPackageBinding.inflate(inflater,container,false)
        return fragmentSubcriptionPackageBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callApiForSubcriptionList()
     //   subcriptionAdapter= SubcriptionAdapter(homeActivity!!,this,subCriptionPackagemodelList)
      //  fragmentSubcriptionPackageBinding!!.recSubcription.adapter=subcriptionAdapter
        fragmentSubcriptionPackageBinding!!.tvSubmit.setTypeface(CustomTypeface.getRajdhaniMedium(homeActivity!!))
         fragmentSubcriptionPackageBinding!!.tvSubmit.setOnClickListener {
             if(selectedPackage!=null) {

                 if (AppSheardPreference(activity!!).getvalue_in_preference(PreferenceConstent.selected_packageid).equals(selectedPackage!!.id)){
                     ToastAlert.CustomToastwornning(homeActivity!!,"Please select different package.")
                 }else {
                     AppSheardPreference(activity!!).setvalue_in_preference(PreferenceConstent.selected_packageid,selectedPackage!!.id)
                     if (selectedPackage!!.package_price.equals("0")) {
                         callApiforUpdatePackage()

                     } else {
                         val intent = Intent(homeActivity!!, PackageActivity::class.java)
                         intent.putExtra("package", selectedPackage)
                         startActivity(intent)
                     }
                 }
             }else{
                 ToastAlert.CustomToastwornning(homeActivity!!,"Please select package.")
             }
         }
    }

    private fun callApiforUpdatePackage() {
        var userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("user_id",userdata!!.user_id)
            paramObject.put("subscription_package_id",selectedPackage!!.id)
            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApiforfreesubcription(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(call: Call<AddUserResponse>, response: Response<AddUserResponse>) {
                    customProgress.hideProgress()
                    if(response.code()==200) {
                        if(response.body()!!.status){
                            ToastAlert.CustomToastSuccess(activity!!,response!!.body()!!.message)
                            val intent = Intent(homeActivity!!, ThankyouActivity::class.java)
                            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(intent)
                        }

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
        homeActivity!!.homeBinding!!.mainView!!.tvHeaderText.setText("PACKAGE")
        homeActivity!!.homeBinding!!.mainView!!.rlheader.visibility=View.VISIBLE
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SubcriptionPackageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    public  fun calladapter(pos:Int){
        subcriptionAdapter= SubcriptionAdapter(homeActivity!!,this,subCriptionPackagemodelList)
        fragmentSubcriptionPackageBinding!!.recSubcription.adapter=subcriptionAdapter
      //  fragmentSubcriptionPackageBinding!!.recSubcription.scrollToPosition(pos)
    }

    public fun callApiForSubcriptionList() {
        var userdata: LoginResponseModel.Userdata?= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("status_id", "1")

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApiforSubPackage(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<SubCriptionPackagResponseemodel> {
                override fun onResponse(call: Call<SubCriptionPackagResponseemodel>, response: Response<SubCriptionPackagResponseemodel>) {
                    customProgress.hideProgress()
                    if(response.code()==200) {
                    subCriptionPackagemodelList=response.body()!!.row
                        for (i in 0 until subCriptionPackagemodelList.size){
                            if (AppSheardPreference(activity!!).getvalue_in_preference(PreferenceConstent.selected_packageid).equals(subCriptionPackagemodelList!!.get(i).id)){
                                subCriptionPackagemodelList.get(i).ischeck=true
                            }
                        }
                        subcriptionAdapter= SubcriptionAdapter(homeActivity!!,this@SubcriptionPackageFragment,subCriptionPackagemodelList)
                        fragmentSubcriptionPackageBinding!!.recSubcription.adapter=subcriptionAdapter
                    }else if(response.code()==401){
                        Alert.showalertForUnAuthorized(activity!!,"Unauthorized")

                    }
                }

                override fun onFailure(call: Call<SubCriptionPackagResponseemodel>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}