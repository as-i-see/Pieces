package com.asisee.streetpieces.model.service

import android.location.Location
import com.asisee.streetpieces.common.exceptions.LocationResultException
import com.github.michaelbull.result.Result

interface LocationService {
    suspend fun getCurrentLocation(): Result<Location, LocationResultException>
}
