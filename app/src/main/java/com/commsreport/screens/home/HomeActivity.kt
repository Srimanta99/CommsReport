package com.commsreport.screens.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.Uri
import android.os.AsyncTask
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
import com.commsreport.model.LoginResponseModel
import com.commsreport.screens.fragments.leaderdashboard.LeaderDashboardFragment
import com.nostra13.universalimageloader.core.DisplayImageOptions
import com.nostra13.universalimageloader.core.ImageLoader
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration
import com.nostra13.universalimageloader.core.assist.ImageScaleType
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent
import org.jsoup.Jsoup
import java.io.IOException


class HomeActivity : AppCompatActivity() {
    public var homeBinding: ActivityHomeBinding?=null
    var linearLayout:LinearLayout?=null
    internal var currentVersion: String?=""
    internal var dialog: Dialog?=null
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
         setuserdata();
         openFragment(LeaderDashboardFragment())
    }
   public  fun setuserdata(){
       var userdata=AppSheardPreference(this).getUser(PreferenceConstent.userData)
       homeBinding!!.tvCompanyname.setText(userdata!!.company_name)
       homeBinding!!.tvUsername.setText(userdata!!.full_name)
      // homeBinding!!.tvEmaile.setText(userdata!!.email)
       val options = DisplayImageOptions.Builder()
           /* .showImageForEmptyUri(R.drawable.ic_empty)
                .showImageOnFail(R.drawable.ic_error)*/
           .resetViewBeforeLoading(true)
           .cacheOnDisk(true)
           .imageScaleType(ImageScaleType.EXACTLY)
           .bitmapConfig(Bitmap.Config.RGB_565)
           .considerExifParams(true)
           .displayer(FadeInBitmapDisplayer(300))
           .build()

       var imageLoader: ImageLoader
       imageLoader = ImageLoader.getInstance() // Get singleton instance
       imageLoader.init(ImageLoaderConfiguration.createDefault(this))
       imageLoader.loadImage(userdata!!.company_logo, options,
           object : SimpleImageLoadingListener() {
               override fun onLoadingComplete(imageUri: String?, view: View?, loadedImage: Bitmap?) {
                   homeBinding!!.imgProfile!!.setImageBitmap(loadedImage)
               }
           })

       if(userdata.user_type.equals("COMPANY_ADMIN")){
           homeBinding!!.llSite.visibility=View.VISIBLE
           homeBinding!!.llUser.visibility=View.VISIBLE

       }
   }

    public fun openFragment(fragment: Fragment) {
        val transaction =supportFragmentManager.beginTransaction()
        transaction.replace(R.id.content_frame, fragment)
        transaction.addToBackStack("")
        transaction.commit()

    }


    private fun setTypeface() {
        homeBinding!!.mainView!!.tvHeaderText!!.setTypeface(CustomTypeface.getRajdhaniBold(this))
        homeBinding!!.tvCompanyname.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        homeBinding!!.tvHome.setTypeface(CustomTypeface.getRajdhaniMedium(this))
        homeBinding!!.tvUsername.setTypeface(CustomTypeface.getRajdhaniBold(this))
       // homeBinding!!.tvEmaile.setTypeface(CustomTypeface.gettitanuiumWebRegular(this))
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
    private fun getCurrentVersion() {
        val pm = this.packageManager
        var pInfo: PackageInfo? = null

        try {
            pInfo = pm.getPackageInfo(this.packageName, 0)
        } catch (e1: PackageManager.NameNotFoundException) {
            // TODO Auto-generated catch block
            e1.printStackTrace()
        }

        currentVersion = pInfo!!.versionName
        GetVersionCode().execute()

    }

    internal inner class GetVersionCode : AsyncTask<Void, String, String>() {

        override fun doInBackground(vararg voids: Void): String? {

            var newVersion: String? = null

            try {
                val document =
                    Jsoup.connect("https://play.google.com/store/apps/details?id=" + this@HomeActivity.packageName + "&hl=en")
                        //  Document document = Jsoup.connect("https://play.google.com/store/apps/details?id=" + "com.app.astrolab"  + "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                if (document != null) {
                    val element = document!!.getElementsContainingOwnText("Current Version")
                    for (ele in element) {
                        if (ele.siblingElements() != null) {
                            val sibElemets = ele.siblingElements()
                            for (sibElemet in sibElemets) {
                                newVersion = sibElemet.text()
                            }
                        }
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }

            return newVersion

        }


        override fun onPostExecute(onlineVersion: String?) {

            super.onPostExecute(onlineVersion)

            if (onlineVersion != null && !onlineVersion.isEmpty()) {

                if (java.lang.Float.valueOf(currentVersion!!) < java.lang.Float.valueOf(
                        onlineVersion
                    )) {
                    //show anything
                    showUpdateDialog()
                }

            }

            // Log.d("update", "Current version " + currentVersion + "playstore version " + onlineVersion);

        }
    }
    private fun showUpdateDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.new_version_avalible))
        builder.setPositiveButton(resources.getText(R.string.update)) { dialog, which ->
            /* startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse
                            ("market://details?id=com.wecompli")));*/
            val appPackageName = packageName // getPackageName() from Context or Activity object
            try {
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$appPackageName")))
            } catch (anfe: android.content.ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                    )
                )
            }

            dialog.dismiss()
        }

        builder.setNegativeButton(
            resources.getString(R.string.cancel),
            object : DialogInterface.OnClickListener {
                override fun onClick(dialog: DialogInterface, which: Int) {
                    dialog.dismiss()
                }
            })

        builder.setCancelable(true)
        dialog = builder.show()
        dialog!!.setCancelable(false)
    }
}