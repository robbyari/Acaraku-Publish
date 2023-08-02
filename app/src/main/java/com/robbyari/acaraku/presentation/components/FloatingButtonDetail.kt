package com.robbyari.acaraku.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.presentation.theme.ButtonColor

@Composable
fun FloatingButtonDetail(
    onClickFavorite: () -> Unit = {},
    navigateToCheckout: (String?) -> Unit = {},
    item: Events = Events(),
    isFavorite: Boolean = false,
) {
    Row(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp)
            .fillMaxWidth()
            .navigationBarsPadding(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            modifier = Modifier
                .size(52.dp)
                .clip(CircleShape)
                .background(ButtonColor),
            onClick = onClickFavorite,
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Filled.Favorite else Icons.Outlined.FavoriteBorder,
                contentDescription = if (isFavorite) stringResource(R.string.dalam_favorit) else stringResource(R.string.add_to_favorite),
                tint = Color.White,
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        TextButton(
            onClick = { navigateToCheckout(item.eventId) },
            modifier = Modifier
                .height(52.dp)
                .clip(CircleShape)
                .background(ButtonColor)
                .weight(1f)
        ) {
            Text(
                text = stringResource(R.string.get_ticket),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}