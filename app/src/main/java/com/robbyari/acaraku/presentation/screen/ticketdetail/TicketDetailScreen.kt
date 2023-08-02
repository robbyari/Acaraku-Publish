package com.robbyari.acaraku.presentation.screen.ticketdetail

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asAndroidBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.robbyari.acaraku.R
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.presentation.components.ActionBarDetail
import com.robbyari.acaraku.presentation.components.FloatingTicketDetail
import com.robbyari.acaraku.presentation.components.TicketItem
import com.robbyari.acaraku.presentation.theme.ButtonColor
import com.robbyari.acaraku.utils.ShareUtils
import com.robbyari.acaraku.utils.openMap
import dev.shreyaspatil.capturable.Capturable
import dev.shreyaspatil.capturable.controller.rememberCaptureController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun TicketDetailScreen(
    id: String?,
    navigateBack: () -> Unit,
    context: Context,
    viewModel: TicketDetailViewModel = hiltViewModel(),
) {
    val detailState: Response<Events> by viewModel.detail.collectAsState()

    LaunchedEffect(id) {
        withContext(Dispatchers.IO) {
            viewModel.getDetailMyTicket(id)
        }
    }

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
                TicketDetailContent(
                    item = data,
                    navigateBack = navigateBack,
                    onClickMap = { openMap(context = context, url = data.map ?: "") }
                )
            }
        }

        is Response.Failure -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.failed_to_load_data),
                    modifier = Modifier
                        .fillMaxSize()
                        .align(Alignment.Center)
                )
            }
        }
    }
}

@Composable
fun TicketDetailContent(
    item: Events,
    navigateBack: () -> Unit,
    onClickMap: () -> Unit,
    modifier: Modifier = Modifier
) {
    val captureController = rememberCaptureController()
    val context = LocalContext.current

    Scaffold(
        floatingActionButton = {
            FloatingTicketDetail(
                onClickShare = { captureController.capture() },
                onClickMap = onClickMap
            )
        },
        floatingActionButtonPosition = FabPosition.Center,
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0)
    ) { innerPadding ->
        Column(
            modifier = modifier
                .fillMaxHeight()
                .padding(innerPadding)
        ) {
            ActionBarDetail(
                title = item.title!!,
                share = {},
                navigateBack = navigateBack,
                modifier = Modifier
                    .padding(top = 40.dp)
                    .background(Color.Transparent),
                shareButton = false
            )
            Spacer(modifier = Modifier.height(20.dp))
            Capturable(
                controller = captureController,
                onCaptured = { bitmap, error ->

                    val imageBitmap = bitmap?.asAndroidBitmap()

                    if (bitmap != null) {
                        ShareUtils.shareImageToOthers(context = context, bitmap = imageBitmap, title = item.title)
                    }
                    if (error != null) {
                        Toast.makeText(context, context.resources.getString(R.string.error_image_not_found), Toast.LENGTH_SHORT).show()
                    }
                }
            ) {
                TicketItem(item = item, image = true)
            }
        }
    }
}
