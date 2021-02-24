package com.commsreport.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ItemSiteListWithFaultCountBinding
import com.commsreport.model.SiteListModel
import com.commsreport.screens.fragments.faults.FaultFragment
import com.commsreport.screens.home.HomeActivity
import com.wecompli.utils.onitemclickinterface.OnItemClickInterface

class SiteFaultAdapter(
    val activity: HomeActivity,
    val sitelist: ArrayList<SiteListModel.RowList>,
    val faultFragment: FaultFragment,
    val onitemClick: OnItemClickInterface, ): RecyclerView.Adapter<SiteFaultAdapter.ViewHolder>() {
    var  itemView: ItemSiteListWithFaultCountBinding?=null
    class ViewHolder(val  itemView: ItemSiteListWithFaultCountBinding) : RecyclerView.ViewHolder(itemView!!.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemSiteListWithFaultCountBinding.inflate(inflater)
        itemView=binding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemView!!.tvFaultcount.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvSitename.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.tvFaultcountVal.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))
        itemView!!.tvSitename!!.setText(sitelist.get(position).site_name)
        itemView!!.tvFaultcountVal!!.setText(padnumber(sitelist.get(position).fault_count).toString())
        itemView!!.rlFault.setOnClickListener {
            onitemClick.OnItemClick(position)
        }

    }

    override fun getItemCount(): Int {
        return  sitelist.size;
    }

    fun padnumber(n: Int): String {
        val num: String
        if (n > 10 || n == 10)
            num = n.toString()
        else
            num = "0$n"
        return num
    }

}