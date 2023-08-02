package com.robbyari.acaraku.utils

import com.google.firebase.Timestamp
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.math.log10
import kotlin.math.pow
import kotlin.random.Random

fun convertDay(timestamp: Timestamp?): String {
    val date: Date? = timestamp?.toDate()
    val calendar: Calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }

    val dateFormat = SimpleDateFormat("EEEE", Locale("id", "ID"))
    return dateFormat.format(calendar.time)
}

fun convertDate(timestamp: Timestamp?): String {
    val date: Date? = timestamp?.toDate()
    val calendar: Calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }

    return calendar.get(Calendar.DAY_OF_MONTH).toString()
}

fun convertMonth(timestamp: Timestamp?): String {
    val date: Date? = timestamp?.toDate()
    val calendar: Calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }

    val dateFormat = SimpleDateFormat("MMMM", Locale("id", "ID"))
    return dateFormat.format(calendar.time)
}

fun convertYear(timestamp: Timestamp?): String {
    val date: Date? = timestamp?.toDate()
    val calendar: Calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }

    val dateFormat = SimpleDateFormat("yyyy", Locale("id", "ID"))
    return dateFormat.format(calendar.time)
}

fun convertTime(timestamp: Timestamp?): String {
    val date: Date? = timestamp?.toDate()
    val calendar: Calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }

    val dateFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return dateFormat.format(calendar.time)
}

fun convertPrice(amount: Int?): String {
    return amount?.let {
        if (it == 0) {
            "FREE"
        } else {
            val units = arrayOf("", "K", "M", "B", "T")
            val digitGroups = (log10(it.toDouble()) / log10(1000.0)).toInt()
            val formattedAmount = DecimalFormat("#.##").format(it.toDouble() / 1000.0.pow(digitGroups.toDouble()))
            val roundedAmount = if (formattedAmount.endsWith(".0")) formattedAmount.replace(".0", "") else formattedAmount
            "Rp $roundedAmount${units[digitGroups]}"
        }
    } ?: ""
}

fun convertPriceTotal(price: Int?): String {
    val formattedPrice = price.toString().reversed().chunked(3).joinToString(".").reversed()
    return "Rp $formattedPrice"
}

fun createTimestamp(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val currentTime = Calendar.getInstance().time
    return dateFormat.format(currentTime)
}

fun dateTransaction(date: String, includeDay: Boolean = true): String {
    val inputFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    val outputFormat = SimpleDateFormat(if (includeDay) "EEEE, d MMM yyyy" else "d MMM yyyy", Locale("id"))

    val format = inputFormat.parse(date)
    return outputFormat.format(format!!)
}

fun generateOrderId(length: Int): String {
    val charPool: List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    return (1..length)
        .map { Random.nextInt(0, charPool.size) }
        .map(charPool::get)
        .joinToString("")
}

fun convertTimestampFirebaseToCalendar(timestamp: Timestamp?): Calendar {
    val date = timestamp?.toDate()

    val calendar = Calendar.getInstance()
    if (date != null) {
        calendar.time = date
    }

    calendar.timeZone = TimeZone.getTimeZone("UTC+7")

    return calendar
}