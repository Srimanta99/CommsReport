package com.commsreport.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ItemPackageBinding
import com.commsreport.model.SubCriptionPackagResponseemodel
import com.commsreport.screens.fragments.subcriptionpackage.SubcriptionPackageFragment
import com.commsreport.screens.home.HomeActivity
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent


class SubcriptionAdapter(
    val activity: HomeActivity,
    val subcriptionPackageFragment: SubcriptionPackageFragment,
    var subCriptionPackagemodelList: ArrayList<SubCriptionPackagResponseemodel.PackageItem>,
): RecyclerView.Adapter<SubcriptionAdapter.ViewHolder>() {
    var  itemView: ItemPackageBinding?=null
    class ViewHolder(val itemView: ItemPackageBinding) : RecyclerView.ViewHolder(itemView!!.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemPackageBinding.inflate(inflater)
        itemView=binding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemView!!.tvUnlimited.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity))
        itemView!!.tvUnlimitedUser.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemView!!.tvUnlimitedSite.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemView!!.tvtengbp.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.tvtengbppermonth.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvUnlimitedDocuments.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemView!!.tvUnlimitedFault.setTypeface(CustomTypeface.getRajdhaniMedium(activity))

        itemView!!.tvUnlimited.setText(subCriptionPackagemodelList.get(position).package_name)
        if(subCriptionPackagemodelList.get(position).package_price.equals("0")){
            itemView!!.tvtengbp.visibility=View.GONE
            itemView!!.tvtengbppermonth.visibility=View.GONE

        }else{
            itemView!!.tvtengbp.setText(subCriptionPackagemodelList.get(position).package_price+" GBP/")
        }

        if (subCriptionPackagemodelList.get(position).is_user_unlimited.equals("1")){
            itemView!!.tvUnlimitedUser.setText("Unlimited Users")
            itemView!!.tvUnlimitedSite.setText("Unlimited Sites")
        }else {
            itemView!!.tvUnlimitedUser.setText(subCriptionPackagemodelList.get(position).user_count + " Users")
            itemView!!.tvUnlimitedSite.setText(subCriptionPackagemodelList.get(position).site_count + " Sites")
        }

        itemView!!.rlUnlimited.setOnClickListener {

                for(i in 0 until  subCriptionPackagemodelList!!.size) {
                    if (i == position) {
                        subcriptionPackageFragment.subCriptionPackagemodelList!!.get(i).ischeck = true

                    }
                    else {
                        subcriptionPackageFragment.subCriptionPackagemodelList!!.get(i).ischeck = false
                    }
                }
              subcriptionPackageFragment.calladapter(position)
        }

        if (subCriptionPackagemodelList!!.get(position).ischeck){
            subcriptionPackageFragment.selectedPackage=subCriptionPackagemodelList!!.get(position);
            itemView!!.chkUnlimitedSelect.isChecked=true
            itemView!!.rlUnlimited.setBackgroundResource(R.drawable.selected)
            itemView!!.tvUnlimited.setTextColor(activity!!.resources.getColor(R.color.white))
            itemView!!.tvUnlimitedUser.setBackgroundResource(R.drawable.rectangular_rounded_shape_selecteduser_site)
            itemView!!.tvUnlimitedUser.setTextColor(activity!!.resources.getColor(R.color.white))
            itemView!!.tvUnlimitedSite.setBackgroundResource(R.drawable.rectangular_rounded_shape_selecteduser_site)
            itemView!!.tvUnlimitedSite.setTextColor(activity!!.resources.getColor(R.color.white))
            itemView!!.tvtengbp.setTextColor(activity!!.resources.getColor(R.color.white))
            itemView!!.tvtengbppermonth.setTextColor(activity!!.resources.getColor(R.color.white))
        }else{
            itemView!!.chkUnlimitedSelect.isChecked=false
            itemView!!.rlUnlimited.setBackgroundResource(R.drawable.paymentbg)
            itemView!!.tvUnlimited.setTextColor(activity!!.resources.getColor(R.color.packtextcolor))
            itemView!!.tvUnlimitedUser.setBackgroundResource(R.drawable.rectangular_rounded_shape_use_site)
            itemView!!.tvUnlimitedSite.setBackgroundResource(R.drawable.rectangular_rounded_shape_use_site)
            itemView!!.tvUnlimitedUser.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
            itemView!!.tvUnlimitedSite.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
            itemView!!.tvtengbp.setTextColor(activity!!.resources.getColor(R.color.black))
            itemView!!.tvtengbppermonth.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
        }
        itemView!!.chkUnlimitedSelect.setOnCheckedChangeListener { compoundButton, b ->

            if(b){
                for(i in 0 until  subCriptionPackagemodelList!!.size) {
                    if (i == position) {
                        subCriptionPackagemodelList!!.get(i).ischeck = true
                    }
                    else {
                        subCriptionPackagemodelList!!.get(i).ischeck = false
                    }
                }
                subcriptionPackageFragment.calladapter(position)

            }else{
                subCriptionPackagemodelList!!.get(position).ischeck=false
                subcriptionPackageFragment.calladapter(position)
            }
        }
       /* if (AppSheardPreference(activity).getvalue_in_preference(PreferenceConstent.selected_packageid).equals(subCriptionPackagemodelList!!.get(position).id)){
            subcriptionPackageFragment.selectedPackage=subCriptionPackagemodelList!!.get(position);
            itemView!!.chkUnlimitedSelect.isChecked=true
            itemView!!.rlUnlimited.setBackgroundResource(R.drawable.selected)
            itemView!!.tvUnlimited.setTextColor(activity!!.resources.getColor(R.color.white))
            itemView!!.tvUnlimitedUser.setBackgroundResource(R.drawable.rectangular_rounded_shape_selecteduser_site)
            itemView!!.tvUnlimitedUser.setTextColor(activity!!.resources.getColor(R.color.white))
            itemView!!.tvUnlimitedSite.setBackgroundResource(R.drawable.rectangular_rounded_shape_selecteduser_site)
            itemView!!.tvUnlimitedSite.setTextColor(activity!!.resources.getColor(R.color.white))
            itemView!!.tvtengbp.setTextColor(activity!!.resources.getColor(R.color.white))
            itemView!!.tvtengbppermonth.setTextColor(activity!!.resources.getColor(R.color.white))
        }*/

    }

    override fun getItemCount(): Int {
        return  subCriptionPackagemodelList!!.size;
    }

}