package com.commsreport.model

data class SiteUserListModel(val status:Boolean,val message:String, val row :ArrayList<UserList> )
{
    data class UserList(val id:String,val user_first_name:String,val user_email_ID:String,val user_contactno:String,val user_address:String
    ,val site_id:String,val site_name:String,val user_profile_picture_path:String,val created_at:String,val updated_at:String,val role_id:String,
    val user_role_type:String,val role_name:String,val status_id:String,val status:String)
}