package com.robbyari.acaraku.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.outlined.Apps
import androidx.compose.material.icons.outlined.CheckCircleOutline
import androidx.compose.material.icons.outlined.Explore
import androidx.compose.material.icons.outlined.Festival
import androidx.compose.material.icons.outlined.School
import androidx.compose.material.icons.outlined.Sports
import androidx.compose.material.icons.outlined.TheaterComedy
import androidx.compose.material.icons.outlined.Upcoming
import androidx.compose.material.icons.outlined.Workspaces

const val DEFAULT_MINIMUM_TEXT_LINE = 3

val categoryList = listOf(
    "My Feed",
    "Entertainment",
    "Education",
    "Festival",
    "Trip",
    "Sport",
    "Other"
)

val iconList = listOf(
    Icons.Outlined.Apps,
    Icons.Outlined.TheaterComedy,
    Icons.Outlined.School,
    Icons.Outlined.Festival,
    Icons.Outlined.Explore,
    Icons.Outlined.Sports,
    Icons.Outlined.Workspaces
)

val iconListFill = listOf(
    Icons.Filled.ConfirmationNumber,
    Icons.Filled.Favorite,
    Icons.Filled.Home,
    Icons.Filled.ReceiptLong,
    Icons.Filled.Person
)

val myTicketCategory = listOf(
    "Upcoming",
    "Finished"
)

val iconMyTicket = listOf(
    Icons.Outlined.Upcoming,
    Icons.Outlined.CheckCircleOutline,
)

val transaction = listOf(
    "Pending",
    "Success"
)
