package com.davidcombita.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.davidcombita.R
import com.davidcombita.utils.SharedPreferenceHelper.UserInfoLogin.USER_EMAIL
import com.davidcombita.utils.SharedPreferenceHelper.UserInfoLogin.USER_LOGIN
import com.davidcombita.utils.SharedPreferenceHelper.UserInfoLogin.USER_NAME
import com.davidcombita.utils.SharedPreferenceHelper.UserInfoLogin.USER_TOKEN

class SharedPreferenceHelper(context: Context) {

    private var sp: SharedPreferences? = null
    private var prefsEditor: SharedPreferences.Editor? = null

    init {
        this.sp = context.getSharedPreferences(context.getString(R.string.preference_local_key), Context.MODE_PRIVATE)
        this.prefsEditor = sp?.edit()
    }

    fun saveIsLoggedIn(userName: String, userEmail: String, userToken: String): Boolean {
        return try {
            prefsEditor?.putBoolean(USER_LOGIN, true)
            prefsEditor?.putString(USER_NAME, userName)
            prefsEditor?.putString(USER_TOKEN, userToken)
            prefsEditor?.putString(USER_EMAIL, userEmail)
            prefsEditor?.apply()
            true
        }catch (e:Exception){
            Log.e("Error save preference", e.message.toString())
            false
        }
    }

    fun isUserLogin(): Boolean {
        return this.sp?.getBoolean(USER_LOGIN, false)?: false
    }

    fun getUserName(): String {
        return this.sp?.getString(USER_NAME, "")?: ""
    }

    protected object UserInfoLogin {
        const val USER_LOGIN = "USER_LOGIN"
        const val USER_NAME = "USER_NAME"
        const val USER_TOKEN = "USER_TOKEN"
        const val USER_EMAIL = "USER_EMAIL"
    }
}