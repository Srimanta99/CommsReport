package com.commsreport.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ItemRemoveSiteBinding
import com.commsreport.model.SiteAndUserModel
import com.commsreport.screens.romovesiteuser.RemoveSiteUserActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class RemoveSiteAdapter(
    val activity: RemoveSiteUserActivity,
    val row_site: ArrayList<SiteAndUserModel.SiteRow>?, ): RecyclerView.Adapter<RemoveSiteAdapter.ViewHolder>() {
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

        itemView!!.tvCreated.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvCreatedDate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvNoImage.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvsitename.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.tvUsercount.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.tvFaultcount.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.tvsitename.setText(row_site!!.get(position).site_name)
        itemView!!.tvUsercount.setText("User "+row_site.get(position).user_count)
        itemView!!.tvFaultcount.setText("Fault "+row_site.get(position).user_count)
        /*itemView!!.llItem.setOnClickListener {
            if (itemView!!.chkSelect.isChecked)
                itemView!!.chkSelect.isChecked=false
            else
                itemView!!.chkSelect.isChecked=true

        }*/
        itemView!!.chkSelect.setOnCheckedChangeListener { compoundButton, b ->

        }

        if(row_site.get(position).site_logo_path!=null){
            itemView!!.tvNoImage.visibility= View.GONE
            itemView!!.imgCompany.visibility=View.VISIBLE
            Glide.with(activity)
                .load(row_site.get(position).site_logo_path)
                .centerCrop()
                .into(itemView!!.imgCompany);
        }
        try {
            val inputdateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            val outdateFormat = SimpleDateFormat("dd, MMMM yyyy")
            var convertedDate = Date()
            convertedDate = inputdateFormat.parse(row_site.get(position).created_at)
            val finalsubDateString: String = outdateFormat.format(convertedDate)
            itemView!!.tvCreatedDate.setText(finalsubDateString)
        }catch (e:Exception)
        {
            e.printStackTrace()
        }

        itemView!!.chkSelect.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
                activity.selectedSite.add(row_site.get(position).id.toString())
            }else{
                activity.selectedSite.remove(row_site.get(position).id.toString())
            }

        }


    }

    override fun getItemCount(): Int {
        return  row_site!!.size;
    }

}