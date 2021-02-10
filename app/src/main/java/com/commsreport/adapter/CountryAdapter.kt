package com.commsreport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commsreport.Utils.FullscreenCountryDialog
import com.commsreport.databinding.CountryListItemBinding
import com.commsreport.model.CountryListModel
import com.commsreport.screens.home.HomeActivity
import com.wecompli.utils.onitemclickinterface.CountryClickInterface

class CountryAdapter(
    val activity: HomeActivity,
    val countrylist: ArrayList<CountryListModel.CountryList>,
    val countryClickInterface: CountryClickInterface?

): RecyclerView.Adapter<CountryAdapter.ViewHolder>() {
    var  itemView: CountryListItemBinding?=null
    class ViewHolder(val  itemView: CountryListItemBinding) : RecyclerView.ViewHolder(itemView!!.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CountryListItemBinding.inflate(inflater)
        itemView=binding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemView!!.tvCountryname.setText(countrylist.get(position).country_name)
        if (countrylist.get(position).country_flag_path!=null) {
            Glide.with(activity)
                .load(countrylist.get(position).country_flag_path)
                .into(itemView!!.imgFlag);
        }

        itemView!!.rlCountry.setOnClickListener {
           // fullscreenCountryDialog.dismiss()
            countryClickInterface!!.OnItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return  countrylist.size;
    }
}