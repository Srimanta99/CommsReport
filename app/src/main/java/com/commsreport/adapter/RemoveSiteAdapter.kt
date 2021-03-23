package com.commsreport.adapter

import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ItemEmailAddBinding
import com.commsreport.databinding.ItemRemoveSiteBinding
import com.commsreport.databinding.ItemRemoveUserBinding
import com.commsreport.model.EmailModel
import com.commsreport.screens.fragments.addemail.AddEmailFragment
import com.commsreport.screens.home.HomeActivity
import com.commsreport.screens.romovesiteuser.RemoveSiteUserActivity

class RemoveSiteAdapter(
    val activity: RemoveSiteUserActivity,
    ): RecyclerView.Adapter<RemoveSiteAdapter.ViewHolder>() {
    var  itemView: ItemRemoveSiteBinding?=null
    class ViewHolder(val  itemView: ItemRemoveSiteBinding) : RecyclerView.ViewHolder(itemView!!.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRemoveSiteBinding.inflate(inflater)
        itemView=binding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
      //  itemView!!.tvCreated.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
      //  itemView!!.tvCreatedDate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
       // itemView!!.tvusename.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.tvUsercount.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
      //  itemView!!.tvSitecount.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))


    }

    override fun getItemCount(): Int {
        return  10;
    }

}