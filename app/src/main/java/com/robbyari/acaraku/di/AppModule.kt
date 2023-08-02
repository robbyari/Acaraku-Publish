package com.robbyari.acaraku.di

import android.app.Application
import android.content.Context
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.robbyari.acaraku.data.EventRepositoryImpl
import com.robbyari.acaraku.data.remote.ApiService
import com.robbyari.acaraku.domain.repository.EventRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun provideEventRepository(context: Context, oneTapClient: SignInClient, apiService: ApiService): EventRepository = EventRepositoryImpl(
        db = Firebase.firestore,
        context = context,
        oneTapClient = oneTapClient,
        apiService = apiService
    )

    @Provides
    fun provideContext(application: Application): Context = application.applicationContext

    @Provides
    fun provideSignInClient(context: Context): SignInClient {
        return Identity.getSignInClient(context)
    }
}