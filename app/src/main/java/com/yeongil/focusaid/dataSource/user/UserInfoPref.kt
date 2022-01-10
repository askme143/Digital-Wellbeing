package com.yeongil.focusaid.dataSource.user

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit
import com.yeongil.focusaid.BuildConfig

class UserInfoPref(context: Context) {
    companion object {
        private const val EMAIL_KEY = "EMAIL"
        private const val USER_NAME_KEY = "USER_NAME"
    }

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(
            "${BuildConfig.APPLICATION_ID}.USER",
            Context.MODE_PRIVATE
        )

    fun getUserInfo(): UserInfoDto? {
        val userName = sharedPref.getString(USER_NAME_KEY, null)
        val email = sharedPref.getString(EMAIL_KEY, null)

        if (userName == null || email == null) return null

        return UserInfoDto(email, userName)
    }

    fun setUserInfo(userName: String, email: String) {
        sharedPref.edit {
            putString(USER_NAME_KEY, userName)
            putString(EMAIL_KEY, email)
            commit()
        }
    }
}