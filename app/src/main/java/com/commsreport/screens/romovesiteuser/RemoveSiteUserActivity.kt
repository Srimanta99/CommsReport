package com.commsreport.screens.romovesiteuser

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.adapter.RemoveSiteAdapter
import com.commsreport.adapter.RemoveUserAdapter
import com.commsreport.databinding.ActivittyRemoveUserSiteBinding

class RemoveSiteUserActivity:AppCompatActivity() {
    var activittyRemoveUserSiteBinding:ActivittyRemoveUserSiteBinding?=null
    var removeUserAdapter:RemoveUserAdapter?=null
    var removeSiteAdapter:RemoveSiteAdapter?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activittyRemoveUserSiteBinding= ActivittyRemoveUserSiteBinding.inflate(LayoutInflater.from(this))
        setContentView(activittyRemoveUserSiteBinding!!.root)
        removeUserAdapter=RemoveUserAdapter(this)
        removeSiteAdapter= RemoveSiteAdapter(this)
        activittyRemoveUserSiteBinding!!.recRemovesite.adapter=removeSiteAdapter
        activittyRemoveUserSiteBinding!!.recRemoveuser.adapter=removeUserAdapter
        activittyRemoveUserSiteBinding!!.btnSite.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activittyRemoveUserSiteBinding!!.btnUser.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        activittyRemoveUserSiteBinding!!.btnUser.setOnClickListener {
            activittyRemoveUserSiteBinding!!.btnUser.background=resources.getDrawable(R.drawable.rectangular_shape_left_two_rounded_corner_blue)
            activittyRemoveUserSiteBinding!!.btnSite.background=resources.getDrawable(R.drawable.rectangular_shape_right_two_rounded_corner_white)
           activittyRemoveUserSiteBinding!!.btnUser.setTextColor(resources.getColor(R.color.white))
            activittyRemoveUserSiteBinding!!.btnSite.setTextColor(resources.getColor(R.color.black))
            activittyRemoveUserSiteBinding!!.recRemovesite.visibility= View.GONE
            activittyRemoveUserSiteBinding!!.recRemoveuser.visibility= View.VISIBLE
        }
        activittyRemoveUserSiteBinding!!.btnSite.setOnClickListener {
            activittyRemoveUserSiteBinding!!.btnSite.background=resources.getDrawable(R.drawable.rectangular_shape_right_two_rounded_corner_blue)
            activittyRemoveUserSiteBinding!!.btnUser.background=resources.getDrawable(R.drawable.rectangular_shape_left_two_rounded_corner_white)
            activittyRemoveUserSiteBinding!!.btnSite.setTextColor(resources.getColor(R.color.white))
            activittyRemoveUserSiteBinding!!.btnUser.setTextColor(resources.getColor(R.color.black))
            activittyRemoveUserSiteBinding!!.recRemovesite.visibility= View.VISIBLE
            activittyRemoveUserSiteBinding!!.recRemoveuser.visibility= View.GONE
        }
    }
}