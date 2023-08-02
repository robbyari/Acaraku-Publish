package com.robbyari.acaraku.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.robbyari.acaraku.R

@Composable
fun TitleBar(
    nearbyOnClick: () -> Unit = {}
) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 15.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.upcoming_events),
            color = Color.White
        )
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = stringResource(R.string.nearby),
            color = Color.LightGray,
            modifier = Modifier
                .clip(RoundedCornerShape(5.dp))
                .clickable { nearbyOnClick() }
        )
    }
}