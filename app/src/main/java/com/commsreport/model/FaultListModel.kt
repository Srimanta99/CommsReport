package com.commsreport.model

data class FaultListModel(val status:Boolean,val message:String,val row:ArrayList<FaultList> ) {
    data class FaultList(val id: String,val fault_description:String,val created_at:String,val timeline:String,val status:String,val fault_files:Array<String>)
}