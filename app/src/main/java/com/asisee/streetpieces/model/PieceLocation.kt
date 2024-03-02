package com.asisee.streetpieces.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PieceLocation(val latitude: Double = 0.0, val longitude: Double = 0.0) : Parcelable {
    override fun toString() = "$latitude, $longitude"
}


