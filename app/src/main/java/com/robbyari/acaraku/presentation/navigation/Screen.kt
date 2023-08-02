package com.robbyari.acaraku.presentation.navigation

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Favorite : Screen("favorite")
    object MyTicket : Screen("myticket")
    object Transaction : Screen("transaction")
    object Account : Screen("account")
    object Login : Screen("login")
    object Notification : Screen("notification")
    object Nearby : Screen("nearby")
    object TicketDetail : Screen("ticketdetail/{id}") {
        fun createRoute(id: String?) = "ticketdetail/${id}"
    }
    object Checkout : Screen("checkout/{id}") {
        fun createRoute(id: String?) = "checkout/${id}"
    }
    object Detail : Screen("detail/{id}") {
        fun createRoute(id: String?) = "detail/$id"
    }
    object Payment : Screen("payment/{urlSnap}") {
        fun createRoute(urlSnap: String?) =
            "payment/$urlSnap"
    }
}
