package com.asisee.streetpieces.model.service

import android.location.Location

interface LocationService {
    suspend fun getCurrentLocation() : Location
}