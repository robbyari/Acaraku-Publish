package com.robbyari.acaraku.presentation.screen.favorite

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import com.robbyari.acaraku.domain.model.UserData
import com.robbyari.acaraku.presentation.components.ContentItem
import com.robbyari.acaraku.presentation.components.SearchBar
import com.robbyari.acaraku.presentation.theme.ButtonColor

@Composable
fun FavoriteScreen(
    modifier: Modifier,
    userData: UserData?,
    navigateToDetail: (String?) -> Unit,
    viewModel: FavoriteViewModel = hiltViewModel()
) {
    val favoriteListState: Response<List<Events>> by viewModel.favoriteList.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("boxempty.json"))
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }

    LaunchedEffect(key1 = userData?.userId) {
        viewModel.getAllFavorite()
    }

    LazyColumn(
        modifier = modifier
            .padding(top = 18.dp)
            .fillMaxSize()
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
        when (favoriteListState) {
            is Response.Loading -> {
                item {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            color = ButtonColor,
                            modifier = Modifier.padding(top = 320.dp)
                        )
                    }
                }
            }

            is Response.Success -> {
                val data = (favoriteListState as Response.Success<List<Events>>).data

                val filteredDataTicket = data?.filter { events ->
                    events.title?.contains(searchQuery, ignoreCase = true) == true
                }

                if (!filteredDataTicket.isNullOrEmpty()) {
                    items(filteredDataTicket) { eventData ->
                        ContentItem(
                            item = eventData,
                            onClick = { navigateToDetail(eventData.eventId) }
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
                                    .padding(top = 200.dp)
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
                            text = stringResource(id = R.string.failed_to_load_data),
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center)
                        )
                    }
                }
            }
        }
        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}