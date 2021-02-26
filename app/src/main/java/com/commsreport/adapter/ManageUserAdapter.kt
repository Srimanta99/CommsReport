package com.commsreport.adapter

import android.app.Dialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commsreport.R
import com.commsreport.Utils.CustomTypeface
import com.commsreport.Utils.alert.Alert
import com.commsreport.Utils.alert.ToastAlert
import com.commsreport.databinding.ItemManageUserBinding

import com.commsreport.model.AddUserResponse
import com.commsreport.model.SiteUserListModel
import com.commsreport.screens.fragments.edituser.EditUserFragment
import com.commsreport.screens.fragments.manageuser.ManageUserFragment
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
import java.util.ArrayList

class ManageUserAdapter(
    val activity: HomeActivity,
    val userList: ArrayList<SiteUserListModel.UserList>,
    val manageUserFragment: ManageUserFragment
) : RecyclerView.Adapter<ManageUserAdapter.ViewHolder>() {
    var itemManageSitesBinding:ItemManageUserBinding?=null
    class ViewHolder(itemView: ItemManageUserBinding) : RecyclerView.ViewHolder(itemView.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         var inflater=LayoutInflater.from(parent.context)
         var bind=ItemManageUserBinding.inflate(inflater)
        itemManageSitesBinding=bind
        return ViewHolder(bind!!)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemManageSitesBinding!!.tvName.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity))
        itemManageSitesBinding!!.emailId.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemManageSitesBinding!!.teacherMobile.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemManageSitesBinding!!.tvLocation.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemManageSitesBinding!!.tvSite.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity))
        itemManageSitesBinding!!.tvSiteName.setTypeface(CustomTypeface.getRajdhaniMedium(activity))
        itemManageSitesBinding!!.tvName.setText(userList.get(position).user_first_name)
        itemManageSitesBinding!!.emailId.setText(userList.get(position).user_email_ID)
        if(!userList.get(position).user_contactno.equals(""))
           itemManageSitesBinding!!.teacherMobile.setText(userList.get(position).user_contactno)
        else
            itemManageSitesBinding!!.teacherMobile.setText("No contact number provided")
        itemManageSitesBinding!!.tvLocation.setText(userList.get(position).user_address)
        itemManageSitesBinding!!.tvSiteName.setText(userList.get(position).site_name)
       /* if (userList.get(position).user_contactno.equals("") || userList.get(position).user_contactno==null){
            itemManageSitesBinding!!.imgPhone.visibility=View.INVISIBLE

        }*/
        itemManageSitesBinding!!.rlEdit.setOnClickListener {
            AppSheardPreference(activity).setUserDetails(PreferenceConstent.selectedUser,userList.get(position))
            activity.openFragment(EditUserFragment())

        }
        itemManageSitesBinding!!.rlDelete.setOnClickListener {
            deleteUserAlert(userList.get(position),position)
        }

        Glide.with(activity)
            .load(userList.get(position).user_profile_picture_path)
            .centerCrop()
            .into( itemManageSitesBinding!!.imgUser);

        if (userList.get(position).status.equals("ACTIVE")){
            itemManageSitesBinding!!.imgStatus.setBackgroundResource(R.drawable.active)
        }else
            itemManageSitesBinding!!.imgStatus.setBackgroundResource(R.drawable.inactive)
    }

    override fun getItemCount(): Int {
        return  userList.size
    }

    fun  deleteUserAlert( userList: SiteUserListModel.UserList, position: Int) {
        val alertDialog = Dialog(activity!!, R.style.Transparent)
        /*alertDialog.setTitle(activity.resources.getString(R.string.app_name))
        alertDialog.setMessage(message)*/
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        val view: View = LayoutInflater.from(activity).inflate(R.layout.alert_layout_yesno, null)
        alertDialog.setContentView(view)
        alertDialog.setCancelable(false)
        val tv_message: TextView = view.findViewById(R.id.tv_message)
        val tv_ok: TextView = view.findViewById(R.id.tv_ok)
        val tv_no: TextView = view.findViewById(R.id.tv_no)
        tv_ok.typeface = CustomTypeface.getRajdhaniMedium(activity)
        tv_no.typeface = CustomTypeface.getRajdhaniMedium(activity)
        tv_message.typeface = CustomTypeface.getRajdhaniRegular(activity)
        tv_ok.setOnClickListener {
            alertDialog.dismiss()
            callApiforDeleteUser(userList,position)

        }
        tv_no.setOnClickListener {
            alertDialog.dismiss()

        }
        tv_message.setText("Are you want to delete this user?")
        alertDialog.show()
        /*alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
            DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
        alertDialog.show()*/

    }

    private fun callApiforDeleteUser(userList: SiteUserListModel.UserList, position: Int) {
        var userdata= AppSheardPreference(activity!!).getUser(PreferenceConstent.userData)
        val  customProgress: CustomProgressDialog = CustomProgressDialog().getInstance()
        customProgress.showProgress(activity!!,"Please Wait..",false)
        val apiInterface= Retrofit.retrofitInstance?.create(ApiInterface::class.java)
        try {
            val paramObject = JSONObject()
            paramObject.put("user_id",userList.id)
            var obj: JSONObject = paramObject
            var jsonParser: JsonParser = JsonParser()
            var gsonObject: JsonObject = jsonParser.parse(obj.toString()) as JsonObject;
            val callApi=apiInterface.callApifordeleteUser(userdata!!.token,gsonObject)
            callApi.enqueue(object : Callback<AddUserResponse> {
                override fun onResponse(call: Call<AddUserResponse>, response: Response<AddUserResponse>) {
                    customProgress.hideProgress()
                    if(response.code()==200) {
                        if(response.body()!!.status){
                            ToastAlert.CustomToastSuccess(activity,"User delete Successfully")
                            //  Alert.showalert(activity!!,"Fault Close Successfully")
                            manageUserFragment.userList.removeAt(position)
                            notifyDataSetChanged()
                        }

                    }else if(response.code()==401){
                        Alert.showalertForUnAuthorized(activity!!,"Unauthorized")

                    }
                }

                override fun onFailure(call: Call<AddUserResponse>, t: Throwable) {
                    customProgress.hideProgress()
                }
            })

        }catch (e:Exception){
            e.printStackTrace()
        }


    }
}