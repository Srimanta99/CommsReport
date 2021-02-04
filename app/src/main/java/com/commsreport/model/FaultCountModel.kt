package com.commsreport.model

data class FaultCountModel(val status:Boolean,val message:String,val fault_count:Int,val response:CountResponse) {
    data class CountResponse(val fault_count:Int)
}