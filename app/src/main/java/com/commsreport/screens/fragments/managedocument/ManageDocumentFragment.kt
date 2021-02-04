package com.commsreport.screens.fragments.managedocument

import android.app.ProgressDialog
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.FileProvider
import com.commsreport.BuildConfig
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.adapter.ManageDocumentAdapter
import com.commsreport.databinding.FragmentManageDocumentBinding
import com.commsreport.model.*

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
import java.util.*
import kotlin.collections.ArrayList

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class ManageDocumentFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    var manageDocumentBinding: FragmentManageDocumentBinding?=null
    var manageDocumentAdapter:ManageDocumentAdapter?=null
    var activity:HomeActivity?=null
    var siteList=ArrayList<SiteListModel.RowList>()
    var docList=ArrayList<DocumentListModel.DocumentItem>()
    var userdata: LoginResponseModel.Userdata? =null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        activity=getActivity() as HomeActivity

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        manageDocumentBinding= FragmentManageDocumentBinding.inflate(inflater,container,false)
        return manageDocumentBinding!!.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        callApiForDocList()


    }

    override fun onResume() {
        super.onResume()
        activity!!.homeBinding!!.mainView!!.tvHeaderText.setText("Manage Documents")
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

    private fun callApiForDocList() {
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!, "Please Wait..", false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("company_id", userdata!!.company_id)
            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.calldocumetList(userdata!!.token, gsonObject)
            callApi.enqueue(object : Callback<DocumentListModel> {
                override fun onResponse(call: Call<DocumentListModel>, response: Response<DocumentListModel>) {
                    customProgress.hideProgress()
                    if (response.code() == 200) {
                        docList = response.body()!!.row
                        if (docList.size > 0){
                            setAdapter()

                        }else
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
        manageDocumentAdapter= ManageDocumentAdapter(activity!!,docList,this)
        manageDocumentBinding!!.recManageDocument.adapter=manageDocumentAdapter
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
                        ToastAlert.CustomToastSuccess(activity!!,"A new file successfully downloaded in your internal storage comms folder")
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

}