package com.robbyari.acaraku.presentation.screen.myticket

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.robbyari.acaraku.R
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.domain.model.Transaction
import com.robbyari.acaraku.presentation.components.MenuCategory
import com.robbyari.acaraku.presentation.components.SearchBar
import com.robbyari.acaraku.presentation.components.TicketItem
import com.robbyari.acaraku.presentation.theme.ButtonColor
import com.robbyari.acaraku.utils.iconMyTicket
import com.robbyari.acaraku.utils.myTicketCategory
import java.util.Date

@Composable
fun MyTicketScreen(
    navigateToTicketDetail: (String?) -> Unit,
    viewModel: MyTicketViewModel = hiltViewModel()
) {

    val myTicketState: Response<List<Transaction>> by viewModel.myTicket.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("boxempty.json"))
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf(myTicketCategory.first()) }

    LazyColumn(
        modifier = Modifier.padding(top = 18.dp)
    ) {
        item {
            SearchBar(
                query = searchQuery,
                onQueryChange = setSearchQuery,
                trailingOnClick = { setSearchQuery("") },
                modifier = Modifier,
                trailingIcon = searchQuery.isNotEmpty()
            )
        }
        item {
            Spacer(modifier = Modifier.height(15.dp))
        }
        item {
            MenuCategory(
                category = myTicketCategory,
                icons = iconMyTicket,
                modifier = Modifier.width(170.dp),
                onCategorySelected = { category ->
                    selectedCategory.value = category
                }
            )
            Spacer(modifier = Modifier.height(20.dp))
        }

        when (myTicketState) {
            is Response.Loading -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = ButtonColor,
                            modifier = Modifier.padding(top = 230.dp)
                        )
                    }
                }
            }

            is Response.Success -> {
                val dataTicket = (myTicketState as Response.Success<List<Transaction>>).data

                val filteredDataTicket = dataTicket?.filter { transaction ->
                    val event = transaction.event
                    val isCategoryMatch = when (selectedCategory.value) {
                        "Upcoming" -> event?.timeStart?.toDate()?.after(Date()) ?: false
                        "Finished" -> event?.timeStart?.toDate()?.before(Date()) ?: false
                        else -> false
                    }
                    val isTitleMatch = event?.title?.contains(searchQuery, ignoreCase = true) == true
                    isCategoryMatch && isTitleMatch
                }

                if (!filteredDataTicket.isNullOrEmpty()) {
                    items(filteredDataTicket) { transaction ->
                        TicketItem(
                            transaction.event ?: Events(),
                            modifier = Modifier
                                .clip(RoundedCornerShape(15))
                                .clickable {
                                    navigateToTicketDetail(transaction.idEvent)
                                }
                        )
                    }
                } else {
                    item {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                        ) {
                            LottieAnimation(
                                composition = composition,
                                restartOnPlay = true,
                                iterations = LottieConstants.IterateForever,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier
                                    .padding(top = 110.dp)
                                    .size(270.dp),
                            )
                            Text(
                                text = stringResource(R.string.empty_item),
                                color = Color.White.copy(alpha = 0.5f),
                                fontStyle = FontStyle.Italic
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