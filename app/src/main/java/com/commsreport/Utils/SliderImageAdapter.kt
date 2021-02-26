package com.commsreport.Utils

import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.commsreport.R
import com.commsreport.screens.home.HomeActivity


class SliderImageAdapter(
    val homeActivity: HomeActivity,
    val images: Array<String>) :PagerAdapter() {
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun getCount(): Int {
        return  images.size
    }


    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val imageLayout = LayoutInflater.from(homeActivity).inflate(R.layout.item_image_slide, container, false)!!

        val imageView = imageLayout.findViewById(R.id.image_slide) as ImageView
        Glide.with(homeActivity)
            .load(images.get(position))
            .centerCrop()
            .into( imageView);
        container.addView(imageLayout, 0)
        return imageLayout
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {
        super.restoreState(state, loader)
    }
    fun clearItem(container: ViewGroup, position: Int, `object`: Any) {
        super.destroyItem(container, position, `object`)

    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
      //  super.destroyItem(container, position, `object`)
        (container as ViewPager).removeView(`object` as View)
    }
}