package com.commsreport.screens.fragments.site

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.FragmentAddSiteBinding
import com.commsreport.screens.home.HomeActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class SiteFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var activity: HomeActivity?=null
    var fragmentAddSiteBinding:FragmentAddSiteBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=  getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        fragmentAddSiteBinding= FragmentAddSiteBinding.inflate(inflater,container,false)
        return fragmentAddSiteBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAddSiteBinding!!.tvEmail.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentAddSiteBinding!!.etEmailSite.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentAddSiteBinding!!.tvAddressSite.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentAddSiteBinding!!.etAddressSite.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentAddSiteBinding!!.tvPincode.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentAddSiteBinding!!.etPinCode.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentAddSiteBinding!!.tvUpload.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentAddSiteBinding!!.etUpload.setTypeface(CustomTypeface.getWhitniBold(activity!!))
        fragmentAddSiteBinding!!.tvBrowes.setTypeface(CustomTypeface.getWhitniBold(activity!!))
        fragmentAddSiteBinding!!.tvSubmitSite.setTypeface(CustomTypeface.getwhitMedium(activity!!))
    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Add Site")
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SiteFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}