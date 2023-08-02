package com.robbyari.acaraku.data

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.Timestamp
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.robbyari.acaraku.BuildConfig
import com.robbyari.acaraku.R
import com.robbyari.acaraku.data.remote.ApiService
import com.robbyari.acaraku.domain.model.Events
import com.robbyari.acaraku.domain.model.Response
import com.robbyari.acaraku.domain.model.SignInResult
import com.robbyari.acaraku.domain.model.Transaction
import com.robbyari.acaraku.domain.model.TransactionWebhook
import com.robbyari.acaraku.domain.model.UserData
import com.robbyari.acaraku.domain.repository.EventRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import java.util.concurrent.CancellationException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val context: Context,
    private val oneTapClient: SignInClient,
    private val apiService: ApiService
) : EventRepository {

    private val auth = Firebase.auth

    override suspend fun getAllEvent(): Flow<Response<List<Events>>> = callbackFlow {
        val eventList = mutableListOf<Events>()
        val subscription = db.collection("events")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    trySend(Response.Failure(e)).isSuccess
                    return@addSnapshotListener
                }

                snapshots?.documentChanges?.forEach { dc ->
                    val event = dc.document.toObject(Events::class.java)
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            eventList.add(0, event)
                        }

                        DocumentChange.Type.REMOVED -> {
                            val index = eventList.indexOfFirst { it.eventId == event.eventId }
                            if (index != -1) {
                                eventList.removeAt(index)
                            }
                        }

                        DocumentChange.Type.MODIFIED -> {
                            val index = eventList.indexOfFirst { it.eventId == event.eventId }
                            if (index != -1) {
                                eventList[index] = event
                            }
                        }
                    }
                }

                trySend(Response.Success(eventList.toList())).isSuccess

            }

        awaitClose { subscription.remove() }
    }

    override suspend fun getDetail(id: String): Flow<Response<Events>> = flow {
        emit(Response.Loading)
        try {
            val getDetail = db.collection("events").document(id).get().await()
            val detail = getDetail.toObject(Events::class.java)

            if (detail != null) {
                emit(Response.Success(detail))
            } else {
                emit(Response.Failure(Exception(context.getString(R.string.event_not_found))))
            }

        } catch (e: Exception) {
            emit(Response.Failure(e))
        }
    }

    override suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    override suspend fun signInWithIntent(intent: Intent): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val user = auth.signInWithCredential(googleCredentials).await().user
            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        username = displayName,
                        email = email,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    override suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    override suspend fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            username = displayName,
            email = email,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    override suspend fun addFavorite(events: Events) {
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val favoriteDocRef = db.collection("favorites")
                    .document(currentUser.uid)
                    .collection("listFavorite")
                    .document(events.eventId!!)


                favoriteDocRef.set(events).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun removeFavorite(events: Events) {
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val favoriteDocRef = db.collection("favorites")
                    .document(currentUser.uid)
                    .collection("listFavorite")
                    .document(events.eventId!!)

                favoriteDocRef.delete()
                    .await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun checkFavorite(idEvent: String): Boolean {
        return try {
            val getFavorite = auth.currentUser?.let { db.collection("favorites").document(it.uid).collection("listFavorite").document(idEvent) }
            return getFavorite?.get()?.await()?.exists() ?: false
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    override suspend fun isLoggedIn(): Boolean {
        val currentUser = auth.currentUser
        return currentUser != null
    }

    override suspend fun getFavorite(): Flow<Response<List<Events>>> = callbackFlow {
        val favoriteList = mutableListOf<Events>()
        val currentUser = auth.currentUser?.uid.toString()
        val subscription = db.collection("favorites")
            .document(currentUser)
            .collection("listFavorite")
            .orderBy("createdAt", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshots, e ->
                if (e != null) {
                    trySend(Response.Failure(e)).isSuccess
                    return@addSnapshotListener
                }

                snapshots?.documentChanges?.forEach { dc ->
                    val event = dc.document.toObject(Events::class.java)
                    when (dc.type) {
                        DocumentChange.Type.ADDED -> {
                            favoriteList.add(0, event)
                        }

                        DocumentChange.Type.REMOVED -> {
                            val index = favoriteList.indexOfFirst { it.eventId == event.eventId }
                            if (index != -1) {
                                favoriteList.removeAt(index)
                            }
                        }

                        DocumentChange.Type.MODIFIED -> {
                            val index = favoriteList.indexOfFirst { it.eventId == event.eventId }
                            if (index != -1) {
                                favoriteList[index] = event
                            }
                        }
                    }
                }
                trySend(Response.Success(favoriteList.toList())).isSuccess
            }
        awaitClose { subscription.remove() }
    }

    override suspend fun addTransaction(event: Events, idOrder: String, email: String, urlSnap: String, price: Int, status: String, transactionTime: String, nameEvent: String, idEvent: String) {
        try {
            val currentUser = auth.currentUser
            if (currentUser != null) {
                val transactionData = hashMapOf(
                    "event" to event,
                    "email" to email,
                    "urlSnap" to urlSnap,
                    "price" to event.price,
                    "status" to status,
                    "transactionTime" to transactionTime,
                    "idOrder" to idOrder,
                    "nameEvent" to nameEvent,
                    "idEvent" to idEvent
                )

                val favoriteDocRef = db.collection("transactions")
                    .document(idOrder)

                favoriteDocRef.set(transactionData).await()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun getSettlementTicket(): Flow<Response<List<Transaction>>> = callbackFlow {
        val currentEmail = auth.currentUser?.email
        val collectionRef = db.collection("transactions")
        val query = collectionRef
            .whereEqualTo("email", currentEmail)
            .whereEqualTo("status", "settlement")
            .whereNotEqualTo("status", "null")

        val listener = query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                trySend(Response.Failure(exception))
                close(exception)
                return@addSnapshotListener
            }

            val myTicket = mutableListOf<Transaction>()

            for (document in snapshot?.documents.orEmpty()) {
                val email = document.getString("email")
                val price = document.getLong("price")?.toInt()
                val status = document.getString("status")
                val transactionTime = document.getString("transactionTime")
                val urlSnap = document.getString("urlSnap")
                val idOrder = document.getString("idOrder")
                val nameEvent = document.getString("nameEvent")
                val idEvent = document.getString("idEvent")
                val eventMap = document.get("event") as? Map<*, *>
                val event = eventMap?.let {
                    Events(
                        additionalInfo = it["additionalInfo"] as? List<String>,
                        category = it["category"] as? String,
                        city = it["city"] as? String,
                        country = it["country"] as? String,
                        description = it["description"] as? String,
                        imageDetailUrl = it["imageDetailUrl"] as? String,
                        location = it["location"] as? String,
                        map = it["map"] as? String,
                        price = it["price"] as? Int,
                        thumbnailUrl = it["thumbnailUrl"] as? String,
                        timeEnd = it["timeEnd"] as? Timestamp,
                        timeStart = it["timeStart"] as? Timestamp,
                        title = it["title"] as? String,
                        maxBuy = it["maxBuy"] as? Int,
                        eventId = it["eventId"] as? String,
                        createdAt = it["createdAt"] as? Timestamp
                    )
                }

                val transaction = Transaction(
                    email = email,
                    price = price,
                    status = status,
                    transactionTime = transactionTime,
                    urlSnap = urlSnap,
                    idOrder = idOrder,
                    nameEvent = nameEvent,
                    event = event,
                    idEvent = idEvent
                )

                myTicket.add(transaction)
            }

            myTicket.sortWith { t1, t2 ->
                t2.transactionTime?.compareTo(t1.transactionTime ?: "") ?: 0
            }

            trySend(Response.Success(myTicket))
        }

        awaitClose { listener.remove() }
    }


    override suspend fun getAllTransaction(): Flow<Response<List<Transaction>>> = callbackFlow {
        val currentEmail = auth.currentUser?.email
        val collectionRef = db.collection("transactions")
        val query = collectionRef.whereEqualTo("email", currentEmail).whereNotEqualTo("status", "null")

        val listener = query.addSnapshotListener { snapshot, exception ->
            if (exception != null) {
                trySend(Response.Failure(exception))
                close(exception)
                return@addSnapshotListener
            }

            val transactions = mutableListOf<Transaction>()

            for (document in snapshot?.documents.orEmpty()) {
                val email = document.getString("email")
                val price = document.getLong("price")?.toInt()
                val status = document.getString("status")
                val transactionTime = document.getString("transactionTime")
                val urlSnap = document.getString("urlSnap")
                val idOrder = document.getString("idOrder")
                val nameEvent = document.getString("nameEvent")

                val transaction = Transaction(
                    email = email,
                    price = price,
                    status = status,
                    transactionTime = transactionTime,
                    urlSnap = urlSnap,
                    idOrder = idOrder,
                    nameEvent = nameEvent
                )

                transactions.add(transaction)
            }

            transactions.sortWith { t1, t2 ->
                t2.transactionTime?.compareTo(t1.transactionTime ?: "") ?: 0
            }

            trySend(Response.Success(transactions))
        }
        awaitClose { listener.remove() }
    }

    override suspend fun updateTransactionInFirebase() {
        try {
            val data = apiService.getAllTransaction(token = BuildConfig.TOKEN).data
            val orderIdsSet = mutableSetOf<String>()

            if (data != null) {
                for (dataItem in data) {
                    val content = dataItem?.content
                    if (content != null) {
                        val transactionData = Gson().fromJson(content, TransactionWebhook::class.java)
                        val orderId = transactionData.orderId
                        val status = transactionData.transactionStatus
                        val transactionTime = transactionData.transactionTime

                        if (orderId != null && orderId !in orderIdsSet) {
                            orderIdsSet.add(orderId)
                            val firebaseTransactionRef = db.collection("transactions").document(orderId)

                            val updateData = hashMapOf<String, Any>(
                                "status" to status.toString(),
                                "transactionTime" to transactionTime.toString()
                            )

                            try {
                                firebaseTransactionRef.update(updateData).await()
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(BuildConfig.WEB_CLIENT_ID)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }
}