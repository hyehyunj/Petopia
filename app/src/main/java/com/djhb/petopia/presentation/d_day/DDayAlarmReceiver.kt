package com.djhb.petopia.presentation.d_day

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.djhb.petopia.R
import com.djhb.petopia.presentation.MainActivity
import com.djhb.petopia.presentation.register.RegisterActivity

//디데이 알람 리시버
class DDayAlarmReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        if (intent != null) {
            val activityIntent = Intent(context, RegisterActivity::class.java).apply {
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }
            val pendingIntent = PendingIntent.getActivity(
                context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT or
                        PendingIntent.FLAG_IMMUTABLE
            )

            val manager =
                context?.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            val builder: NotificationCompat.Builder
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                val channelId = "one-channel"
                val channelName = "디데이 알림"
                val importance = NotificationManager.IMPORTANCE_DEFAULT
                val channel = NotificationChannel(
                    channelId,
                    channelName,
                    importance
                )
                manager.createNotificationChannel(channel)
                builder = NotificationCompat.Builder(context, channelId)
            } else {
                builder = NotificationCompat.Builder(context)
            }
            builder.run {
                setSmallIcon(R.drawable.icon_heart)
                setWhen(System.currentTimeMillis())
                setContentTitle("오늘은 각별한 날이에요.")
                setContentText("편지에 마음을 담아 전해보는 건 어떨까요?")
                setContentIntent(pendingIntent)
                setAutoCancel(true)
            }
            manager.notify(1, builder.build())
        }


    }
}