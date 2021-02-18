package com.commsreport.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.databinding.ItemManageSitesBinding
import com.commsreport.model.AddUserResponse


import com.commsreport.model.SiteListModel
import com.commsreport.screens.fragments.editsite.EditSiteFragment
import com.commsreport.screens.fragments.managesite.ManageSiteFragment
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

class ManageSiteAdapter(
    val activity: HomeActivity,
    val siteList: ArrayList<SiteListModel.RowList>,
    val  manageSiteFragment: ManageSiteFragment
): RecyclerView.Adapter<ManageSiteAdapter.ViewHolder>() {
    var  itemView: ItemManageSitesBinding?=null
    class ViewHolder(val  itemView: ItemManageSitesBinding) : RecyclerView.ViewHolder(itemView!!.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemManageSitesBinding.inflate(inflater)
        itemView=binding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemView!!.tvNoImage.setTypeface(CustomTypeface.getWhitniBold(activity))
        itemView!!.tvCompanyname.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemView!!.tvAdddress.setTypeface(CustomTypeface.getRajdhaniRegular(activity))

        itemView!!.tvCompanyname.setText(siteList.get(position).site_name)
        itemView!!.tvcountryname.setText(siteList.get(position).country_name)
        itemView!!.tvAdddress.setText(siteList.get(position).site_address)
        if (siteList.get(position).country_flag_path!=null) {
            Glide.with(activity)
                .load(siteList.get(position).country_flag_path)
                .into(itemView!!.imgFlag);
        }
        if (siteList.get(position).company_logo_path.equals("") || siteList.get(position).company_logo_path==null){
            itemView!!.tvNoImage!!.visibility=View.VISIBLE
            itemView!!.imgCompany!!.visibility=View.GONE
        }else {
            itemView!!.tvNoImage!!.visibility=View.GONE
            itemView!!.imgCompany!!.visibility=View.VISIBLE
            if (siteList.get(position).site_logo_path!=null) {
                Glide.with(activity)
                    .load(siteList.get(position).site_logo_path)
                    .into(itemView!!.imgCompany);
            }
        }
        if (siteList.get(position).status.equals("ACTIVE")){
            itemView!!.imgFlagStatus.setBackgroundResource(R.drawable.active)
        }else
            itemView!!.imgFlagStatus.setBackgroundResource(R.drawable.inactive)

        itemView!!.rledit.setOnClickListener {
              AppSheardPreference(activity).setSiteDetails(PreferenceConstent.siteDetails,siteList.get(position))
              activity.openFragment(EditSiteFragment())
        }

        itemView!!.rlDelete.setOnClickListener {
            val alertDialog = Dialog(activity!!, R.style.Transparent)
            /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
            alertDialog.setMessage(message)*/
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view: View = LayoutInflater.from(activity).inflate(R.layout.alert_layout_yesno, null)
            alertDialog.setContentView(view)
            alertDialog.setCancelable(false)
            val tv_message: TextView = view.findViewById(R.id.tv_message)
            val tv_ok: TextView = view.findViewById(R.id.tv_ok)
            val tv_no: TextView = view.findViewById(R.id.tv_no)
            tv_ok.typeface = CustomTypeface.getRajdhaniMedium(activity)
            tv_no.typeface = CustomTypeface.getRajdhaniMedium(activity)
            tv_message.typeface = CustomTypeface.getRajdhaniRegular(activity)
            tv_ok.setOnClickListener {
                alertDialog.dismiss()
                callApiforCloseFault(siteList.get(position),position)
            }
            tv_no.setOnClickListener {
                alertDialog.dismiss()

            }
            tv_message.setText("Are you want to delete this site?")
            alertDialog.show()
        }

    }

    private fun callApiforCloseFault(siteList1: SiteListModel.RowList, position: Int) {
        var userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()

            paramObject.put("site_id",siteList1.id)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApifordeletesite(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(call: Call<AddUserResponse>, response: Response<AddUserResponse>) {
                    customProgress.hideProgress()
                    if(response.code()==200) {
                        if(response.body()!!.status){
                            ToastAlert.CustomToastSuccess(activity,"Site delete Successfully")
                            //  Alert.showalert(activity!!,"Fault Close Successfully")
                            manageSiteFragment.siteList.removeAt(position)
                            notifyDataSetChanged()
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

    override fun getItemCount(): Int {
        return  siteList.size;
    }
}