package com.commsreport.screens.fragments.document

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.FragmentUploadDocumentsBinding
import com.commsreport.screens.home.HomeActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DocumentUploadFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var activity: HomeActivity?=null
    var fragmentUploadDocumentsBinding : FragmentUploadDocumentsBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=  getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        fragmentUploadDocumentsBinding= FragmentUploadDocumentsBinding.inflate(inflater,container,false)
        return fragmentUploadDocumentsBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentUploadDocumentsBinding!!.tvEmailUploaddoc.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.etEmailUploaddoc.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvStart.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.etStartdate.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvExpiry.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.etExpirydate.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.chkNotify.setTypeface(CustomTypeface.getWhitniBold(activity!!))
        fragmentUploadDocumentsBinding!!.tvUploadDoc.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvSubmitUploadDoc.setTypeface(CustomTypeface.getwhitMedium(activity!!))
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                DocumentUploadFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Upload Documents")
    }
}