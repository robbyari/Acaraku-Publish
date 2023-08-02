package com.robbyari.acaraku.domain.model

data class Transaction(
    val email: String? = "",
    val price: Int? = 0,
    val status: String? = "",
    val transactionTime: String? = "",
    val urlSnap: String? = "",
    val nameEvent: String? = "",
    val idOrder: String? = "",
    val event: Events? = null,
    val idEvent: String? = ""
)
