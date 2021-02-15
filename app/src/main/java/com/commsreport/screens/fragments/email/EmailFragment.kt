package com.commsreport.screens.fragments.email

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.FragmentEmailBinding
import com.commsreport.screens.fragments.addemail.AddEmailFragment
import com.commsreport.screens.home.HomeActivity

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EmailFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EmailFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var activity:HomeActivity?=null
    var emailBinding: FragmentEmailBinding?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let { param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        emailBinding= FragmentEmailBinding.inflate(inflater,container,false)
        emailBinding!!.tvAddemail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        return emailBinding!!.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        emailBinding!!.tvAddemail.setOnClickListener {
            activity!!.openFragment(AddEmailFragment())
        }
    }
    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView!!.tvHeaderText.setText("Email")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.VISIBLE
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EmailFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}