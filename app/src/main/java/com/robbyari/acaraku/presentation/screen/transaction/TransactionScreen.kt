package com.robbyari.acaraku.presentation.screen.transaction

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.robbyari.acaraku.R
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.domain.model.Transaction
import com.robbyari.acaraku.presentation.components.TransactionItem
import com.robbyari.acaraku.presentation.theme.ButtonColor
import com.robbyari.acaraku.utils.convertPriceTotal
import com.robbyari.acaraku.utils.createTimestamp
import com.robbyari.acaraku.utils.dateTransaction

@Composable
fun TransactionScreen(
    navigateToPayment: (String?) -> Unit,
    navigateToMyTicket: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {
    val transactionState: Response<List<Transaction>> by viewModel.transactionList.collectAsState()
    val showSuccessDialog = remember { mutableStateOf(false) }
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("success.json"))

    val totalSettlementPrice = remember { mutableStateOf(0) }
    val data = (transactionState as? Response.Success)?.data
    data?.let {
        totalSettlementPrice.value = it.filter { transactionData ->
            transactionData.status == stringResource(id = R.string.settlement)
        }.sumOf { transactionData ->
            transactionData.price ?: 0
        }
    }

    LazyColumn(
        modifier = Modifier
            .padding(top = 20.dp)
            .fillMaxSize()
    ) {
        item {
            Box(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    painter = painterResource(id = R.drawable.gradienttransaction),
                    contentScale = ContentScale.Crop,
                    contentDescription = stringResource(R.string.background_image_transaction),
                    modifier = Modifier.height(200.dp).fillMaxSize()
                )
                Box(
                    modifier = Modifier
                        .background(Color.Black.copy(alpha = 0.25f))
                        .height(200.dp)
                        .fillMaxSize()
                )
                Column(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = stringResource(R.string.total_transactions),
                        fontSize = 20.sp,
                        color = Color.White,
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = convertPriceTotal(totalSettlementPrice.value),
                        style = TextStyle(
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        ),
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.latest_update) + dateTransaction(createTimestamp(), includeDay = false),
                        fontSize = 16.sp,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ButtonColor)
                            .padding(5.dp),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(20.dp))
                }
            }
        }
        item {
            val dataTotal = (transactionState as? Response.Success)?.data
            val totalItems = dataTotal?.size ?: 0
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = stringResource(R.string.history),
                    color = Color.White
                )
                Text(
                    text = stringResource(R.string.total) + "(${totalItems})",
                    color = Color.White
                )
            }
        }
        when (transactionState) {
            is Response.Loading -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = ButtonColor,
                            modifier = Modifier.padding(top = 150.dp)
                        )
                    }
                }
            }

            is Response.Success -> {
                val dataTransaction = (transactionState as Response.Success<List<Transaction>>).data
                if (!dataTransaction.isNullOrEmpty()) {
                    items(dataTransaction) { transactionData ->
                        TransactionItem(
                            date = dateTransaction(transactionData.transactionTime.toString()),
                            idOrder = transactionData.idOrder ?: "",
                            totalPrice = if (transactionData.price == 0) stringResource(R.string.free) else convertPriceTotal(transactionData.price),
                            nameEvent = transactionData.nameEvent ?: "",
                            status = transactionData.status ?: "",
                            time = transactionData.transactionTime?.substring(11, 19) ?: "",
                            onClick = {
                                if (transactionData.status == "settlement") {
                                    showSuccessDialog.value = true
                                } else {
                                    val encodedUrlSnap = Uri.encode(transactionData.urlSnap)
                                    navigateToPayment(encodedUrlSnap)
                                }
                            }
                        )
                    }
                    item {
                        if (showSuccessDialog.value) {
                            AlertDialog(
                                onDismissRequest = { showSuccessDialog.value = false },
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
                                                Text(text = stringResource(R.string.my_ticket))
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier
                                    .height(380.dp)
                            )
                        }
                    }
                    item {
                        Spacer(modifier = Modifier.height(30.dp))
                    }

                } else {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxSize()
                                .height(240.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Bottom
                        ) {
                            Text(
                                text = stringResource(R.string.empty_transaction),
                                color = Color.White.copy(alpha = 0.5f),
                                fontStyle = FontStyle.Italic,
                            )
                        }
                    }
                }
            }

            is Response.Failure -> {
                item {
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
    }
}