package com.robbyari.acaraku.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.robbyari.acaraku.R
import com.robbyari.acaraku.presentation.theme.Green
import com.robbyari.acaraku.presentation.theme.Orange
import java.util.Locale

@Composable
fun TransactionItem(
    date: String,
    idOrder: String,
    totalPrice: String,
    nameEvent: String,
    status: String,
    time: String,
    onClick: () -> Unit = {}
) {
    val backgroundColor = when (status) {
        stringResource(R.string.success) -> Green
        stringResource(R.string.settlement) -> Green
        stringResource(R.string.pending) -> Orange
        stringResource(R.string.expire) -> Color.Red
        else -> Color.Transparent
    }

    val displayStatus = when (status) {
        stringResource(R.string.settlement) -> stringResource(R.string.success_capital)
        else -> status
    }

    Column(
        modifier = Modifier
            .padding(start = 20.dp, end = 20.dp, top = 0.dp, bottom = 10.dp)
            .border(BorderStroke(1.dp, Color.White.copy(alpha = 0.5f)), shape = RoundedCornerShape(15))
            .clip(RoundedCornerShape(10))
            .clickable { onClick() }
            .padding(top = 20.dp, bottom = 20.dp, start = 20.dp, end = 20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = time, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = displayStatus.replaceFirstChar {
                    if (it.isLowerCase()) it.titlecase(Locale.ROOT) else it.toString()
                },
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier
                    .background(backgroundColor, shape = RoundedCornerShape(20))
                    .padding(start = 5.dp, end = 5.dp)
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier
            ) {
                Text(text = date, color = Color.White)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = idOrder, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row(modifier = Modifier.fillMaxWidth()) {
            Text(text = nameEvent, color = Color.White)
            Spacer(modifier = Modifier.weight(1f))
            Text(text = totalPrice, fontWeight = FontWeight.Bold, color = Color.White)
        }

    }
}
