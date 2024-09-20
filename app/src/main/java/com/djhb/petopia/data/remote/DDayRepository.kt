package com.djhb.petopia.data.remote

import android.content.Context

interface DDayRepository {
fun updateAlarm(context: Context, alarmSwitch: Boolean) : Boolean
    fun loadAlarm(context: Context) : Boolean
}