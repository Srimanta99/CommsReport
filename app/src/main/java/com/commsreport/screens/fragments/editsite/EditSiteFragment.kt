package com.commsreport.screens.fragments.editsite

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
import com.bumptech.glide.Glide
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogEditSite
import com.commsreport.Utils.custompopupsite.CustomPopUpDialogSiteListAddUser
import com.commsreport.databinding.FragmentEditSiteBinding
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
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditSiteFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var editSiteBinding:FragmentEditSiteBinding?=null
    var siteDetails:SiteListModel.RowList?=null
    var activity: HomeActivity?=null
    var userdata: LoginResponseModel.Userdata? =null
    public  var selectedSiteId=""
    var image: String?=null
    var imgFile: File?=null
    var siteList=ArrayList<SiteListModel.RowList>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
         editSiteBinding =FragmentEditSiteBinding.inflate(inflater,container,false)
        return editSiteBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        siteDetails=AppSheardPreference(activity!!).getSite(PreferenceConstent.siteDetails)
        if(userdata!!.user_type.equals("COMPANY_ADMIN")){
            editSiteBinding!!.llSelectsite.visibility=View.VISIBLE
            callApiForSiteList()

        }else
            selectedSiteId=siteDetails!!.id
        editSiteBinding!!.tvEmail.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        editSiteBinding!!.etSiteName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        editSiteBinding!!.tvAddressSite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        editSiteBinding!!.etAddressSite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        editSiteBinding!!.tvPincode.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        editSiteBinding!!.etPinCode.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        editSiteBinding!!.tvUpload.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        // fragmentAddSiteBinding!!.etUpload.setTypeface(CustomTypeface.getWhitniBold(activity!!))
        editSiteBinding!!.tvBrowes.setTypeface(CustomTypeface.getRajdhaniBold(activity!!))
        editSiteBinding!!.tvSubmitSite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))

        editSiteBinding!!.etSiteName.setText(siteDetails!!.site_name)
        editSiteBinding!!.etAddressSite.setText(siteDetails!!.site_address)
        editSiteBinding!!.etPinCode.setText(siteDetails!!.site_postcode)
        if (siteDetails!!.site_logo_path!=null) {
            Glide.with(activity!!)
                .load(siteDetails!!.site_logo_path)
                .into(editSiteBinding!!.imgEditsiteImage);
        }
        editSiteBinding!!.tvBrowes.setOnClickListener {
            showAlertForChooseImage()
        }
        editSiteBinding!!.tvDropdownSelectsite.setOnClickListener {
            val customPopUpDialogEditSite= CustomPopUpDialogEditSite(activity, siteList, this)
            customPopUpDialogEditSite!!.show()
        }
        editSiteBinding!!.tvSubmitSite.setOnClickListener {
            if(checkblankvalidation())
                cllApiforUpdateSite()
        }
    }

    private fun cllApiforUpdateSite() {

        val customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val apiInterface = Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("company_id",userdata!!.company_id)
        builder.addFormDataPart("site_id" , siteDetails!!.id)
        builder.addFormDataPart("site_name", editSiteBinding!!.etSiteName.text.toString())
        builder.addFormDataPart("site_address", editSiteBinding!!.etAddressSite.text.toString())
        builder.addFormDataPart("site_postcode" , editSiteBinding!!.etPinCode.text.toString())
        builder.addFormDataPart("status_id" , "1")
        if (imgFile!=null)
            builder.addFormDataPart("site_logo",imgFile!!.absolutePath , okhttp3.RequestBody.create(
                MediaType.parse("image/jpeg"), imgFile))

        val requestBody = builder.build()
        var request: Request? = null
        request = Request.Builder()
            .addHeader("Authorization", userdata!!.token)
            .addHeader("Content-Type","application/json")
            .url(NetworkUtility.BASE_URL + NetworkUtility.EDIT_SITE)
            .post(requestBody)
            .build()

        val client = okhttp3.OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(100, TimeUnit.SECONDS)
            .readTimeout(100, TimeUnit.SECONDS)
            .build()

        val call = client.newCall(request)
        call.enqueue(object : okhttp3.Callback {
            override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
                customProgress.hideProgress()
                try {
                    var resStr :String=response.body()!!.string()
                    var response_obj: JSONObject = JSONObject(resStr)
                    activity!!.runOnUiThread {
                        if (response_obj.getBoolean("status")){
                            //   val check_process_log_id:String=response_obj.getInt("check_process_log_id").toString()
                            //callApiforfaultcreate(check_process_log_id);
                            ToastAlert.CustomToastSuccess(activity!!, response_obj.getString("message"))
                            activity!!.getSupportFragmentManager().popBackStack();
                            // Toast.makeText(activity, response_obj.getString("message"), Toast.LENGTH_LONG).show()
                        }else{
                            ToastAlert.CustomToasterror(activity!!, response_obj.getString("message"))

                        }
                    }
                    //val response_obj = JSONObject(response.body()!!.string())

                }
                catch (e: java.lang.Exception){
                    e.printStackTrace()
                    activity!!.runOnUiThread {
                        ToastAlert.CustomToasterror(activity!!, "Try later. Something Wrong.")
                    }


                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                customProgress.hideProgress()
            }
        })
    }

    private fun checkblankvalidation():Boolean{
        if (editSiteBinding!!.etSiteName.text.toString().equals("")){
            editSiteBinding!!.etSiteName.requestFocus()
            ToastAlert.CustomToastwornning(activity!!,"Enter site name")
            return false
        }
        if (editSiteBinding!!.etAddressSite.text.toString().equals("")){
            editSiteBinding!!.etAddressSite.requestFocus()
            ToastAlert.CustomToastwornning(activity!!,"Enter site address")
            return false
        }
        if (editSiteBinding!!.etPinCode.text.toString().equals("")){
            editSiteBinding!!.etPinCode.requestFocus()
            ToastAlert.CustomToastwornning(activity!!,"Enter site Post-code")
            return false
        }
        if (selectedSiteId.equals("")){
            ToastAlert.CustomToastwornning(activity!!,"Select site ")
            return false
        }
        return true

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
            ActivityCompat.requestPermissions(
                activity!!, arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
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
                val myDir = File("$root/mycomms/siteedit")
                myDir.mkdirs()
                /* val generator = Random()
                  var n = 100
                  n = generator.nextInt(n)*/
                val fname ="siteedit_image.jpg"
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
               editSiteBinding!!.imgEditsiteImage.setImageBitmap(bm)

            } catch (e: IOException) {
                e.printStackTrace()
            }

        }


    }
    private fun onCaptureImageResult(data: Intent) {
        val thumbnail = data.extras!!.get("data") as Bitmap?

        try {
             val root = Environment.getExternalStorageDirectory().toString()
             val myDir = File("$root/mycomms/siteedit")
             myDir.mkdirs()
            /* val generator = Random()
              var n = 100
              n = generator.nextInt(n)*/
            val fname ="uedit_image.jpg"
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
                editSiteBinding!!.imgEditsiteImage.setImageBitmap(thumbnail)
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }

        } catch (e: IOException) {
            e.printStackTrace()
        }

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
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Edit Site")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.VISIBLE


    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditSiteFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
    private fun callApiForSiteList() {

        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)

            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callSiteListApi(userdata!!.token, gsonObject)
            callApi.enqueue(object : Callback<SiteListModel> {
                override fun onResponse(call: Call<SiteListModel>, response: Response<SiteListModel>) {
                    customProgress.hideProgress()
                    if (response.code() == 200) {
                        siteList = response.body()!!.row
                      //  if (siteList.size == 0)
                          //  Alert.showalert(activity!!, "No user found")

                    } else if (response.code() == 401) {
                        Alert.showalertForUnAuthorized(activity!!, "Unauthorized")

                    }
                }

                override fun onFailure(call: Call<SiteListModel>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e: Exception){
            e.printStackTrace()
        }
    }
}