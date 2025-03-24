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

private const val DATE_PATTERN = "dd-MM-yyyy HH:mm:ss"

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
        if (isChecked) {
            statusText.text = getString(R.string.clean)
            statusText.setTextColor(getColor(android.R.color.holo_green_dark)) // ✅
        } else {
            statusText.text = getString(R.string.dirty)
            statusText.setTextColor(getColor(android.R.color.holo_red_dark))   // ✅
        }
    }

    private fun updateTimestampText(timeMillis: Long) {
        if (timeMillis == 0L) {
            timestampText.text = getString(R.string.never)
        } else {
            val date = Date(timeMillis)
            val formatter = SimpleDateFormat(DATE_PATTERN, Locale.getDefault())
            val formattedDate = formatter.format(date)
            timestampText.text = formattedDate
        }
    }
}
