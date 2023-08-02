package com.robbyari.acaraku.data.remote

import com.robbyari.acaraku.domain.model.ResponseWebhook
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("token/{token}/requests?sorting=newest")
    suspend fun getAllTransaction(
        @Path("token") token: String
    ): ResponseWebhook
}