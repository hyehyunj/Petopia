package com.djhb.petopia.data.remote

import android.content.Context
import com.djhb.petopia.data.AlarmLocalDataSource
import com.djhb.petopia.data.DDayModel

class DDayRepositoryImpl(private val alarmLocalDataSource: AlarmLocalDataSource) :
DDayRepository {
    override fun updateAlarm(context: Context, alarmSwitch: Boolean): Boolean {
        return alarmLocalDataSource.updateAlarmData(context, alarmSwitch)
    }

    override fun loadAlarm(context: Context): Boolean {
        return alarmLocalDataSource.loadAlarmData(context)
    }

}