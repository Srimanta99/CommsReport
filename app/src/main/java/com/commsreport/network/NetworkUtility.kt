package com.wecompli.network

class NetworkUtility {
    companion object{
     val BASE_URL ="http://commsapp.io/app/"
      //  val BASE_URL = "https://codopoliz.com/dev-app/comms/comms-api/"
       // val BASE_URL ="https://wecompli.io/rest/api/mobile/"
        const val  LOGIN="api/user/login"
        const val  SITELIST="api/site/list"
        const val  CREATE_SITE="api/site/create"
        const val  CREATE_FAULT="api/fault/create"
        const val  CREATE_USER="api/site/user/create"
        const val  SITE_USER_LIST="api/site/user/list"
        const val  SITE_USER_UPDATE="api/site/user/update"
        const val  UPLOAD_DOC="api/document/create"
        const val  FAULTLIST="api/fault/list"
        const val  FAULTREPAIR="api/fault/repair"
        const val  DOCUMENT_LIST="api/document/list"
        const val FAULTCOUNT="api/dashboard/information"
        const val DOCUMENT_REMOVE="api/document/remove"
        const val EDIT_USER="api/site/user/update"
        const val EDIT_SITE="api/site/update"
        const val PROFILE_UPDATE="api/user/profile-update"
        const val COUNTRYLIST="api/country/list"
        const val EMAILLIST="api/email/list"
        const val  EMAILCREATE="api/email/create"

    }
}