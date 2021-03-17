package com.commsreport.model

import java.io.Serializable

data class SubCriptionPackagResponseemodel(val status:Boolean,val message:String,var row:ArrayList<PackageItem>) {
    data class  PackageItem(val id: String, val package_name:String,val short_description:String,val package_type:String,
                            val is_user_unlimited:String,val user_count:String, val is_site_unlimited:String, val site_count: String,val package_price:String,val status_id:String,val status:String, var ischeck:Boolean): Serializable{

    }
}