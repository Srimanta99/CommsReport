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
import com.commsreport.databinding.ItemManageUserBinding
import com.commsreport.model.FaultListModel
import com.commsreport.model.SiteUserListModel
import com.commsreport.screens.fragments.edituser.EditUserFragment
import com.commsreport.screens.home.HomeActivity
import com.wecompli.utils.sheardpreference.AppSheardPreference
import com.wecompli.utils.sheardpreference.PreferenceConstent
import java.util.ArrayList

class ManageUserAdapter(val activity: HomeActivity,val userList: ArrayList<SiteUserListModel.UserList>) : RecyclerView.Adapter<ManageUserAdapter.ViewHolder>() {
    var itemManageSitesBinding:ItemManageUserBinding?=null
    class ViewHolder(itemView:  ItemManageUserBinding) : RecyclerView.ViewHolder(itemView.root)

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
        itemManageSitesBinding!!.tvName.setText(userList.get(position).user_first_name)
        itemManageSitesBinding!!.emailId.setText(userList.get(position).user_email_ID)
        itemManageSitesBinding!!.teacherMobile.setText(userList.get(position).user_contactno)
        itemManageSitesBinding!!.tvLocation.setText(userList.get(position).user_address)
        if (userList.get(position).user_contactno.equals("") || userList.get(position).user_contactno==null){
            itemManageSitesBinding!!.imgPhone.visibility=View.INVISIBLE

        }
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
}