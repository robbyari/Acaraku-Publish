package com.robbyari.acaraku.presentation.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cancel
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.robbyari.acaraku.R
import com.robbyari.acaraku.presentation.theme.ButtonBackground

@Composable
fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    trailingOnClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: Boolean = false,
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchItem(
            query = query,
            onQueryChange = onQueryChange,
            trailingOnClick = trailingOnClick,
            trailingIcon = trailingIcon
        )
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchItem(
    query: String,
    onQueryChange: (String) -> Unit,
    trailingOnClick: () -> Unit,
    modifier: Modifier = Modifier,
    trailingIcon: Boolean = false,
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    TextField(
        value = query,
        onValueChange = onQueryChange,
        leadingIcon = {
            Icon(
                imageVector = Icons.Rounded.Search,
                contentDescription = stringResource(R.string.search),
                tint = Color.White
            )
        },
        colors = TextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            focusedContainerColor = ButtonBackground,
            unfocusedContainerColor = ButtonBackground,
            disabledIndicatorColor = Color.Transparent,
            focusedIndicatorColor = Color.Transparent,
            unfocusedIndicatorColor = Color.Transparent
        ),
        placeholder = {
            Text(stringResource(R.string.search_all_events), color = Color.White)
        },
        trailingIcon = {
            if (trailingIcon) {
                IconButton(onClick = trailingOnClick) {
                    Icon(
                        imageVector = Icons.Outlined.Cancel,
                        contentDescription = stringResource(R.string.cancel),
                        tint = Color.White
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                focusManager.clearFocus()
            },
        ),
        singleLine = true,
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp, bottom = 5.dp)
            .fillMaxWidth()
            .heightIn(min = 55.dp)
            .clip(RoundedCornerShape(30.dp)),
    )
}
