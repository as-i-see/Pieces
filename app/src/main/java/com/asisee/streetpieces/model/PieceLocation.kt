package com.asisee.streetpieces.model

data class PieceLocation(
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
) {
    fun text() = "$latitude, $longitude"
    fun isNotSet() = latitude == 0.0 && longitude == 0.0
}

