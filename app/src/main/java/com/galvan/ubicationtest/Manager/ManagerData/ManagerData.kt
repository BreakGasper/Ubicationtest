package com.adedeveloment.slotmachine.manager

import android.content.Context
import android.content.SharedPreferences
import com.galvan.ubicationtest.Data.GlobalConfigurations.GlobalConfigurations.KEY_USER
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ManagerData @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(KEY_USER, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveString(key: String, data: String) {
        editor.putString(key, data).apply()
    }

    fun saveInt(key: String, data: Int) {
        editor.putInt(key, data).apply()
    }

    fun saveBoolean(key: String, data: Boolean) {
        editor.putBoolean(key, data).apply()
    }

    fun saveFloat(key: String, data: Float) {
        editor.putFloat(key, data).apply()
    }

    fun saveLong(key: String, data: Long) {
        editor.putLong(key, data).apply()
    }

    // Métodos para obtener datos
    fun getString(key: String): String {
        return sharedPreferences.getString(key, "") ?: ""
    }

    fun getStringDate(key: String): String {
        return sharedPreferences.getString(key, "n") ?: "n"
    }

    fun getInt(key: String): Int {
        return sharedPreferences.getInt(key, 0)
    }

    fun getBoolean(key: String): Boolean {
        return sharedPreferences.getBoolean(key, false)
    }

    fun getBooleanNotification(key: String): Boolean {
        return sharedPreferences.getBoolean(key, true)
    }

    fun getFloat(key: String): Float {
        return sharedPreferences.getFloat(key, 0.0f)
    }

    fun getLong(key: String): Long {
        return sharedPreferences.getLong(key, 0L)
    }

    fun remove(key: String) {
        editor.remove(key).apply()
    }

    fun clear() {
        editor.clear().apply()
    }
}
