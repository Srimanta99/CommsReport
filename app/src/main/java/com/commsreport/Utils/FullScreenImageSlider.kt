package com.commsreport.Utils

import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import androidx.viewpager.widget.ViewPager
import com.commsreport.R
import com.commsreport.screens.home.HomeActivity
import com.viewpagerindicator.CirclePageIndicator
import java.util.*

class FullScreenImageSlider(val activity: HomeActivity,val faultFiles: Array<String>,):DialogFragment() {
    var mPager:ViewPager?=null
    var indicator:CirclePageIndicator?=null
    var back:ImageView?=null
    private var currentPage = 0
    private var NUM_PAGES = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.FullScreenDialogStyle)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.full_screen_image_slider, container, false)
        mPager=view.findViewById(R.id.mPager)
        indicator=view.findViewById(R.id.indicator)
        back=view.findViewById(R.id.back)
        back!!.setOnClickListener {
            dismiss()
        }
        setuppager()
        return  view
    }
    private fun setuppager() {
        mPager!!.adapter=SliderImageAdapter(activity,faultFiles)
        indicator!!.setViewPager(mPager)
        val density = resources.displayMetrics.density
        indicator!!.radius = 5 * density

        val handler = Handler()
        val Update = Runnable {
            if (currentPage == NUM_PAGES) {
                currentPage = 0
            }
            mPager!!.setCurrentItem(currentPage++, true)
        }

        val swipeTimer = Timer()
        swipeTimer.schedule(object : TimerTask() {
            override fun run() {
                handler.post(Update)
            }
        }, 3000, 3000)

        indicator!!.setOnPageChangeListener(object : ViewPager.OnPageChangeListener{
            override fun onPageSelected(position: Int) {
                currentPage = position
            }

            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }
        })
    }
}