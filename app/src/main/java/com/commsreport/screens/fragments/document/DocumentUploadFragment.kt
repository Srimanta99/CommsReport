package com.commsreport.screens.fragments.document

import android.Manifest
import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.databinding.FragmentUploadDocumentsBinding
import com.commsreport.model.LoginResponseModel
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
import java.io.*
import java.util.*
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

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
    var docCount=1
    internal var imagearraylist = ArrayList<File>()
    var filetypeed= ArrayList<String>()
    var filetype:String?=""
    var notifyChk="0"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=  getActivity() as HomeActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        fragmentUploadDocumentsBinding= FragmentUploadDocumentsBinding.inflate(inflater, container, false)
        return fragmentUploadDocumentsBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentUploadDocumentsBinding!!.tvEmailUploaddoc.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!)
        )
        fragmentUploadDocumentsBinding!!.etNameUploaddoc.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!)
        )
        fragmentUploadDocumentsBinding!!.tvStart.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvStartdate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!)
        )
        fragmentUploadDocumentsBinding!!.tvExpiry.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvExpirydate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentUploadDocumentsBinding!!.chkNotify.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvUploadDoc.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvSubmitUploadDoc.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvnotename.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        fragmentUploadDocumentsBinding!!.tvfeildsareall.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
       // fragmentUploadDocumentsBinding!!.tvAttachment1.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        val textnote="<font color=#FE0100>Note: </font> <font color=#1E3F6C>Please provide a relevant document name, it will help you to searching the document</font>";
        fragmentUploadDocumentsBinding!!.tvnotename.setText(Html.fromHtml(textnote))

        val textnote1="<font color=#FE0100>Note: [*]</font> <font color=#1E3F6C>fields are all mandatory fields</font>";
        fragmentUploadDocumentsBinding!!.tvfeildsareall.setText(Html.fromHtml(textnote1))


        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        fragmentUploadDocumentsBinding!!.llCal1.setOnClickListener {
            datepickerdeStartDate()
        }
        fragmentUploadDocumentsBinding!!.llCal2.setOnClickListener {
            datepickerderEndDate()
        }
        fragmentUploadDocumentsBinding!!.tvStartdate.setOnClickListener {
            datepickerdeStartDate()
        }
        fragmentUploadDocumentsBinding!!.tvExpirydate.setOnClickListener {
            datepickerderEndDate()
        }
        fragmentUploadDocumentsBinding!!.tvUploadDoc.setOnClickListener {
            if (docCount<=4)
                checkpermession()
            else
                ToastAlert.CustomToastwornning(activity!!, "Max. 4 attachments allow")
        }

        fragmentUploadDocumentsBinding!!.tvSubmitUploadDoc.setOnClickListener {
            if (!fragmentUploadDocumentsBinding!!.etNameUploaddoc.text.toString().equals("")){
                if (fragmentUploadDocumentsBinding!!.chkNotify.isChecked) {
                    notifyChk="1"
                    if (!fragmentUploadDocumentsBinding!!.tvStartdate.text.toString().equals("")) {
                        if (!fragmentUploadDocumentsBinding!!.tvExpirydate.text.toString().equals("")) {
                            if (imagearraylist.size > 0)
                                callApiforUploadDoc()
                            else
                                ToastAlert.CustomToastwornning(activity!!, "Select at least one document file")
                        } else
                        //Alert.showalert(activity!!, "Select Expiry Date")
                            ToastAlert.CustomToastwornning(activity!!, "Select Expiry Date")
                    } else
                        ToastAlert.CustomToastwornning(activity!!, "Select Start Date")
                    //Alert.showalert(activity!!, "Select Start Date")
                }else{
                    notifyChk="0"
                    if (imagearraylist.size > 0){
                        callApiforUploadDoc()
                    }
                    else
                        ToastAlert.CustomToastwornning(activity!!, "Select at least one document file")
                }
            }else{
                fragmentUploadDocumentsBinding!!.etNameUploaddoc.requestFocus()
                ToastAlert.CustomToastwornning(activity!!, "Enter Document Name")
            }


        }


    }

    private fun callApiforUploadDoc() {
        val customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val builder = MultipartBody.Builder().setType(MultipartBody.FORM)
        builder.addFormDataPart("company_id", userdata!!.company_id)
        builder.addFormDataPart("document_name", fragmentUploadDocumentsBinding!!.etNameUploaddoc.text.toString())
        builder.addFormDataPart("affected_date", fragmentUploadDocumentsBinding!!.tvStartdate.text.toString())
        builder.addFormDataPart("expire_date", fragmentUploadDocumentsBinding!!.tvExpirydate.text.toString())
        builder.addFormDataPart("status_id", "1")
        builder.addFormDataPart("notify_about_expiry", notifyChk)

        for (i in imagearraylist.indices) {
            builder.addFormDataPart("document_file[]", imagearraylist.get(i).name, okhttp3.RequestBody.create(
                    MediaType.parse(filetypeed.get(i)), imagearraylist.get(i)
                )
            )
        }
        //builder.addFormDataPart("fault_image", imagearraylist.get(0).name, okhttp3.RequestBody.create(MediaType.parse("image/jpeg"), imagearraylist.get(0)))

        val requestBody = builder.build()
        var request: Request? = null
        request = Request.Builder()
            .addHeader("Authorization", userdata!!.token)
            .addHeader("Content-Type", "application/json")
            .url(NetworkUtility.BASE_URL + NetworkUtility.UPLOAD_DOC)
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
                System.out.println("response" + response)
                try {
                    var resStr: String = response.body()!!.string()
                    var response_obj = JSONObject(resStr)
                    activity!!.runOnUiThread {
                        if (response_obj.getBoolean("status")) {
                            //   val check_process_log_id:String=response_obj.getInt("check_process_log_id").toString()
                            //Toast.makeText(activity, response_obj.getString("message"), Toast.LENGTH_LONG).show()
                            ToastAlert.CustomToastSuccess(
                                activity!!,
                                response_obj.getString("message")
                            )
                            activity!!.getSupportFragmentManager().popBackStack();
                        } else {
                            //Toast.makeText(activity, response_obj.getString("message"), Toast.LENGTH_LONG).show()
                            ToastAlert.CustomToasterror(
                                activity!!,
                                response_obj.getString("message")
                            )
                        }
                    }
                    //val response_obj = JSONObject(response.body()!!.string())

                } catch (e: java.lang.Exception) {
                    e.printStackTrace()
                    activity!!.runOnUiThread {
                        ToastAlert.CustomToasterror(activity!!, "Try later. Something Wrong.")
                    }
                    // Toast.makeText(activity!!, "Try later. Something Wrong.", Toast.LENGTH_LONG).show()

                }
            }

            override fun onFailure(call: okhttp3.Call, e: IOException) {
                customProgress.hideProgress()
            }
        })
    }

    private fun checkpermession() {
        if (ContextCompat.checkSelfPermission(activity!!, Manifest.permission.WRITE_EXTERNAL_STORAGE) !== PackageManager.PERMISSION_GRANTED){ ActivityCompat.requestPermissions(
            activity!!, arrayOf<String>(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE), 0)
        }else{
            galleryIntent()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray) {
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
        var bm: Bitmap? = null

            if (data != null) {
                val selectedfile: Uri? = data!!.getData()
                filetype=getMimeType(selectedfile!!)

                if (filetype!!.contains("image")){
                    filetypeed.add(filetype!!)
                try {
                    bm = MediaStore.Images.Media.getBitmap(activity!!.contentResolver, data.data)
                    /*  val bytes = ByteArrayOutputStream()
                      bm!!.compress(Bitmap.CompressFormat.JPEG, 90, bytes)
                      val destination = File(Environment.getExternalStorageDirectory(), "fault_image"+ ".jpg")*/
                    // val destination = File(Environment.getExternalStorageDirectory(), System.currentTimeMillis().toString() + ".jpg")
                    val root = Environment.getExternalStorageDirectory().toString()
                    val myDir = File("$root/mycomms/document")
                    myDir.mkdirs()
                    /* val generator = Random()
                      var n = 100
                      n = generator.nextInt(n)*/
                    val fname ="doc"+docCount+".jpg"
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
                        File=file
                        imagearraylist.add(File!!)
                    } catch (e: FileNotFoundException) {
                        e.printStackTrace()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                    val view: View = LayoutInflater.from(activity).inflate(R.layout.item_attachment_view, null)
                    val tvattachment:TextView=view.findViewById(R.id.tv_attachment1)
                    tvattachment.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
                    tvattachment.setText("Attachment " + docCount)
                    fragmentUploadDocumentsBinding!!.llAttachment.addView(view)
                    docCount++
                    //  fragmentUploadDocumentsBinding!!.llAttachment.visibility=View.VISIBLE
                    //fragmentAddSiteBinding!!.imgSelectedImage.setImageBitmap(bm)

                } catch (e: IOException) {
                    e.printStackTrace()
                }
                }else if (filetype!!.contains("pdf")){
                    val uri = data.data
                    filetypeed.add(filetype!!)
                    try {
                        val root = Environment.getExternalStorageDirectory().toString()
                        val myDir = File("$root/mycomms/document")
                        myDir.mkdirs()

                        val fname ="doc"+docCount+".pdf"
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

                            val `is`: InputStream = activity!!.getContentResolver().openInputStream(uri!!)!!;

                            val buffer = ByteArray(4096)
                            var len1 = 0

                            while (`is`.read(buffer).also({ len1 = it }) != -1) {
                                out.write(buffer, 0, len1)
                            }

                            out.close()
                            `is`.close()
                            File=file
                            imagearraylist.add(File!!)
                        } catch (e: FileNotFoundException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }
                        val view: View = LayoutInflater.from(activity).inflate(
                            R.layout.item_attachment_view,
                            null
                        )
                        val tvattachment:TextView=view.findViewById(R.id.tv_attachment1)
                        tvattachment.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
                        tvattachment.setText("Attachment " + docCount)
                        fragmentUploadDocumentsBinding!!.llAttachment.addView(view)
                        docCount++

                    } catch (e: IOException) {
                    e.printStackTrace()
                }

                }else
                    ToastAlert.CustomToasterror(activity!!, "Only image and pdf file is allowed")

            }

    }
    private fun galleryIntent() {
        /*  val mimeTypes = arrayOf("image/*", "application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.addCategory(Intent.CATEGORY_OPENABLE)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent.type = if (mimeTypes.size === 1) mimeTypes[0] else "*/*"
            if (mimeTypes.size > 0) {
                intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            }
        } else {
            var mimeTypesStr = ""
            for (mimeType in mimeTypes) {
                mimeTypesStr += "$mimeType|"
            }
            intent.type = mimeTypesStr.substring(0, mimeTypesStr.length - 1)
        }
        startActivityForResult(Intent.createChooser(intent, "ChooseFile"), SELECT_FILE);*/
        val mimeTypes = arrayOf("image/*", "application/pdf")
        val intent = Intent(Intent.ACTION_GET_CONTENT)
            .setType("image/*|application/pdf")
            .putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)

     //  val intent = Intent()
      // intent.type = "image/*"
       // intent.action = Intent.ACTION_GET_CONTENT
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
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.VISIBLE
    }

    fun datepickerdeStartDate() {
        val c = Calendar.getInstance()
        val mYear = c.get(Calendar.YEAR)
        val mMonth = c.get(Calendar.MONTH)
        val mDay = c.get(Calendar.DAY_OF_MONTH)


        val datePickerDialog = DatePickerDialog(
            activity!!, R.style.AppDatepickerDilogtheam,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val choosedate =
                    padnumber(dayOfMonth) + "/" + padnumber(monthOfYear + 1) + "/" + year.toString()
                // val checkdate = year.toString() + "-" + padnumber(monthOfYear + 1) + "-" + padnumber(dayOfMonth)
                //  val listcheckdate = padnumber(monthOfYear + 1) + "/" + padnumber(dayOfMonth) + "/" + year.toString()
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
            activity!!, R.style.AppDatepickerDilogtheam,
            DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val choosedate =
                    padnumber(dayOfMonth) + "/" + padnumber(monthOfYear + 1) + "/" + year.toString()
                // val checkdate = year.toString() + "-" + padnumber(monthOfYear + 1) + "-" + padnumber(dayOfMonth)
                // val listcheckdate = padnumber(monthOfYear + 1) + "/" + padnumber(dayOfMonth) + "/" + year.toString()
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
    fun getMimeType(path: String, context: Context?): String? {
        val extention = path.substring(path.lastIndexOf("."))
        val mimeTypeMap: String = MimeTypeMap.getFileExtensionFromUrl(extention)
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(mimeTypeMap)
    }
    fun getMimeType(uri: Uri): String? {
        var mimeType: String? = null
        mimeType = if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            val cr: ContentResolver = activity!!.getContentResolver()
            cr.getType(uri)
        } else {
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(
                uri
                    .toString()
            )
            MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase()
            )
        }
        return mimeType
    }
}