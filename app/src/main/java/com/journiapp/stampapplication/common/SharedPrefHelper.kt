package com.journiapp.stampapplication.common

import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import com.google.gson.Gson
import com.journiapp.stampapplication.model.UserInformation

/** save and load UserInformation (session token for authentication) from Shared Preferences */
class SharedPrefHelper(context: Context) : ContextWrapper(context) {

    val PREFS_NAME = "Session"
    val KEY_USER = "USERINFORMATION"
    val gson = Gson()

    protected val prefEditor: SharedPreferences.Editor
        get() = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE).edit()

    protected val sharedPrefs: SharedPreferences
        get() = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    fun saveUserInfo(userJson: UserInformation) {
        saveUserInfo(gson.toJson(userJson))
    }

    fun saveUserInfo(userJson: String) {
        val e = prefEditor
        e.putString(KEY_USER, userJson)
        e.commit()
    }

    fun loadUserInfoJson(): UserInformation? {
        val json = sharedPrefs.getString(KEY_USER, null)
        if (json == null || json == "") {
            return null
        } else {
            return gson.fromJson<UserInformation>(json, UserInformation::class.java)
        }
    }
}