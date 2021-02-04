package com.commsreport.Utils.alert

import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.Window
import android.widget.Button
import android.widget.TextView
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.screens.home.HomeActivity
import com.commsreport.screens.login.LoginActivity
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent

class Alert {
    companion object{

        fun showalert(activity: Activity, message: String) {
            //  var deviceResolution:DeviceResolution?=null
            val alertDialog = Dialog(activity, R.style.Transparent)
            /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
            alertDialog.setMessage(message)*/
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view: View = LayoutInflater.from(activity).inflate(R.layout.alert_layout, null)
            alertDialog.setContentView(view)
            alertDialog.setCancelable(false)
            val tv_message: TextView = view.findViewById(R.id.tv_message)
            val btn_ok: TextView = view.findViewById(R.id.btn_ok)
            btn_ok.typeface = CustomTypeface.getRajdhaniMedium(activity)
            tv_message.typeface = CustomTypeface.getWhitniBold(activity)
            btn_ok.setOnClickListener {
                alertDialog.dismiss()
            }
            tv_message.setText(message)
            alertDialog.show()
            /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            alertDialog.show()*/
        }

        fun showalerttoHome(activity: Activity, message: String) {
            //  var deviceResolution:DeviceResolution?=null
            val alertDialog = Dialog(activity, R.style.Transparent)
            /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
            alertDialog.setMessage(message)*/
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view: View = LayoutInflater.from(activity).inflate(R.layout.alert_layout, null)
            alertDialog.setContentView(view)
            alertDialog.setCancelable(false)
            val tv_message: TextView = view.findViewById(R.id.tv_message)
            val btn_ok: TextView = view.findViewById(R.id.btn_ok)
            btn_ok.typeface = CustomTypeface.getRajdhaniMedium(activity)
            tv_message.typeface = CustomTypeface.getWhitniBold(activity)
            btn_ok.setOnClickListener {
                alertDialog.dismiss()

            }
            tv_message.setText(message)
            alertDialog.show()
            /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            alertDialog.show()*/
        }

        fun showalertForUnAuthorized(activity: Activity, message: String) {
            //  var deviceResolution:DeviceResolution?=null
            val alertDialog = Dialog(activity, R.style.Transparent)
            /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
            alertDialog.setMessage(message)*/
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view: View = LayoutInflater.from(activity).inflate(R.layout.alert_layout, null)
            alertDialog.setContentView(view)
            alertDialog.setCancelable(false)
            val tv_message: TextView = view.findViewById(R.id.tv_message)
            val btn_ok: TextView = view.findViewById(R.id.btn_ok)
            btn_ok.typeface = CustomTypeface.getRajdhaniMedium(activity)
            tv_message.typeface = CustomTypeface.getRajdhaniMedium(activity)
            btn_ok.setOnClickListener {
                alertDialog.dismiss()
                val i = Intent(activity, LoginActivity::class.java)
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(i)

            }
            tv_message.setText(message)
            alertDialog.show()
            /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            alertDialog.show()*/
        }
        fun showYesNoAlert( activity: HomeActivity, message: String) {
            // var deviceResolution:DeviceResolution?=null
            val alertDialog = Dialog(activity, R.style.Transparent)
            /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
            alertDialog.setMessage(message)*/
            alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            val view: View = LayoutInflater.from(activity).inflate(R.layout.alert_layout_yesno, null)
            alertDialog.setContentView(view)
            alertDialog.setCancelable(false)
            val tv_message: TextView = view.findViewById(R.id.tv_message)
            val tv_ok: TextView = view.findViewById(R.id.tv_ok)
            val tv_no: TextView = view.findViewById(R.id.tv_no)
            tv_ok.typeface = CustomTypeface.getRajdhaniMedium(activity)
            tv_no.typeface = CustomTypeface.getRajdhaniMedium(activity)
            tv_message.typeface = CustomTypeface.getRajdhaniRegular(activity)
            tv_ok.setOnClickListener {
                alertDialog.dismiss()
                AppSheardPreference(activity).setvalue_in_preference(PreferenceConstent.iS_LOGIN,"0")
                val i = Intent(activity, LoginActivity::class.java)
// set the new task and clear flags
                i.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                activity.startActivity(i)
                //  activity.homeOnClick!!.logoutyes()
                // activity.alertyesfuncation();
                // activity.calllogoutdeleteusertoken()
            }
            tv_no.setOnClickListener {
                alertDialog.dismiss()

            }
            tv_message.setText(message)
            alertDialog.show()
            /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
            alertDialog.show()*/
        }
    }

}