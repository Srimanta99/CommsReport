package com.commsreport.screens.fragments.subcriptionpackage

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.FragmentPackageBinding


import com.commsreport.screens.home.HomeActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class PackageFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var homeActivity:HomeActivity?=null
    var packageBinding: FragmentPackageBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        homeActivity=activity as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        packageBinding =FragmentPackageBinding.inflate(inflater,container,false)
        return packageBinding!!.root;
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settypeface();
        packageBinding!!.rlFree.setOnClickListener {
            if(packageBinding!!.chkFreeSelect.isChecked){
                packageBinding!!.chkFreeSelect.isChecked=false
            }else {
                packageBinding!!.chkFreeSelect.isChecked = true
                packageBinding!!.chkPremiumSelect.isChecked = false
                packageBinding!!.chkUnlimitedSelect.isChecked = false
            }
        }
         packageBinding!!.rlPremium.setOnClickListener {
            if(packageBinding!!.chkPremiumSelect.isChecked){
                packageBinding!!.chkPremiumSelect.isChecked=false
            }else {
                packageBinding!!.chkPremiumSelect.isChecked = true
                packageBinding!!.chkUnlimitedSelect.isChecked = false
                packageBinding!!.chkFreeSelect.isChecked = false
            }
        }
         packageBinding!!.rlUnlimited.setOnClickListener {
            if(packageBinding!!.chkUnlimitedSelect.isChecked){
                packageBinding!!.chkUnlimitedSelect.isChecked=false
            }else {
                packageBinding!!.chkUnlimitedSelect.isChecked = true
                packageBinding!!.chkFreeSelect.isChecked = false
                packageBinding!!.chkPremiumSelect.isChecked = false
            }
        }
        packageBinding!!.chkFreeSelect.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                packageBinding!!.rlFree.setBackgroundResource(R.drawable.selected)
                packageBinding!!.tvFree.setTextColor(activity!!.resources.getColor(R.color.white))
                packageBinding!!.tvFreeUser.setBackgroundResource(R.drawable.rectangular_rounded_shape_selecteduser_site)
                packageBinding!!.tvFreeUser.setTextColor(activity!!.resources.getColor(R.color.white))
                packageBinding!!.tvFreeSite.setBackgroundResource(R.drawable.rectangular_rounded_shape_selecteduser_site)
                packageBinding!!.tvFreeSite.setTextColor(activity!!.resources.getColor(R.color.white))

            }else{
                packageBinding!!.rlFree.setBackgroundResource(R.drawable.paymentbg)
                packageBinding!!.tvFree.setTextColor(activity!!.resources.getColor(R.color.packtextcolor))
                packageBinding!!.tvFreeUser.setBackgroundResource(R.drawable.rectangular_rounded_shape_use_site)
                packageBinding!!.tvFreeSite.setBackgroundResource(R.drawable.rectangular_rounded_shape_use_site)
                packageBinding!!.tvFreeUser.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
                packageBinding!!.tvFreeSite.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
            }

        }

        packageBinding!!.chkPremiumSelect.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                packageBinding!!.rlPremium.setBackgroundResource(R.drawable.selected)
                packageBinding!!.tvPremium.setTextColor(activity!!.resources.getColor(R.color.white))
                packageBinding!!.tvPremiumUser.setBackgroundResource(R.drawable.rectangular_rounded_shape_selecteduser_site)
                packageBinding!!.tvPremiumUser.setTextColor(activity!!.resources.getColor(R.color.white))
                packageBinding!!.tvPremiumSite.setBackgroundResource(R.drawable.rectangular_rounded_shape_selecteduser_site)
                packageBinding!!.tvPremiumSite.setTextColor(activity!!.resources.getColor(R.color.white))
                packageBinding!!.tv7gbp.setTextColor(activity!!.resources.getColor(R.color.white))
                packageBinding!!.tv7gbppermonth.setTextColor(activity!!.resources.getColor(R.color.white))

            }else{
                packageBinding!!.rlPremium.setBackgroundResource(R.drawable.paymentbg)

                packageBinding!!.tvPremium.setTextColor(activity!!.resources.getColor(R.color.packtextcolor))
                packageBinding!!.tvPremiumUser.setBackgroundResource(R.drawable.rectangular_rounded_shape_use_site)
                packageBinding!!.tvPremiumSite.setBackgroundResource(R.drawable.rectangular_rounded_shape_use_site)
                packageBinding!!.tvPremiumUser.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
                packageBinding!!.tvPremiumSite.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
                packageBinding!!.tv7gbp.setTextColor(activity!!.resources.getColor(R.color.black))
                packageBinding!!.tv7gbppermonth.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
            }

        }

        packageBinding!!.chkUnlimitedSelect.setOnCheckedChangeListener { compoundButton, b ->
            if(b){
                packageBinding!!.rlUnlimited.setBackgroundResource(R.drawable.selected)
                packageBinding!!.tvUnlimited.setTextColor(activity!!.resources.getColor(R.color.white))
                packageBinding!!.tvUnlimitedUser.setBackgroundResource(R.drawable.rectangular_rounded_shape_selecteduser_site)
                packageBinding!!.tvUnlimitedUser.setTextColor(activity!!.resources.getColor(R.color.white))
                packageBinding!!.tvUnlimitedSite.setBackgroundResource(R.drawable.rectangular_rounded_shape_selecteduser_site)
                packageBinding!!.tvUnlimitedSite.setTextColor(activity!!.resources.getColor(R.color.white))
                packageBinding!!.tvtengbp.setTextColor(activity!!.resources.getColor(R.color.white))
                packageBinding!!.tvtengbppermonth.setTextColor(activity!!.resources.getColor(R.color.white))

            }else{
                packageBinding!!.rlUnlimited.setBackgroundResource(R.drawable.paymentbg)
                packageBinding!!.tvUnlimited.setTextColor(activity!!.resources.getColor(R.color.packtextcolor))
                packageBinding!!.tvUnlimitedUser.setBackgroundResource(R.drawable.rectangular_rounded_shape_use_site)
                packageBinding!!.tvUnlimitedSite.setBackgroundResource(R.drawable.rectangular_rounded_shape_use_site)
                packageBinding!!.tvUnlimitedUser.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
                packageBinding!!.tvUnlimitedSite.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
                packageBinding!!.tvtengbp.setTextColor(activity!!.resources.getColor(R.color.black))
                packageBinding!!.tvtengbppermonth.setTextColor(activity!!.resources.getColor(R.color.usertextcolor))
            }

        }
    }

    private fun settypeface() {
        packageBinding!!.tvFree.typeface=CustomTypeface.getRajdhaniSemiBold(homeActivity!!)
        packageBinding!!.tvFreeUser.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvFreeSite.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvFreeUnlimiteddocument.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvFreeunlimitedfaults.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvPremium.typeface=CustomTypeface.getRajdhaniSemiBold(homeActivity!!)
        packageBinding!!.tvPremiumSite.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvPremiumUser.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvPremiumfault.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tv7gbp.typeface=CustomTypeface.getRajdhaniSemiBold(homeActivity!!)
        packageBinding!!.tv7gbppermonth.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvPremiumDocument.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvUnlimited.typeface=CustomTypeface.getRajdhaniSemiBold(homeActivity!!)
        packageBinding!!.tvUnlimitedSite.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvUnlimitedDocuments.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvtengbp.typeface=CustomTypeface.getRajdhaniSemiBold(homeActivity!!)
        packageBinding!!.tvUnlimitedUser.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvtengbppermonth.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
        packageBinding!!.tvSubmit.typeface=CustomTypeface.getRajdhaniMedium(homeActivity!!)
    }

    override fun onResume() {
        super.onResume()
        homeActivity!!.homeBinding!!.mainView!!.tvHeaderText.setText("PACKAGE")
        homeActivity!!.homeBinding!!.mainView!!.rlheader.visibility=View.VISIBLE
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            PackageFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}