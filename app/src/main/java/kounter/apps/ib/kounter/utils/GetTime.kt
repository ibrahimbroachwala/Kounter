package kounter.apps.ib.kounter.utils

import android.util.Log

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Created by ibrah on 1/22/2018.
 */

object GetTime {

    private val MINUTE_MILLIS = (1000 * 60).toLong()
    private val HOUR_MILLIS = 60 * MINUTE_MILLIS
    private val DAY_MILLIS = 24 * HOUR_MILLIS

    fun getTime(dateMillis: Long): String {
        var date = ""
        val now = System.currentTimeMillis()

        // Change how the date is displayed depending on whether it was written in the last minute,
        // the hour, etc.
        if (now - dateMillis < DAY_MILLIS) {
            if (now - dateMillis < HOUR_MILLIS) {
                val minutes = Math.round(((now - dateMillis) / MINUTE_MILLIS).toFloat()).toLong()
                date = minutes.toString() + "m ago"
            } else {
                val minutes = Math.round(((now - dateMillis) / HOUR_MILLIS).toFloat()).toLong()
                date = minutes.toString() + "h ago"
            }
        } else {
            val dateDate = Date(dateMillis)
            try {
                val sDateFormat = SimpleDateFormat("dd-MMM-yy", Locale.US)
                date = "on " + sDateFormat.format(dateDate)
            } catch (e: IllegalArgumentException) {
                Log.d("EXCEPTION", e.message)
            }

        }
        return date
    }
}
