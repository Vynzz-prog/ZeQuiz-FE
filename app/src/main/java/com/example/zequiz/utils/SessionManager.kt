package com.example.zequiz.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)

    companion object {
        private const val PREFS_NAME = "zequiz_prefs"
        private const val USER_TOKEN = "user_token"
        private const val USERNAME = "username"
        private const val ROLE = "role"
    }

    fun saveAuthToken(token: String) {
        val editor = prefs.edit()
        editor.putString(USER_TOKEN, token)
        editor.apply()
    }

    fun fetchAuthToken(): String? {
        return prefs.getString(USER_TOKEN, null)
    }

    fun saveUsername(username: String) {
        val editor = prefs.edit()
        editor.putString(USERNAME, username)
        editor.apply()
    }

    fun fetchUsername(): String? {
        return prefs.getString(USERNAME, null)
    }

    fun saveRole(role: String) {
        val editor = prefs.edit()
        editor.putString(ROLE, role)
        editor.apply()
    }

    fun fetchRole(): String? {
        return prefs.getString(ROLE, null)
    }

    fun clearSession() {
        val editor = prefs.edit()
        editor.clear()
        editor.apply()
    }
}
