package com.asisee.streetpieces.common.ext

import android.location.Location
import com.asisee.streetpieces.model.PieceLocation

fun Location.toPieceLocation() = PieceLocation(latitude, longitude)