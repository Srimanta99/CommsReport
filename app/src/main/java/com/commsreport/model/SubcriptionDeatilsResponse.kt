package com.commsreport.model

data class SubcriptionDeatilsResponse(val status:Boolean, val message:String, val row:Subcription ) {
    data class Subcription(val id:String,val payment_gateway_pk:String, val package_name:String,val short_description:String,val package_type:String,val is_user_unlimited:String,
    val user_count:String,val is_site_unlimited:String, val site_count:String,val package_price:String,val payment_gateway_pk_PRICE:String,val subscription_date:String,val status_id:String,val status:String,val last_billing_date:String,
                           val next_billing_date:String
    ){

    }

}