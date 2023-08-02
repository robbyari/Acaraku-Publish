package com.robbyari.acaraku.presentation.screen.account

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Logout
import androidx.compose.material.icons.outlined.NavigateNext
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.robbyari.acaraku.R
import com.robbyari.acaraku.domain.model.SignInState
import com.robbyari.acaraku.domain.model.UserData
import com.robbyari.acaraku.presentation.theme.ButtonBackground
import com.robbyari.acaraku.presentation.theme.ButtonColor

@Composable
fun AccountScreen(
    state: SignInState,
    userData: UserData?,
    onSignIn: () -> Unit,
    onSignOut: () -> Unit,
    navigateToTransaction: () -> Unit,
    navigateToMyTicket: () -> Unit,
    navigateToFavorite: () -> Unit,
    navigateToNotification: () -> Unit
) {
    val context = LocalContext.current
    val showDialog = remember { mutableStateOf(false) }

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(280.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            if (userData != null) {
                AsyncImage(
                    model = userData.profilePictureUrl,
                    contentDescription = stringResource(R.string.profile_picture),
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .border(BorderStroke(1.5.dp, Color.White), CircleShape)
                        .size(110.dp)
                        .padding(10.dp)
                        .clip(CircleShape)
                )
                Text(
                    text = userData.username ?: stringResource(R.string.isnull),
                    style = TextStyle(
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    modifier = Modifier
                        .padding(top = 195.dp)
                )
                Text(
                    text = userData.email ?: stringResource(R.string.isnull),
                    style = TextStyle(
                        color = Color.White,
                    ),
                    modifier = Modifier
                        .padding(top = 225.dp)
                )
            } else {
                Image(
                    painter = painterResource(id = R.drawable.account_circle),
                    contentDescription = stringResource(R.string.account),
                    modifier = Modifier
                        .padding(top = 60.dp)
                        .size(110.dp)
                        .border(BorderStroke(1.5.dp, Color.White), CircleShape)
                        .clip(CircleShape)
                )
                Button(
                    onClick = onSignIn,
                    modifier = Modifier
                        .padding(top = 195.dp)
                        .height(55.dp)
                        .width(230.dp),
                    shape = CircleShape,
                    colors = ButtonDefaults.buttonColors(containerColor = ButtonBackground)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.icongooglesvg),
                            contentDescription = stringResource(R.string.google),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(20.dp))
                        Text(
                            text = stringResource(R.string.sign_in_with_google),
                        )
                    }
                }

            }
        }
        TextButton(
            onClick = navigateToTransaction,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.transaction),
                    style = TextStyle(
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.NavigateNext,
                    contentDescription = stringResource(R.string.navigate_next),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        TextButton(
            onClick = navigateToMyTicket,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.my_ticket),
                    style = TextStyle(
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.NavigateNext,
                    contentDescription = stringResource(R.string.navigate_next),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        TextButton(
            onClick = navigateToFavorite,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.my_favorites),
                    style = TextStyle(
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.NavigateNext,
                    contentDescription = stringResource(R.string.navigate_next),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        TextButton(
            onClick = navigateToNotification,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = stringResource(R.string.notifications),
                    style = TextStyle(
                        color = Color.White
                    )
                )
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    imageVector = Icons.Outlined.NavigateNext,
                    contentDescription = stringResource(R.string.navigate_next),
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
        Button(
            onClick = onSignIn,
            enabled = userData != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(55.dp)
                .padding(start = 20.dp, end = 20.dp)
                .alpha(if (userData != null) 1f else 0f),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(containerColor = ButtonBackground)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (userData != null) 1f else 0f)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.icongooglesvg),
                    contentDescription = stringResource(R.string.sign_in_with_google),
                    alpha = if (userData != null) 1f else 0f,
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = stringResource(R.string.change_account),
                )
            }
        }
        Spacer(modifier = Modifier.height(20.dp))
        Button(
            onClick = { if (userData != null) showDialog.value = true },
            enabled = userData != null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 20.dp, end = 20.dp)
                .height(55.dp)
                .alpha(if (userData != null) 1f else 0f),
            shape = CircleShape,
            colors = ButtonDefaults.buttonColors(ButtonBackground)
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .alpha(if (userData != null) 1f else 0f)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Logout,
                    contentDescription = stringResource(R.string.sign_out),
                    tint = if (userData != null) Color.White else Color.White.copy(alpha = 0f),
                    modifier = Modifier.size(24.dp)
                )
                Spacer(modifier = Modifier.width(20.dp))
                Text(
                    text = stringResource(R.string.sign_out),
                )
            }
        }
        if (showDialog.value) {
            AlertDialog(
                onDismissRequest = { showDialog.value = false },
                title = { Text(text = stringResource(R.string.sign_out), fontWeight = FontWeight.Bold) },
                confirmButton = {
                    Column(
                        modifier = Modifier,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.are_you_sure_you_want_to_sign_out),
                            textAlign = TextAlign.Center,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Button(
                                onClick = { showDialog.value = false },
                                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                            ) {
                                Text(text = stringResource(R.string.cancel))
                            }
                            Spacer(modifier = Modifier.width(15.dp))
                            Button(
                                onClick = {
                                    onSignOut()
                                    showDialog.value = false
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = ButtonColor),
                                modifier = Modifier
                                    .weight(1f)
                                    .height(52.dp)
                            ) {
                                Text(text = stringResource(R.string.sign_out))
                            }
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.Outlined.Logout,
                        contentDescription = stringResource(R.string.sign_out),
                        tint = Color.Black,
                        modifier = Modifier.size(50.dp)
                    )
                },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .height(250.dp)
            )
        }
    }
}