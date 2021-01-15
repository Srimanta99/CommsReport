package com.wecompli.utils.sheardpreference

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences

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

    fun clerpreference() {
        editor.clear()
        editor.commit()
    }

    companion object {
        val MyPREFERENCES = "MyPrefs"
    }
}
