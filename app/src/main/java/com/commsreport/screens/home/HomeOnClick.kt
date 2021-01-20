package com.commsreport.screens.home

import android.view.Gravity
import android.view.View
import androidx.annotation.NonNull
import androidx.core.view.GravityCompat
import com.commsreport.R
import com.commsreport.Utils.alert.Alert
import com.commsreport.databinding.ActivityHomeBinding
import com.commsreport.screens.fragments.adduser.AddUserFragment


import com.commsreport.screens.fragments.document.DocumentUploadFragment
import com.commsreport.screens.fragments.faults.FaultFragment
import com.commsreport.screens.fragments.leaderdashboard.LeaderDashboardFragment
import com.commsreport.screens.fragments.managesite.ManageSiteFragment
import com.commsreport.screens.fragments.settings.SettingFragment
import com.commsreport.screens.fragments.site.SiteFragment

class HomeOnClick(val homeActivity: HomeActivity, val homeBinding: ActivityHomeBinding):View.OnClickListener {

    init {
        homeBinding!!.mainView!!.imgMenu.setOnClickListener(this)
        homeBinding.llHome.setOnClickListener(this)
        homeBinding.llSite.setOnClickListener(this)
        homeBinding.llUser.setOnClickListener(this)
        homeBinding.llDocument.setOnClickListener(this)
        homeBinding.llFault.setOnClickListener(this)
        homeBinding.rlSettings.setOnClickListener(this)
        homeBinding.rlLogout.setOnClickListener(this)

    }
    override fun onClick(p0: View?) {
        when(p0!!.id){
            R.id.ll_home->{
                homeBinding!!.drawerLayout!!.closeDrawer(GravityCompat.START)
                homeActivity.openFragment(LeaderDashboardFragment())
            }
             R.id.img_menu->{
                 homeBinding!!.drawerLayout!!.openDrawer(Gravity.LEFT)
             }
            R.id.ll_document->{
                homeBinding!!.drawerLayout!!.closeDrawer(GravityCompat.START)
                homeActivity.openFragment(DocumentUploadFragment())
            }
            R.id.ll_site->{
                homeBinding!!.drawerLayout!!.closeDrawer(GravityCompat.START)
                homeActivity.openFragment(ManageSiteFragment())
            }
            R.id.ll_user->{
                homeBinding!!.drawerLayout!!.closeDrawer(GravityCompat.START)
                homeActivity.openFragment(AddUserFragment())
            }
            R.id.ll_fault->{
                homeBinding!!.drawerLayout!!.closeDrawer(GravityCompat.START)
                homeActivity.openFragment(FaultFragment())
            }
            R.id.rl_settings->{
                homeBinding!!.drawerLayout!!.closeDrawer(GravityCompat.START)
                homeActivity.openFragment(SettingFragment())
            }
            R.id.rl_logout->{
                homeBinding!!.drawerLayout!!.closeDrawer(GravityCompat.START)
                Alert.showYesNoAlert(homeActivity!!,"Are you want to logout?")
            }
        }
    }
}