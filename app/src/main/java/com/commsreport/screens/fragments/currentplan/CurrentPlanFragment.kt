package com.commsreport.screens.fragments.currentplan

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.databinding.FragmentCurrentPlanBinding
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SubcriptionDeatilsResponse
import com.commsreport.screens.fragments.subcriptionpackage.SubcriptionPackageFragment
import com.commsreport.screens.home.HomeActivity
import com.commsreport.screens.romovesiteuser.RemoveSiteUserActivity
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
import java.text.SimpleDateFormat
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CurrentPlanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var fragmentCurrentPlanBinding:FragmentCurrentPlanBinding?=null
    var activity:HomeActivity?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        fragmentCurrentPlanBinding= FragmentCurrentPlanBinding.inflate(inflater, container, false)
        return fragmentCurrentPlanBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCurrentPlanBinding!!.tvCurrentplan.setTypeface(
            CustomTypeface.getRajdhaniBold(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvUnlimited.setTypeface(
            CustomTypeface.getRajdhaniSemiBold(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvUnlimitedUser.setTypeface(
            CustomTypeface.getRajdhaniMedium(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvUnlimitedSite.setTypeface(
            CustomTypeface.getRajdhaniMedium(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvtengbp.setTypeface(
            CustomTypeface.getRajdhaniSemiBold(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvtengbppermonth.setTypeface(
            CustomTypeface.getRajdhaniMedium(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvUnlimitedFault.setTypeface(
            CustomTypeface.getRajdhaniMedium(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvUnlimitedDocuments.setTypeface(
            CustomTypeface.getRajdhaniMedium(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvSubcriptiondate.setTypeface(
            CustomTypeface.getRajdhaniMedium(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvLastbillingdate.setTypeface(
            CustomTypeface.getRajdhaniMedium(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvNextBillingDate.setTypeface(
            CustomTypeface.getRajdhaniMedium(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvSubcriptiondateValue.setTypeface(
            CustomTypeface.getRajdhaniSemiBold(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvNextBillingDateVal.setTypeface(
            CustomTypeface.getRajdhaniSemiBold(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvLastBillingDateVal.setTypeface(
            CustomTypeface.getRajdhaniSemiBold(
                activity!!
            )
        )
        fragmentCurrentPlanBinding!!.tvSubmit.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentCurrentPlanBinding!!.tvSubmit.setOnClickListener {
            activity!!.openFragment(SubcriptionPackageFragment())
          //  activity!!.startActivity(Intent(activity,RemoveSiteUserActivity::class.java))
        }

        callApiForSubcriptionList()
    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Payment")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.VISIBLE
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CurrentPlanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    public fun callApiForSubcriptionList() {
        var userdata: LoginResponseModel.Userdata?= AppSheardPreference(activity!!).getUser(
            PreferenceConstent.userData
        )
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("status_id", "1")
            paramObject.put("user_id", userdata!!.user_id)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApiforsubcription(userdata!!.token, gsonObject)
            callApi.enqueue(object : Callback<SubcriptionDeatilsResponse> {
                override fun onResponse(
                    call: Call<SubcriptionDeatilsResponse>,
                    response: Response<SubcriptionDeatilsResponse>
                ) {
                    customProgress.hideProgress()
                    if (response.code() == 200) {
                        if (response.body()!!.status) {
                           // fragmentCurrentPlanBinding!!.rlUnlimited.visibility=View.VISIBLE
                           // fragmentCurrentPlanBinding!!.lldate.visibility=View.VISIBLE

                            val subDetsils = response.body()!!.row
                            AppSheardPreference(activity!!).setvalue_in_preference(PreferenceConstent.selected_packageid,subDetsils.id)
                            fragmentCurrentPlanBinding!!.tvUnlimited.setText(subDetsils.package_name)
                            if (subDetsils.package_price.equals("0")) {
                                fragmentCurrentPlanBinding!!.tvtengbp.visibility = View.GONE
                                fragmentCurrentPlanBinding!!.tvtengbppermonth.visibility = View.GONE

                            } else {
                                fragmentCurrentPlanBinding!!.tvtengbp.setText(subDetsils.package_price + " GBP/")
                            }

                            if (subDetsils.is_user_unlimited.equals("1")) {
                                fragmentCurrentPlanBinding!!.tvUnlimitedUser.setText("Unlimited Users")
                                fragmentCurrentPlanBinding!!.tvUnlimitedSite.setText("Unlimited Sites")
                            } else {
                                fragmentCurrentPlanBinding!!.tvUnlimitedUser.setText(subDetsils.user_count + " Users")
                                fragmentCurrentPlanBinding!!.tvUnlimitedSite.setText(subDetsils.site_count + " Sites")
                            }
                            try {
                                val inputdateFormat = SimpleDateFormat("yyyy-MM-dd")
                                val outdateFormat = SimpleDateFormat("dd, MMMM yyyy")
                                var convertedDate = Date()
                                convertedDate = inputdateFormat.parse(subDetsils.subscription_date)
                                val finalsubDateString: String = outdateFormat.format(convertedDate)
                                val lastbilling: String = outdateFormat.format(inputdateFormat.parse(subDetsils.last_billing_date))
                                val nextbilling: String = outdateFormat.format(inputdateFormat.parse(subDetsils.next_billing_date))
                                fragmentCurrentPlanBinding!!.tvSubcriptiondateValue.setText(finalsubDateString)
                                fragmentCurrentPlanBinding!!.tvLastBillingDateVal.setText(lastbilling)
                                fragmentCurrentPlanBinding!!.tvNextBillingDateVal.setText(nextbilling)
                            }catch (e:Exception){
                                e.printStackTrace()
                            }



                        }

                    } else if (response.code() == 401) {
                        Alert.showalertForUnAuthorized(activity!!, "Unauthorized")

                    }
                }

                override fun onFailure(call: Call<SubcriptionDeatilsResponse>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}