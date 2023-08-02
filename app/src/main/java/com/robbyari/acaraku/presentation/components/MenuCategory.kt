package com.robbyari.acaraku.presentation.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.robbyari.acaraku.R
import com.robbyari.acaraku.presentation.theme.ButtonBackground
import com.robbyari.acaraku.presentation.theme.ButtonColor
import kotlinx.coroutines.launch

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun MenuCategory(category: List<String>, icons: List<ImageVector>, modifier: Modifier, onCategorySelected: (String) -> Unit) {
    val selectedIndex = remember { mutableStateOf(0) }
    val lazyListState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        state = lazyListState,
        horizontalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterHorizontally),
        contentPadding = PaddingValues(horizontal = 12.dp),
        flingBehavior = rememberSnapFlingBehavior(lazyListState = lazyListState)
    ) {
        itemsIndexed(category) { index, item ->
            MenuItem(
                category = item,
                isSelected = index == selectedIndex.value,
                icon = icons.getOrNull(index) ?: Icons.Default.Info,
                onClick = {
                    selectedIndex.value = index
                    coroutineScope.launch {
                        lazyListState.animateScrollToItem(index)
                    }
                    onCategorySelected(item)
                },
                modifier = modifier,
            )
        }
    }
}

@Composable
fun MenuItem(
    category: String,
    isSelected: Boolean,
    icon: ImageVector,
    modifier: Modifier,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier.height(55.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = if (isSelected) ButtonColor else ButtonBackground,
        ),
        shape = RoundedCornerShape(25.dp),
        contentPadding = PaddingValues(10.dp),
        onClick = onClick
    ) {
        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .background(Color.White, shape = CircleShape)
                    .padding(5.dp)
            ) {
                Icon(
                    modifier = Modifier
                        .size(24.dp),
                    imageVector = icon,
                    tint = if (isSelected) ButtonColor else ButtonBackground,
                    contentDescription = stringResource(R.string.icon_category)
                )
            }
            Spacer(Modifier.width(6.dp))
            Text(
                modifier = Modifier,
                text = category,
                color = Color.White
            )
        }
    }
}
