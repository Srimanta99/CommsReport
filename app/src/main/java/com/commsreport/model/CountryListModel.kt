package com.commsreport.model

data class CountryListModel(val status:Boolean, val message:String, val row:ArrayList<CountryList>) {
    data class CountryList(val id:String,val country_name:String,val country_flag_path:String,val status_id:String, val status:String){

    }



}