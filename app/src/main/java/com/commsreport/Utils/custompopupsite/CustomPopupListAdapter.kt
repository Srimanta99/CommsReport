package com.commsreport.Utils.custompopupsite

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.model.SiteListModel
import com.commsreport.screens.home.HomeActivity
import com.wecompli.utils.onitemclickinterface.OnItemClickInterface

class CustomPopupListAdapter(
    val homeActivity: HomeActivity,
    var siteList: List<SiteListModel.RowList>,
    val onItemClickInterface: OnItemClickInterface
) : RecyclerView.Adapter<CustomPopupListAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater.from(homeActivity).inflate(R.layout.site_list_item,null)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return siteList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.textView!!.text=siteList.get(position).site_name
        holder.textView!!.setTypeface(CustomTypeface.getwhitMedium(homeActivity))
        holder.rl_sitename!!.setOnClickListener {
            onItemClickInterface.OnItemClick(position)
        }

    }


    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
         var textView: TextView?=itemView.findViewById(R.id.tv_site)
          var rl_sitename:RelativeLayout?=itemView.findViewById(R.id.rl_sitename)

    }

    fun filterList(filterdNames: List<SiteListModel.RowList>) {
        this.siteList = filterdNames
        notifyDataSetChanged()
    }
}
