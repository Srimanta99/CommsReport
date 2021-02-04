package com.commsreport.model

data class DocumentListModel(val status:Boolean,val message:String,val row:ArrayList<DocumentItem>) {
    data class DocumentItem(val id:String,val document_name:String,val affected_date:String,val expire_date:String,val document_file:Array<String>,
    val created_at:String,val status_id:String,val status:String,val company_name:String,val site_name:String)
}