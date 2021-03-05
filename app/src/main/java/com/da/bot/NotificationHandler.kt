package com.da.bot

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build

class NotificationHandler(
    private val context: Context,
    private val notificationManager: NotificationManager
) {
    init {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANEL_ID,
                "messages",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendMessage(message: String){
        val mBuilder = Notification.Builder(context)
            .setSmallIcon(R.drawable.ic_message_icon)
            .setContentTitle("Chat Bot")
            .setContentText(message)
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mBuilder.setChannelId(CHANEL_ID)
        }

        val noti = mBuilder.build()
        noti.flags = noti.flags or Notification.FLAG_AUTO_CANCEL
        notificationManager.notify(0, noti)
    }

    companion object {
        private const val CHANEL_ID = "10001"
    }
}