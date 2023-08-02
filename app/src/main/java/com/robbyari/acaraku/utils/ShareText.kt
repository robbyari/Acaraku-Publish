package com.robbyari.acaraku.utils

import android.content.Context
import android.content.Intent
import com.robbyari.acaraku.domain.model.Events

fun sendText(context: Context, item: Events) {
    val intent = Intent(Intent.ACTION_SEND)
    intent.type = "text/plain"
    intent.putExtra(Intent.EXTRA_TEXT, generateShareText(item))
    context.startActivity(Intent.createChooser(intent, "Share via"))
}

private fun generateShareText(item: Events): String {
    val deepLinkUrl = "https://acaraku.com/detail/${item.eventId}"
    val formattedTimeStart = convertDay(item.timeStart) + ", " + convertDate(item.timeStart) + " " + convertMonth(item.timeStart) + " " + convertYear(item.timeStart)
    return "Don't miss the exciting event\n\n🎉 Event: ${item.title}\n\n📅 Date: $formattedTimeStart\n\n📍 Location: ${item.location}\n\nSee more details and join us: $deepLinkUrl \n\n🚀🚀🚀🚀🚀🚀🚀"
}