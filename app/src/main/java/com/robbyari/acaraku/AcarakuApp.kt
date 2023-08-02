package com.robbyari.acaraku

import android.app.Activity
import android.content.Intent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.robbyari.acaraku.domain.model.UserData
import com.robbyari.acaraku.presentation.components.BottomBar
import com.robbyari.acaraku.presentation.navigation.Screen
import com.robbyari.acaraku.presentation.screen.account.AccountScreen
import com.robbyari.acaraku.presentation.screen.account.AccountViewModel
import com.robbyari.acaraku.presentation.screen.checkout.CheckoutScreen
import com.robbyari.acaraku.presentation.screen.detail.DetailScreen
import com.robbyari.acaraku.presentation.screen.favorite.FavoriteScreen
import com.robbyari.acaraku.presentation.screen.home.HomeScreen
import com.robbyari.acaraku.presentation.screen.myticket.MyTicketScreen
import com.robbyari.acaraku.presentation.screen.nearby.NearbyScreen
import com.robbyari.acaraku.presentation.screen.notification.NotificationScreen
import com.robbyari.acaraku.presentation.screen.payment.PaymentScreen
import com.robbyari.acaraku.presentation.screen.signin.SignScreen
import com.robbyari.acaraku.presentation.screen.ticketdetail.TicketDetailScreen
import com.robbyari.acaraku.presentation.screen.transaction.TransactionScreen
import kotlinx.coroutines.launch

@Composable
fun AcarakuApp(
    location: String?,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    viewModel: AccountViewModel = hiltViewModel()
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current

    val state by viewModel.state.collectAsStateWithLifecycle()
    var userData by remember { mutableStateOf<UserData?>(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult(),
        onResult = { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                coroutineScope.launch {
                    val signInResult = viewModel.signInWithIntent(
                        intent = result.data ?: return@launch
                    )
                    viewModel.onSignInResult(signInResult)
                }
            }
        }
    )

    LaunchedEffect(key1 = state.isSignInSuccessful) {
        if (state.isSignInSuccessful) {
            Toast.makeText(
                context,
                context.resources.getString(R.string.signin_succesfull),
                Toast.LENGTH_LONG
            ).show()
            viewModel.resetState()
        }
        coroutineScope.launch {
            userData = viewModel.getSignedInUser()
        }

    }

    Scaffold(
        bottomBar = {
            when (currentRoute) {
                Screen.Home.route,
                Screen.MyTicket.route,
                Screen.Account.route,
                Screen.Transaction.route,
                Screen.Favorite.route -> {
                    BottomBar(navController = navController)
                }

                else -> {}
            }
        },
        modifier = modifier,
        containerColor = Color.Transparent,
        contentWindowInsets = WindowInsets(top = 0.dp)
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) {
                HomeScreen(
                    modifier,
                    navigateToDetail = { id ->
                        navController.navigate(Screen.Detail.createRoute(id))
                    },
                    currentImageUser = userData?.profilePictureUrl,
                    navigateToNotification = { navController.navigate(Screen.Notification.route) },
                    location = location,
                    navigateToNearby = { navController.navigate(Screen.Nearby.route) }
                )
            }

            composable(Screen.Notification.route) {
                NotificationScreen(navigateBack = { navController.popBackStack() })
            }

            composable(Screen.Nearby.route) {
                NearbyScreen(navigateBack = { navController.popBackStack() })
            }

            composable(Screen.Favorite.route) {
                FavoriteScreen(
                    modifier,
                    userData,
                    navigateToDetail = { id ->
                        navController.navigate(Screen.Detail.createRoute(id))
                    }
                )
            }
            composable(Screen.MyTicket.route) {
                MyTicketScreen(
                    navigateToTicketDetail = { id ->
                        navController.navigate(Screen.TicketDetail.createRoute(id))
                    }
                )
            }
            composable(Screen.Transaction.route) {
                TransactionScreen(
                    navigateToPayment = { url ->
                        navController.navigate(Screen.Payment.createRoute(url))
                    },
                    navigateToMyTicket = {
                        navController.navigate(Screen.MyTicket.route) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    }
                )
            }
            composable(Screen.Account.route) {
                AccountScreen(
                    state = state,
                    onSignIn = {
                        coroutineScope.launch {
                            val signInIntentSender = viewModel.signIn()
                            launcher.launch(
                                IntentSenderRequest.Builder(
                                    signInIntentSender ?: return@launch
                                ).build()
                            )
                        }
                    },
                    onSignOut = {
                        coroutineScope.launch {
                            viewModel.signOut()
                            Toast.makeText(
                                context,
                                context.resources.getString(R.string.signed_out),
                                Toast.LENGTH_LONG
                            ).show()
                            userData = null
                        }
                    },
                    userData = userData,
                    navigateToTransaction = {
                        navController.navigate(Screen.Transaction.route) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                    navigateToMyTicket = {
                        navController.navigate(Screen.MyTicket.route) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                    navigateToFavorite = {
                        navController.navigate(Screen.Favorite.route) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    },
                    navigateToNotification = {
                        navController.navigate(Screen.Notification.route) {
                            popUpTo(navController.graph.startDestinationId)
                        }
                    }
                )
            }
            composable(
                route = Screen.Detail.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType }),
                deepLinks = listOf(
                    navDeepLink {
                        uriPattern = "https://acaraku.com/detail/{id}"
                        action = Intent.ACTION_VIEW
                    }
                ),
            ) {
                val id = it.arguments?.getString("id") ?: ""
                DetailScreen(
                    id = id,
                    navigateBack = {
                        navController.popBackStack()
                    },
                    navigateToLogin = { navController.navigate(Screen.Login.route) },
                    navigateToCheckout = {
                        navController.navigate(Screen.Checkout.createRoute(id))
                    },
                    context = context
                )
            }
            composable(Screen.Login.route) {
                if (state.isSignInSuccessful) {
                    LaunchedEffect(key1 = Unit) {
                        navController.navigate(Screen.Home.route) {
                            popUpTo(navController.graph.startDestinationId)
                            launchSingleTop = true
                        }
                    }
                } else {
                    SignScreen(
                        state = state,
                        onSignIn = {
                            coroutineScope.launch {
                                val signInIntentSender = viewModel.signIn()
                                launcher.launch(
                                    IntentSenderRequest.Builder(
                                        signInIntentSender ?: return@launch
                                    ).build()
                                )
                            }
                        }
                    )
                }
            }
            composable(
                Screen.Checkout.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("id") ?: ""
                CheckoutScreen(
                    id = id,
                    userData = userData,
                    navigateBack = { navController.popBackStack() },
                    navigateToPayment = { urlSnap ->
                        navController.navigate(Screen.Payment.createRoute(urlSnap))
                    },
                    navigateToMyTicket = {
                        navController.navigate(Screen.MyTicket.route) {
                            popUpTo(Screen.Home.route) { inclusive = false }
                        }
                    }
                )
            }
            composable(
                Screen.Payment.route,
                arguments = listOf(navArgument("urlSnap") { type = NavType.StringType })
            ) {
                val urlSnap = it.arguments?.getString("urlSnap") ?: ""
                PaymentScreen(
                    context = context,
                    url = urlSnap,
                    backHandler = {
                        navController.popBackStack()
                    }
                )
            }
            composable(
                Screen.TicketDetail.route,
                arguments = listOf(navArgument("id") { type = NavType.StringType })
            ) {
                val id = it.arguments?.getString("id") ?: ""
                TicketDetailScreen(
                    id = id,
                    navigateBack = { navController.popBackStack() },
                    context = context
                )
            }
        }
    }
}

