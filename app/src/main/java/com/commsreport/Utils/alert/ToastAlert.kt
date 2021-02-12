package com.commsreport.Utils.alert


import android.app.Activity
import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface


class ToastAlert {
    companion object{
        fun CustomToasterror(activity: Activity, message: String){
            val view: View = activity.getLayoutInflater().inflate(R.layout.toast_layout, null)
            var toastTextView:TextView = view.findViewById<View>(R.id.tv_toastmessage) as TextView
            var lltoast:LinearLayout = view.findViewById<View>(R.id.ll_toast) as LinearLayout
           // ll_toast
            toastTextView.setText(message)
            toastTextView.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
            //lltoast.setBackgroundResource(R.drawable.rectangular_rounded_shape_toast_blue)
            val mToast = Toast(activity)
            mToast.setView(view)
            mToast.setGravity(Gravity.BOTTOM, 0, 0)
            mToast.duration = Toast.LENGTH_LONG
            mToast.show()

        }
        fun CustomToastwornning(activity: Activity, message: String){
            val view: View = activity.getLayoutInflater().inflate(R.layout.toast_layout, null)
            var toastTextView:TextView = view.findViewById<View>(R.id.tv_toastmessage) as TextView
            var lltoast:LinearLayout = view.findViewById<View>(R.id.ll_toast) as LinearLayout
            lltoast.setBackgroundResource(R.drawable.rectangular_rounded_shape_toast_yellow)
            toastTextView.setText(message)
            toastTextView.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
            //lltoast.setBackgroundResource(R.drawable.rectangular_rounded_shape_toast_blue)
            val mToast = Toast(activity)
            mToast.setView(view)
            mToast.setGravity(Gravity.BOTTOM, 0, 0)
            mToast.duration = Toast.LENGTH_LONG
            mToast.show()

        }
        fun CustomToastSuccess(activity: Activity, message: String){
            val view: View = activity.getLayoutInflater().inflate(R.layout.toast_layout, null)
            var toastTextView:TextView = view.findViewById<View>(R.id.tv_toastmessage) as TextView
            var lltoast:LinearLayout = view.findViewById<View>(R.id.ll_toast) as LinearLayout
            // ll_toast
            toastTextView.setText(message)
            toastTextView.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
            lltoast.setBackgroundResource(R.drawable.rectangular_rounded_shape_toast_green)
            val mToast = Toast(activity)
            mToast.setView(view)
            mToast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0)
            mToast.duration = Toast.LENGTH_LONG
            mToast.show()

        }
    }
}