package com.commsreport.screens.fragments.site

import android.Manifest
import android.app.Activity
import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.databinding.FragmentAddSiteBinding
import com.commsreport.screens.home.HomeActivity
import com.sculptee.utils.customprogress.CustomProgressDialog
import com.wecompli.network.NetworkUtility
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent
import okhttp3.*
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.lang.Exception
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var REQUEST_CAMERA = 111
var SELECT_FILE = 112
class SiteFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var activity: HomeActivity?=null
    var image: String?=null
    var fragmentAddSiteBinding:FragmentAddSiteBinding?=null
    var imgFile:File?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=  getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        fragmentAddSiteBinding= FragmentAddSiteBinding.inflate(inflater,container,false)
        return fragmentAddSiteBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentAddSiteBinding!!.tvEmail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentAddSiteBinding!!.etSiteName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentAddSiteBinding!!.tvAddressSite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentAddSiteBinding!!.etAddressSite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentAddSiteBinding!!.tvPincode.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentAddSiteBinding!!.etPinCode.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentAddSiteBinding!!.tvUpload.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
       // fragmentAddSiteBinding!!.etUpload.setTypeface(CustomTypeface.getWhitniBold(activity!!))
        fragmentAddSiteBinding!!.tvBrowes.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))
        fragmentAddSiteBinding!!.tvSubmitSite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentAddSiteBinding!!.tvBrowes.setOnClickListener {
           showAlertForChooseImage()
        }
        fragmentAddSiteBinding!!.tvSubmitSite.setOnClickListener {

            if(checkValidation())
             CallApiForAddSite()
        }
    }

    private fun checkValidation():Boolean{
        if(fragmentAddSiteBinding!!.etSiteName.text.toString().equals("")){
            fragmentAddSiteBinding!!.etSiteName.requestFocus()
            ToastAlert.CustomToastwornning(activity!!,"Enter Site name")
            return false
        }
        if(fragmentAddSiteBinding!!.etAddressSite.text.toString().equals("")){
            fragmentAddSiteBinding!!.etAddressSite.requestFocus()
            ToastAlert.CustomToastwornning(activity!!,"Enter Site address")
            return false
        }
        if(fragmentAddSiteBinding!!.etPinCode.text.toString().equals("")){
            fragmentAddSiteBinding!!.etPinCode.requestFocus()
            ToastAlert.CustomToastwornning(activity!!,"Enter Site postcode")
            return false
        }

        return true
    }

    private fun CallApiForAddSite() {
        var userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        val customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("company_id" ,userdata!!.company_id)
        builder.addFormDataPart("site_name", fragmentAddSiteBinding!!.etSiteName.text.toString())
        builder.addFormDataPart("site_address", fragmentAddSiteBinding!!.etAddressSite.text.toString())
        builder.addFormDataPart("site_postcode",fragmentAddSiteBinding!!.etPinCode.text.toString())
        if (imgFile!=null)
        builder.addFormDataPart("site_logo",imgFile!!.absolutePath , okhttp3.RequestBody.create(
            MediaType.parse("image/jpeg"), imgFile))

        builder.addFormDataPart("status_id" ,"1")
        val requestBody = builder.build()
        var request: Request? = null
        request = Request.Builder()
            .addHeader("Authorization", userdata.token)
            .addHeader("Content-Type","application/json")
            .url(NetworkUtility.BASE_URL + NetworkUtility.CREATE_SITE)
            .post(requestBody)
            .build()

        val client = okhttp3.OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()

        val call = client.newCall(request)
        call.enqueue(object :Callback{
            override fun onResponse(call: Call, response: Response) {
                customProgress.hideProgress()
                try {
                    var resStr :String=response.body()!!.string()
                    var response_obj= JSONObject(resStr)
                    //val response_obj = JSONObject(response.body()!!.string())
                    activity!!.runOnUiThread {
                        if (response_obj.getBoolean("status")){
                            //   val check_process_log_id:String=response_obj.getInt("check_process_log_id").toString()
                            //callApiforfaultcreate(check_process_log_id);
                            ToastAlert.CustomToastSuccess(activity!!,response_obj.getString("message"))
                            activity!!.getSupportFragmentManager().popBackStack();
                        }else{
                            ToastAlert.CustomToasterror(activity!!,response_obj.getString("message"))

                        }
                    }

                }
                catch (e: Exception){
                    e.printStackTrace()
                    activity!!.runOnUiThread {
                        ToastAlert.CustomToasterror(activity!!, "Try later. Something Wrong.")
                    }


                }
            }

            override fun onFailure(call: Call, e: IOException) {
                customProgress.hideProgress()
            }
        })


    }

    private fun showAlertForChooseImage() {
        val alertDialog = Dialog(activity!!, R.style.Transparent)
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View = LayoutInflater.from(activity).inflate(R.layout.alert_custom_imageselection, null)
        alertDialog.setContentView(view)
        alertDialog.setCancelable(false)
        val tv_message: TextView = view.findViewById(R.id.tv_message)
        val tv_gallery: TextView = view.findViewById(R.id.tv_gallery)
        val tv_camera: TextView =view.findViewById(R.id.tv_camera)
        val tv_cancel: TextView =view.findViewById(R.id.tv_cancel)
        tv_gallery.typeface=CustomTypeface.getRajdhaniSemiBold(activity!!)
        tv_camera.typeface=CustomTypeface.getRajdhaniSemiBold(activity!!)
        tv_cancel.typeface = CustomTypeface.getRajdhaniSemiBold(activity!!)
        tv_message.typeface = CustomTypeface.getRajdhaniSemiBold(activity!!)
        tv_gallery.setOnClickListener {
            alertDialog.dismiss()
            choosefromGallery()
        }
        tv_camera.setOnClickListener {
            alertDialog.dismiss()
            chooseInagefromCamera()
        }
        tv_cancel.setOnClickListener {
            alertDialog.dismiss()
        }
        alertDialog.show()
    }

    private fun chooseInagefromCamera() {
        image = "camera"
        checkpermession()
    }

    private fun choosefromGallery() {
        image = "gallery"
        checkpermession()
    }
    fun checkpermession(){
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.CAMERA) !== PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity!!, arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        } else {
            if (image == "gallery")
                galleryIntent()
            else if (image == "camera")
                takePhotoFromCamera()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                if (image == "gallery")
                    galleryIntent()
                else if (image == "camera")
                    takePhotoFromCamera()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data)
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data!!)
        }
    }

    private fun galleryIntent() {
        val intent = Intent()
        intent.type = "image/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
    }

    private fun takePhotoFromCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_CAMERA)
    }
    private fun onSelectFromGalleryResult(data: Intent?) {
        var bm: Bitmap? = null
        if (data != null) {
            try {
                bm = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, data.data)
                /*  val bytes = ByteArrayOutputStream()
                  bm!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
                  val destination = File(Environment.getExternalStorageDirectory(), "fault_image"+ ".jpg")*/
                // val destination = File(Environment.getExternalStorageDirectory(), System.currentTimeMillis().toString() + ".jpg")

                val root = Environment.getExternalStorageDirectory().toString()
                val myDir = File("$root/mycomms/site")
                myDir.mkdirs()
                /* val generator = Random()
                  var n = 100
                  n = generator.nextInt(n)*/
                val fname ="site.jpg"
                val file = File(myDir, fname)
                val fo: FileOutputStream
                if (file.exists())
                    file.delete()
                try {
                    /* destination.createNewFile()
                     fo = FileOutputStream(destination)
                     fo.write(bytes.toByteArray())
                     fo.close()*/
                    val out = FileOutputStream(file)
                    bm.compress(Bitmap.CompressFormat.JPEG, 100, out)
                    out.flush()
                    out.close()
                    imgFile=file

                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                fragmentAddSiteBinding!!.imgSelectedImage.setImageBitmap(bm)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }


    }
    private fun onCaptureImageResult(data: Intent) {
        val thumbnail = data.extras!!.get("data") as Bitmap?
        /* val bytes = ByteArrayOutputStream()
         thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
         val destination = File(Environment.getExternalStorageDirectory(), "fault_image"+ ".jpg")
         val fo: FileOutputStream
 */
        try {
            // thumbnail = MediaStore.Images.Media.getBitmap(applicationContext.contentResolver, data.data)
            /*  val bytes = ByteArrayOutputStream()
              bm!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
              val destination = File(Environment.getExternalStorageDirectory(), "fault_image"+ ".jpg")*/
            // val destination = File(Environment.getExternalStorageDirectory(), System.currentTimeMillis().toString() + ".jpg")

            val root = Environment.getExternalStorageDirectory().toString()
            val myDir = File("$root/mycomms/site")
            myDir.mkdirs()
            /* val generator = Random()
              var n = 100
              n = generator.nextInt(n)*/
            val fname ="site.jpg"
            val file = File(myDir, fname)
            val fo: FileOutputStream
            if (file.exists())
                file.delete()
            try {
                /* destination.createNewFile()
                 fo = FileOutputStream(destination)
                 fo.write(bytes.toByteArray())
                 fo.close()*/
                val out = FileOutputStream(file)
                thumbnail!!.compress(Bitmap.CompressFormat.JPEG, 100, out)
                imgFile=file
                out.flush()
                out.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
        fragmentAddSiteBinding!!.imgSelectedImage.setImageBitmap(thumbnail)
        /* try {
             destination.createNewFile()
             fo = FileOutputStream(destination)
             fo.write(bytes.toByteArray())
             fo.close()
             imagearraylist.add(destination)
             imagearraylistpath.add(destination.path)
         } catch (e: FileNotFoundException) {
             e.printStackTrace()
         } catch (e: IOException) {
             e.printStackTrace()
         }*/


    }
    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Add Site")
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                SiteFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }
}