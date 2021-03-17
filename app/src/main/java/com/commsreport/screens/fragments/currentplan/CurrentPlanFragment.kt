package com.commsreport.screens.fragments.currentplan

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.FragmentCurrentPlanBinding
import com.commsreport.screens.fragments.subcriptionpackage.SubcriptionPackageFragment
import com.commsreport.screens.home.HomeActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class CurrentPlanFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var fragmentCurrentPlanBinding:FragmentCurrentPlanBinding?=null
    var activity:HomeActivity?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentCurrentPlanBinding= FragmentCurrentPlanBinding.inflate(inflater,container,false)
        return fragmentCurrentPlanBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentCurrentPlanBinding!!.tvCurrentplan.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))
        fragmentCurrentPlanBinding!!.tvUnlimited.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        fragmentCurrentPlanBinding!!.tvUnlimitedUser.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentCurrentPlanBinding!!.tvUnlimitedSite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentCurrentPlanBinding!!.tvtengbp.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        fragmentCurrentPlanBinding!!.tvtengbppermonth.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentCurrentPlanBinding!!.tvUnlimitedFault.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentCurrentPlanBinding!!.tvUnlimitedDocuments.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentCurrentPlanBinding!!.tvSubcriptiondate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentCurrentPlanBinding!!.tvLastbillingdate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentCurrentPlanBinding!!.tvNextBillingDate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentCurrentPlanBinding!!.tvSubcriptiondateValue.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        fragmentCurrentPlanBinding!!.tvNextBillingDateVal.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        fragmentCurrentPlanBinding!!.tvLastBillingDateVal.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        fragmentCurrentPlanBinding!!.tvSubmit.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentCurrentPlanBinding!!.tvSubmit.setOnClickListener {
            activity!!.openFragment(SubcriptionPackageFragment())
        }


    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Payment")
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            CurrentPlanFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}