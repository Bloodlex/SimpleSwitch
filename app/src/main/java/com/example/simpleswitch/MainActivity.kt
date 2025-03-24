package com.example.simpleswitch

import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import androidx.core.content.edit
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    private lateinit var switchToggle: SwitchCompat
    private lateinit var statusText: TextView
    private lateinit var timestampText: TextView

    private val PREFS_NAME = "switch_prefs"
    private val SWITCH_STATE_KEY = "switch_state"
    private val SWITCH_TIME_KEY = "switch_time"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        switchToggle = findViewById(R.id.switchToggle)
        statusText = findViewById(R.id.statusText)
        timestampText = findViewById(R.id.timestampText)

        val prefs = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val savedState = prefs.getBoolean(SWITCH_STATE_KEY, false)
        val savedTime = prefs.getLong(SWITCH_TIME_KEY, 0)

        switchToggle.isChecked = savedState
        updateStatusText(savedState)
        updateTimestampText(savedTime)

        switchToggle.setOnCheckedChangeListener { _, isChecked ->
            val currentTime = System.currentTimeMillis()

            prefs.edit {
                putBoolean(SWITCH_STATE_KEY, isChecked)
                    .putLong(SWITCH_TIME_KEY, currentTime)
            }

            updateStatusText(isChecked)
            updateTimestampText(currentTime)
        }
    }

    private fun updateStatusText(isChecked: Boolean) {
        statusText.text = "Switch is ${if (isChecked) "ON" else "OFF"}"
    }

    private fun updateTimestampText(timeMillis: Long) {
        if (timeMillis == 0L) {
            timestampText.text = "Last modified: never"
        } else {
            val date = Date(timeMillis)
            val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
            val formattedDate = formatter.format(date)
            timestampText.text = "Last modified: $formattedDate"
        }
    }
}
