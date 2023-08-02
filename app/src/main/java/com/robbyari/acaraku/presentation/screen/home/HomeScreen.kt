package com.robbyari.acaraku.presentation.screen.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.robbyari.acaraku.R
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.presentation.components.ActionBar
import com.robbyari.acaraku.presentation.components.ContentItem
import com.robbyari.acaraku.presentation.components.MenuCategory
import com.robbyari.acaraku.presentation.components.SearchBar
import com.robbyari.acaraku.presentation.components.TitleBar
import com.robbyari.acaraku.presentation.theme.ButtonBackground
import com.robbyari.acaraku.utils.categoryList
import com.robbyari.acaraku.utils.iconList
import com.valentinilk.shimmer.shimmer

@Composable
fun HomeScreen(
    modifier: Modifier,
    navigateToDetail: (String?) -> Unit,
    currentImageUser: String?,
    navigateToNotification: () -> Unit,
    navigateToNearby: () -> Unit,
    location: String?,
    viewModel: HomeViewModel = hiltViewModel()
) {

    val eventListState: Response<List<Events>> by viewModel.eventList.collectAsState()
    val composition by rememberLottieComposition(LottieCompositionSpec.Asset("ghost.json"))
    val (searchQuery, setSearchQuery) = remember { mutableStateOf("") }
    val selectedCategory = remember { mutableStateOf(categoryList.first()) }

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 35.dp),
    ) {
        item {
            Column(modifier = Modifier.fillMaxWidth()) {
                ActionBar(currentImageUser, navigateToNotification = navigateToNotification, location = location)
                SearchBar(
                    query = searchQuery,
                    onQueryChange = setSearchQuery,
                    trailingOnClick = { setSearchQuery("") },
                    trailingIcon = searchQuery.isNotEmpty()
                )
                TitleBar(nearbyOnClick = navigateToNearby)
                MenuCategory(
                    category = categoryList,
                    icons = iconList,
                    modifier = Modifier,
                    onCategorySelected = { category ->
                        selectedCategory.value = category
                    }
                )
            }
        }
        when (eventListState) {
            is Response.Loading -> {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .shimmer(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            Modifier
                                .padding(start = 20.dp, end = 20.dp, top = 20.dp)
                                .background(ButtonBackground, shape = RoundedCornerShape(20))
                                .height(270.dp)
                                .fillMaxWidth()
                        )
                        Box(
                            Modifier
                                .padding(20.dp)
                                .background(ButtonBackground, shape = RoundedCornerShape(20))
                                .height(270.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }

            is Response.Success -> {
                val data = (eventListState as Response.Success<List<Events>>).data
                val filteredDataTicket = data?.filter { events ->
                    val isCategoryMatch = when (selectedCategory.value) {
                        "Entertainment" -> events.category == "Entertainment"
                        "Education" -> events.category == "Education"
                        "Festival" -> events.category == "Festival"
                        "Trip" -> events.category == "Trip"
                        "Sport" -> events.category == "Sport"
                        "Other" -> events.category == "Other"
                        "My Feed" -> true
                        else -> false
                    }
                    val isTitleMatch = events.title?.contains(searchQuery, ignoreCase = true) == true
                    isCategoryMatch && isTitleMatch
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
                                    .padding(top = 120.dp)
                                    .size(270.dp),
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

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}