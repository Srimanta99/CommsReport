package com.commsreport.adapter

import android.app.Dialog
import android.content.Intent
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
import com.commsreport.databinding.ItemManageFaultsBinding

import com.commsreport.model.AddUserResponse

import com.commsreport.model.FaultListModel
import com.commsreport.screens.fragments.faults.FaultFragment
import com.commsreport.screens.home.HomeActivity
import com.commsreport.screens.login.LoginActivity
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

class ManageFaultAdapter(
    val activity: HomeActivity?,
    val faultList: ArrayList<FaultListModel.FaultList>,
    val faultFragment: FaultFragment
) : RecyclerView.Adapter<ManageFaultAdapter.ViewHolder>() {
    var itemManageFaultsBinding: ItemManageFaultsBinding?=null
    class ViewHolder(itemView:  ItemManageFaultsBinding) : RecyclerView.ViewHolder(itemView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflate=LayoutInflater.from(parent.context)
        val viewbind=ItemManageFaultsBinding.inflate(inflate)
        itemManageFaultsBinding=viewbind
        return ViewHolder(viewbind)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemManageFaultsBinding!!.faultDecription.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemManageFaultsBinding!!.tvFaultdescription.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemManageFaultsBinding!!.tvClose.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemManageFaultsBinding!!.tvFaultdate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemManageFaultsBinding!!.faultdate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemManageFaultsBinding!!.tvRemove.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemManageFaultsBinding!!.tvFaultdescription.setText(faultList.get(position).fault_description)

        itemManageFaultsBinding!!.tvClose.setOnClickListener {
            closeFaultAlert(faultList.get(position),position)
            //callApiforCloseFault(faultList.get(position),position)
        }
        itemManageFaultsBinding!!.tvRemove.setOnClickListener {
            fixFaultAlert(faultList.get(position),position)
               // callApiforRemoveFault(faultList.get(position),position);
        }


        try {
            if (faultList.get(position).created_at!=null){
                itemManageFaultsBinding!!.tvFaultdate.setText(faultList.get(position).created_at.split(" ")[0])
            }
            if (faultList.get(position).fault_files.size==1){
                itemManageFaultsBinding!!.imgFault1.visibility= View.VISIBLE
                Glide.with(activity)
                    .load(faultList.get(position).fault_files[0])
                    .into( itemManageFaultsBinding!!.imgFault1);
            }
            if (faultList.get(position).fault_files.size==1){
                Glide.with(activity)
                    .load(faultList.get(position).fault_files[0])
                    .into( itemManageFaultsBinding!!.imgFault1);
                Glide.with(activity)
                    .load(faultList.get(position).fault_files[1])
                    .into( itemManageFaultsBinding!!.imgFault2);
            }
            if (faultList.get(position).fault_files.size==3){
                Glide.with(activity)
                    .load(faultList.get(position).fault_files[0])
                    .into( itemManageFaultsBinding!!.imgFault1);
                Glide.with(activity)
                    .load(faultList.get(position).fault_files[1])
                    .into( itemManageFaultsBinding!!.imgFault2);
                Glide.with(activity)
                    .load(faultList.get(position).fault_files[2])
                    .into( itemManageFaultsBinding!!.imgFault3);


            }
            if (faultList.get(position).fault_files.size==4){
                Glide.with(activity)
                    .load(faultList.get(position).fault_files[0])
                    .into( itemManageFaultsBinding!!.imgFault1);
                Glide.with(activity)
                    .load(faultList.get(position).fault_files[1])
                    .into( itemManageFaultsBinding!!.imgFault2);
                Glide.with(activity)
                    .load(faultList.get(position).fault_files[2])
                    .into( itemManageFaultsBinding!!.imgFault3);
                Glide.with(activity)
                    .load(faultList.get(position).fault_files[3])
                    .into( itemManageFaultsBinding!!.imgFault4);
                /*  itemManageFaultsBinding!!.imgFault1.visibility= View.VISIBLE
                  itemManageFaultsBinding!!.imgFault2.visibility= View.VISIBLE
                  itemManageFaultsBinding!!.imgFault3.visibility= View.VISIBLE
                  itemManageFaultsBinding!!.imgFault4.visibility= View.VISIBLE*/
            }

        }catch (e:Exception){
            e.printStackTrace()
        }

    }

    private fun callApiforRemoveFault(fault: FaultListModel.FaultList,position: Int) {
        var userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("checks_process_fault_id", fault.id)
            paramObject.put("work_type", "repair")
            paramObject.put("fault_status_id", "4")
            paramObject.put("site_id",faultFragment.selectedSiteId)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApiforFaultRepair(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(call: Call<AddUserResponse>, response: Response<AddUserResponse>) {
                    customProgress.hideProgress()
                    if(response.code()==200) {
                        if(response.body()!!.status){
                            ToastAlert.CustomToastSuccess(activity!!,"Fault Remove Successfully")
                            faultFragment.faultList.removeAt(position)
                            faultFragment.setAdpterValue()
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

    private fun callApiforCloseFault(fault: FaultListModel.FaultList, position: Int) {
        var userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("checks_process_fault_id", fault.id)
            paramObject.put("work_type", "repair")
            paramObject.put("fault_status_id", "6")
            paramObject.put("site_id",faultFragment.selectedSiteId)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApiforFaultRepair(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(call: Call<AddUserResponse>, response: Response<AddUserResponse>) {
                    customProgress.hideProgress()
                    if(response.code()==200) {
                        if(response.body()!!.status){
                            ToastAlert.CustomToastSuccess(activity,"Fault Close Successfully")
                          //  Alert.showalert(activity!!,"Fault Close Successfully")
                            faultFragment.faultList.removeAt(position)
                            faultFragment.setAdpterValue()
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
       return  faultList.size
    }

    fun  closeFaultAlert(fault: FaultListModel.FaultList, position: Int) {
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
            callApiforCloseFault(faultList.get(position),position)
        }
        tv_no.setOnClickListener {
            alertDialog.dismiss()

        }
        tv_message.setText("Are you want to close this fault?")
        alertDialog.show()
        /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        alertDialog.show()*/

    }

    fun fixFaultAlert(fault: FaultListModel.FaultList, position: Int) {
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
            callApiforRemoveFault(faultList.get(position),position);
        }
        tv_no.setOnClickListener {
            alertDialog.dismiss()

        }
        tv_message.setText("Are you want to fix this fault?")
        alertDialog.show()
        /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        alertDialog.show()*/

    }
}