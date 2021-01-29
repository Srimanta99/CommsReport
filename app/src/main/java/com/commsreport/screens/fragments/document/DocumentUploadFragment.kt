package com.commsreport.screens.fragments.document

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.databinding.FragmentUploadDocumentsBinding
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SiteListModel
import com.commsreport.screens.fragments.site.REQUEST_CAMERA
import com.commsreport.screens.fragments.site.SELECT_FILE
import com.commsreport.screens.home.HomeActivity
import com.sculptee.utils.customprogress.CustomProgressDialog
import com.wecompli.network.NetworkUtility
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.Request
import org.json.JSONObject
import java.io.File
import java.io.FileNotFoundException
import java.io.FileOutputStream
import java.io.IOException
import java.util.*
import java.util.concurrent.TimeUnit

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class DocumentUploadFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var activity: HomeActivity?=null
    var File:File?=null
    var userdata: LoginResponseModel.Userdata? =null
    var fragmentUploadDocumentsBinding : FragmentUploadDocumentsBinding?=null
    internal var imagearraylist = ArrayList<File>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=  getActivity() as HomeActivity
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentUploadDocumentsBinding= FragmentUploadDocumentsBinding.inflate(inflater, container, false)
        return fragmentUploadDocumentsBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentUploadDocumentsBinding!!.tvEmailUploaddoc.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.etNameUploaddoc.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvStart.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvStartdate.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvExpiry.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvExpirydate.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.chkNotify.setTypeface(CustomTypeface.getWhitniBold(activity!!))
        fragmentUploadDocumentsBinding!!.tvUploadDoc.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvSubmitUploadDoc.setTypeface(CustomTypeface.getwhitMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvAttachment1.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))

        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        fragmentUploadDocumentsBinding!!.llCal1.setOnClickListener {
            datepickerdeStartDate()
        }
        fragmentUploadDocumentsBinding!!.llCal2.setOnClickListener {
            datepickerderEndDate()
        }
        fragmentUploadDocumentsBinding!!.tvUploadDoc.setOnClickListener {
            checkpermession()
        }

        fragmentUploadDocumentsBinding!!.tvSubmitUploadDoc.setOnClickListener {
            if (!fragmentUploadDocumentsBinding!!.etNameUploaddoc.equals("")){
                if (!fragmentUploadDocumentsBinding!!.tvStartdate.equals("")){
                    if (!fragmentUploadDocumentsBinding!!.tvExpirydate.equals("")){
                        callApiforUploadDoc()
                    }else
                        Alert.showalert(activity!!,"Select Expiry Date")
                }else
                    Alert.showalert(activity!!,"Select Start Date")
            }else
                fragmentUploadDocumentsBinding!!.etNameUploaddoc.requestFocus()

        }


    }

    private fun callApiforUploadDoc() {
        val customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("company_id" ,userdata!!.company_id)
        builder.addFormDataPart("document_name", fragmentUploadDocumentsBinding!!.etNameUploaddoc.text.toString())
        builder.addFormDataPart("affected_date", fragmentUploadDocumentsBinding!!.tvStartdate.text.toString())
        builder.addFormDataPart("expire_date",fragmentUploadDocumentsBinding!!.tvExpirydate.text.toString())
        builder.addFormDataPart("status_id","1")
        builder.addFormDataPart("notify_about_expiry","1")

        for (i in imagearraylist.indices) {
            builder.addFormDataPart("document_file[]", imagearraylist.get(i).name, okhttp3.RequestBody.create(
                MediaType.parse("image/jpeg"), imagearraylist.get(i)))
        }
        //builder.addFormDataPart("fault_image", imagearraylist.get(0).name, okhttp3.RequestBody.create(MediaType.parse("image/jpeg"), imagearraylist.get(0)))

        val requestBody = builder.build()
        var request: Request? = null
        request = Request.Builder()
            .addHeader("Authorization", userdata!!.token)
            .addHeader("Content-Type","application/json")
            .url(NetworkUtility.BASE_URL + NetworkUtility.UPLOAD_DOC)
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
                    if (response_obj.getBoolean("status")){
                        //   val check_process_log_id:String=response_obj.getInt("check_process_log_id").toString()
                        Toast.makeText(activity, response_obj.getString("message"), Toast.LENGTH_LONG).show()
                    }else{
                        Toast.makeText(activity, response_obj.getString("message"), Toast.LENGTH_LONG).show()

                    }
                }
                catch (e: java.lang.Exception){
                    e.printStackTrace()
                    // Toast.makeText(activity!!, "Try later. Something Wrong.", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                customProgress.hideProgress()
            }
        })
    }

    private fun checkpermession() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(
                activity!!, arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }else{
            galleryIntent()
        }
    }
    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        if (requestCode == 0) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                galleryIntent()
            }
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data)
        }
    }
    private fun onSelectFromGalleryResult(data: Intent?) {
        if (data != null) {
            if (data != null) {
                try {
                    val root = Environment.getExternalStorageDirectory().toString()
                    val myDir = File("$root/comos/document")
                    myDir.mkdirs()
                    val fname ="doc"
                    val file = File(myDir, fname)
                    val fo: FileOutputStream
                    if (file.exists())
                        file.delete()
                    try {
                        val out = FileOutputStream(file)

                        out.flush()
                        out.close()
                        File=file
                        imagearraylist.add(File!!)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                    fragmentUploadDocumentsBinding!!.llAttachment.visibility=View.VISIBLE
                    //fragmentAddSiteBinding!!.imgSelectedImage.setImageBitmap(bm)

                } catch (e: IOException) {
                    e.printStackTrace()
                }

            }
        }
    }
    private fun galleryIntent() {
        val mimeTypes = arrayOf(
                "application/msword",
                "application/vnd.openxmlformats-officedocument.wordprocessingml.document",  // .doc & .docx
                "application/vnd.ms-powerpoint",
                "application/vnd.openxmlformats-officedocument.presentationml.presentation",  // .ppt & .pptx
                "application/vnd.ms-excel",
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",  // .xls & .xlsx
                "text/plain",
                "application/pdf",
                "application/zip",
                "application/vnd.android.package-archive"
        )
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE)
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                DocumentUploadFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView.tvHeaderText.setText("Upload Documents")
    }

    fun datepickerdeStartDate() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            activity!!, R.style.MyTimePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val choosedate = padnumber(dayOfMonth) + "/" + padnumber(monthOfYear + 1) + "/" + year.toString()
                val checkdate = year.toString() + "-" + padnumber(monthOfYear + 1) + "-" + padnumber(dayOfMonth)
                val listcheckdate = padnumber(monthOfYear + 1) + "/" + padnumber(dayOfMonth) + "/" + year.toString()
                fragmentUploadDocumentsBinding!!.tvStartdate!!.setText(choosedate)

            }, mYear, mMonth, mDay
        )
        //  datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show()

    }
    fun datepickerderEndDate() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            activity!!, R.style.MyTimePickerDialogTheme,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val choosedate = padnumber(dayOfMonth) + "/" + padnumber(monthOfYear + 1) + "/" + year.toString()
                val checkdate = year.toString() + "-" + padnumber(monthOfYear + 1) + "-" + padnumber(dayOfMonth)
                val listcheckdate = padnumber(monthOfYear + 1) + "/" + padnumber(dayOfMonth) + "/" + year.toString()
                fragmentUploadDocumentsBinding!!.tvExpirydate!!.setText(choosedate)

            }, mYear, mMonth, mDay
        )
        //  datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show()

    }
    fun padnumber(n: Int): String {
        val num: String
        if (n > 10 || n == 10)
            num = n.toString()
        else
            num = "0$n"
        return num
    }

}