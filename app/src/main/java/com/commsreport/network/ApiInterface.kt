package com.wecompli.network

import com.commsreport.model.*
import com.google.gson.JsonObject

import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*

interface ApiInterface {

    @POST(NetworkUtility.LOGIN)
    fun callLogInApi(@Body body: JsonObject): Call<LoginResponseModel>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.SITELIST)
    fun callSiteListApi(@Header("Authorization") token:String,@Body body: JsonObject): Call<SiteListModel>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.CREATE_USER)
    fun caallCreateUserApi(@Header("Authorization") token:String, @Body body: JsonObject): Call<AddUserResponse>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.SITE_USER_UPDATE)
    fun callSiteUserUpdateApi(@Header("Authorization") token:String, @Body body: JsonObject): Call<AddUserResponse>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.SITE_USER_LIST)
    fun callSiteUserListApi(@Header("Authorization") token:String, @Body body: JsonObject): Call<SiteUserListModel>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.FAULTLIST)
    fun callFaultApi(@Header("Authorization") token:String, @Body body: JsonObject): Call<FaultListModel>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.FAULTREPAIR)
    fun callApiforFaultRepair(@Header("Authorization") token:String, @Body body: JsonObject): Call<AddUserResponse>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.DOCUMENT_LIST)
    fun calldocumetList(@Header("Authorization") token:String,@Body body: JsonObject): Call<DocumentListModel>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.FAULTCOUNT)
    fun callFaultCountApt(@Header("Authorization") token:String,@Body body: JsonObject): Call<FaultCountModel>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.DOCUMENT_REMOVE)
    fun callApifordocumentremove(@Header("Authorization") token:String, @Body body: JsonObject): Call<AddUserResponse>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.PROFILE_UPDATE)
    fun callApifordochangepassword(@Header("Authorization") token:String, @Body body: JsonObject): Call<AddUserResponse>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.COUNTRYLIST)
    fun callApiforcountrylist(@Header("Authorization") token:String, @Body body: JsonObject): Call<CountryListModel>


    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.EMAILLIST)
    fun callApiforemaillist(@Header("Authorization") token:String, @Body body: JsonObject): Call<EmailListModel>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.EMAILCREATE)
    fun callApiforemailadd(@Header("Authorization") token:String, @Body body: JsonObject): Call<AddUserResponse>

    // @Headers("Content-Type: application/json")
    /*@POST(NetworkUtility.LOG_IN)
    fun callLogInApi(@Field("user_email") email:String,
                     @Field("password") pass:String,
                     @Field("identification_id_token") identification_id_token:String,
                     @Field("device_model") device_model:String,
                     @Field("device_os") device_os:String): Call<LoginResponse>
*/


  /*  @Headers("Content-Type: application/json")
    @POST(NetworkUtility.LOG_IN)
    fun callLogInApi(@Body body: JsonObject): Call<LoginResponse>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.TODAYSEASON)
    fun calltodayseason(@Header("Authorization") token:String,@Body body: JsonObject): Call<SeasonListApiresponse>

   @Headers("Content-Type: application/json")
   @POST(NetworkUtility.REGENERATE_TOKEN)
   fun regeratetoken(@Body body: JsonObject) : Call<RegenerateTokenResponse>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.COMPONENTCHECKLIST)
    fun callcomponetChecklist(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject): Call<CheckSummeryResponse>


    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.COMPONENT_CHECK_ELEMENTDE_DETAILS)
    fun callcomponetChecklelementdetails(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject): Call<CheckElementDetailsResponse>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.COMPONENET_CHECK_SUBMIT)
    fun callcomponetCheckSubmit(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.FAULT_LIST)
    fun callApiforfaultlist(@Header("Content-Type") contenttype:String, @Header("Authorization") token:String, @Header("site_id") site_id:String, @Body body: JsonObject): Call<FaultApiResponse>


    @Multipart
    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.COMPONENET_CHECK_SUBMIT)
    fun callApifaultsubmitusingImsage(@Header("Authorization") token:String,
                                      @Header("site_id") site_id:String,
                                      @Part("check_id") check_id:RequestBody,
                                      @Part("season_id") season_id:RequestBody,
                                      @Part("check_type_id") check_type_id:RequestBody,
                                      @Part("check_type_values_id") check_type_values_id:RequestBody,
                                      @Part("check_process_type") check_process_type:RequestBody,
                                      @Part("check_date") check_date:RequestBody,
                                      @Part("process_remark") process_remark:RequestBody,
                                      @Part("process_status") process_status:RequestBody,
                                      @Part process_file: MultipartBody.Part):Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.FAULTCREATE)
    fun callcreatefaultApi(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject): Call<ResponseBody>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.FAULTDETAILS)
    fun callfaultdetailsapi(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject): Call<FaultadetailsModel>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.ADHOCFAULTDETAILS)
    fun callAdhocfaultdetailsapi(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject): Call<FaultadetailsModel>

    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.FAULSTATUSLIST)
    fun callfaultstatuslist(@Header("Authorization") token:String,@Header("site_id") site_id:String): Call<FaultStatusListModel>


    @Headers("Content-Type: application/json")
    @POST(NetworkUtility.FAULTSTATUSMESSAGE_CHANGE)
    fun callfaultstatusmessagechange(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject): Call<FaultStatusMessageChange>

   // @Headers("Content-Type: application/json")
    @POST(NetworkUtility.FAULTBYSCAN)
    fun callfaultdetailsbyscan(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject): Call<FaultDetailsByScanModel>

    @POST(NetworkUtility.SERVERTYLEVEL)
    fun callApiforSERVRITYLABEL(@Header("Content-Type") contenttype:String, @Header("Authorization") token:String,@Body body: JsonObject): Call<ServrityModel>

    @POST(NetworkUtility.LOCATIONLIST)
    fun calllocationlistapi(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject)  :Call<LocatioApiResponse>

    @POST(NetworkUtility.CREATEINCIDENTREPORT)
    fun calllCreateIncidentapi(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject)  :Call<ResponseBody>


    @POST(NetworkUtility.REMOVEFAULT)
    fun calllApiForRemoveFalt(@Header("Authorization") token:String,@Header("site_id") site_id:String, @Body body: JsonObject)  :Call<ResponseBody>*/

}