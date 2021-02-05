package com.commsreport.screens.fragments.reportfault

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
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteList
import com.commsreport.adapter.ManageSiteAdapter
import com.commsreport.databinding.FragmentReportFaultaBinding
import com.commsreport.model.LoginResponseModel

import com.commsreport.model.SiteListModel
import com.commsreport.screens.fragments.site.REQUEST_CAMERA
import com.commsreport.screens.fragments.site.SELECT_FILE
import com.commsreport.screens.home.HomeActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.sculptee.utils.customprogress.CustomProgressDialog
import com.wecompli.network.ApiInterface
import com.wecompli.network.NetworkUtility
import com.wecompli.network.Retrofit
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ReportFaultFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var activity:HomeActivity?=null
    var fragmentReportFaultBinding:FragmentReportFaultaBinding?=null
    var siteList=ArrayList<SiteListModel.RowList>()
    var image: String?=null
    var imgFile:File?=null
    internal var imagearraylist = java.util.ArrayList<File>()
    var ImageSelectposition:Int = 0
    var formattedDate:String?=null
    public  var selectedSiteId=""
     var userdata:LoginResponseModel.Userdata? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentReportFaultBinding=FragmentReportFaultaBinding.inflate(inflater,container,false)
        return fragmentReportFaultBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fragmentReportFaultBinding!!.tvaddNote.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentReportFaultBinding!!.etAddnote.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentReportFaultBinding!!.tvSelectsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentReportFaultBinding!!.tvSelectedsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentReportFaultBinding!!.tvImg1.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        fragmentReportFaultBinding!!.tvImg2.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        fragmentReportFaultBinding!!.tvImg3.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        fragmentReportFaultBinding!!.tvImg4.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        fragmentReportFaultBinding!!.tvAddfault.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        val c = Calendar.getInstance()
        val df = SimpleDateFormat("dd/MM/yyyy")
        formattedDate = df.format(c.time)
        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        if(userdata!!.user_type.equals("COMPANY_ADMIN")){
            fragmentReportFaultBinding!!.llSelectsite.visibility=View.VISIBLE
            callApiForSiteList()
        }else
            selectedSiteId=userdata!!.site_id

        fragmentReportFaultBinding!!.tvSelectedsite.setOnClickListener {
           val customPopUpDialogSiteList= CustomPopUpDialogSiteList(activity,siteList,this)
            customPopUpDialogSiteList!!.show()
        }
        fragmentReportFaultBinding!!.rlImg1.setOnClickListener {
            ImageSelectposition=1
            showAlertForChooseImage()
           // val customPopUpDialogSiteList= CustomPopUpDialogSiteList(activity,siteList,this)
           // customPopUpDialogSiteList!!.show()
        }
        fragmentReportFaultBinding!!.rlImg2.setOnClickListener {
            ImageSelectposition=2
            showAlertForChooseImage()
        }
        fragmentReportFaultBinding!!.rlImg3.setOnClickListener {
            ImageSelectposition=3
            showAlertForChooseImage()
        }
        fragmentReportFaultBinding!!.rlImg4.setOnClickListener {
            ImageSelectposition=4
            showAlertForChooseImage()
        }
        fragmentReportFaultBinding!!.tvAddfault.setOnClickListener {
            if (!selectedSiteId.equals("")) {
                if (!fragmentReportFaultBinding!!.etAddnote.text.toString().equals("")) {
                    submitfalutusingmultipartBulider()
                }else {
                    fragmentReportFaultBinding!!.etAddnote.requestFocus()
                   ToastAlert.CustomToastwornning(activity!!,"Enter note")
                }
            }else
                ToastAlert.CustomToastwornning(activity!!,"Please select site")
        }

    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Report Fault")
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ReportFaultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    private fun callApiForSiteList() {

        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callSiteListApi(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<SiteListModel> {
                override fun onResponse(call: Call<SiteListModel>, response: Response<SiteListModel>) {
                    customProgress.hideProgress()
                    if(response.code()==200) {
                        siteList = response.body()!!.row

                    }else if(response.code()==401){
                        Alert.showalertForUnAuthorized(activity!!,"Unauthorized")

                    }
                }

                override fun onFailure(call: Call<SiteListModel>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }
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
                val myDir = File("$root/mycomms/fault")
                myDir.mkdirs()
                /* val generator = Random()
                  var n = 100
                  n = generator.nextInt(n)*/
                val fname ="fault_image.jpg"
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
                   // imgFile=file
                    imagearraylist!!.add(file!!)
                } catch (e: FileNotFoundException) {
                    e.printStackTrace()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
                if (ImageSelectposition==1){
                    fragmentReportFaultBinding!!.imgFault1.visibility=View.VISIBLE
                    fragmentReportFaultBinding!!.imgCamera1.visibility=View.GONE
                    fragmentReportFaultBinding!!.tvImg1.visibility=View.GONE
                    fragmentReportFaultBinding!!.imgFault1.setImageBitmap(bm)

                }
                else if (ImageSelectposition==2){
                    fragmentReportFaultBinding!!.imgFault2.visibility=View.VISIBLE
                    fragmentReportFaultBinding!!.imgCamera2.visibility=View.GONE
                    fragmentReportFaultBinding!!.tvImg2.visibility=View.GONE
                    fragmentReportFaultBinding!!.imgFault2.setImageBitmap(bm)
                    //imagearraylist!!.add(imgFile!!)
                }
                else if (ImageSelectposition==3){
                    fragmentReportFaultBinding!!.imgFault3.visibility=View.VISIBLE
                    fragmentReportFaultBinding!!.imgCamera3.visibility=View.GONE
                    fragmentReportFaultBinding!!.tvImg3.visibility=View.GONE
                    fragmentReportFaultBinding!!.imgFault3.setImageBitmap(bm)
                  //  imagearraylist.add(imgFile!!)
                }
                else if (ImageSelectposition==4){
                    fragmentReportFaultBinding!!.imgFault4.visibility=View.VISIBLE
                    fragmentReportFaultBinding!!.imgCamera4.visibility=View.GONE
                    fragmentReportFaultBinding!!.tvImg4.visibility=View.GONE
                    fragmentReportFaultBinding!!.imgFault4.setImageBitmap(bm)
                   // imagearraylist.add(imgFile!!)
                }
                //fragmentAddSiteBinding!!.imgSelectedImage.setImageBitmap(bm)

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
            val myDir = File("$root/mycomms/fault")
            myDir.mkdirs()
            /* val generator = Random()
              var n = 100
              n = generator.nextInt(n)*/
            val fname ="fault_image.jpg"
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
                //imgFile=file
                imagearraylist.add(file)
                out.flush()
                out.close()
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

            if (ImageSelectposition==1){
                fragmentReportFaultBinding!!.imgFault1.visibility=View.VISIBLE
                fragmentReportFaultBinding!!.imgCamera1.visibility=View.GONE
                fragmentReportFaultBinding!!.tvImg1.visibility=View.GONE
                fragmentReportFaultBinding!!.imgFault1.setImageBitmap(thumbnail)
              //  imagearraylist.add(imgFile!!)
            }
            else if (ImageSelectposition==2){
                fragmentReportFaultBinding!!.imgFault2.visibility=View.VISIBLE
                fragmentReportFaultBinding!!.imgCamera2.visibility=View.GONE
                fragmentReportFaultBinding!!.tvImg2.visibility=View.GONE
                fragmentReportFaultBinding!!.imgFault2.setImageBitmap(thumbnail)
              //  imagearraylist.add(imgFile!!)
            }
            else if (ImageSelectposition==3){
                fragmentReportFaultBinding!!.imgFault3.visibility=View.VISIBLE
                fragmentReportFaultBinding!!.imgCamera3.visibility=View.GONE
                fragmentReportFaultBinding!!.tvImg3.visibility=View.GONE
                fragmentReportFaultBinding!!.imgFault3.setImageBitmap(thumbnail)
               // imagearraylist.add(imgFile!!)
            }
            else if (ImageSelectposition==4){
                fragmentReportFaultBinding!!.imgFault4.visibility=View.VISIBLE
                fragmentReportFaultBinding!!.imgCamera4.visibility=View.GONE
                fragmentReportFaultBinding!!.tvImg4.visibility=View.GONE
                fragmentReportFaultBinding!!.imgFault4.setImageBitmap(thumbnail)
               // imagearraylist.add(imgFile!!)
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }
       // fragmentAddSiteBinding!!.imgSelectedImage.setImageBitmap(thumbnail)
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

    fun submitfalutusingmultipartBulider(){

        val customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("site_id" ,selectedSiteId)
        builder.addFormDataPart("check_process_type", "checks")
        builder.addFormDataPart("fault_description", fragmentReportFaultBinding!!.etAddnote.text.toString())
        builder.addFormDataPart("status_id","1")
        builder.addFormDataPart("fault_entry_date",formattedDate)

        for (i in imagearraylist.indices) {
            builder.addFormDataPart("fault_image[]", imagearraylist.get(i).name, okhttp3.RequestBody.create(
                MediaType.parse("image/jpeg"), imagearraylist.get(i)))
        }
        //builder.addFormDataPart("fault_image", imagearraylist.get(0).name, okhttp3.RequestBody.create(MediaType.parse("image/jpeg"), imagearraylist.get(0)))

        val requestBody = builder.build()
        var request: Request? = null
        request = Request.Builder()
            .addHeader("Authorization", userdata!!.token)
            .addHeader("Content-Type","application/json")
            .url(NetworkUtility.BASE_URL + NetworkUtility.CREATE_FAULT)
            .post(requestBody)
            .build()

        val client = okhttp3.OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()

        val call = client.newCall(request)
        call.enqueue(object :okhttp3.Callback{
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                customProgress.hideProgress()
                System.out.println("response"+response)
                try {
                    var resStr :String=response.body()!!.string()
                    var response_obj= JSONObject(resStr)
                    //val response_obj = JSONObject(response.body()!!.string())
                    activity!!.runOnUiThread {
                        if (response_obj.getBoolean("status")) {
                            //   val check_process_log_id:String=response_obj.getInt("check_process_log_id").toString()
                          //  Toast.makeText(activity, response_obj.getString("message"), Toast.LENGTH_LONG).show()
                            ToastAlert.CustomToastSuccess(activity!!,response_obj.getString("message"))
                           // fragmentReportFaultBinding!!.etAddnote.setText("")
                             activity!!.getSupportFragmentManager().popBackStack()

                        } else {
                           // Toast.makeText(activity, response_obj.getString("message"), Toast.LENGTH_LONG).show()
                            ToastAlert.CustomToasterror(activity!!,response_obj.getString("message"))
                        }
                    }
                }
                catch (e: java.lang.Exception){
                    e.printStackTrace()
                    activity!!.runOnUiThread {
                        Toast.makeText(activity!!, "Try later. Something Wrong.", Toast.LENGTH_LONG).show()
                    }
                   // Toast.makeText(activity!!, "Try later. Something Wrong.", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                customProgress.hideProgress()
            }
        })

    }

}