package com.robbyari.acaraku.domain.model

data class SignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
