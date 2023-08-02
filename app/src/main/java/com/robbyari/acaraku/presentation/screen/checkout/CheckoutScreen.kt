package com.robbyari.acaraku.presentation.screen.checkout

import android.net.Uri
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.midtrans.Midtrans
import com.midtrans.httpclient.SnapApi
import com.midtrans.httpclient.error.MidtransError
import com.robbyari.acaraku.BuildConfig
import com.robbyari.acaraku.R
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.domain.model.UserData
import com.robbyari.acaraku.presentation.components.ActionBarDetail
import com.robbyari.acaraku.presentation.components.TicketItem
import com.robbyari.acaraku.presentation.theme.ButtonColor
import com.robbyari.acaraku.utils.convertPriceTotal
import com.robbyari.acaraku.utils.createTimestamp
import com.robbyari.acaraku.utils.requestBody
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun CheckoutScreen(
    id: String?,
    navigateBack: () -> Unit,
    navigateToPayment: (String?) -> Unit,
    navigateToMyTicket: () -> Unit,
    userData: UserData?,
    viewModel: CheckoutViewModel = hiltViewModel()
) {
    val idOrder: String by viewModel.idOrder.collectAsState()
    val detailState: Response<Events> by viewModel.detail.collectAsState()
    val showSuccessDialog = remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("success.json"))
    val isLoading = remember { mutableStateOf(false) }
    val coroutineScope = rememberCoroutineScope()
    Midtrans.serverKey = BuildConfig.MIDTRANS_SERVER_KEY
    Midtrans.clientKey = BuildConfig.MIDTRANS_CLIENT_KEY
    Midtrans.isProduction = false


    LaunchedEffect(id) {
        viewModel.getDetail(id)
        viewModel.getIdOrder()
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
                CheckoutContent(
                    navigateBack = navigateBack,
                    idOrder = idOrder,
                    item = data,
                    continuePayment = {
                        if (data.price == 0) {
                            coroutineScope.launch {
                                viewModel.addTransaction(
                                    event = data,
                                    idOrder = idOrder,
                                    email = userData?.email!!,
                                    urlSnap = "Free",
                                    price = 0,
                                    status = "settlement",
                                    transactionTime = createTimestamp(),
                                    nameEvent = data.title ?: "null",
                                    idEvent = data.eventId ?: "null"
                                )
                                showSuccessDialog.value = true
                            }
                        } else {
                            isLoading.value = true
                            coroutineScope.launch {
                                try {
                                    val urlSnap = withContext(Dispatchers.IO) {
                                        SnapApi.createTransactionRedirectUrl(
                                            requestBody(
                                                idOrder = idOrder,
                                                totalPrice = data.price.toString(),
                                                idEvent = data.eventId!!,
                                                nameEvent = data.title!!,
                                                firstName = userData?.username!!,
                                                email = userData.email!!
                                            )
                                        )
                                    }
                                    viewModel.addTransaction(
                                        event = data,
                                        idOrder = idOrder,
                                        email = userData?.email!!,
                                        urlSnap = urlSnap,
                                        price = data.price!!,
                                        status = "null",
                                        transactionTime = "null",
                                        nameEvent = data.title ?: "null",
                                        idEvent = data.eventId ?: "null"
                                    )
                                    val encodedUrlSnap = Uri.encode(urlSnap)
                                    navigateToPayment(encodedUrlSnap)
                                    Log.d("TestToken", urlSnap)
                                } catch (e: MidtransError) {
                                    e.printStackTrace()
                                }
                            }
                        }
                    }
                )
                if (showSuccessDialog.value) {
                    AlertDialog(
                        onDismissRequest = navigateBack,
                        confirmButton = {
                            Column(
                                modifier = Modifier,
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                LottieAnimation(
                                    composition = composition,
                                    restartOnPlay = true,
                                    iterations = LottieConstants.IterateForever,
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(200.dp),
                                )
                                Text(
                                    text = stringResource(R.string.successful),
                                    textAlign = TextAlign.Center,
                                    color = Color.Black,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                                Spacer(modifier = Modifier.height(10.dp))
                                Text(
                                    text = stringResource(R.string.see_you_at_the_event),
                                    textAlign = TextAlign.Center,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(20.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Button(
                                        onClick = navigateToMyTicket,
                                        colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                                        modifier = Modifier
                                            .weight(1f)
                                            .height(52.dp)
                                    ) {
                                        Text(text = stringResource(id = R.string.my_ticket))
                                    }
                                }
                            }
                        },
                        modifier = Modifier
                            .height(380.dp)
                    )
                }
            } else {
                Text(text = stringResource(R.string.no_detail_available))
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

    if (isLoading.value) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.5f)),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(color = ButtonColor)
        }
    }

}

@Composable
fun CheckoutContent(
    item: Events,
    continuePayment: () -> Unit,
    navigateBack: () -> Unit,
    idOrder: String
) {
    val pathEffect = PathEffect.dashPathEffect(floatArrayOf(25f, 20f), 0f)
    Column(
        modifier = Modifier.padding(top = 40.dp)
    ) {
        ActionBarDetail(
            title = stringResource(id = R.string.checkout),
            share = { },
            navigateBack = navigateBack,
            modifier = Modifier,
            shareButton = false
        )
        Spacer(modifier = Modifier.height(20.dp))
        TicketItem(item = item)
    }
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 15.dp)
            .systemBarsPadding(),
        verticalArrangement = Arrangement.Bottom
    ) {
        Column(
            modifier = Modifier
                .padding(20.dp)
                .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(25))
                .padding(top = 20.dp, bottom = 20.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.id_order), color = Color.White)
                Text(text = idOrder, color = Color.White)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.price), color = Color.White)
                Text(text = convertPriceTotal(item.price), color = Color.White)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.item), color = Color.White)
                Text(text = stringResource(R.string.x1), color = Color.White)
            }
            Spacer(modifier = Modifier.height(10.dp))
            Canvas(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp)
            ) {
                drawLine(
                    color = Color.White.copy(alpha = 0.3f),
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 5f,
                    pathEffect = pathEffect
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = stringResource(R.string.total_price), color = Color.White, fontWeight = FontWeight.Bold)
                Text(text = convertPriceTotal(item.price), color = Color.White, fontWeight = FontWeight.Bold)
            }
        }
        Spacer(modifier = Modifier.height(10.dp))
        TextButton(
            onClick = continuePayment,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .height(52.dp)
                .clip(CircleShape)
                .background(ButtonColor)
        ) {
            Text(
                text = stringResource(R.string.continue_payment),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp
            )
        }
    }
}