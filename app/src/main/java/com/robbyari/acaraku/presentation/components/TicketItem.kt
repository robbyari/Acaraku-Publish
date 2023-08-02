package com.robbyari.acaraku.presentation.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.robbyari.acaraku.R
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.utils.convertDate
import com.robbyari.acaraku.utils.convertMonth
import com.robbyari.acaraku.utils.convertTime
import com.robbyari.acaraku.utils.convertYear

@Composable
fun TicketItem(
    item: Events,
    modifier: Modifier = Modifier,
    image: Boolean = false
) {
    val ticketShape = TicketShape(circleRadius = 30.dp, cornerSize = CornerSize(10))
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 20f), 0f)

    Column(
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, bottom = 10.dp)
            .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(15)),
    ) {
        if (image) {
            Image(
                painter = when (item.category) {
                    stringResource(R.string.entertainment) -> painterResource(id = R.drawable.entertainmentcategory)
                    stringResource(R.string.education) -> painterResource(id = R.drawable.educationcategory)
                    stringResource(R.string.festival) -> painterResource(id = R.drawable.festivalcategory)
                    stringResource(R.string.trip) -> painterResource(id = R.drawable.tripcategory)
                    stringResource(R.string.sport) -> painterResource(id = R.drawable.sportcategory)
                    else -> painterResource(id = R.drawable.othercategory)
                },
                contentDescription = stringResource(R.string.ticket_item),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(top = 20.dp, start = 20.dp, end = 20.dp)
                    .height(170.dp)
                    .clip(RoundedCornerShape(topStartPercent = 20, topEndPercent = 20))
            )
        }
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .height(400.dp)
                .padding(start = 20.dp, end = 20.dp, bottom = 20.dp, top = 20.dp)
                .background(Color.White, shape = ticketShape),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = item.title!!,
                style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(top = 20.dp, bottom = 20.dp)
            )
            Divider()
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.date))
                Text(text = stringResource(R.string.time))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = convertDate(item.timeStart) +
                            " " +
                            convertMonth(item.timeStart).substring(0, 3) +
                            " " +
                            convertYear(item.timeStart),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = convertTime(item.timeStart) +
                            " " +
                            stringResource(id = R.string.wib),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.venue))
                Text(text = stringResource(R.string.seat))
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = item.location!!,
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
                Text(
                    text = stringResource(R.string.space),
                    style = TextStyle(
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 16.sp
                    )
                )
            }
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Bottom
            ) {
                Canvas(
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 35.dp, end = 35.dp, top = 20.dp)
                ) {
                    drawLine(
                        color = Color.Black.copy(alpha = 0.3f),
                        start = Offset(0f, 0f),
                        end = Offset(size.width, 0f),
                        strokeWidth = 5f,
                        pathEffect = pathEffect
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.barcode),
                    contentDescription = stringResource(R.string.barcode),
                    contentScale = ContentScale.Fit,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 30.dp, end = 20.dp, start = 20.dp, bottom = 15.dp)
                        .height(60.dp)
                        .clip(RoundedCornerShape(topStartPercent = 15, topEndPercent = 15))
                )
            }
        }
    }
}