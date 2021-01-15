package com.commsreport.screens.home

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ActivityHomeBinding
import com.commsreport.screens.fragments.LeaderDashboardFragment


class HomeActivity : AppCompatActivity() {
    public var homeBinding: ActivityHomeBinding?=null
    var linearLayout:LinearLayout?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        homeBinding= ActivityHomeBinding.inflate(LayoutInflater.from(this))
        setContentView(homeBinding!!.root)
        HomeOnClick(this, homeBinding!!)
        linearLayout=findViewById(R.id.mainView)
        homeBinding!!.drawerLayout!!.setScrimColor(Color.TRANSPARENT)
        var actionBarDrawerToggle: ActionBarDrawerToggle =
            object : ActionBarDrawerToggle(this, homeBinding!!.drawerLayout, R.string.start, R.string.cancel) {
                private val scaleFactor = 4f
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    val slideX = drawerView.width * slideOffset
                     linearLayout!!.setTranslationX(slideX)
                    linearLayout!!.setScaleX(1 - slideOffset / scaleFactor)
                    linearLayout!!.setScaleY(1 - slideOffset / scaleFactor)
                    //  drawerView!!.setBackgroundColor(resources.getColor(R.color.login_bg_header))
                    // homeViewBind!!.mainView!!.setBackgroundColor(resources.getColor(R.color.login_bg_header))
                }

            }
        homeBinding!!.drawerLayout!!.addDrawerListener(actionBarDrawerToggle)
        setTypeface();
       openFragment(LeaderDashboardFragment())
    }

    public fun openFragment(fragment: Fragment) {
        val transaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment)
        transaction.addToBackStack("")
        transaction.commit()

    }


    private fun setTypeface() {
        homeBinding!!.mainView!!.tvHeaderText!!.setTypeface(CustomTypeface.getRajdhaniBold(this))
        homeBinding!!.tvCompanyname.setTypeface(CustomTypeface.getRajdhaniBold(this))
        homeBinding!!.tvUsername.setTypeface(CustomTypeface.getRajdhaniRegular(this))
        homeBinding!!.tvEmaile.setTypeface(CustomTypeface.getRajdhaniRegular(this))
        homeBinding!!.tvSites.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        homeBinding!!.tvUser.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        homeBinding!!.tvFault.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        homeBinding!!.tvDocument.setTypeface(CustomTypeface.getRajdhaniMedium(this))

    }
    /*override fun onBackPressed() {
        fragmentManager.executePendingTransactions()
        val count = fragmentManager.backStackEntryCount
        if (count <= 1) {
            finish()
        } else {
            fragmentManager.popBackStack()
        }
    }*/

}