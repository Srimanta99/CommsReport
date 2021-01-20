package com.wecompli.utils.sheardpreference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import com.commsreport.model.LoginResponseModel
import com.google.gson.Gson

class AppSheardPreference(activity: Activity) {
    internal var sharedpreferences: SharedPreferences
    internal var editor: SharedPreferences.Editor

    init {
        sharedpreferences = activity.getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE)
        editor = sharedpreferences.edit()
    }


    fun setvalue_in_preference(key: String, value: String) {
        editor.putString(key, value)
        editor.commit()
        editor.apply()
    }

    fun getvalue_in_preference(key: String): String {
        return sharedpreferences.getString(key, "")!!
    }

    fun setUserData(key: String,user:LoginResponseModel.Userdata){
       // val editor=sharedpreferences!!.edit()
        editor.putString(key, Gson().toJson(user))
        editor.apply()
    }

    fun getUser(key: String):LoginResponseModel.Userdata?{
        var sata = sharedpreferences!!.getString(key, "")
        return Gson().fromJson(sata,LoginResponseModel.Userdata::class.java)
    }

    fun clerpreference() {
        editor.clear()
        editor.commit()
    }

    companion object {
        val MyPREFERENCES = "MyPrefs"
    }

}
