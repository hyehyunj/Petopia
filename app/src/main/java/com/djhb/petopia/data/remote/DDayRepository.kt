package com.djhb.petopia.data.remote

import android.content.Context
import com.djhb.petopia.data.DDayModel

interface DDayRepository {
fun updateAlarm(context: Context, alarmSwitch: Boolean) : Boolean
}