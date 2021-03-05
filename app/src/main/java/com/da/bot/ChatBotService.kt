package com.da.bot

import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.util.Log

class ChatBotService: Service() {
    private val messages = arrayOf("Hello %s!", "How are you?", "Good Bye %s!")
    private var counter = 0
    private lateinit var notificationHandler: NotificationHandler

    override fun onCreate() {
        super.onCreate()
        notificationHandler = NotificationHandler(this, getSystemService(NOTIFICATION_SERVICE) as NotificationManager)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy: service is stopping")
    }

    private fun sendBroadcast(message: String){
        val responseBundle = Bundle()
        responseBundle.putString(KEY_RESPONSE_MSG, message)
        val responseIntent = Intent().apply {
            putExtras(responseBundle)
            action = KEY_RESPONSE_CMD
        }
        sendBroadcast(responseIntent)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        intent?.let {
            val data = intent.extras
            when (val cmd = data?.getInt(KEY_CMD)) {
                CMD_GENERATE_MESSAGE -> {
                    val name = data.getString(KEY_NAME)
                    Log.d(TAG, "onStartCommand: $cmd, $name")
                    val message = messages[counter].format(name)
                    counter = (counter + 1) % 3

                    sendBroadcast(message)
                    notificationHandler.sendMessage(message)
                }
                CMD_STOP_SERVICE -> {
                    Log.d(TAG, "onStartCommand: $cmd")
                    val message = "ChatBot Stopped: 78"
                    notificationHandler.sendMessage(message)
                    stopSelf()
                }
                else -> {
                    Log.d(TAG, "onStartCommand: $cmd")
                }
            }
        }
        return START_STICKY
    }

    companion object {
        const val CMD_GENERATE_MESSAGE = 10
        const val CMD_STOP_SERVICE = 20
        const val KEY_CMD = "cmd"
        const val KEY_NAME = "name"

        const val KEY_RESPONSE_MSG = "response_msg"
        const val KEY_RESPONSE_CMD = "response_cmd"

        private const val TAG = "ChatBotService"
    }
}