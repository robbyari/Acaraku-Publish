package com.robbyari.acaraku.domain.repository

import android.content.Intent
import android.content.IntentSender
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.domain.model.SignInResult
import com.robbyari.acaraku.domain.model.Transaction
import com.robbyari.acaraku.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface EventRepository {
    suspend fun getAllEvent(): Flow<Response<List<Events>>>
    suspend fun getDetail(id: String): Flow<Response<Events>>
    suspend fun signIn(): IntentSender?
    suspend fun signInWithIntent(intent: Intent): SignInResult
    suspend fun signOut()
    suspend fun getSignedInUser(): UserData?
    suspend fun addFavorite(events: Events)
    suspend fun removeFavorite(events: Events)
    suspend fun checkFavorite(idEvent: String): Boolean
    suspend fun isLoggedIn(): Boolean
    suspend fun getFavorite(): Flow<Response<List<Events>>>
    suspend fun addTransaction(event: Events, idOrder: String, email: String, urlSnap: String, price: Int, status: String, transactionTime: String, nameEvent: String, idEvent: String)
    suspend fun getSettlementTicket(): Flow<Response<List<Transaction>>>
    suspend fun getAllTransaction(): Flow<Response<List<Transaction>>>
    suspend fun updateTransactionInFirebase()
}