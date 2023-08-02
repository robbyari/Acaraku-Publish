package com.robbyari.acaraku.utils

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import com.robbyari.acaraku.domain.model.Events
import java.util.Calendar
import java.util.TimeZone

fun addToCalendar(item: Events, context: Context) {
    val beginTime: Calendar = convertTimestampFirebaseToCalendar(item.timeStart)
    beginTime.timeInMillis

    val endTime: Calendar = convertTimestampFirebaseToCalendar(item.timeEnd)
    endTime.timeInMillis

    try {
        val intent = Intent(Intent.ACTION_INSERT)
        intent.data = CalendarContract.Events.CONTENT_URI
        intent.putExtra(CalendarContract.Events.TITLE, item.title)
        intent.putExtra(CalendarContract.Events.DESCRIPTION, item.description)
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, item.location)
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, beginTime.timeInMillis)
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime.timeInMillis)
        intent.putExtra(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().id)
        intent.putExtra(CalendarContract.Reminders.MINUTES, 30)
        context.startActivity(intent)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

