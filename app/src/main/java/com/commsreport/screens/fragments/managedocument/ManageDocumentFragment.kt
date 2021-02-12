package com.commsreport.screens.fragments.managedocument

import android.Manifest
import android.content.pm.PackageManager
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.adapter.ManageDocumentAdapter
import com.commsreport.databinding.ContentManageDocumentBinding
import com.commsreport.databinding.FragmentManageDocumentBinding
import com.commsreport.model.DocumentListModel
import com.commsreport.model.LoginResponseModel
import com.commsreport.model.SiteListModel
import com.commsreport.screens.home.HomeActivity
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.sculptee.utils.customprogress.CustomProgressDialog
import com.wecompli.network.ApiInterface
import com.wecompli.network.Retrofit
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.net.HttpURLConnection

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"
var actionBarDrawerToggle: ActionBarDrawerToggle? = null
class ManageDocumentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var manageDocumentBinding: FragmentManageDocumentBinding?=null
    var contentManageDocumentBinding:ContentManageDocumentBinding?=null
    var manageDocumentAdapter:ManageDocumentAdapter?=null
    var activity:HomeActivity?=null
    var siteList=ArrayList<SiteListModel.RowList>()
    var docList=ArrayList<DocumentListModel.DocumentItem>()
    var userdata: LoginResponseModel.Userdata? =null
    var documentname=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        manageDocumentBinding= FragmentManageDocumentBinding.inflate(inflater, container, false)
        contentManageDocumentBinding=manageDocumentBinding!!.contentManageDocument
        return manageDocumentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        manageDocumentBinding!!.navDocSearch.tvSearch.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        manageDocumentBinding!!.navDocSearch.tvSearchByName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        manageDocumentBinding!!.navDocSearch.etsherchName.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
       // manageDocumentBinding!!.navDocSearch.tvSelectsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
      //  manageDocumentBinding!!.navDocSearch.tvDropdownSelectsite.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        manageDocumentBinding!!.navDocSearch.Search.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))

        manageDocumentBinding!!.contentManageDocument.tvHeaderText.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        contentManageDocumentBinding!!.imgMenu.setOnClickListener {
            activity!!.homeBinding!!.drawerLayout!!.openDrawer(Gravity.LEFT)
        }
        contentManageDocumentBinding!!.imgSearch.setOnClickListener {
            manageDocumentBinding!!.searchDrawer!!.openDrawer(Gravity.RIGHT)
        }
        manageDocumentBinding!!.imgNavclose.setOnClickListener {
            manageDocumentBinding!!.searchDrawer!!.closeDrawer(Gravity.RIGHT)
        }
        manageDocumentBinding!!.navDocSearch.Search.setOnClickListener {
            if (!manageDocumentBinding!!.navDocSearch.etsherchName.text.toString().equals("")){
                callApiForDocList(manageDocumentBinding!!.navDocSearch.etsherchName.text.toString())
            }else
                ToastAlert.CustomToastwornning(activity!!,"Please enter some value")
        }

        //manageDocumentBinding!!.navView.bringToFront()
       /* actionBarDrawerToggle = ActionBarDrawerToggle(
            activity,
            manageDocumentBinding!!.searchDrawer!!,
            R.string.app_name,
            R.string.app_name
        )

        manageDocumentBinding!!.searchDrawer!!.addDrawerListener(actionBarDrawerToggle!!)
        actionBarDrawerToggle!!.syncState()

        */
        callApiForDocList(documentname)


    }

    override fun onResume() {
        super.onResume()
       // activity!!.homeBinding!!.mainView!!.tvHeaderText.setText("Manage Documents")
        activity!!.homeBinding!!.mainView!!.rlheader.visibility=View.GONE
    }
    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ManageDocumentFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    public fun callApiForDocList(documentname: String) {
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)
           // paramObject.put("site_id", userdata!!.site_id)
            paramObject.put("document_name", documentname)
            paramObject.put("status_id", "1")
            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.calldocumetList(userdata!!.token, gsonObject)
            callApi.enqueue(object : Callback<DocumentListModel> {
                override fun onResponse(
                    call: Call<DocumentListModel>,
                    response: Response<DocumentListModel>
                ) {
                    customProgress.hideProgress()

                    if (response.code() == 200) {
                        docList.clear()
                        docList = response.body()!!.row
                        if (docList.size > 0) {
                            setAdapter()

                        } else
                            ToastAlert.CustomToasterror(activity!!, "No Document found")

                    } else if (response.code() == 401) {
                        Alert.showalertForUnAuthorized(activity!!, "Unauthorized")

                    }
                }

                override fun onFailure(call: Call<DocumentListModel>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e: Exception){
            e.printStackTrace()
        }
    }

    public fun setAdapter() {
        manageDocumentAdapter= ManageDocumentAdapter(activity!!, docList, this)
        manageDocumentBinding!!.contentManageDocument!!.recManageDocument.adapter=manageDocumentAdapter
    }
    public fun downloadfromUrl(quotationDownloadUrl: String, generatedQuotationFileName: String) {
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)

        class someTask() : AsyncTask<Void, Void, String>() {
            override fun doInBackground(vararg params: Void?): String? {
                try {
                    val url = java.net.URL(quotationDownloadUrl)
                    val c: HttpURLConnection = url.openConnection() as HttpURLConnection
                    c.setRequestMethod("GET")
                    c.setDoOutput(true)
                    c.connect()

                    val PATH = (Environment.getExternalStorageDirectory().toString()+"/" +"commms")
                    val file = File(PATH)
                    file.mkdirs()
                    val outputFile = File(file, generatedQuotationFileName)
                    val fos = FileOutputStream(outputFile)
                    val `is`: InputStream = c.inputStream

                    val buffer = ByteArray(4096)
                    var len1 = 0

                    while (`is`.read(buffer).also({ len1 = it }) != -1) {
                        fos.write(buffer, 0, len1)
                    }

                    fos.close()
                    `is`.close()
                    activity!!.runOnUiThread {
                        ToastAlert.CustomToastSuccess(
                            activity!!,
                            "A new file successfully downloaded in your internal storage comms folder"
                        )
                    }

                   /* activity!!.runOnUiThread {
                        //Toast.makeText(activity, " A new file is downloaded successfully in your internal storage Evision folder ",
                        //   Toast.LENGTH_LONG).show()
                        val authority = activity!!.applicationContext.packageName.toString() + ".fileprovider"
                        //val uriToFile: Uri = FileProvider.getUriForFile(activity!!, authority, outputFile)
                        val uriToFile: Uri =  FileProvider.getUriForFile(
                            Objects.requireNonNull(activity!!),
                            BuildConfig.APPLICATION_ID + ".provider", outputFile);
                        val shareIntent = Intent(Intent.ACTION_VIEW)
                        shareIntent.setDataAndType(uriToFile, "application/pdf")
                        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

                        try {
                            if (shareIntent.resolveActivity(activity!!.packageManager) != null) {
                                activity!!.startActivity(shareIntent)
                            }
                        } catch (e: ActivityNotFoundException) {
                            e.printStackTrace()
                            // Instruct the user to install a PDF reader here, or something
                        }
                    }
*/


                }catch (e: Exception){
                    e.printStackTrace()
                }
                return null
            }

            override fun onPreExecute() {
                super.onPreExecute()
                //customProgress.hideProgress()
                // ...
            }

            override fun onPostExecute(result: String?) {
                super.onPostExecute(result)
                customProgress.hideProgress()
                // ...
            }
        }
        someTask().execute();
    }

    public fun checkpermession():Boolean {
        if (ContextCompat.checkSelfPermission(
                activity!!,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) !== PackageManager.PERMISSION_GRANTED){ ActivityCompat.requestPermissions(
            activity!!, arrayOf<String>(
                Manifest.permission.CAMERA,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ), 0
        )
            return false
        }else
           return true

    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0) {
            if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
                && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

            }
        }
    }

}