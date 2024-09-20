package com.djhb.petopia.data

import android.content.Context

object AlarmLocalDataSource {

    fun updateAlarmData(context: Context, alarmSwitch: Boolean): Boolean {

        val pref = context.getSharedPreferences("pref", 0)
        val edit = pref.edit()
        edit.putString("ALARM", alarmSwitch.toString())
        edit.apply()


        return loadAlarmData(context)
    }

    fun loadAlarmData(context: Context): Boolean {
        val pref = context.getSharedPreferences("pref", 0)
        val alarmData = pref.getString("ALARM", "").toBoolean()

        return alarmData
    }
}










