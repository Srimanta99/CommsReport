package com.commsreport.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ItemManageSitesBinding


import com.commsreport.model.SiteListModel
import com.commsreport.screens.fragments.editsite.EditSiteFragment
import com.commsreport.screens.home.HomeActivity
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent

class ManageSiteAdapter(val activity: HomeActivity, val siteList: ArrayList<SiteListModel.RowList>): RecyclerView.Adapter<ManageSiteAdapter.ViewHolder>() {
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
        itemView!!.tvAdddress.setText(siteList.get(position).site_address)
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
        itemView!!.imgEdit.setOnClickListener {
              AppSheardPreference(activity).setSiteDetails(PreferenceConstent.siteDetails,siteList.get(position))
              activity.openFragment(EditSiteFragment())
        }

    }

    override fun getItemCount(): Int {
        return  siteList.size;
    }
}