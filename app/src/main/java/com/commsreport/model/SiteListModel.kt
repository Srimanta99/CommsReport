package com.commsreport.model

data class SiteListModel(val status:Boolean,val message:String,val row :ArrayList<RowList>) {
    data class RowList(val  id:String,val site_name:String,val site_logo_path:String,val company_name:String,val company_logo_path:String,val ite_UR:String,
    val site_email:String,val site_contact_no:String,val site_address:String,val site_business_starttime:String,val site_business_closingtime:String,val site_postcode:String,
                       val country_flag_path:String,val country_name:String,val status_id:String, val status:String){

    }
}