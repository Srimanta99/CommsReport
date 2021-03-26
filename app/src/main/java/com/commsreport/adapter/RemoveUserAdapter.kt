package com.commsreport.adapter

import android.graphics.Color
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.commsreport.Utils.CustomTypeface
import com.commsreport.databinding.ItemRemoveUserBinding
import com.commsreport.model.SiteAndUserModel
import com.commsreport.screens.romovesiteuser.RemoveSiteUserActivity
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class RemoveUserAdapter(
    val activity: RemoveSiteUserActivity,
    val row_user: ArrayList<SiteAndUserModel.UserRow>?, ): RecyclerView.Adapter<RemoveUserAdapter.ViewHolder>() {
    var  itemView: ItemRemoveUserBinding?=null
    class ViewHolder(val itemView: ItemRemoveUserBinding) : RecyclerView.ViewHolder(itemView!!.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRemoveUserBinding.inflate(inflater)
        itemView=binding
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        itemView!!.tvName.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.emailId.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.sitename.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvCreatedat.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.tvDate.setTypeface(CustomTypeface.getRajdhaniMedium(activity!!))
        itemView!!.tvFault.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.tvDocument.setTypeface(CustomTypeface.getRajdhaniSemiBold(activity!!))
        itemView!!.tvName.setText(row_user!!.get(position).user_first_name)
        itemView!!.emailId.setText(row_user!!.get(position).user_email_ID)
        itemView!!.sitename.setText("Site name: " + row_user!!.get(position).site_name)
    //    val span:Spannable = SpannableString( itemView!!.sitename.getText())
     //   span.setSpan(ForegroundColorSpan(Color.BLACK), 0, "Site name:".length, Spannable.SPAN_INCLUSIVE_EXCLUSIVE)
      //  itemView!!.sitename.setText(span)
           try {
               val inputdateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
               val outdateFormat = SimpleDateFormat("dd, MMMM yyyy")
               var convertedDate = Date()
               convertedDate = inputdateFormat.parse(row_user.get(position).created_at)
               val finalsubDateString: String = outdateFormat.format(convertedDate)
               itemView!!.tvDate.setText(finalsubDateString)
           }catch (e:Exception){
               e.printStackTrace()
           }



        itemView!!.tvFault.setText("Fault " + row_user.get(position).fault_count)

        itemView!!.tvDocument.setText("Document " + row_user.get(position).document_count)
        if(row_user.get(position).user_profile_picture_path!=null){


            Glide.with(activity)
                .load(row_user.get(position).user_profile_picture_path)
                .centerCrop()
                .into(itemView!!.imgUser);
        }
        itemView!!.chkUser.setOnCheckedChangeListener { compoundButton, b ->
            if (b){
               activity.seletedUser.add(row_user.get(position).id.toString())
            }else{
                activity.seletedUser.remove(row_user.get(position).id.toString())
            }

        }



    }



    override fun getItemCount(): Int {
        return  row_user!!.size;
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
}