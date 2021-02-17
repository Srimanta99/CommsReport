package com.commsreport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ItemManageEmailBinding
import com.commsreport.model.EmailListModel
import com.commsreport.screens.home.HomeActivity

class ManageEmailAdapter(
    val activity: HomeActivity,
   var  emailList: ArrayList<EmailListModel.EmailRow>, ): RecyclerView.Adapter<ManageEmailAdapter.ViewHolder>() {
    var  itemView: ItemManageEmailBinding?=null
    class ViewHolder(val  itemView: ItemManageEmailBinding) : RecyclerView.ViewHolder(itemView!!.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemManageEmailBinding.inflate(inflater)
        itemView=binding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemView!!.emailname.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvName.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.tvEmail.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.email.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvCompanyname.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.emailname.setText(emailList.get(position).name)
        itemView!!.email.setText(emailList.get(position).email)
        itemView!!.tvCompanyname.setText(emailList.get(position).purpose)

    }

    override fun getItemCount(): Int {
        return  emailList.size;
    }

}