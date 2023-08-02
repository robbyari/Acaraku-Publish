package com.robbyari.acaraku.presentation.screen.account

import android.content.Intent
import android.content.IntentSender
import androidx.lifecycle.ViewModel
import com.robbyari.acaraku.domain.model.SignInResult
import com.robbyari.acaraku.domain.model.SignInState
import com.robbyari.acaraku.domain.repository.EventRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val repo: EventRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SignInState())
    val state = _state.asStateFlow()

    fun onSignInResult(result: SignInResult) {
        _state.update {
            it.copy(
                isSignInSuccessful = result.data != null,
                signInError = result.errorMessage,
            )
        }
    }

    fun resetState() {
        _state.update { SignInState() }
    }

    suspend fun signInWithIntent(intent: Intent): SignInResult = repo.signInWithIntent(intent)

    suspend fun signIn(): IntentSender? = repo.signIn()

    suspend fun getSignedInUser() = repo.getSignedInUser()

    suspend fun signOut() = repo.signOut()
}