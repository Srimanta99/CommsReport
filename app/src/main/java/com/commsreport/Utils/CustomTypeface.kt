package com.commsreport.Utils

import android.app.Activity
import android.graphics.Typeface
import androidx.databinding.BindingAdapter

class CustomTypeface {

companion object{
    public  fun getRajdhaniBold(activity: Activity): Typeface? {
        var    rajdhaniBold:Typeface = Typeface.createFromAsset(activity.assets, "font/Rajdhani-Bold.ttf")
        return rajdhaniBold
    }

    public  fun getRajdhaniLight(activity: Activity): Typeface? {
        var    rajdhaniLight:Typeface = Typeface.createFromAsset(activity.assets, "font/Rajdhani-Light.ttf")
        return rajdhaniLight
    }

    public  fun getRajdhaniMedium(activity: Activity): Typeface? {
        var    rajdhaniMedium:Typeface = Typeface.createFromAsset(activity.assets, "font/Rajdhani-Medium.ttf")
        return rajdhaniMedium
    }
    public  fun getRajdhaniRegular(activity: Activity): Typeface? {
        var    rajdhaniregular:Typeface = Typeface.createFromAsset(activity.assets, "font/Rajdhani-Regular.ttf")
        return rajdhaniregular
    }
    public  fun getRajdhaniSemiBold(activity: Activity): Typeface? {
        var    rajdhanisemibold:Typeface = Typeface.createFromAsset(activity.assets, "font/Rajdhani-SemiBold.ttf")
        return rajdhanisemibold
    }
}

}