package com.robbyari.acaraku.presentation.screen.nearby

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.robbyari.acaraku.R
import com.robbyari.acaraku.presentation.components.ActionBarDetail

@Composable
fun NearbyScreen(
    navigateBack: () -> Unit
) {
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("box.json"))

    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        ActionBarDetail(
            title = stringResource(R.string.nearby),
            navigateBack = navigateBack,
            shareButton = false,
            modifier = Modifier.padding(top = 40.dp),
            share = {}
        )
        LottieAnimation(
            composition = composition,
            restartOnPlay = true,
            iterations = LottieConstants.IterateForever,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .padding(top = 230.dp)
                .size(270.dp),
        )
    }
}