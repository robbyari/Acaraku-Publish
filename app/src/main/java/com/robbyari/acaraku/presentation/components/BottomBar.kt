package com.robbyari.acaraku.presentation.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ConfirmationNumber
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.PersonOutline
import androidx.compose.material.icons.outlined.ReceiptLong
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.robbyari.acaraku.R
import com.robbyari.acaraku.presentation.navigation.NavigationItem
import com.robbyari.acaraku.presentation.navigation.Screen
import com.robbyari.acaraku.presentation.theme.ButtonColor
import com.robbyari.acaraku.utils.iconListFill

@Composable
fun BottomBar(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    BottomAppBar(
        containerColor = Color.Transparent,
        modifier = modifier,
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        val navigationItems = listOf(
            NavigationItem(
                icon = Icons.Outlined.ConfirmationNumber,
                screen = Screen.MyTicket
            ),
            NavigationItem(
                icon = Icons.Outlined.FavoriteBorder,
                screen = Screen.Favorite
            ),
            NavigationItem(
                icon = Icons.Outlined.Home,
                screen = Screen.Home
            ),
            NavigationItem(
                icon = Icons.Outlined.ReceiptLong,
                screen = Screen.Transaction
            ),
            NavigationItem(
                icon = Icons.Outlined.PersonOutline,
                screen = Screen.Account
            )
        )
        navigationItems.mapIndexed { index, item ->
            NavigationBarItem(
                icon = {
                    val isSelected = currentRoute == item.screen.route
                    Icon(
                        imageVector = if (isSelected) iconListFill[index] else item.icon,
                        contentDescription = buildString {
                            append(stringResource(R.string.navigation_to))
                            append(item.screen.route)
                        },
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp)
                    )
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = ButtonColor,
                    unselectedIconColor = Color.White,
                    indicatorColor = Color.White
                ),
                selected = currentRoute == item.screen.route,
                onClick = {
                    navController.navigate(item.screen.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                    }
                },
                modifier = Modifier
            )
        }
    }
}
