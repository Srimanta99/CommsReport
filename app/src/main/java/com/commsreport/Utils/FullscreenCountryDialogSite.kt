package com.commsreport.Utils

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.RecyclerView
import com.commsreport.R
import com.commsreport.adapter.CountryAdapter
import com.commsreport.databinding.CountryAlertLayoutBinding
import com.commsreport.model.CountryListModel
import com.commsreport.model.SiteListModel
import com.commsreport.screens.home.HomeActivity
import com.wecompli.utils.onitemclickinterface.CountryClickInterface

class FullscreenCountryDialogSite(
    var countrylist: ArrayList<CountryListModel.CountryList>,
    val activity: HomeActivity,
    val countryClickInterface: CountryClickInterface) :DialogFragment() {
    var countryAlertLayoutBinding:CountryAlertLayoutBinding?=null
    var countryAdapter:CountryAdapter?=null
    var countrylistall: ArrayList<CountryListModel.CountryList>?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.country_alert_layout, container, false)
        val img_back=view.findViewById<ImageView>(R.id.img_back)
        val reclyerview=view.findViewById<RecyclerView>(R.id.rec_country)
        val et_site_name=view.findViewById<EditText>(R.id.et_site_name)

        img_back.setOnClickListener {
            dismiss()
        }

        et_site_name!!.addTextChangedListener(object : TextWatcher {
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                // Alert.showalert(homeActivity!!, homeActivity!!.getResources().getString(R.string.underdevelopment))
            }

            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
        countrylistall=countrylist
         countryAdapter=CountryAdapter(activity!!,countrylist,countryClickInterface,)
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
    private fun filter(text: String) {
        val filterdNames = ArrayList<CountryListModel.CountryList>()
        //rowfaultlist.siteList=null
        //looping through existing elements
        for (s in countrylistall!!) {
            //if the existing elements contains the search input
            if (s.country_name!!.toLowerCase().contains(text.toLowerCase())) {

                //adding the element to filtered list
                filterdNames.add(s)

            }
        }
        //calling a method of the adapter class and passing the filtered list
        countrylist=filterdNames
        countryAdapter!!.filterList(countrylist)
    }
}