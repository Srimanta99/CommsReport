package com.commsreport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ItemNotifyBinding
import com.commsreport.model.EmailListModel
import com.commsreport.screens.fragments.reportfault.ReportFaultFragment
import com.commsreport.screens.home.HomeActivity

class NotifyEmailAdapter(
    val activity: HomeActivity,
    val emailList: ArrayList<EmailListModel.EmailRow>,
    val reportFaultFragment: ReportFaultFragment, ): RecyclerView.Adapter<NotifyEmailAdapter.ViewHolder>() {
    var  itemView: ItemNotifyBinding?=null
    class ViewHolder(val  itemView: ItemNotifyBinding) : RecyclerView.ViewHolder(itemView!!.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemNotifyBinding.inflate(inflater)
        itemView=binding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemView!!.chkEmail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.chkEmail.setText(emailList.get(position).email)
        itemView!!.chkEmail.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                reportFaultFragment.selectedEmail.add(emailList.get(position).email)
            }else
                reportFaultFragment.selectedEmail.remove(emailList.get(position).email)

        }
    }

    override fun getItemCount(): Int {
        return  emailList.size;
    }

}