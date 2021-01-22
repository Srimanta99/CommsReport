package com.commsreport.screens.fragments.leaderdashboard

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.FragmentLeaderDashboardBinding
import com.commsreport.screens.fragments.document.DocumentUploadFragment
import com.commsreport.screens.fragments.reportfault.ReportFaultFragment
import com.commsreport.screens.home.HomeActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class LeaderDashboardFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var activity: HomeActivity?=null
    var fragmentLeaderDashboardBinding:FragmentLeaderDashboardBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=  getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        fragmentLeaderDashboardBinding= FragmentLeaderDashboardBinding.inflate(inflater, container, false);
        return fragmentLeaderDashboardBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentLeaderDashboardBinding!!.tvReportfault.setTypeface(CustomTypeface.getRajdhaniSemiBold(getActivity()!!))
        fragmentLeaderDashboardBinding!!.tvUploadDocument.setTypeface(CustomTypeface.getRajdhaniSemiBold(getActivity()!!))
        fragmentLeaderDashboardBinding!!.tvFaultereport.setTypeface(CustomTypeface.getRajdhaniBold(getActivity()!!))
        fragmentLeaderDashboardBinding!!.tvfaultcount.setTypeface(CustomTypeface.getRajdhaniBold(getActivity()!!))
        fragmentLeaderDashboardBinding!!.tvUploadDocument.setOnClickListener {
            activity!!.openFragment(DocumentUploadFragment())
        }
        fragmentLeaderDashboardBinding!!.tvReportfault.setOnClickListener {
            activity!!.openFragment(ReportFaultFragment())
        }
    }
    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Dashboard")
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                LeaderDashboardFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}