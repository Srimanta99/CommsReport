package com.commsreport.model

data class EmailListModel(val status:Boolean,val message:String,val row:ArrayList<EmailRow>) {
    data class EmailRow(val email_id:String,val email:String,val name:String,val company_id:String,val site_id:String,val purpose:String,val status_id:String
    ,val added_by_userid:String,val updated_by_userid:String,val created_at:String,val updated_at:String)
}