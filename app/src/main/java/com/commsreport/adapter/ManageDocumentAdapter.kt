package com.commsreport.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.databinding.ItemManageDocumentBinding
import com.commsreport.model.AddUserResponse
import com.commsreport.model.DocumentListModel
import com.commsreport.screens.fragments.managedocument.ManageDocumentFragment
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
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ManageDocumentAdapter(
    val activity: HomeActivity,
    val docList: ArrayList<DocumentListModel.DocumentItem>,
    val manageDocumentFragment: ManageDocumentFragment
) : RecyclerView.Adapter<ManageDocumentAdapter.viewHolder>() {
    var itemManageDocumentBinding:ItemManageDocumentBinding?=null
    class viewHolder(itemView: ItemManageDocumentBinding) : RecyclerView.ViewHolder(itemView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): viewHolder {
        val inflater=LayoutInflater.from(parent.context)
        val binding=ItemManageDocumentBinding.inflate(inflater)
        itemManageDocumentBinding=binding
        return  viewHolder(binding!!)

    }

    override fun onBindViewHolder(holder: viewHolder, position: Int) {
        itemManageDocumentBinding!!.tvDocname.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity))

        itemManageDocumentBinding!!.startDate.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity))
        itemManageDocumentBinding!!.endDate.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity))
        itemManageDocumentBinding!!.tvStartDate.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemManageDocumentBinding!!.tvEndDate.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemManageDocumentBinding!!.tvDownload1.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemManageDocumentBinding!!.tvRemove.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemManageDocumentBinding!!.tvDownload2.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemManageDocumentBinding!!.tvDownload3.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemManageDocumentBinding!!.tvDownload4.setTypeface(CustomTypeface.getRajdhaniMedium(activity))

        itemManageDocumentBinding!!.tvDocname.setText(docList.get(position).document_name)
        if (!docList.get(position).affected_date.equals("0000-00-00")) {
            var spf = SimpleDateFormat("yyyy-MM-dd")
            val newDate: Date = spf.parse(docList.get(position).affected_date)
            spf = SimpleDateFormat("dd MMM yyyy")
            var date:String = spf.format(newDate)
            itemManageDocumentBinding!!.tvStartDate.setText(date)
        }
        else
            itemManageDocumentBinding!!.llStartdate.visibility=View.GONE

        if (!docList.get(position).expire_date.equals("0000-00-00")){
            var spf = SimpleDateFormat("yyyy-MM-dd")
            val newDate: Date = spf.parse(docList.get(position).expire_date)
            spf = SimpleDateFormat("dd MMM yyyy")
            var date:String = spf.format(newDate)
            itemManageDocumentBinding!!.tvEndDate.setText(date)
        }


        else
            itemManageDocumentBinding!!.llEnddate.visibility=View.GONE

        if (docList.get(position).affected_date.equals("0000-00-00") && docList.get(position).expire_date.equals("0000-00-00"))
            itemManageDocumentBinding!!.llDate.visibility=View.GONE

        itemManageDocumentBinding!!.tvRemove.setOnClickListener {
            documentClose(docList.get(position).id, position)
         //   callApiforRemoveDocument(docList.get(position).id, position)
        }
        if(docList.get(position).document_file.size==1){
            itemManageDocumentBinding!!.rlDownload1.visibility=View.VISIBLE
            itemManageDocumentBinding!!.llDoc1.visibility=View.VISIBLE
            itemManageDocumentBinding!!.llDoc2.visibility=View.GONE
        }
        if(docList.get(position).document_file.size==2){
            itemManageDocumentBinding!!.rlDownload1.visibility=View.VISIBLE
            itemManageDocumentBinding!!.rlDownload2.visibility=View.VISIBLE
            itemManageDocumentBinding!!.llDoc2.visibility=View.GONE
            itemManageDocumentBinding!!.llDoc1.visibility=View.VISIBLE

        }
        if(docList.get(position).document_file.size==3){
            itemManageDocumentBinding!!.rlDownload1.visibility=View.VISIBLE
            itemManageDocumentBinding!!.rlDownload2.visibility=View.VISIBLE
            itemManageDocumentBinding!!.rlDownload3.visibility=View.VISIBLE
            itemManageDocumentBinding!!.llDoc2.visibility=View.VISIBLE

        }
        if(docList.get(position).document_file.size==4){
            itemManageDocumentBinding!!.rlDownload1.visibility=View.VISIBLE
            itemManageDocumentBinding!!.rlDownload2.visibility=View.VISIBLE
            itemManageDocumentBinding!!.rlDownload3.visibility=View.VISIBLE
            itemManageDocumentBinding!!.rlDownload4.visibility=View.VISIBLE
            itemManageDocumentBinding!!.llDoc2.visibility=View.VISIBLE
        }

        itemManageDocumentBinding!!.rlDownload1.setOnClickListener {
                if (manageDocumentFragment.checkpermession()) { // for (i in 0 until docList.get(position).document_file.size) {
                    val bits: List<String> = docList.get(position).document_file[0].split("/")
                    manageDocumentFragment.downloadfromUrl(docList.get(position).document_file[0], bits.get(bits.size - 1))
                }

        }
        itemManageDocumentBinding!!.rlDownload2.setOnClickListener {
                if (manageDocumentFragment.checkpermession()) { // for (i in 0 until docList.get(position).document_file.size) {
                    val bits: List<String> = docList.get(position).document_file[1].split("/")
                    manageDocumentFragment.downloadfromUrl(docList.get(position).document_file[1], bits.get(bits.size - 1))
                    // }
                }
        }
        itemManageDocumentBinding!!.rlDownload3.setOnClickListener {
            if (manageDocumentFragment.checkpermession()) { // for (i in 0 until docList.get(position).document_file.size) {
                val bits: List<String> = docList.get(position).document_file[2].split("/")
                manageDocumentFragment.downloadfromUrl(docList.get(position).document_file[2], bits.get(bits.size - 1))
                // }
            }
        }
        itemManageDocumentBinding!!.rlDownload4.setOnClickListener {
            if (manageDocumentFragment.checkpermession()) { // for (i in 0 until docList.get(position).document_file.size) {
                val bits: List<String> = docList.get(position).document_file[3].split("/")
                manageDocumentFragment.downloadfromUrl(docList.get(position).document_file[3], bits.get(bits.size - 1))
                // }
            }
        }
    }

    override fun getItemCount(): Int {
       return  docList.size
    }

    private fun callApiforRemoveDocument(fault_id: String, position: Int) {
        var userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("document_id", fault_id)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApifordocumentremove(userdata!!.token, gsonObject)
            callApi.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(
                    call: Call<AddUserResponse>,
                    response: Response<AddUserResponse>
                ) {
                    customProgress.hideProgress()
                    if (response.code() == 200) {
                        if (response.body()!!.status) {
                            ToastAlert.CustomToastSuccess(activity!!,"Document remove successfully")
                            manageDocumentFragment.docList.removeAt(position)
                            manageDocumentFragment.setAdapter()
                        }

                    } else if (response.code() == 401) {
                        Alert.showalertForUnAuthorized(activity!!, "Unauthorized")

                    }
                }

                override fun onFailure(call: Call<AddUserResponse>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    fun documentClose(id: String, position: Int) {
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
            callApiforRemoveDocument(docList.get(position).id, position)
        }
        tv_no.setOnClickListener {
            alertDialog.dismiss()

        }
        tv_message.setText("Are you want to remove this document?")
        alertDialog.show()
    }

}