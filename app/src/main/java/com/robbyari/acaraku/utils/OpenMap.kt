package com.robbyari.acaraku.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

fun openMap(context: Context, url: String) {
    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
    context.startActivity(intent)
}