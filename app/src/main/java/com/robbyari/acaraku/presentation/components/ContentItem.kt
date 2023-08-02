package com.robbyari.acaraku.presentation.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.robbyari.acaraku.R
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.presentation.theme.ButtonBackground
import com.robbyari.acaraku.utils.convertDate
import com.robbyari.acaraku.utils.convertMonth
import com.robbyari.acaraku.utils.convertPrice
import com.robbyari.acaraku.utils.convertTime
import com.valentinilk.shimmer.shimmer

@Composable
fun ContentItem(
    item: Events,
    onClick: () -> Unit = {},
) {
    val painter = rememberAsyncImagePainter(item.thumbnailUrl)
    val state = painter.state
    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f
    )

    Box(
        modifier = Modifier
            .padding(top = 20.dp, start = 20.dp, end = 20.dp)
            .clip(RoundedCornerShape(40.dp))
            .clickable { onClick() }
    ) {
        when (state) {
            is AsyncImagePainter.State.Loading -> {
                Box(
                    modifier = Modifier
                        .shimmer()
                        .background(ButtonBackground)
                        .height(270.dp)
                        .fillMaxSize()
                )
            }

            is AsyncImagePainter.State.Error -> {
                Box(
                    modifier = Modifier
                        .background(ButtonBackground)
                        .height(270.dp)
                        .fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Outlined.BrokenImage,
                        contentDescription = stringResource(R.string.broken_image),
                        tint = Color.White,
                        modifier = Modifier
                            .size(150.dp)
                            .fillMaxSize()
                            .align(Alignment.Center)
                    )
                }
            }

            else -> {}
        }
        Image(
            painter = painter,
            contentDescription = stringResource(R.string.image_item),
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .alpha(transition)
                .fillMaxWidth()
                .height(270.dp)
        )
        Box(
            modifier = Modifier
                .padding(top = 15.dp, end = 15.dp)
                .align(Alignment.TopEnd)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.Gray.copy(0.5f))
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                ) {
                    Text(
                        text = convertDate(item.timeStart),
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold
                    )
                    Text(
                        text = convertMonth(item.timeStart).substring(0, 3),
                        color = Color.White,
                        fontWeight = FontWeight.ExtraBold,
                        maxLines = 1,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.width(35.dp)
                    )
                }
            }
        }
        Box(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .background(Color.Gray.copy(0.5f))
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                Column(modifier = Modifier) {
                    Text(
                        text = item.title!!,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Row {
                        Text(
                            text = "${item.city} ",
                            color = Color.White,
                        )
                        Text(
                            text = convertTime(item.timeStart) + stringResource(R.string.wib),
                            color = Color.White,
                        )
                    }
                }
                Box(
                    modifier = Modifier
                        .clip(CircleShape)
                        .background(Color.White)
                        .padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 15.dp)
                ) {
                    Text(
                        text = convertPrice(item.price),
                        color = Color.Black,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
            }
        }
    }
}