package com.commsreport.model

data class LoginResponseModel(val status:Boolean,val data:Userdata,val message:String) {
    data class Userdata(val user_id:String,val full_name:String,val email:String,val company_id:String,val company_name:String,
    val company_logo:String,val user_type:String,val user_profile_picture_path:String,val created_at:String,val token:String,val site_id:String,
    val site_name:String,val site_logo_path:String,val site_URL:String,val site_email:String,val site_contact_no:String,val site_status:String){

    }
}