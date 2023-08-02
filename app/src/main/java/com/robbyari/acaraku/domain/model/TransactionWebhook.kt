package com.robbyari.acaraku.domain.model

import com.google.gson.annotations.SerializedName

data class TransactionWebhook(
    @SerializedName("transaction_time")
    val transactionTime: String? = null,

    @SerializedName("transaction_status")
    val transactionStatus: String? = null,

    @SerializedName("order_id")
    val orderId: String? = null
)
