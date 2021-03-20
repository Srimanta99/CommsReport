package com.commsreport.screens.subcription

import android.content.Intent
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.databinding.ActivityPackageBinding
import com.commsreport.model.SubCriptionPackagResponseemodel
import com.commsreport.screens.cardinfo.CardActivity
import java.text.Format
import java.text.SimpleDateFormat
import java.util.*

class PackageActivity:AppCompatActivity() {
    var activityPackageBinding :ActivityPackageBinding?=null
    var selectedpackage:SubCriptionPackagResponseemodel.PackageItem?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
         selectedpackage = intent.getSerializableExtra("package") as? SubCriptionPackagResponseemodel.PackageItem
        val formatter: Format = SimpleDateFormat("dd MMMM, yyyy")
        val today: String = formatter.format(Date())
        activityPackageBinding= ActivityPackageBinding.inflate(LayoutInflater.from(this))
        setContentView(activityPackageBinding!!.root)
        activityPackageBinding!!.tvHeaderText.setTypeface(CustomTypeface.getRajdhaniBold(this))
        activityPackageBinding!!.tvSubmit.setTypeface(CustomTypeface.getRajdhaniMedium(this!!))
        activityPackageBinding!!.tvUnlimited.setTypeface(CustomTypeface.getRajdhaniSemiBold(this))
        activityPackageBinding!!.tvUnlimitedUser.setTypeface(CustomTypeface.getRajdhaniSemiBold(this))
        activityPackageBinding!!.tvUnlimitedSite.setTypeface(CustomTypeface.getRajdhaniSemiBold(this))
        activityPackageBinding!!.tvSubcriptiondate.setTypeface(CustomTypeface.getRajdhaniMedium(this!!))
        activityPackageBinding!!.tvSubcriptiondateValue.setTypeface(CustomTypeface.getRajdhaniMedium(this!!))
        activityPackageBinding!!.tvTotalamount.setTypeface(CustomTypeface.getRajdhaniMedium(this!!))
        activityPackageBinding!!.tvTotalAmountVal.setTypeface(CustomTypeface.getRajdhaniMedium(this!!))
        activityPackageBinding!!.tvNote.setTypeface(CustomTypeface.getRajdhaniMedium(this!!))
        activityPackageBinding!!.chkterm.setTypeface(CustomTypeface.getRajdhaniRegular(this))
        activityPackageBinding!!.tvSubmit.setTypeface(CustomTypeface.getRajdhaniRegular(this))
        activityPackageBinding!!.termcondition.setTypeface(CustomTypeface.getwhitMedium(this))

        val textnote="<font color=#FE0100>Note: </font> <font color=#1E3F6C>Every month this amount will be charged from your account</font>";
        activityPackageBinding!!.tvNote.setText(Html.fromHtml(textnote))
        activityPackageBinding!!.tvSubcriptiondateValue.setText(today)

        activityPackageBinding!!.tvUnlimited.setText(selectedpackage!!.package_name)
        if(selectedpackage!!.package_price.equals("0")){
            activityPackageBinding!!.tvtengbp.visibility= View.GONE
            activityPackageBinding!!.tvtengbppermonth.visibility= View.GONE
            activityPackageBinding!!.tvTotalAmountVal.setText("0")

        }else{
            activityPackageBinding!!.tvtengbp.setText(selectedpackage!!.package_price + " GBP/")
            activityPackageBinding!!.tvTotalAmountVal.setText(selectedpackage!!.package_price + " GBP")
        }

        if (selectedpackage!!.is_user_unlimited.equals("1")){
            activityPackageBinding!!.tvUnlimitedUser.setText("Unlimited Users")
            activityPackageBinding!!.tvUnlimitedSite.setText("Unlimited Sites")
        }else {
            activityPackageBinding!!.tvUnlimitedUser.setText(selectedpackage!!.user_count + " Users")
            activityPackageBinding!!.tvUnlimitedSite.setText(selectedpackage!!.site_count + " Sites")
        }


        activityPackageBinding!!.tvSubmit.setOnClickListener {
            if (activityPackageBinding!!.chkterm.isChecked) {
                val intent = Intent(this, CardActivity::class.java)
                intent.putExtra("package",selectedpackage)
                startActivity(intent)
            }else{
                ToastAlert.CustomToastwornning(this,"accept teem and condition")
            }
        }
        activityPackageBinding!!.imgBack.setOnClickListener {
            finish()
        }
    }
}