package com.da.bot

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.da.bot.ChatBotService.Companion.CMD_GENERATE_MESSAGE
import com.da.bot.ChatBotService.Companion.CMD_STOP_SERVICE
import com.da.bot.ChatBotService.Companion.KEY_CMD
import com.da.bot.ChatBotService.Companion.KEY_NAME
import com.da.bot.ChatBotService.Companion.KEY_RESPONSE_MSG

class MainActivity : AppCompatActivity() {
    private lateinit var textView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnGenerateMessage = findViewById<Button>(R.id.btnGenerateMessage)
        btnGenerateMessage.setOnClickListener {
            val data = Bundle().apply {
                putInt(KEY_CMD, CMD_GENERATE_MESSAGE)
                putString(KEY_NAME, NAME)
            }
            intent = Intent(applicationContext, ChatBotService::class.java)
            intent.putExtras(data)
            startService(intent)
        }

        val btnStopService = findViewById<Button>(R.id.btnStopService)
        btnStopService.setOnClickListener {
            val data = Bundle().apply {
                putInt(KEY_CMD, CMD_STOP_SERVICE)
            }
            intent = Intent(applicationContext, ChatBotService::class.java)
            intent.putExtras(data)
            startService(intent)
        }

        textView = findViewById(R.id.tvMessagesBox)
        textView.movementMethod = ScrollingMovementMethod()
        val intentFilter = IntentFilter()
        intentFilter.addAction(ChatBotService.KEY_RESPONSE_CMD)
        registerReceiver(broadcastReceiver, intentFilter)
    }

    private val broadcastReceiver = object : BroadcastReceiver()  {
        val TAG = "BroadcastReceiver"
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            val data = intent?.extras
            Log.d(TAG, "onReceive: $action")
            if (action == ChatBotService.KEY_RESPONSE_CMD) {
                if (data != null) {
                    textView.append("${data.getString(KEY_RESPONSE_MSG)}\n")
                }
            }
        }
    }

    companion object {
        const val NAME = "Dmytro"
    }
}