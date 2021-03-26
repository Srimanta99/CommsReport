package com.commsreport.model

data class SiteAndUserModel(val status:Boolean,val message:String,val row_user:ArrayList<UserRow>,val row_site:ArrayList<SiteRow>) {
    data class UserRow(val id:Int, val user_first_name:String,val user_email_ID:String,val user_contactno:String,val user_address:String,val site_id:String
    ,val site_name:String,val user_profile_picture_path:String,val user_country_id:String,val country_name:String,val country_flag:String,val created_at:String,
    val status:String,val fault_count:Int,val document_count:Int)

    data class SiteRow(val id:String,val site_name:String,val site_logo_path:String,val company_name:String,val company_logo_path:String,
    val user_count:Int,val fault_count:Int,val  created_at:String)
}