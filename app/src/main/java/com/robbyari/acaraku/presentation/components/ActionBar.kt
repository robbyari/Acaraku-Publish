package com.robbyari.acaraku.presentation.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.robbyari.acaraku.R
import com.robbyari.acaraku.presentation.theme.ButtonBackground

@Composable
fun ActionBar(
    urlImage: String?,
    navigateToNotification: () -> Unit = {},
    location: String?,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 20.dp, end = 20.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (!urlImage.isNullOrEmpty()) {
            AsyncImage(
                model = urlImage,
                contentDescription = stringResource(R.string.profile),
                modifier = Modifier
                    .size(47.dp)
                    .clip(CircleShape)
            )
        } else {
            Icon(
                Icons.Outlined.Person,
                contentDescription = stringResource(R.string.profile),
                tint = Color.White,
                modifier = Modifier
                    .size(48.dp)
                    .background(color = ButtonBackground, shape = CircleShape)
                    .padding(8.dp)
            )
        }
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.baseline_location_on_24),
                contentDescription = stringResource(R.string.profile),
                modifier = Modifier
                    .size(18.dp)
                    .clip(CircleShape)
            )
            Text(
                text = location ?: stringResource(R.string.location),
                color = Color.White,
                modifier = Modifier

            )
        }
        IconButton(
            onClick = navigateToNotification,
            modifier = Modifier
                .clip(CircleShape)
                .background(ButtonBackground)

        ) {
            Icon(
                Icons.Outlined.Notifications,
                contentDescription = stringResource(R.string.notifications),
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}
