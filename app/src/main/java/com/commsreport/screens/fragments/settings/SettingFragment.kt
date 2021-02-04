package com.commsreport.screens.fragments.settings

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.FragmentSettingsBinding

import com.commsreport.screens.home.HomeActivity

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"



class SettingFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null
    var settingsBinding: FragmentSettingsBinding?=null
    var activity :HomeActivity?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        settingsBinding!!.etName.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        settingsBinding!!.tvName.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        settingsBinding!!.etEmail.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        settingsBinding!!.tvEmail.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        settingsBinding!!.tvPassword.setTypeface(CustomTypeface.getWhitniBold(activity!!))
        settingsBinding!!.tvCurrentpassword.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        settingsBinding!!.etCurrentpassword.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        settingsBinding!!.tvNewpassword.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        settingsBinding!!.etNewpassword.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        settingsBinding!!.tvConfirmpass.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        settingsBinding!!.etConpass.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        settingsBinding!!.submitTvid.setTypeface(CustomTypeface.getwhitMedium(activity!!))
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        settingsBinding= FragmentSettingsBinding.inflate(inflater,container,false)
        return settingsBinding!!.root
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("SETTINGS")
    }
}