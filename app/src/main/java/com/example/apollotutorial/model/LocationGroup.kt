package com.example.apollotutorial.model

import arrow.core.Option

data class LocationGroup(
    val uid: Option<String>,
    val type: String,
    val locations: List<Location>
)