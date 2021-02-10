package com.commsreport.Utils

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.commsreport.R
import com.commsreport.adapter.CountryAdapter
import com.commsreport.databinding.CountryAlertLayoutBinding
import com.commsreport.model.CountryListModel
import com.commsreport.screens.home.HomeActivity
import com.wecompli.utils.onitemclickinterface.CountryClickInterface

class FullscreenCountryDialog(
    val countrylist: ArrayList<CountryListModel.CountryList>,
    val activity: HomeActivity,
    val countryClickInterface: CountryClickInterface) :DialogFragment() {
    var countryAlertLayoutBinding:CountryAlertLayoutBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.country_alert_layout, container, false)
        val img_back=view.findViewById<ImageView>(R.id.img_back)
        val reclyerview=view.findViewById<RecyclerView>(R.id.rec_country)

        img_back.setOnClickListener {
            dismiss()
        }


        val countryAdapter=CountryAdapter(activity!!,countrylist,countryClickInterface)
        reclyerview.adapter=countryAdapter

        return view
    }

    override fun onStart() {
        super.onStart()

        if(dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog!!.window?.setLayout(width, height)
        }
    }
}