package com.example.apollotutorial.model

import arrow.core.Option

data class Location(
    val localeId: Option<String>,
    val latitude: Double,
    val longitude: Double,
    val address: Option<String>
)