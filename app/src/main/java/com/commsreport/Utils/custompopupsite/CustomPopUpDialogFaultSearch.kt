package com.commsreport.Utils.custompopupsite

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.Window
import android.widget.EditText
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.model.SiteListModel
import com.commsreport.screens.fragments.adduser.AddUserFragment
import com.commsreport.screens.fragments.editsite.EditSiteFragment
import com.commsreport.screens.fragments.faultlistdetails.FaultListFragment
import com.commsreport.screens.fragments.reportfault.ReportFaultFragment
import com.commsreport.screens.home.HomeActivity
import com.wecompli.utils.onitemclickinterface.OnItemClickInterface



class CustomPopUpDialogFaultSearch(context: HomeActivity?, val siteList: List<SiteListModel.RowList>?, val faultListFragment: FaultListFragment) : Dialog(context!!) {

     var tv_dialogtitle: TextView ?= null
     var et_search: EditText?= null
     var rv_list: RecyclerView?= null
     var homeActivity: HomeActivity?= context
     var customPopupListAdapter:CustomPopupListAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setCanceledOnTouchOutside(true)
        setContentView(R.layout.custompopupsitelist_layout)
        rv_list = findViewById(R.id.rv_list)
        tv_dialogtitle = findViewById(R.id.tv_dialogtitle)
        et_search = findViewById(R.id.et_search)
        tv_dialogtitle!!.setTypeface(CustomTypeface.getRajdhaniBold(homeActivity!!))
        et_search!!.setTypeface(CustomTypeface.getRajdhaniMedium(homeActivity!!))
        setreclyerview()
        et_search!!.addTextChangedListener(object :TextWatcher{
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
               // Alert.showalert(homeActivity!!, homeActivity!!.getResources().getString(R.string.underdevelopment))
            }

            override fun afterTextChanged(p0: Editable?) {
                filter(p0.toString())
            }

            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }
        })
    }
    private fun setreclyerview() {

        customPopupListAdapter = CustomPopupListAdapter(homeActivity!!,siteList!!,object :OnItemClickInterface{
            override fun OnItemClick(position: Int) {
                faultListFragment.faultListBinding!!.navFaultSearch.tvDropdownSelectsite.setText(faultListFragment.siteList.get(position).site_name)
                faultListFragment.selected_Site_id=faultListFragment.siteList.get(position).id
                dismiss()

            }
        })
        val mLayoutManager = LinearLayoutManager(homeActivity)
        rv_list!!.setLayoutManager(mLayoutManager)
        rv_list!!.setAdapter(customPopupListAdapter)

    }

    private fun filter(text: String) {
        val filterdNames = ArrayList<SiteListModel.RowList>()
        //rowfaultlist.siteList=null
        //looping through existing elements
        for (s in siteList!!) {
            //if the existing elements contains the search input
            if (s.site_name!!.toLowerCase().contains(text.toLowerCase())) {

                //adding the element to filtered list
                filterdNames.add(s)

            }
        }
        //calling a method of the adapter class and passing the filtered list
        faultListFragment.siteList=filterdNames
        customPopupListAdapter!!.filterList(filterdNames)
    }
}