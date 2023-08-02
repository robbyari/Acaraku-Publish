package com.robbyari.acaraku.domain.model

import com.google.firebase.Timestamp

data class Events(
    val additionalInfo: List<String>? = emptyList(),
    val category: String? = "",
    val city: String? = "",
    val country: String? = "",
    val description: String? = "",
    val imageDetailUrl: String? = "",
    val location: String? = "",
    val map: String? = "",
    val price: Int? = 0,
    val thumbnailUrl: String? = "",
    val timeEnd: Timestamp? = Timestamp(0, 0),
    val timeStart: Timestamp? = Timestamp(0, 0),
    val title: String? = "",
    val maxBuy: Int? = 0,
    val eventId: String? = "",
    val createdAt: Timestamp? = Timestamp(0, 0)
)