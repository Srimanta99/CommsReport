package com.commsreport.screens.thankyoupage

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ActivityThankYouBinding
import com.commsreport.screens.cardinfo.CardActivity
import com.commsreport.screens.home.HomeActivity

class ThankyouActivity: AppCompatActivity() {
     var activityThankYouBinding: ActivityThankYouBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityThankYouBinding= ActivityThankYouBinding.inflate(LayoutInflater.from(this))
        setContentView(activityThankYouBinding!!.root)
        activityThankYouBinding!!.tvThakyou.setTypeface(CustomTypeface.getRajdhaniBold(this))
        activityThankYouBinding!!.tvSubcription.setTypeface(CustomTypeface.getRajdhaniSemiBold(this))
        activityThankYouBinding!!.tvNote.setTypeface(CustomTypeface.getRajdhaniSemiBold(this))
        activityThankYouBinding!!.tvForsupport.setTypeface(CustomTypeface.getRajdhaniRegular(this))
        activityThankYouBinding!!.loginUnderline.setTypeface(CustomTypeface.getRajdhaniSemiBold(this))

        activityThankYouBinding!!.llDashboard.setOnClickListener {
            val intent=Intent(this, HomeActivity::class.java)
            intent.flags=Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
        }

    }

}