package com.robbyari.acaraku.presentation.screen.detail

import ExpandableText
import android.content.Context
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BrokenImage
import androidx.compose.material.icons.outlined.CalendarMonth
import androidx.compose.material.icons.outlined.Login
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.CompositingStrategy
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import com.robbyari.acaraku.R
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.presentation.components.ActionBarDetail
import com.robbyari.acaraku.presentation.components.FloatingButtonDetail
import com.robbyari.acaraku.presentation.components.ItemInfo
import com.robbyari.acaraku.presentation.theme.ButtonBackground
import com.robbyari.acaraku.presentation.theme.ButtonColor
import com.robbyari.acaraku.presentation.theme.gradient50
import com.robbyari.acaraku.utils.addToCalendar
import com.robbyari.acaraku.utils.convertDate
import com.robbyari.acaraku.utils.convertDay
import com.robbyari.acaraku.utils.convertMonth
import com.robbyari.acaraku.utils.convertPrice
import com.robbyari.acaraku.utils.convertTime
import com.robbyari.acaraku.utils.convertYear
import com.robbyari.acaraku.utils.sendText
import com.valentinilk.shimmer.shimmer
import kotlinx.coroutines.launch

@Composable
fun DetailScreen(
    id: String?,
    navigateBack: () -> Unit,
    navigateToLogin: () -> Unit,
    navigateToCheckout: (String?) -> Unit,
    context: Context,
    viewModel: DetailViewModel = hiltViewModel()
) {
    val dataLoaded = rememberSaveable { mutableStateOf(false) }


    LaunchedEffect(id) {
        if (!dataLoaded.value) {
            viewModel.getDetail(id)
            dataLoaded.value = true
        }
    }

    val isFavorite: Boolean by viewModel.isFavorite.collectAsState()
    val detailState: Response<Events> by viewModel.detail.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val showLoginDialog = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()

    when (val response = detailState) {
        is Response.Loading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = ButtonColor
                )
            }
        }

        is Response.Success<Events> -> {
            val data = response.data

            if (data != null) {
                DetailContent(
                    item = data,
                    navigateBack = navigateBack,
                    onClickFavorite = {
                        if (isLoggedIn) {
                            coroutineScope.launch {
                                if (isFavorite) {
                                    viewModel.removeFavorite(data)
                                } else {
                                    viewModel.addFavorite(data)
                                }
                            }
                        } else {
                            showLoginDialog.value = true
                        }
                    },
                    navigateToCheckout = {
                        if (isLoggedIn) {
                            navigateToCheckout(data.eventId)
                        } else {
                            showLoginDialog.value = true
                        }
                    },
                    isFavorite = isFavorite,
                    context = context,
                    modifier = Modifier.fillMaxHeight()
                )
                if (showLoginDialog.value) {
                    AlertDialog(
                        onDismissRequest = { showLoginDialog.value = false },
                        title = { Text(text = stringResource(R.string.you_must_be_logged_in), fontWeight = FontWeight.Bold) },
                        confirmButton = {
                            Column(
                                modifier = Modifier,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = stringResource(R.string.do_you_want_to_continue),
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(
                                        onClick = { showLoginDialog.value = false },
                                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(52.dp)
                                    ) {
                                        Text(text = stringResource(R.string.no))
                                    }
                                    Spacer(modifier = Modifier.width(15.dp))
                                    Button(
                                        onClick = navigateToLogin,
                                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(52.dp)
                                    ) {
                                        Text(text = stringResource(R.string.yes))
                                    }
                                }
                            }
                        },
                        icon = {
                            Icon(
                                imageVector = Icons.Outlined.Login,
                                contentDescription = stringResource(R.string.sign_in_with_google),
                                tint = Color.Black,
                                modifier = Modifier.size(50.dp)
                            )
                        },
                        modifier = Modifier
                            .height(250.dp)
                    )
                }
            } else {
                Text(text = stringResource(id = R.string.no_detail_available))
            }
        }

        is Response.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(id = R.string.failed_to_load_data),
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun DetailContent(
    item: Events,
    navigateBack: () -> Unit,
    onClickFavorite: () -> Unit,
    navigateToCheckout: (String?) -> Unit,
    isFavorite: Boolean,
    context: Context,
    modifier: Modifier
) {
    val painter = rememberAsyncImagePainter(item.imageDetailUrl)
    val state = painter.state
    val transition by animateFloatAsState(
        targetValue = if (state is AsyncImagePainter.State.Success) 1f else 0f
    )

    Scaffold(
        floatingActionButton = {
            FloatingButtonDetail(
                onClickFavorite = onClickFavorite,
                isFavorite = isFavorite,
                item = item,
                navigateToCheckout = navigateToCheckout
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0)
    ) { innerPadding ->
        Box(
            modifier = modifier
                .fillMaxHeight()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                item {
                    Box(modifier = Modifier) {
                        when (state) {
                            is AsyncImagePainter.State.Loading -> {
                                Box(
                                    modifier = Modifier
                                        .shimmer()
                                        .background(ButtonBackground)
                                        .height(290.dp)
                                        .fillMaxSize()
                                        .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                                        .drawWithContent {
                                            drawContent()
                                            drawRect(brush = gradient50, blendMode = BlendMode.DstIn)
                                        }
                                )
                            }

                            is AsyncImagePainter.State.Error -> {
                                Box(
                                    modifier = Modifier
                                        .background(ButtonBackground)
                                        .height(290.dp)
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
                            contentDescription = stringResource(R.string.image_detail),
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(550.dp)
                                .alpha(transition)
                                .graphicsLayer { compositingStrategy = CompositingStrategy.Offscreen }
                                .drawWithContent {
                                    drawContent()
                                    drawRect(brush = gradient50, blendMode = BlendMode.DstIn)
                                }
                        )
                        Column(
                            modifier = Modifier.padding(top = 285.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
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
                                                text = item.location + ", " + item.city,
                                                color = Color.LightGray,
                                            )
                                        }
                                    }
                                    Box(
                                        modifier = Modifier
                                            .height(45.dp)
                                            .clip(CircleShape)
                                            .background(Color.White)
                                            .padding(start = 20.dp, end = 20.dp),
                                        contentAlignment = Alignment.Center
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
                            Box(
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
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
                                            text = convertDate(item.timeStart),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = convertMonth(item.timeStart).substring(0, 3) + " " + convertYear(item.timeStart),
                                            color = Color.LightGray,
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(20.dp))
                                    Column(modifier = Modifier) {
                                        Text(
                                            text = convertDay(item.timeStart),
                                            color = Color.White,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Text(
                                            text = convertTime(item.timeStart) + " - " + convertTime(item.timeEnd) + " " + stringResource(id = R.string.wib),
                                            color = Color.LightGray,
                                        )
                                    }
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(
                                        onClick = {
                                            addToCalendar(item, context)
                                        },
                                        modifier = Modifier
                                            .size(52.dp)
                                            .clip(CircleShape)
                                            .background(Color.White.copy(alpha = 0.3f))

                                    ) {
                                        Icon(
                                            Icons.Outlined.CalendarMonth,
                                            contentDescription = stringResource(R.string.add_to_calendar),
                                            tint = Color.White,
                                        )
                                    }
                                }
                            }
                            Text(
                                text = stringResource(R.string.description),
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(start = 20.dp, top = 20.dp)
                            )
                            ExpandableText(
                                text = item.description!!,
                                style = TextStyle(
                                    lineHeight = 24.sp,
                                    color = Color.LightGray,
                                    fontSize = 16.sp,
                                    fontFamily = FontFamily.Default,
                                ),
                                modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 8.dp),
                            )
                            Text(
                                text = stringResource(R.string.about_this_events),
                                style = TextStyle(
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(start = 20.dp, top = 10.dp, bottom = 10.dp)
                            )
                        }
                    }
                }
                items(
                    item.additionalInfo!!.size,
                    key = { info -> info }
                ) { index ->
                    ItemInfo(info = item.additionalInfo[index])
                }
                item {
                    Spacer(modifier = Modifier.height(150.dp))
                }
            }
            ActionBarDetail(
                title = item.title!!,
                share = { sendText(item = item, context = context) },
                navigateBack = navigateBack,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .background(Color.Transparent),
                shareButton = true
            )
        }
    }
}