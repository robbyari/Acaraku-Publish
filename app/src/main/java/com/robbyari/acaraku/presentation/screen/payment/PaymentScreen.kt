package com.robbyari.acaraku.presentation.screen.payment

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.WebViewClient
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun PaymentScreen(
    url: String?,
    context: Context,
    backHandler: () -> Unit
) {
    val webView = remember {
        android.webkit.WebView(context).apply {
            webViewClient = WebViewClient()
            settings.javaScriptEnabled = true
            settings.loadsImagesAutomatically = true
        }
    }

    BackHandler(onBack = backHandler)

    LaunchedEffect(url) {
        if (url != null) {
            webView.loadUrl(url)
        }
    }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .systemBarsPadding()
    ) {
        AndroidView(
            factory = { webView },
            modifier = Modifier.fillMaxSize()
        )
    }
}