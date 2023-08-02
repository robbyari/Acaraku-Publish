package com.robbyari.acaraku.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Place
import androidx.compose.material.icons.outlined.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbyari.acaraku.R
import com.robbyari.acaraku.presentation.theme.ButtonColor

@Composable
fun FloatingTicketDetail(
    onClickMap: () -> Unit = {},
    onClickShare: () -> Unit = {},
) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TextButton(
            onClick = onClickMap,
            modifier = Modifier
                .weight(0.4f)
                .height(52.dp)
                .clip(CircleShape)
                .background(ButtonColor)
        ) {
            Icon(
                imageVector = Icons.Outlined.Place,
                contentDescription = stringResource(R.string.go_to_location),
                tint = Color.White,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.location),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
        Spacer(modifier = Modifier.weight(0.05f))
        TextButton(
            onClick = onClickShare,
            modifier = Modifier
                .weight(0.4f)
                .height(52.dp)
                .clip(CircleShape)
                .background(ButtonColor)
        ) {
            Icon(
                imageVector = Icons.Outlined.Share,
                contentDescription = stringResource(R.string.share),
                tint = Color.White,
            )
            Spacer(modifier = Modifier.width(5.dp))
            Text(
                text = stringResource(R.string.share),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}
